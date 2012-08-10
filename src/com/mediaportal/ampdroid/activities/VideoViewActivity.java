package com.mediaportal.ampdroid.activities;

import java.util.Random;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.database.DownloadsDatabaseHandler;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.downloadservice.DownloadJob;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.DownloaderUtils;

public class VideoViewActivity extends BaseActivity {
   
   private static int UPDATE_INTERVAL = 2000;
   private static int UPDATE_INTERVAL_SLEEP_CYCLES = 20;

   protected class StartVideoPlaybackTask extends AsyncTask<String, String, Boolean> {
      private Context mContext;
      private DownloadsDatabaseHandler mUpdaterDatabase;
      private boolean mTriggerStart;
      private boolean mUpdating;

      protected StartVideoPlaybackTask(Context _parent) {
         mContext = _parent;
      }
      
      public void triggerStart(){
         mTriggerStart = true;
      }
      @Override
      protected Boolean doInBackground(String... _keys) {
         mUpdaterDatabase = new DownloadsDatabaseHandler(mContext);
         mUpdaterDatabase.open();
         mUpdating = true;
         while (mUpdating) {
            if (mUpdating) {
               DownloadJob streamJob = mUpdaterDatabase.getDownload(mStreamingId);
               if(streamJob.getProgress() > 5){
                  return true;
               }
            }

            try {
               for(int i = 0; i < UPDATE_INTERVAL_SLEEP_CYCLES; i++){
                  if(mTriggerStart){
                     mTriggerStart = false;
                     break;
                  }
                  Thread.sleep(UPDATE_INTERVAL / UPDATE_INTERVAL_SLEEP_CYCLES);
               }
            } catch (InterruptedException e) {
            }
         }
         
         return false;
      }

      @Override
      protected void onPostExecute(Boolean _result) {
         super.onPostExecute(_result);
         if (_result == true) {
            startVideoPlayback();
         } 
         mUpdaterDatabase.close();
      }
   }
   
   private String mStreamingFile;
   private DownloadJob mStreamingJob;
   private int mStreamingId;
   private StartVideoPlaybackTask mStartVideoTask;
   private DownloadItemType mStreamingType;
   private String mStreamingUrl;
   private VideoView view;
   private String mDisplayName;
   private String mProfile;
   private String mIdentifier;
   private long mStartPosition;
   private long mVideoLength;
   private String mClientName;

   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_videoview);
      mService = DataHandler.getCurrentRemoteInstance();
      
      Bundle extras = getIntent().getExtras();
      mStreamingFile = extras.getString("video_id");
      int type = extras.getInt("video_type", 0);
      mStreamingType = DownloadItemType.fromInt(type);
      mDisplayName = extras.getString("video_name");
      mProfile = extras.getString("profile_name");
      mIdentifier = createIdentifier();
      mStartPosition = extras.getLong("video_startfrom");

      if (extras.containsKey("video_length")) {
         mVideoLength = extras.getLong("video_length");
      }
      
      Log.i(Constants.LOG_CONST, "Initialising Tv Stream");
      mClientName = PreferencesManager.getClientName();
      boolean success = mService.initTvStreaming(mIdentifier, mClientName,
            Integer.parseInt(mStreamingFile), mProfile);
      if (success) {
         mStreamingUrl = mService.startTvStreaming(mIdentifier, mStartPosition / 1000);

         startVideoPlayback();
      }
   }
   
   @Override
   protected void onDestroy() {
      mService.stopTvStreaming(mIdentifier);
      super.onDestroy();
   }

   private String createIdentifier() {
      return "aMPdroid." + new Random().nextInt() + ".ts";
   }

   public void startVideoPlayback() {
      try {
         view = (VideoView) findViewById(R.id.video);
         MediaController mc = new MediaController(this);
         view.setMediaController(mc);
         
         //view.setVideoPath(DownloaderUtils.getBaseDirectory() + "/Shares/Movies/0.ts");
         view.setVideoURI(Uri.parse(mStreamingUrl));

         view.requestFocus();
         view.start();
         
         int duration = view.getDuration();
         int x = duration;

         view.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer _player) {
               //view.setVideoPath(DownloaderUtils.getBaseDirectory() + "/Shares/Movies/1.ts");
               //view.start();
            }
         });

         view.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer arg0, int arg1, int arg2) {
               // mService.stopStreaming(file);
               return false;
            }
         });
      } catch (Exception ex) {
         Log.e(Constants.LOG_CONST, ex.toString());
      }
   }

   @Override
   protected void onStop() {
      mService.stopStreaming(mStreamingFile);
      super.onStop();
   }
}
