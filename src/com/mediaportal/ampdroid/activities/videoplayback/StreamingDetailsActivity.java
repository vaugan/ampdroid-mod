package com.mediaportal.ampdroid.activities.videoplayback;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseActivity;
import com.mediaportal.ampdroid.api.ApiCredentials;
import com.mediaportal.ampdroid.controls.ListViewItemOnTouchListener;
import com.mediaportal.ampdroid.data.FileInfo;
import com.mediaportal.ampdroid.data.MediaInfo;
import com.mediaportal.ampdroid.data.PlaybackSession;
import com.mediaportal.ampdroid.data.StreamProfile;
import com.mediaportal.ampdroid.database.PlaybackDatabaseHandler;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.downloadservice.DownloadJob;
import com.mediaportal.ampdroid.downloadservice.ItemDownloaderHelper;
import com.mediaportal.ampdroid.downloadservice.MediaItemType;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.DateTimeHelper;
import com.mediaportal.ampdroid.utils.StringUtils;
import com.mediaportal.ampdroid.utils.Util;

public class StreamingDetailsActivity extends BaseActivity {
   public FileInfo mFileInfo;
   public MediaInfo mMediaInfo;
   public Bitmap mVideoThumb;
   public boolean mFinishedLoading;
   private DownloadStreamToSDCard mStartStreamingDownloadTask;

   private class DownloadStreamToSDCard extends AsyncTask<Integer, Integer, Boolean> {
      private Context mContext;

      private DownloadStreamToSDCard(Context _context) {
         mContext = _context;
      }

      @Override
      protected Boolean doInBackground(Integer... arg0) {
         String id = "aMPdroid." + new Random().nextInt() + ".mpeg";
         String url = null;
         if (mIsTv) {
            boolean success = mService.initTvStreaming(id, "aMPdroid debug download",
                  Integer.valueOf(mStreamingFile), mSelectedProfile.getName());
            if (success) {
               url = mService.startTvStreaming(id, 0);
            }
         } else if (mStreamingType == DownloadItemType.TvRecording) {
            mService.initRecordingStreaming(id, "aMPdroid debug download",
                  Integer.valueOf(mStreamingFile), mSelectedProfile.getName(), 0);
            url = mService.startRecordingStreaming(id);
         } else {
            mService.initStreaming(id, "aMPdroid debug download", mStreamingType, mStreamingFile);
            url = mService.startStreaming(id, mSelectedProfile.getName(), 0);
         }

         ApiCredentials cred = mService.getDownloadCredentials();
         if (url != null) {
            DownloadJob job = new DownloadJob();
            job.setUrl(url);
            job.setFileName("streaming_debug.ts");
            job.setDisplayName(mStreamingFile);
            job.setMediaType(MediaItemType.Video);
            job.setLength(-99);
            if (cred.useAut()) {
               job.setAuth(cred.getUsername(), cred.getPassword());
            }

            Intent download = ItemDownloaderHelper.createDownloadIntent(mContext, job);
            startService(download);
         }

         return null;
      }

   }

   private class LoadStreamDetailsTask extends AsyncTask<Integer, Integer, Boolean> {
      Activity mContext;

      private LoadStreamDetailsTask(Activity _context) {
         mContext = _context;
      }

      @Override
      protected Boolean doInBackground(Integer... _params) {
         if (mStreamingType == DownloadItemType.LiveTv) {
            mProfiles = mService.getTvTranscoderProfiles();
            publishProgress(2);
         } else if (mStreamingType == DownloadItemType.TvRecording) {
            mMediaInfo = mService.getRecordingMediaInfo(Integer.parseInt(mStreamingFile));
            publishProgress(1);
            
            mProfiles = mService.getTvTranscoderProfiles();
            publishProgress(2);
         } else {
            mFileInfo = mService.getFileInfo(mStreamingFile, mStreamingType);
            publishProgress(0);

            mMediaInfo = mService.getMediaInfo(mStreamingFile, mStreamingType);
            publishProgress(1);

            mProfiles = mService.getTranscoderProfiles();
            publishProgress(2);

            int thumbPos = 10;
            if (mLastSession != null && mLastSession.getStartPosition() > 10000) {
               thumbPos = (int) (mLastSession.getStartPosition() / 1000);
            }

            Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
                  .getDefaultDisplay();
            mVideoThumb = mService.getBitmapFromMedia(mStreamingType, mStreamingFile, thumbPos,
                  display.getWidth(), display.getHeight());
            publishProgress(3);

         }
         return true;
      }

