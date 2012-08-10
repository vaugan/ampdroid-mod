/***
Copyright (c) 2008-2009 CommonsWare, LLC
Licensed under the Apache License, Version 2.0 (the "License"); you may
not use this file except in compliance with the License. You may obtain
a copy of the License at
http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.mediaportal.ampdroid.activities.videoplayback;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseActivity;
import com.mediaportal.ampdroid.data.FileInfo;
import com.mediaportal.ampdroid.data.MediaInfo;
import com.mediaportal.ampdroid.data.PlaybackSession;
import com.mediaportal.ampdroid.data.StreamTranscodingInfo;
import com.mediaportal.ampdroid.database.PlaybackDatabaseHandler;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.DateTimeHelper;
import com.mediaportal.ampdroid.utils.Util;
import com.mediaportal.ampdroid.videoplayer.TappableSurfaceView;

public class VideoStreamingPlayerActivity extends BaseActivity implements OnCompletionListener,
      MediaPlayer.OnPreparedListener, SurfaceHolder.Callback {
   private class ConnectionMonitor extends BroadcastReceiver {

      @Override
      public void onReceive(Context context, Intent intent) {
         String action = intent.getAction();

         if (!action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            return;
         }

         boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY,
               false);
         NetworkInfo aNetworkInfo = (NetworkInfo) intent
               .getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

         if (!noConnectivity) {
            if ((aNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                  || (aNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
               // Handle connected case
            }
         } else {
            if ((aNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                  || (aNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
               // Handle disconnected case
               Util.showToast(mContext, "DISCONNECTEEEEEEEEEEED");
            }
         }
      }
   }

   private synchronized void startMonitoringConnection() {
      IntentFilter aFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
      registerReceiver(mConnectionReceiver, aFilter);
   }

   private synchronized void stopMonitoringConnection() {
      unregisterReceiver(mConnectionReceiver);
   }

   private class StartStreamingTask extends AsyncTask<Boolean, Integer, Boolean> {
      protected Boolean doInBackground(Boolean... _params) {
         boolean init = _params[0];

         if (mIsTv) {
            Log.i(Constants.LOG_CONST, "Initialising Tv Stream");
            boolean success = mService.initTvStreaming(mIdentifier, mClientName,
                  Integer.parseInt(mStreamingFile), mProfile);
            if (!success) {
               return false;
            }

            mStreamingUrl = mService.startTvStreaming(mIdentifier, mStartPosition / 1000);

            // tried to manually connect to th stream to trigger transcoding
            // start -> not working
            // try {
            // URL myFileUrl = new URL(mStreamingUrl);
            // HttpURLConnection conn = (HttpURLConnection)
            // myFileUrl.openConnection();
            // conn.setDoInput(true);
            // conn.connect();
            // InputStream inputStream = conn.getInputStream();
            // inputStream.read();
            // inputStream.close();
            // conn.disconnect();
            // } catch (IOException e) {
            // // TODO Auto-generated catch block
            // e.printStackTrace();
            // }
         } else if (mIsRecording) {
            Log.i(Constants.LOG_CONST, "Initialising Recording Stream");
            boolean success = mService.initRecordingStreaming(mIdentifier, mClientName,
                  Integer.parseInt(mStreamingFile), mProfile, (int) (mStartPosition / 1000));

            if (!success) {
               return false;
            }
            mStreamingUrl = mService.startRecordingStreaming(mIdentifier);
         } else {
            if (init) {
               Log.i(Constants.LOG_CONST, "Initialising Media Stream");
               mService.initStreaming(mIdentifier, mClientName, mStreamingType, mStreamingFile);
            }

            if (mStartPosition > 0 && mVideoNeedsPreconversion && !mSeekingAllowedOnVideo) {
               // the video needs pre-conversion before we can seek -> ask user
               // if he want's to to start from 0 instead
               mJumpToPositionWhenAvailable = mStartPosition;
               mStreamingUrl = null;
            } else {
               mStreamingUrl = mService
                     .startStreaming(mIdentifier, mProfile, mStartPosition / 1000);
            }
         }

         Log.i(Constants.LOG_CONST, "New Streaming url: " + mStreamingUrl);
         return true;
      }

      @Override
      protected void onPostExecute(Boolean _result) {
         if (_result) {
            if (mStreamingUrl != null) {
               if (mUseInternalPlayer) {
                  playVideo(USE_AUTOSTART);
               } else {
                  showExternalPlayerStartOverlay(true);
                  setSeeking(false);
               }
            } else {
               showStartFromBeginningDialog();
            }
         } else {
            showInitError(mContext);
         }
         super.onPostExecute(_result);
      }

      @Override
      protected void onProgressUpdate(Integer... values) {
         super.onProgressUpdate(values);
      }
   }

   public StreamTranscodingInfo mLatestTranscodingInfo;

   private class CheckTranscodingTask extends AsyncTask<Boolean, StreamTranscodingInfo, Boolean> {
      private boolean mChecking;
      private boolean mSeekPreparing;

      protected void stopChecking() {
         mChecking = false;
      }

      protected Boolean doInBackground(Boolean... _params) {
         mChecking = true;
         while (mChecking) {
            if (mIdentifier != null) {
               StreamTranscodingInfo info = mService.getTransocdingInfo(mIdentifier);
               if (info != null) {
                  publishProgress(info);
               }

               try {
                  Thread.sleep(4000);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }
         }

         Log.i(Constants.LOG_CONST, "New Streaming url: " + mStreamingUrl);
         return true;
      }

      @Override
      protected void onPostExecute(Boolean _result) {
         if (_result) {
         }
         super.onPostExecute(_result);
      }

      @Override
      protected void onProgressUpdate(StreamTranscodingInfo... values) {
         StreamTranscodingInfo info = values[0];

         if (mLatestTranscodingInfo == null || (mSeekingAllowedOnVideo && !info.isSeekAvailable())) {
            setTranscodingStatus(getString(R.string.streaming_seeking_preparing), true);

         }

         if (!mSeekingAllowedOnVideo && info.isSeekAvailable()) {
            // seeking on video just became available
            showToast(getString(R.string.streaming_seeking_available));
            setTranscodingStatus(null, false);

            if (mJumpToPositionWhenAvailable > 0) {
               mStartPosition = mJumpToPositionWhenAvailable;
               mJumpToPositionWhenAvailable = 0;

               mStartStreamingTask = new StartStreamingTask();
               mStartStreamingTask.execute(false);
            }
            // mTextViewTranscodingStatus.setAnimation(outToLeftAnimation());
         }

         if (mSeekPreparing && !info.isSeekPreparing() && !info.isSeekAvailable()) {
            // preparing of seek failed
            showToast(getString(R.string.streaming_seeking_preparefailed));
            setTranscodingStatus(null, false);
         }

         mSeekingAllowedOnVideo = info.isSeekAvailable();
         mSeekPreparing = info.isSeekPreparing();

         mLatestTranscodingInfo = info;
         Log.d(Constants.LOG_CONST, "Transcoding info: " + info.toString());

         super.onProgressUpdate(values);
      }
   }

   private long mJumpToPositionWhenAvailable;
   private int mVideoWidth = 0;
   private int mVideoHeight = 0;
   private MediaPlayer mMediaPlayer;
   private TappableSurfaceView mSurface;
   private SurfaceHolder mHolder;
   private View mTopPanel = null;
   private View mBottomPanel = null;
   private long mLastActionTime = 0L;
   private boolean mIsPaused = false;
   private SeekBar mTimeline = null;
   private ImageButton mButtonPlayPause = null;
   public boolean mUpdating;
   public boolean mIsPlaying;
   private long mVideoLength = 0;
   private long mStartPosition;
   private Date mStartSeek;
   private ProgressBar mProgressbarSeeking;
   private Context mContext;

   private String mStreamingFile;
   private int mProgressLoaded;
   private DownloadItemType mStreamingType;
   private String mStreamingUrl;
   private FileInfo mFileInfo;
   private MediaInfo mMediaInfo;
   private boolean mUserTracking;
   private boolean mSurfaceCreated;
   private String mIdentifier;
   private String mProfile;
   private boolean mPanelsVisible;
   private TextView mTextViewVideoName;
   private StartVideoPlaybackTask mStartVideoTask;
   private boolean mAutoStart;
   private String mDisplayName;
   private boolean mStartMediaPlayerOnSurfaceCreated;
   private boolean mMediaPlayerPrepared = false;
   private TextView mTextViewVideoPosition;
   private ImageView mImageViewOverlay;
   private String mClientName;
   private LinearLayout mStartExternalPlayerOverlay;
   private boolean mUseInternalPlayer;
   private boolean mIsTv = false;
   private String[] mStreamingProfiles;
   private StartStreamingTask mStartStreamingTask;
   private boolean mIsRecording = false;
   private boolean mSeekingAllowedOnVideo;
   private CheckTranscodingTask mCheckTranscodingStateTask;
   private TextView mTextViewTranscodingStatus;
   private LinearLayout mLinearLayoutTranscodingStatus;
   private ProgressBar mProgressBarTranscodingStatus;
   private boolean mIsSeeking;
   private boolean mVideoNeedsPreconversion;
   private boolean mActivityActive;
   private ConnectionMonitor mConnectionReceiver;

   private static boolean USE_AUTOSTART = true;

   public void onCreate(Bundle _bundle) {
      super.onCreate(_bundle);
      setVolumeControlStream(AudioManager.STREAM_MUSIC);
      mSurfaceCreated = false;
      Thread.setDefaultUncaughtExceptionHandler(onUncaughtException);

      setContentView(R.layout.activity_videoplayer);

      if (android.os.Build.VERSION.SDK_INT >= 9) {
         // only allow usage of inbuilt video player if at least
         // gingerbread (2.3). If after 2.3, the user can still decide
         // to use an external player
         mUseInternalPlayer = !PreferencesManager.getUseExternalPlayer();
      } else {
         mUseInternalPlayer = false;
      }

      mProgressbarSeeking = (ProgressBar) findViewById(R.id.progressBarSeeking);
      mTextViewVideoName = (TextView) findViewById(R.id.TextViewVideoName);
      mTextViewVideoPosition = (TextView) findViewById(R.id.TextViewVideoPosition);

      mTextViewTranscodingStatus = (TextView) findViewById(R.id.TextViewCurrentServerState);
      mLinearLayoutTranscodingStatus = (LinearLayout) findViewById(R.id.LinearLayoutTranscodingStatus);
      mLinearLayoutTranscodingStatus.setVisibility(View.INVISIBLE);
      mProgressBarTranscodingStatus = (ProgressBar) findViewById(R.id.ProgressBarTranscodingStatus);

      mImageViewOverlay = (ImageView) findViewById(R.id.ImageViewVideoOverlay);
      mStartExternalPlayerOverlay = (LinearLayout) findViewById(R.id.LinearLayoutStartExternalPlayer);
      showExternalPlayerStartOverlay(false);
      mStartExternalPlayerOverlay.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View arg0) {
            playVideoInExternalPlayer();
         }
      });

      initialiseMediaPlayer();

      mTopPanel = findViewById(R.id.top_panel);
      mBottomPanel = findViewById(R.id.bottom_panel);

      mTimeline = (SeekBar) findViewById(R.id.timeline);
      if (mTimeline != null) {
         mTimeline.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar _seekbar) {
               setSeeking(true);
               mStartSeek = new Date();
               mUserTracking = false;

               mStartPosition = mVideoLength * _seekbar.getProgress() / 1000;
               mTimeline.setEnabled(false);

               downloadAndShowOverlayImage((int) (mStartPosition / 1000));

               if (mUseInternalPlayer) {
                  mMediaPlayer.stop();
                  mMediaPlayer.reset();
               }

               mStartStreamingTask = new StartStreamingTask();
               mStartStreamingTask.execute(false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar _seekbar) {
               mUserTracking = true;

               if (mUseInternalPlayer) {
                  mMediaPlayer.pause();
               }
               showExternalPlayerStartOverlay(false);
            }

            @Override
            public void onProgressChanged(SeekBar _seekbar, int _progress, boolean _fromUser) {
               if (_fromUser) {
                  long pos = (long) (mVideoLength * _seekbar.getProgress() / 1000);
                  mTextViewVideoPosition.setText(DateTimeHelper.getTimeStringFromMs(pos));
               }
            }
         });
      }

      mTimeline.setMax(1000);
      setSeeking(true);

      mButtonPlayPause = (ImageButton) findViewById(R.id.media);
      mButtonPlayPause.setOnClickListener(onMedia);

      Bundle extras = getIntent().getExtras();
      final Object retainData = getLastNonConfigurationInstance();

      mClientName = PreferencesManager.getClientName();

      mContext = this;

      if (retainData != null) {
         Log.d(Constants.LOG_CONST, "MediaPlayer: start from retained state");
         // we have retain data (from orientation change)
         PlaybackSession session = (PlaybackSession) retainData;
         mStreamingFile = session.getVideoId();
         mStreamingType = DownloadItemType.fromInt(session.getVideoType());
         mDisplayName = session.getDisplayName();
         mStartPosition = session.getStartPosition();
         mMediaInfo = session.getMetaData();
         mFileInfo = session.getVideoFile();
         mStreamingUrl = session.getVideoUrl();

         if (mMediaInfo != null) {
            mVideoLength = mMediaInfo.getDuration();
            mTimeline.setEnabled(true);
         }

         playVideo(true);
      } else if (extras != null) {
         Log.d(Constants.LOG_CONST, "MediaPlayer: start from fresh state");
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

         mVideoNeedsPreconversion = extras.getBoolean("video_needs_preconversion");

         mStreamingProfiles = extras.getStringArray("streaming_profiles");

         mIsTv = (mStreamingType == DownloadItemType.LiveTv);
         mIsRecording = (mStreamingType == DownloadItemType.TvRecording);

         downloadAndShowOverlayImage((int) (mStartPosition / 1000));

         new Thread(new Runnable() {
            public void run() {
               if (mIsTv) {

               } else if (mIsRecording) {
                  mMediaInfo = mService.getRecordingMediaInfo(Integer.parseInt(mStreamingFile));
               } else {
                  mFileInfo = mService.getFileInfo(mStreamingFile, mStreamingType);
                  mMediaInfo = mService.getMediaInfo(mStreamingFile, mStreamingType);
               }

               if (mMediaInfo != null) {
                  mVideoLength = mMediaInfo.getDuration();
               }

               mStartStreamingTask = new StartStreamingTask();
               mStartStreamingTask.execute(true);
            }
         }).start();
      } else {
         // something went wrong (false parameters?)
         Log.e(Constants.LOG_CONST, "Invalid parameters when opening video player");
         Util.showToast(this, "Invalid parameters when opening video player");
      }

      mTextViewVideoName.setText(mDisplayName);

      mConnectionReceiver = new ConnectionMonitor();

      getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
      getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
   }

   private void hideStatusBar() {
      WindowManager.LayoutParams attrs = getWindow().getAttributes();
      attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
      getWindow().setAttributes(attrs);
   }

   private void showStatusBar() {
      WindowManager.LayoutParams attrs = getWindow().getAttributes();
      attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
      getWindow().setAttributes(attrs);
   }

   private void initialiseMediaPlayer() {
      LinearLayout parent = (LinearLayout) findViewById(R.id.surface);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT);
      params.gravity = Gravity.CENTER;
      mSurface = new TappableSurfaceView(this);
      mSurface.setLayoutParams(params);

      mSurface.addTapListener(onTap);
      parent.removeAllViews();
      parent.addView(mSurface);

      mHolder = mSurface.getHolder();
      mHolder.addCallback(this);
      mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

      if (mUseInternalPlayer) {
         mMediaPlayer = new MediaPlayer();
         mMediaPlayer.setScreenOnWhilePlaying(true);
         mMediaPlayer.setOnPreparedListener(this);
         mMediaPlayer.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer arg0, int _error1, int _error2) {
               switch (_error1) {
               case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                  mTracker.trackEvent("MediaPlayer Errors", getMediaTypeString(),
                        "MEDIA_ERROR_SERVER_DIED", _error2);
                  if (mIsTv) {
                     Log.w(Constants.LOG_CONST, "Media died on tv, trying to restart");
                     restartTv();
                  } else {
                     showMediaDiedError();
                  }
                  break;
               case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                  mTracker.trackEvent("MediaPlayer Errors", getMediaTypeString(),
                        "MEDIA_ERROR_UNKNOWN", _error2);
                  showErrorToast("MEDIA_ERROR_UNKNOWN", _error2);
                  break;
               case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
                  mTracker.trackEvent("MediaPlayer Errors", getMediaTypeString(),
                        "MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK", _error2);
                  showErrorToast("MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK", _error2);
                  break;
               default:
                  mTracker.trackEvent("MediaPlayer Errors", getMediaTypeString(),
                        String.valueOf(_error1), _error2);
                  showErrorToast(String.valueOf(_error1), _error2);
                  Log.w(Constants.LOG_CONST, "Media Error: " + String.valueOf(_error1) + " | "
                        + String.valueOf(_error2));
               }

               return true;
            }
         });

         mMediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer _player, int _buffer) {
               // Log.d(Constants.LOG_CONST, "Buffering: " + _buffer);
            }
         });

         mMediaPlayer.setOnCompletionListener(this);
      }
   }

   protected String getMediaTypeString() {
      if (mIsTv) {
         return "LiveTv";
      } else if (mIsRecording) {
         return "TvRecording";
      } else {
         return "Media";
      }
   }

   public void showStartFromBeginningDialog() {
      AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
      builder.setTitle("Seeking not available");
      builder.setMessage("Seeking not available fo this video, start from pos 0 while waiting");
      builder.setCancelable(false);
      builder.setPositiveButton(getString(R.string.dialog_yes),
            new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                  mStartPosition = 0;
                  mStartStreamingTask = new StartStreamingTask();
                  mStartStreamingTask.execute(false);
               }
            });

      builder.setNegativeButton(getString(R.string.dialog_no),
            new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                  // show progress dialog
               }
            });

      AlertDialog alert = builder.create();
      alert.show();
   }

   public void setTranscodingStatus(String _text, boolean _progress) {
      if (_progress) {
         mProgressBarTranscodingStatus.setVisibility(View.VISIBLE);
      } else {
         mProgressBarTranscodingStatus.setVisibility(View.GONE);
      }

      if (_text != null) {
         mLinearLayoutTranscodingStatus.setVisibility(View.VISIBLE);
         mTextViewVideoName.setGravity(Gravity.RIGHT);
         mTextViewTranscodingStatus.setText(_text);
         mLinearLayoutTranscodingStatus.startAnimation(inFromLeftAnimation());
      } else {
         mTextViewVideoName.setGravity(Gravity.CENTER_HORIZONTAL);
         mLinearLayoutTranscodingStatus.startAnimation(outToLeftAnimation());
      }
   }

   private Animation inFromLeftAnimation() {
      Animation inFromLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f);
      inFromLeft.setDuration(500);
      inFromLeft.setInterpolator(new AccelerateInterpolator());
      inFromLeft.setFillAfter(true);
      return inFromLeft;
   }

   private Animation outToLeftAnimation() {
      Animation outtoLeft = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, -1.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f);
      outtoLeft.setDuration(500);
      outtoLeft.setInterpolator(new AccelerateInterpolator());
      outtoLeft.setFillAfter(true);
      return outtoLeft;
   }

   private String createIdentifier() {
      return "aMPdroid." + new Random().nextInt() + ".ts";
   }

   public void showInitError(Context _context) {
      if (mActivityActive) {
         AlertDialog.Builder builder = new AlertDialog.Builder(_context);
         builder.setTitle(getString(R.string.streaming_initfailed_title));
         builder.setMessage(getString(R.string.streaming_initfailed_text));
         builder.setCancelable(false);
         builder.setPositiveButton(getString(R.string.dialog_ok),
               new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                     finish();
                  }
               });

         AlertDialog alert = builder.create();
         alert.show();
      }
   }

   public void showMediaDiedError() {
      if (mActivityActive) {
         AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
         builder.setTitle(getString(R.string.streaming_media_died_title));
         builder.setMessage(getString(R.string.streaming_media_died_text));
         builder.setCancelable(false);
         builder.setPositiveButton(getString(R.string.dialog_yes),
               new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                     playVideoInExternalPlayer();
                     mStartSeek = new Date(); //
                     String oldIdentifier = mIdentifier; //
                     stopStreamingAsync(oldIdentifier); // mIdentifier =
                     createIdentifier();

                     mStartPosition = mStartPosition + mMediaPlayer.getCurrentPosition();
                     downloadAndShowOverlayImage((int) (mStartPosition / 1000));

                     setSeeking(true);
                     if (mUseInternalPlayer) {
                        mMediaPlayer.stop();
                        mMediaPlayer.reset();
                     }
                     showExternalPlayerStartOverlay(false);

                     mStartStreamingTask = new StartStreamingTask();
                     mStartStreamingTask.execute(false);

                  }
               });

         builder.setNegativeButton(getString(R.string.dialog_no),
               new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id) {
                     // finish();
                  }
               });

         AlertDialog alert = builder.create();
         alert.show();
      }
   }

   protected void showExternalPlayerStartOverlay(boolean _visible) {
      if (_visible) {
         mStartExternalPlayerOverlay.setVisibility(View.VISIBLE);
      } else {
         mStartExternalPlayerOverlay.setVisibility(View.INVISIBLE);
      }
   }

   protected void downloadAndShowOverlayImage(final int _position) {
      // not yet implemented for tv
      if (!mIsTv) {
         new Thread(new Runnable() {
            public void run() {
               try {
                  Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                        .getDefaultDisplay();
                  int pos = _position;
                  Bitmap bm = mService.getBitmapFromMedia(mStreamingType, mStreamingFile, pos,
                        display.getWidth(), display.getHeight());
                  final Bitmap displayImage = bm;
                  if (bm != null) {
                     mImageViewOverlay.post(new Runnable() {
                        public void run() {
                           setOverlayImage(new BitmapDrawable(displayImage));
                        }
                     });
                  }
               } catch (Exception ex) {
                  //Log.w(Constants.LOG_CONST, ex.toString());
                  
                  //TODO: I'm getting a nullpointer-exception here for some reason -> check out why and how to fix it
               }
            }
         }).start();
      }
   }

   protected void setSeeking(boolean _seeking) {
      mIsSeeking = _seeking;
      if (_seeking) {
         mProgressbarSeeking.setVisibility(View.VISIBLE);
         mTextViewVideoPosition.setVisibility(View.INVISIBLE);
      } else {
         mProgressbarSeeking.setVisibility(View.INVISIBLE);
         mTextViewVideoPosition.setVisibility(View.VISIBLE);
      }
   }

   protected void showErrorToast(String _error1, int _error2) {
      Util.showToast(this, "Error: " + _error1 + ", " + String.valueOf(_error2));
   }

   protected void showToast(String _msg2) {
      Util.showToast(this, _msg2);
   }

   @Override
   protected void onPause() {
      Log.d(Constants.LOG_CONST, "MediaPlayer onPause");

      stopMonitoringConnection();

      mIsPaused = true;

      if (!mIsTv) {
         PlaybackSession session = createPlaybackSession();

         Log.i(Constants.LOG_CONST, "Saving playback session state to db");
         try {
            PlaybackDatabaseHandler db = new PlaybackDatabaseHandler(this);
            db.open();
            db.savePlaybackSession(session);
            db.close();
         } catch (Exception ex) {
            Log.e(Constants.LOG_CONST,
                  "Error saving playback session state to db: " + ex.toString());
         }
      }
      super.onPause();
   }

   @Override
   protected void onStop() {
      Log.d(Constants.LOG_CONST, "MediaPlayer onStop");
      mActivityActive = false;
      if (mMediaPlayer != null) {
         mMediaPlayer.stop();
         Log.d(Constants.LOG_CONST, "MediaPlayer stop finished");
      }

      if (mCheckTranscodingStateTask != null) {
         mCheckTranscodingStateTask.stopChecking();
      }

      super.onStop();
   }

   @Override
   protected void onResume() {
      Log.d(Constants.LOG_CONST, "MediaPlayer onResume");
      startMonitoringConnection();
      super.onResume();

      mActivityActive = true;
      mIsPaused = false;
      mSurface.postDelayed(onEverySecond, 1000);

      if (!mIsTv && !mIsRecording) {
         mCheckTranscodingStateTask = new CheckTranscodingTask();
         mCheckTranscodingStateTask.execute(true);
      } else if (mIsRecording) {
         mSeekingAllowedOnVideo = true;
      } else if (mIsTv) {
         mSeekingAllowedOnVideo = false;
      }
   }

   @Override
   protected void onDestroy() {
      Log.d(Constants.LOG_CONST, "MediaPlayer onDestroy");
      onEverySecond = null;

      if (mMediaPlayer != null) {
         mMediaPlayer.release();
         mMediaPlayer = null;
      }

      mUpdating = false;

      stopStreamingAsync(mIdentifier);

      mSurface.removeTapListener(onTap);
      super.onDestroy();
   }

   private void stopStreamingAsync(final String _identifier) {
      new Thread(new Runnable() {
         public void run() {
            if (mIsTv) {
               mService.stopTvStreaming(_identifier);
            } else {
               mService.stopStreaming(_identifier);
            }
         }
      }).start();
   }

   private PlaybackSession createPlaybackSession() {
      PlaybackSession session = new PlaybackSession();
      session.setMetaData(mMediaInfo);
      session.setVideoFile(mFileInfo);

      if (mUseInternalPlayer) {
         session.setStartPosition(mStartPosition + mMediaPlayer.getCurrentPosition());
      } else {
         session.setStartPosition(mStartPosition);
      }

      session.setVideoId(mStreamingFile);
      session.setVideoType(DownloadItemType.toInt(mStreamingType));
      session.setVideoUrl(mStreamingUrl);
      session.setDisplayName(mDisplayName);
      session.setLastWatched(new Date());
      return session;
   }

   @Override
   public Object onRetainNonConfigurationInstance() {
      Log.d(Constants.LOG_CONST, "MediaPlayer onRetainNonConfigurationInstance");
      // TODO: For orientation changes -> will try again at a later point,
      // proves to be really difficult.
      /*
       * PlaybackSession session = createPlaybackSession();
       * 
       * if (mMediaPlayer != null) { mMediaPlayer.stop(); mMediaPlayer.reset();
       * mMediaPlayer.release(); }
       * 
       * return session;
       */
      return null;
   }

   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
      mLastActionTime = SystemClock.elapsedRealtime();

      /*
       * if (keyCode == KeyEvent.KEYCODE_BACK && (mTopPanel.getVisibility() ==
       * View.VISIBLE || mBottomPanel.getVisibility() == View.VISIBLE)) {
       * mLastActionTime = 0;
       * 
       * setPanelsVisible(false);
       * 
       * return (true); }
       */

      mSurface.setBackgroundDrawable(null);
      return (super.onKeyDown(keyCode, event));
   }

   public void onCompletion(MediaPlayer arg0) {
      Log.d(Constants.LOG_CONST, "MediaPlayer onCompletion");
      mStartPosition = 0;
   }

   public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
      Log.d(Constants.LOG_CONST, "MediaPlayer surfaceChanged");
   }

   public void surfaceDestroyed(SurfaceHolder surfaceholder) {
      Log.d(Constants.LOG_CONST, "MediaPlayer surfaceDestroyed");
   }

   public void surfaceCreated(SurfaceHolder holder) {
      Log.d(Constants.LOG_CONST, "MediaPlayer surfaceCreated");

      if (mUseInternalPlayer) {
         mMediaPlayer.setDisplay(holder);
         mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

         mStartSeek = new Date();

         mSurfaceCreated = true;

         if (mStartMediaPlayerOnSurfaceCreated) {
            prepareMediaPlayerAsync();
         }
      }
   }

   private void prepareMediaPlayerAsync() {
      try {
         Log.d(Constants.LOG_CONST, "MediaPlayer prepareMediaPlayerAsync");
         mMediaPlayerPrepared = false;

         mMediaPlayer.setDataSource(this, Uri.parse(mStreamingUrl));
         mMediaPlayer.prepareAsync();
      } catch (IllegalArgumentException e) {
         Log.e(Constants.LOG_CONST, e.toString());
      } catch (SecurityException e) {
         Log.e(Constants.LOG_CONST, e.toString());
      } catch (IllegalStateException e) {
         Log.e(Constants.LOG_CONST, e.toString());
      } catch (IOException e) {
         Log.e(Constants.LOG_CONST, e.toString());
      }

   }

   private void startCheckingPlayStatus() {
      Log.d(Constants.LOG_CONST, "MediaPlayer start checking transcoding status");
      if (mStartVideoTask != null) {
         mStartVideoTask.cancelTask();
      }
      mStartVideoTask = new StartVideoPlaybackTask(this, mService);
      mStartVideoTask.execute();

   }

   private void playVideoInExternalPlayer() {
      try {
         Intent i = new Intent(Intent.ACTION_VIEW);
         i.setDataAndType(Uri.parse(mStreamingUrl), "video/*");
         startActivityForResult(i, 1);
      } catch (Exception ex) {
         Log.e(Constants.LOG_CONST, ex.toString());
      }
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == 1) {
         if (resultCode < 0) {
            Util.showToast(this, getString(R.string.tvserver_errorplaying) + mStreamingUrl
                  + getString(R.string.tvserver_errorplaying_resultcode) + resultCode);
         } else {
            // Util.showToast(this, getString(R.string.tvserver_finishedplaying)
            // + mStreamingUrl);
         }
         if (mIsTv) {
            mService.stopTimeshift(PreferencesManager.getClientName());
         } else {
            showExternalPlayerStartOverlay(false);
            mStartStreamingTask = new StartStreamingTask();
            mStartStreamingTask.execute(false);
         }
      }

      super.onActivityResult(requestCode, resultCode, data);
   }

   private void playVideo(boolean autostart) {
      Log.d(Constants.LOG_CONST, "MediaPlayer playVideo");
      try {
         if (!autostart) {
            startCheckingPlayStatus();
         }
         mAutoStart = autostart;
         if (mSurfaceCreated) {
            prepareMediaPlayerAsync();
         } else {
            mStartMediaPlayerOnSurfaceCreated = true;
         }
      } catch (Throwable t) {
         Log.e(Constants.LOG_CONST, "Exception in media prep", t);
      }
   }

   protected void setOverlayImage(BitmapDrawable _bitmap) {
      if (_bitmap != null) {
         mImageViewOverlay.setImageDrawable(_bitmap);
      } else {
         // TODO: fade out
         mImageViewOverlay.setImageDrawable(null);
      }
   }

   public void onPrepared(MediaPlayer mediaplayer) {
      Log.d(Constants.LOG_CONST, "MediaPlayer onPrepared");
      mVideoWidth = mMediaPlayer.getVideoWidth();
      mVideoHeight = mMediaPlayer.getVideoHeight();

      if (mVideoWidth != 0 && mVideoHeight != 0) {

         // Get the width of the screen
         int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
         int screenHeight = getWindowManager().getDefaultDisplay().getHeight();

         // Get the SurfaceView layout parameters
         android.view.ViewGroup.LayoutParams lp = mSurface.getLayoutParams();

         if (screenWidth > screenHeight) {
            // landscape
            lp.width = screenWidth;
            lp.height = (int) (((float) mVideoHeight / (float) mVideoWidth) * (float) screenWidth);

         } else {
            lp.width = screenWidth;
            lp.height = (int) (((float) mVideoHeight / (float) mVideoWidth) * (float) screenWidth);
         }

         // // Commit the layout parameters
         mSurface.setLayoutParams(lp);

         // holder.setFixedSize(width, height);
         // timeline.setProgress(0);
         // timeline.setSecondaryProgress(0);

         if (mStartSeek != null) {
            String seekTime = "Time for seeking: "
                  + String.valueOf(new Date().getTime() - mStartSeek.getTime()) + " ms";

            Log.d(Constants.LOG_CONST, seekTime);
         }

         if (mAutoStart) {
            // if(mIsTv){
            // Log.d(Constants.LOG_CONST, "Sleeping");
            // try {
            // Thread.sleep(10000);
            // } catch (InterruptedException e) {
            // e.printStackTrace();
            // }
            // Log.d(Constants.LOG_CONST, "Sleeping end");
            // }
            mMediaPlayer.start();
            setOverlayImage(null);
         }
         mBottomPanel.setVisibility(View.VISIBLE);
         setSeeking(false);

         mMediaPlayerPrepared = true;
         mSurface.postDelayed(onEverySecond, 1000);
      }

      // media.setEnabled(true);
   }

   private TappableSurfaceView.TapListener onTap = new TappableSurfaceView.TapListener() {
      public void onTap(MotionEvent event) {
         mLastActionTime = SystemClock.elapsedRealtime();

         setPanelsVisible(!mPanelsVisible);
      }
   };

   private Runnable onEverySecond = new Runnable() {
      private long mCurrentPos;

      public void run() {
         try {
            if (mLastActionTime > 0 && SystemClock.elapsedRealtime() - mLastActionTime > 3000) {
               mLastActionTime = 0;

               if (mMediaPlayer.isPlaying()) {
                  setPanelsVisible(false);
               }
            }

            if (!mUserTracking) {
               if (mVideoLength > 0 && (!mUseInternalPlayer || mMediaPlayerPrepared)) {
                  if (mSeekingAllowedOnVideo && !mIsSeeking) {
                     mTimeline.setEnabled(true);
                  } else {
                     mTimeline.setEnabled(false);
                  }

                  if (mMediaPlayer != null) {
                     // internal player
                     mCurrentPos = (mStartPosition + mMediaPlayer.getCurrentPosition()) * 1000
                           / mVideoLength;
                     mTextViewVideoPosition
                           .setText(DateTimeHelper.getTimeStringFromMs(mStartPosition
                                 + mMediaPlayer.getCurrentPosition()));
                     Log.d(Constants.LOG_CONST + "_VIDEO",
                           "Pos: " + (mMediaPlayer.getCurrentPosition() / 1000));
                     // mTextViewVideoPosition.setText(String.valueOf(mMediaPlayer.getCurrentPosition()));

                     if (!mUserTracking) {
                        mTimeline.setProgress((int) mCurrentPos);
                     }
                  }
               } else {
                  mTimeline.setEnabled(false);
               }
            }

            if (!mIsPaused) {
               mSurface.postDelayed(onEverySecond, 1000);
            }
         } catch (Exception ex) {
            Log.e(Constants.LOG_CONST, "Error on runnable: " + ex.toString());
         }
      }
   };

   private View.OnClickListener onMedia = new View.OnClickListener() {
      public void onClick(View v) {
         mLastActionTime = SystemClock.elapsedRealtime();

         if (mUseInternalPlayer) {
            if (mMediaPlayer != null) {
               if (mMediaPlayer.isPlaying()) {
                  mButtonPlayPause.setImageResource(R.drawable.ic_media_play);
                  mMediaPlayer.pause();
                  // mMediaPlayer.stop();
                  // mMediaPlayer.reset();
               } else {
                  mButtonPlayPause.setImageResource(R.drawable.ic_media_pause);
                  mMediaPlayer.start();
                  // playVideo(mStreamingUrl, true);
               }
            }
         } else {
            playVideoInExternalPlayer();
         }
      }
   };

   private Thread.UncaughtExceptionHandler onUncaughtException = new Thread.UncaughtExceptionHandler() {
      public void uncaughtException(Thread thread, Throwable ex) {
         Log.e(Constants.LOG_CONST, "Uncaught exception", ex);
      }
   };
   private SubMenu mQualityItem;

   protected void setPanelsVisible(boolean _visible) {
      mPanelsVisible = _visible;
      if (_visible) {
         mTopPanel.setVisibility(View.VISIBLE);
         mBottomPanel.setVisibility(View.VISIBLE);
      } else {
         mTopPanel.setVisibility(View.GONE);
         mBottomPanel.setVisibility(View.GONE);
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu _menu) {
      if (!mIsTv) {
         // TODO: also enable beaming for tv
         MenuItem settingsItem = _menu.add(0, Menu.FIRST, Menu.NONE,
               getString(R.string.menu_beam_to_pc));
         settingsItem.setIcon(R.drawable.ic_menu_share);
         settingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
               if (mService.isClientControlConnected()) {
                  int pos = (int) ((mStartPosition + mMediaPlayer.getCurrentPosition()) / 1000);
                  mService.playVideoFileOnClient(mFileInfo.getFullPath(), pos);
                  mButtonPlayPause.setImageResource(R.drawable.ic_media_play);
                  mMediaPlayer.pause();
               }
               return true;
            }
         });
      }

      mQualityItem = _menu.addSubMenu(0, Menu.FIRST, Menu.NONE,
            getString(R.string.menu_select_stream_quality));
      mQualityItem.setIcon(R.drawable.ic_menu_equalizer);

      return true;
   }

   private void restartTv() {
      if (mUseInternalPlayer) {
         setSeeking(true);
         mMediaPlayer.stop();
         mMediaPlayer.reset();
         mMediaPlayer.release();
         setOverlayImage(null);
         mSurfaceCreated = false;
         mSurface.destroyDrawingCache();
         mSurface = null;
         mHolder = null;

         initialiseMediaPlayer();

         playVideo(USE_AUTOSTART);
      }
   }

   @Override
   public boolean onPrepareOptionsMenu(Menu menu) {
      showStatusBar();
      if (mStreamingProfiles != null) {
         mQualityItem.clear();
         for (final String p : mStreamingProfiles) {
            MenuItem profile = mQualityItem.add(0, Menu.FIRST, Menu.NONE, p);
            profile.setCheckable(true);
            if (mProfile.equals(p)) {
               profile.setChecked(true);
            }

            profile.setOnMenuItemClickListener(new OnMenuItemClickListener() {
               @Override
               public boolean onMenuItemClick(MenuItem item) {
                  changeStreamingQuality(p);
                  return true;
               }
            });
         }
      }
      return super.onPrepareOptionsMenu(menu);
   }

   @Override
   public void onOptionsMenuClosed(Menu menu) {
      hideStatusBar();
      super.onOptionsMenuClosed(menu);
   }
   
   

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      hideStatusBar();
      return super.onOptionsItemSelected(item);
   }

   protected void changeStreamingQuality(String _profile) {
      if (mProfile != _profile) {
         mProfile = _profile;
         mStartSeek = new Date();
         // String oldIdentifier = mIdentifier;
         // stopStreamingAsync(oldIdentifier);
         // mIdentifier = createIdentifier();

         mStartPosition = mStartPosition + mMediaPlayer.getCurrentPosition();
         downloadAndShowOverlayImage((int) (mStartPosition / 1000));

         setSeeking(true);
         if (mUseInternalPlayer) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
         }
         showExternalPlayerStartOverlay(false);

         mStartStreamingTask = new StartStreamingTask();
         mStartStreamingTask.execute(false);
      }
   }

   protected MediaPlayer getMediaPlayer() {
      return mMediaPlayer;
   }

   public void startMediaTaskFinished() {
      mStartVideoTask = null;
   }
}