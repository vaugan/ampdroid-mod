package com.mediaportal.ampdroid.activities.videoplayback;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.StreamTranscodingInfo;
import com.mediaportal.ampdroid.database.DownloadsDatabaseHandler;
import com.mediaportal.ampdroid.utils.Constants;

public class StartVideoPlaybackTask extends AsyncTask<String, String, Boolean> {
   private Context mContext;
   private DownloadsDatabaseHandler mUpdaterDatabase;
   private boolean mTriggerStart;
   private boolean mUpdating;
   private VideoStreamingPlayerActivity mParent;
   private DataHandler mService;
   private static int UPDATE_INTERVAL = 2000;
   private static int UPDATE_INTERVAL_SLEEP_CYCLES = 20;

   protected StartVideoPlaybackTask(VideoStreamingPlayerActivity _parent, DataHandler _service) {
      mParent = _parent;
      mService = _service;
   }

   public void triggerStart() {
      mTriggerStart = true;
   }

   public void cancelTask() {
      mUpdating = false;
   }

   @Override
   protected Boolean doInBackground(String... _keys) {
      mUpdaterDatabase = new DownloadsDatabaseHandler(mContext);
      mUpdaterDatabase.open();
      mUpdating = true;
      try {
         while (mUpdating) {
            if (mUpdating) {
               StreamTranscodingInfo status = null;// = mParent.mService.getTransocdingInfo(mIdentifier);
               if (status != null) {
                  Log.d(Constants.LOG_CONST,
                        "StreamingStatus checker: " + status.getCurrentTime());
                  if (status.getCurrentTime() > 20) {
                     return true;
                  }
               } else {
                  Log.d(Constants.LOG_CONST, "StreamingStatus checker: returned null");
                  return false;
               }
            }

            try {
               for (int i = 0; i < UPDATE_INTERVAL_SLEEP_CYCLES; i++) {
                  if (mTriggerStart) {
                     mTriggerStart = false;
                     break;
                  }
                  Thread.sleep(UPDATE_INTERVAL / UPDATE_INTERVAL_SLEEP_CYCLES);
               }
            } catch (InterruptedException e) {
            }
         }
      } catch (Exception e) {
         Log.e(Constants.LOG_CONST, e.toString());
      }

      return false;
   }

   @Override
   protected void onPostExecute(Boolean _result) {
      super.onPostExecute(_result);
      if (_result == true && mUpdating) {
         Log.d(Constants.LOG_CONST, "StreamingStatus checker: start playback");
         mParent.getMediaPlayer().start();
         mParent.setOverlayImage(null);
      }
      mParent.startMediaTaskFinished();
   }
}