      @Override
      protected void onProgressUpdate(Integer... values) {
         int index = values[0];
         switch (index) {
         case 0:
            if (mFileInfo != null) {
               mTextViewVideoSize.setText(StringUtils.formatFileSize(mFileInfo.getLength()));
            }
            break;
         case 1:
            if (mMediaInfo != null) {
               mTextViewVideoFormat.setText(mMediaInfo.getSourceCodec());
               mTextViewVideoScale.setText(mMediaInfo.getDisplayAspectRatioString());
               mTextViewVideoLength.setText(DateTimeHelper.getTimeStringFromMs(mMediaInfo
                     .getDuration()));
            }
            break;
         case 2:
            if (mProfiles != null) {
               mProfileItems = new String[mProfiles.size()];
               for (int i = 0; i < mProfiles.size(); i++) {
                  mProfileItems[i] = mProfiles.get(i).getName();
               }
               setEnabled(mButtonSelectQuality, true);
               setEnabled(mButtonStartFromBeginning, true);
               setEnabled(mButtonUseRtsp, true);

               if (mLastSession != null) {
                  setEnabled(mButtonResumeFromPos, true);
               }

               setEnabled(mButtonRememberSettings, true);

               if (mProfiles.size() > 0) {
                  String savedDefault = PreferencesManager.getDefaultProfile(mIsTv);
                  if (savedDefault != null) {
                     boolean found = false;
                     for (StreamProfile p : mProfiles) {
                        if (p.getName().equals(savedDefault)) {
                           mSelectedProfile = p;
                           found = true;
                           break;
                        }
                     }
                     if (found == false) {
                        // delete saved profile (is invalid) and select first
                        // item
                        PreferencesManager.setDefaultProfile(null, mIsTv);
                        mSelectedProfile = mProfiles.get(0);
                     }
                  } else {
                     mSelectedProfile = mProfiles.get(0);
                  }

                  handleProfileSelected();
               }
            }
            break;
         case 3:
            if (mVideoThumb != null) {
               mImageViewVideoThumb.setImageBitmap(mVideoThumb);
            }
            break;
         }

         super.onProgressUpdate(values);
      }

      @Override
      protected void onPostExecute(Boolean _result) {
         mFinishedLoading = true;
      }
   }

   private LinearLayout mButtonStartFromBeginning;
   private LinearLayout mButtonResumeFromPos;
   private LinearLayout mButtonSelectQuality;
   private TextView mTextViewVideoFormat;
   private TextView mTextViewVideoScale;
   private TextView mTextViewVideoLength;
   private TextView mTextViewVideoSize;
   private ImageView mImageViewVideoThumb;
   private List<StreamProfile> mProfiles;
   private String[] mProfileItems;
   private StreamProfile mSelectedProfile;
   private String mDisplayName;
   private String mStreamingFile;
   private DownloadItemType mStreamingType;
   private LoadStreamDetailsTask mLoadStreamDetailsTask;
   private TextView mTextViewVideoName;
   private PlaybackSession mLastSession;
   private LinearLayout mButtonRememberSettings;
   private boolean mIsTv;
   private LinearLayout mButtonUseRtsp;
   protected String mPlayingUrl;
   private FrameLayout mStreamingHeader;
   private int mVideoLength;

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_streamingdetails);

      mStreamingHeader = (FrameLayout) findViewById(R.id.LinearLayoutHeader);

      mButtonStartFromBeginning = (LinearLayout) findViewById(R.id.LinearLayoutStartFromBeginning);
      mButtonStartFromBeginning.setOnTouchListener(new ListViewItemOnTouchListener());
      mButtonStartFromBeginning.setOnClickListener(new OnClickListener() {

         @Override
         public void onClick(View _view) {
            if (mFinishedLoading && mSelectedProfile != null) {
               if (getChecked(mButtonRememberSettings)) {
                  PreferencesManager.setDefaultProfile(mSelectedProfile.getName(), mIsTv);
               }

               Intent streamIntent = new Intent(_view.getContext(),
                     VideoStreamingPlayerActivity.class);
               streamIntent.putExtra("video_id", mStreamingFile);
               streamIntent.putExtra("video_type", DownloadItemType.toInt(mStreamingType));
               streamIntent.putExtra("video_name", mDisplayName);
               streamIntent.putExtra("profile_name", mSelectedProfile.getName());
               streamIntent.putExtra("streaming_profiles", mProfileItems);
               if (mVideoLength > 0) {
                  streamIntent.putExtra("video_length", mVideoLength);
               }
               if (mMediaInfo != null) {
                  streamIntent.putExtra("video_needs_preconversion",
                        mMediaInfo.isStreamingNeedsPreconversion());
               }
               _view.getContext().startActivity(streamIntent);
            }
         }
      });

      mButtonStartFromBeginning.setOnLongClickListener(new View.OnLongClickListener() {
         @Override
         public boolean onLongClick(final View _view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(_view.getContext());
            builder.setTitle("Really download stream");
            builder
                  .setMessage("This is a debug option to track down stream errors. Really download this stream to your sd card?");
            builder.setCancelable(false);
            builder.setPositiveButton(getString(R.string.dialog_yes),
                  new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                        mStartStreamingDownloadTask = new DownloadStreamToSDCard(_view.getContext());
                        mStartStreamingDownloadTask.execute();
                     }
                  });

            builder.setNegativeButton(getString(R.string.dialog_no),
                  new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {
                     }
                  });

            AlertDialog alert = builder.create();
            alert.show();
            return false;
         }
      });

      mButtonUseRtsp = (LinearLayout) findViewById(R.id.LinearLayoutUseRtsp);
      mButtonUseRtsp.setOnTouchListener(new ListViewItemOnTouchListener());
      mButtonUseRtsp.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View _view) {
            if (mFinishedLoading) {
               mPlayingUrl = mService.startTimeshift(Integer.parseInt(mStreamingFile),
                     PreferencesManager.getClientName());
               if (mPlayingUrl != null) {
                  try {
                     Intent i = new Intent(Intent.ACTION_VIEW);
                     i.setDataAndType(Uri.parse(mPlayingUrl), "video/*");
                     startActivityForResult(i, 1);
                  } catch (Exception ex) {
                     Log.e(Constants.LOG_CONST, ex.toString());
                  }
               } else {
                  Log.w(Constants.LOG_CONST, "Couldn't start timeshift on server");
                  Util.showToast(_view.getContext(), getString(R.string.tvserver_errorplaying));
               }
            }
         }
      });

      mButtonResumeFromPos = (LinearLayout) findViewById(R.id.LinearLayoutResumeFrom);
      mButtonResumeFromPos.setOnTouchListener(new ListViewItemOnTouchListener());
      mButtonResumeFromPos.setOnClickListener(new OnClickListener() {

         @Override
         public void onClick(View _view) {
            if (mFinishedLoading && mSelectedProfile != null) {
               Intent streamIntent = new Intent(_view.getContext(),
                     VideoStreamingPlayerActivity.class);
               streamIntent.putExtra("video_id", mStreamingFile);
               streamIntent.putExtra("video_type", DownloadItemType.toInt(mStreamingType));
               streamIntent.putExtra("video_name", mDisplayName);
               streamIntent.putExtra("profile_name", mSelectedProfile.getName());
               streamIntent.putExtra("video_startfrom", mLastSession.getStartPosition());
               streamIntent.putExtra("streaming_profiles", mProfileItems);
               if (mVideoLength > 0) {
                  streamIntent.putExtra("video_length", mVideoLength);
               }
               if (mMediaInfo != null) {
                  streamIntent.putExtra("video_needs_preconversion",
                        mMediaInfo.isStreamingNeedsPreconversion());
               }
               _view.getContext().startActivity(streamIntent);
            }
         }
      });

      mButtonSelectQuality = (LinearLayout) findViewById(R.id.LinearLayoutStreamingQuality);
      mButtonSelectQuality.setOnTouchListener(new ListViewItemOnTouchListener());
      mButtonSelectQuality.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View _view) {
            if (mFinishedLoading && mProfileItems != null) {
               AlertDialog.Builder builder = new AlertDialog.Builder(_view.getContext());
               builder.setTitle("Profiles");

               builder.setItems(mProfileItems, new AlertDialog.OnClickListener() {

                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                     mSelectedProfile = mProfiles.get(which);
                     handleProfileSelected();
                  }
               });

               builder.show();
            }
         }
      });

      mButtonRememberSettings = (LinearLayout) findViewById(R.id.LinearLayoutRememberSettings);
      mButtonRememberSettings.setOnTouchListener(new ListViewItemOnTouchListener());
      mButtonRememberSettings.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View _view) {
            if (mFinishedLoading) {
               setChecked(mButtonRememberSettings, !getChecked(mButtonRememberSettings));
            }
         }
      });

      mTextViewVideoFormat = (TextView) findViewById(R.id.TextViewVideoFormat);
      mTextViewVideoScale = (TextView) findViewById(R.id.TextViewVideoScale);
      mTextViewVideoLength = (TextView) findViewById(R.id.TextViewVideoLength);
      mTextViewVideoSize = (TextView) findViewById(R.id.TextViewVideoSize);
      mImageViewVideoThumb = (ImageView) findViewById(R.id.ImageViewVideoThumb);
      mTextViewVideoName = (TextView) findViewById(R.id.TextViewVideoName);
      // mTextViewResumeFromText = (TextView)
      // findViewById(R.id.TextViewResumeFromText);
      // mImageViewCheckbox = (ImageView)findViewById(R.id.ImageViewCheckbox);
      // mImageViewCheckbox.setImageResource(android.R.drawable.checkbox_off_background);
      // mImageViewCheckbox.setBackgroundResource(android.R.drawable.btn_check_buttonless_off)

      Bundle extras = getIntent().getExtras();
      if (extras != null) {
         mStreamingFile = extras.getString("video_id");
         mStreamingType = DownloadItemType.fromInt(extras.getInt("video_type", 0));
         mDisplayName = extras.getString("video_name");

         if (extras.containsKey("video_length")) {
            mVideoLength = extras.getInt("video_length");
         }
         mTextViewVideoName.setText(mDisplayName);

         if (mStreamingType == DownloadItemType.LiveTv) {
            mIsTv = true;
            setVisible(mButtonUseRtsp, true);
            setVisible(mButtonResumeFromPos, false);

            mStreamingHeader.setVisibility(View.GONE);
         } else if (mStreamingType == DownloadItemType.TvRecording) {
            setVisible(mButtonUseRtsp, false);
            setVisible(mButtonResumeFromPos, true);
            mStreamingHeader.setVisibility(View.GONE);
         } else {
            setVisible(mButtonUseRtsp, false);
            setVisible(mButtonResumeFromPos, true);
         }
      }

   }

   public void handleProfileSelected() {
      if (mSelectedProfile != null) {
         setText(mButtonStartFromBeginning, getString(R.string.streaming_playfromstart_text)
               + mSelectedProfile.getName());
      }
   }

   private void setVisible(LinearLayout _button, boolean _visible) {
      if (_visible) {
         _button.setVisibility(View.VISIBLE);
      } else {
         _button.setVisibility(View.GONE);
      }

   }

   public void setTitle(LinearLayout _button, String _text) {
      TextView title = (TextView) _button.findViewById(R.id.TextViewTitle);
      if (title != null) {
         title.setTextColor(Color.WHITE);
      }
   }

   public void setText(LinearLayout _button, String _text) {
      TextView text = (TextView) _button.findViewById(R.id.TextViewText);
      if (text != null) {
         text.setTextColor(Color.WHITE);
         text.setText(_text);
      }
   }

   public void setChecked(LinearLayout _button, boolean _checked) {
      CheckBox cb = (CheckBox) _button.findViewById(R.id.CheckBoxChecked);
      if (cb != null) {
         cb.setChecked(_checked);
      }
   }

   public boolean getChecked(LinearLayout _button) {
      CheckBox cb = (CheckBox) _button.findViewById(R.id.CheckBoxChecked);
      if (cb != null) {
         return cb.isChecked();
      }
      return false;
   }

   public void setEnabled(LinearLayout _button, boolean _enabled) {
      TextView title = (TextView) _button.findViewById(R.id.TextViewTitle);
      TextView text = (TextView) _button.findViewById(R.id.TextViewText);
      _button.setEnabled(_enabled);
      if (_enabled) {
         if (text != null)
            text.setTextColor(Color.WHITE);
         if (title != null)
            title.setTextColor(Color.WHITE);
      } else {
         if (text != null)
            text.setTextColor(Color.GRAY);
         if (title != null)
            title.setTextColor(Color.GRAY);
      }
   }

   @Override
   protected void onResume() {
      mFinishedLoading = false;
      if (!mIsTv) {
         Log.i(Constants.LOG_CONST, "getting playback session state from db");
         mLastSession = null;
         try {
            PlaybackDatabaseHandler db = new PlaybackDatabaseHandler(this);
            db.open();
            mLastSession = db.getPlaybackSession(mStreamingType, mStreamingFile);
            db.close();
         } catch (Exception ex) {
            Log.e(Constants.LOG_CONST,
                  "Error getting playback session state from db: " + ex.toString());
         }
      }

      setEnabled(mButtonResumeFromPos, false);
      setEnabled(mButtonRememberSettings, false);
      setEnabled(mButtonSelectQuality, false);
      setEnabled(mButtonUseRtsp, false);
      setEnabled(mButtonStartFromBeginning, false);

      mLoadStreamDetailsTask = new LoadStreamDetailsTask(this);
      mLoadStreamDetailsTask.execute();

      if (mLastSession != null) {
         setText(mButtonResumeFromPos, getString(R.string.streaming_resumefrompos_text)
               + DateTimeHelper.getTimeStringFromMs(mLastSession.getStartPosition()));
      }

      super.onStart();
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == 1) {
         if (resultCode < 0) {
            Util.showToast(this, getString(R.string.tvserver_errorplaying) + mPlayingUrl
                  + getString(R.string.tvserver_errorplaying_resultcode) + resultCode);
         } else {
            Util.showToast(this, getString(R.string.tvserver_finishedplaying) + mPlayingUrl);
         }
         mService.stopTimeshift(PreferencesManager.getClientName());
      }

      super.onActivityResult(requestCode, resultCode, data);
   }

}
