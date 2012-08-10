/*******************************************************************************
 * Copyright (c) 2011 Benjamin Gmeiner.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Benjamin Gmeiner - Project Owner
 ******************************************************************************/
package com.mediaportal.ampdroid.activities;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.actionbar.ActionBar;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.api.RemoteCommands;
import com.mediaportal.ampdroid.data.commands.RemoteKey;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.downloadservice.MediaItemType;
import com.mediaportal.ampdroid.lists.Utils;
import com.mediaportal.ampdroid.quickactions.QuickActionView;
import com.mediaportal.ampdroid.remote.RemoteImageMessage;
import com.mediaportal.ampdroid.remote.RemoteNowPlaying;
import com.mediaportal.ampdroid.remote.RemoteNowPlayingUpdate;
import com.mediaportal.ampdroid.remote.RemotePropertiesUpdate;
import com.mediaportal.ampdroid.remote.RemoteStatusMessage;
import com.mediaportal.ampdroid.remote.RemoteVolumeMessage;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.DownloaderUtils;
import com.mediaportal.ampdroid.utils.DpiUtils;
import com.mediaportal.ampdroid.utils.QuickActionUtils;
import com.mediaportal.ampdroid.utils.Util;

public class StatusBarActivityHandler {
   Activity mParent;
   DataHandler mRemote;
   TextView mStatusText;
   ImageButton mPauseButton;
   ImageButton mPrevButton;
   ImageButton mNextButton;
   ImageButton mVolumeButton;
   ImageButton mRemoteButton;
   SeekBar mSeekBar;
   SlidingDrawer mSlider;

   private boolean isHome = false;
   private ActionBar actionBar;
   private TextView mSliderTitleText;
   private SeekBar mPositionSlider;
   private boolean mProgressSeekBarChanging;
   private int mIgnoreProgressChangedCounter = 0;
   private boolean mVolumeSeekBarChanging;
   private Button mInfoButton;
   private TextView mSliderTextDetails;
   private RelativeLayout mStatusBar;
   private View mBottomMarginLayout;
   private ImageView mImage;
   private LinearLayout mLayoutDefault;
   private LinearLayout mLayoutContent;
   private LinearLayout mLayoutControls;
   private ImageButton mSwitchClientButton;
   private QuickActionView mQuickActionView;

   private static String currentStatusString;
   private static RemoteNowPlaying currentNowPlayingMessage;
   private static RemoteVolumeMessage currentVolumeMessage;
   private static boolean currentIsTvPlaying;
   private static String currentTitle;
   private static String currentDesc;
   private static int currentProgress;
   private static String currentImagePath;
   private static RemoteStatusMessage currentStatusMessage;

   public StatusBarActivityHandler(Activity _parent, DataHandler _remote) {
      this(_parent, _remote, false);
   }

   public StatusBarActivityHandler(Activity _parent, DataHandler _remote, boolean _isHome) {
      mParent = _parent;
      mRemote = _remote;
      isHome = _isHome;

      mSlider = (SlidingDrawer) mParent.findViewById(R.id.SlidingDrawerStatus);
      mStatusBar = (RelativeLayout) mParent.findViewById(R.id.BottomBarLayout);
      mBottomMarginLayout = (View) mParent.findViewById(R.id.BottomMarginLayout);

      mPauseButton = (ImageButton) mParent.findViewById(R.id.ImageButtonBottomPause);
      mPrevButton = (ImageButton) mParent.findViewById(R.id.ImageButtonBottomRewind);
      mNextButton = (ImageButton) mParent.findViewById(R.id.ImageButtonBottomNext);
      mRemoteButton = (ImageButton) mParent.findViewById(R.id.ImageButtonBottomRemote);
      mVolumeButton = (ImageButton) mParent.findViewById(R.id.ImageButtonBottomVolume);
      mInfoButton = (Button) mParent.findViewById(R.id.ButtonInfo);
      mImage = (ImageView) mParent.findViewById(R.id.ImageViewSlider);
      mLayoutDefault = (LinearLayout) mParent.findViewById(R.id.LinearLayoutDefault);
      mLayoutContent = (LinearLayout) mParent.findViewById(R.id.LinearLayoutContent);
      mLayoutControls = (LinearLayout) mParent.findViewById(R.id.LinearLayoutControls);

      mPositionSlider = (SeekBar) mParent.findViewById(R.id.SeekBarSliderPosition);
      if (mPositionSlider != null) {
         mPositionSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar _seekBar) {
               int seekValue = _seekBar.getProgress();// between 0 - 100
               if (mRemote.isClientControlConnected()) {
                  mRemote.sendClientPosition(seekValue);
                  mIgnoreProgressChangedCounter = 3;
               }

               mProgressSeekBarChanging = false;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
               mProgressSeekBarChanging = true;
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
         });
      }

      if (mPauseButton != null) {
         mPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               sendRemoteKey(RemoteCommands.pauseButton);
            }

         });

         mPauseButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               sendRemoteKey(RemoteCommands.stopButton);
               return true;
            }
         });
      }

      if (mPrevButton != null) {
         mPrevButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View _view) {
               sendRemoteKey(RemoteCommands.prevButton);
            }
         });
      }

      if (mNextButton != null) {
         mNextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View _view) {
               sendRemoteKey(RemoteCommands.nextButton);
            }
         });
      }

      if (mRemoteButton != null) {
         mRemoteButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View _view, MotionEvent _event) {
               if (_event.getAction() == MotionEvent.ACTION_DOWN) {
                  Util.Vibrate(mParent, 70);
               }
               return false;
            }
         });
         mRemoteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View _view) {
               // Util.Vibrate(_view.getContext(), 50);
               if (!mParent.getClass().equals(RemoteControlActivity.class)) {
                  Intent myIntent = new Intent(_view.getContext(), RemoteControlActivity.class);
                  mParent.startActivity(myIntent);
               }
            }
         });
      }

      if (mVolumeButton != null) {
         mVolumeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View _view) {
               if (mSeekBar.getVisibility() == View.VISIBLE) {
                  setVolumeControlsVisibility(true);
               } else {
                  setVolumeControlsVisibility(false);
               }
            }
         });

         mVolumeButton.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
               // send mute button
               mRemote.sendRemoteButton(RemoteCommands.muteButton);

               return true;
            }
         });
      }

      if (mInfoButton != null) {
         mInfoButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               mRemote.sendRemoteButton(RemoteCommands.infoButton);
            }
         });
      }

      mStatusText = (TextView) mParent.findViewById(R.id.TextViewSliderSatusText);
      mSliderTitleText = (TextView) mParent.findViewById(R.id.TextViewSliderTitle);
      mSliderTextDetails = (TextView) mParent.findViewById(R.id.TextViewSliderTextDetail);

      mSeekBar = (SeekBar) mParent.findViewById(R.id.SeekBarBottomVolume);
      mQuickActionView = (QuickActionView) mParent.findViewById(R.id.QuickActionNowPlaying);
      if (mQuickActionView != null && StatusBarActivityHandler.currentNowPlayingMessage != null) {
         updateQuickActionView(StatusBarActivityHandler.currentNowPlayingMessage);
      }

      mVolumeSeekBarChanging = false;

      if (mSeekBar != null) {
         mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar _seekBar) {
               mVolumeSeekBarChanging = false;
            }

            @Override
            public void onStartTrackingTouch(SeekBar _seekBar) {
               mVolumeSeekBarChanging = true;
               if (!mRemote.isClientControlConnected()) {
                  Util.showToast(mParent, mParent.getString(R.string.info_remote_notconnected));
               }
            }

            @Override
            public void onProgressChanged(SeekBar _seekBar, int _progress, boolean _fromUser) {
               if (_fromUser) {
                  int seekValue = _seekBar.getProgress();// between 0 - 20
                  Util.Vibrate(mParent, seekValue * 2);
                  if (mRemote.isClientControlConnected()) {
                     mRemote.sendClientVolume((int) seekValue * 5);
                  }
               }
            }
         });
      }

      actionBar = (ActionBar) mParent.findViewById(R.id.actionbar);

      if (actionBar != null) {
         actionBar.setTitle(mParent.getTitle());
         actionBar.setHome(isHome);

         if (!actionBar.isInitialised()) {
            mSwitchClientButton = (ImageButton) actionBar.getChangeClientButton();
            mSwitchClientButton.setTag("switchclient");
            mParent.registerForContextMenu(mSwitchClientButton);
            mSwitchClientButton.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View v) {
                  mSwitchClientButton.showContextMenu();
               }
            });
            final ProgressBar progress = (ProgressBar) actionBar.getProgressBar();
            final ImageButton searchButton = (ImageButton) actionBar.getSearchButton();
            final ImageButton logoutButton = (ImageButton)actionBar.getLogoutButton();
            searchButton.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View v) {
                  progress.setVisibility(View.INVISIBLE);
               }
            });

            ImageButton homeButton = (ImageButton) actionBar.getHomeButton();
            homeButton.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View v) {
                  Intent intent = new Intent(mParent, HomeActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  mParent.startActivity(intent);
               }
            });
            
            logoutButton.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View arg0) {
                  Intent intent = new Intent(mParent, WelcomeScreenActivity.class);
                  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  mParent.startActivity(intent);
               }
            });

            actionBar.setInitialised(true);
         }
      }

      fillDefaults();
   }

   private void fillDefaults() {
      if (StatusBarActivityHandler.currentStatusMessage != null
            && (StatusBarActivityHandler.currentStatusMessage.isIsPlaying() || StatusBarActivityHandler.currentStatusMessage
                  .isIsPaused())) {
         setNowPlayingInfoVisible(true);
      } else {
         setNowPlayingInfoVisible(false);
      }
      if (currentTitle != null && mStatusText != null && mSliderTitleText != null) {
         mStatusText.setText(currentTitle);
         mSliderTitleText.setText(currentTitle);
      }

      if (currentDesc != null && mSliderTextDetails != null) {
         mSliderTextDetails.setText(currentDesc);
      }

      setProgress(currentProgress);
      setVolume(currentVolumeMessage);
   }

   public void setNowPlayingInfoVisible(boolean _visible) {
      if (_visible) {
         if (mStatusText != null) {
            mStatusText.setVisibility(View.VISIBLE);
         }
         if (mLayoutContent != null) {
            mLayoutContent.setVisibility(View.VISIBLE);
         }
         if (mLayoutControls != null) {
            mLayoutControls.setVisibility(View.VISIBLE);
         }
         if (mLayoutDefault != null) {
            mLayoutDefault.setVisibility(View.INVISIBLE);
         }
      } else {
         if (mStatusText != null) {
            mStatusText.setVisibility(View.INVISIBLE);
         }
         if (mLayoutContent != null) {
            mLayoutContent.setVisibility(View.INVISIBLE);
         }
         if (mLayoutControls != null) {
            mLayoutControls.setVisibility(View.INVISIBLE);
         }
         if (mLayoutDefault != null) {
            mLayoutDefault.setVisibility(View.VISIBLE);
         }
      }
   }

   public void setConnected(boolean _connected) {
      if (mRemoteButton != null) {
         if (_connected) {
            mRemoteButton.setBackgroundResource(R.drawable.home_mepo_connected);
         } else {
            mRemoteButton.setBackgroundResource(R.drawable.home_mepo);
         }
      }
   }

   public void setHome(boolean isHome) {
      this.isHome = isHome;
   }

   public boolean isHome() {
      return isHome;
   }

   public boolean getLoading() {
      if (actionBar != null) {
         return actionBar.getLoading();
      } else {
         return false;
      }
   }

   public void setLoading(boolean _loading) {
      if (actionBar != null) {
         actionBar.setLoading(_loading);
      }
   }

   private void sendRemoteKey(RemoteKey _button) {
      Util.Vibrate(mParent, 50);
      if (mRemote.isClientControlConnected()) {
         mRemote.sendRemoteButton(_button);
      } else {
         Util.showToast(mParent, mParent.getString(R.string.info_remote_notconnected));
      }

   }

   public void setVolumeControlsVisibility(boolean _visibility) {
      if (_visibility) {
         if (mPrevButton != null)
            mPrevButton.setVisibility(View.VISIBLE);
         if (mNextButton != null)
            mNextButton.setVisibility(View.VISIBLE);
         if (mPauseButton != null)
            mPauseButton.setVisibility(View.VISIBLE);
         if (mRemoteButton != null)
            mRemoteButton.setVisibility(View.VISIBLE);

         if (mSeekBar != null)
            mSeekBar.setVisibility(View.INVISIBLE);
      } else {
         if (mPrevButton != null)
            mPrevButton.setVisibility(View.INVISIBLE);
         if (mNextButton != null)
            mNextButton.setVisibility(View.INVISIBLE);
         if (mPauseButton != null)
            mPauseButton.setVisibility(View.INVISIBLE);
         if (mRemoteButton != null)
            mRemoteButton.setVisibility(View.INVISIBLE);

         if (mSeekBar != null)
            mSeekBar.setVisibility(View.VISIBLE);
      }
   }

   public void setNowPlaying(RemoteNowPlaying _nowPlayingMessage) {
      if (_nowPlayingMessage != null) {
         setNowPlayingInfoVisible(true);
      }
      StatusBarActivityHandler.currentNowPlayingMessage = _nowPlayingMessage;
      StatusBarActivityHandler.currentIsTvPlaying = _nowPlayingMessage.isTv();

      if (mSliderTitleText != null && StatusBarActivityHandler.currentNowPlayingMessage != null) {
         int duration = StatusBarActivityHandler.currentNowPlayingMessage.getDuration();
         int position = StatusBarActivityHandler.currentNowPlayingMessage.getPosition();

         if (duration == 0) {
            mPositionSlider.setProgress(100);
         } else if (position == 0) {
            mPositionSlider.setProgress(0);
         } else {
            mPositionSlider.setProgress(duration * 100 / position);
         }
      }

      updateQuickActionView(_nowPlayingMessage);
   }

   private void updateQuickActionView(RemoteNowPlaying _nowPlaying) {
   // TODO: Needs better nowplaying info from WifiRemote
      if (false && mQuickActionView != null) {
         mQuickActionView.clearActionList();
         if (_nowPlaying.getMediaInfo() != null) {
            String filePath = _nowPlaying.getFile();
            String title = _nowPlaying.getMediaInfo().getTitle();
            String type = _nowPlaying.getMediaInfo().getMediaType();

            if (type != null) {
               if (type.equals("series")) {
                  String displayName = _nowPlaying.getMediaInfo().getSeriesName() + ": " + title;
                  String itemId = _nowPlaying.getMediaInfo().getItemId();
                  String dirName = DownloaderUtils.getTvEpisodePath(_nowPlaying.getMediaInfo()
                        .getSeriesName(), Integer.parseInt(_nowPlaying.getMediaInfo().getSeason()));
                  String fileName = dirName + Utils.getFileNameWithExtension(filePath, "\\");

                  File localFile = new File(DownloaderUtils.getBaseDirectory() + "/" + fileName);
                  if (localFile.exists()) {
                     QuickActionUtils.createPlayOnDeviceQuickAction(mParent, mQuickActionView,
                           localFile, MediaItemType.Video);
                  } else {
                     QuickActionUtils.createDownloadSdCardQuickAction(mParent, mQuickActionView,
                           mRemote, itemId, filePath, DownloadItemType.TvSeriesItem,
                           MediaItemType.Video, fileName, displayName);
                  }

                  if (Constants.ENABLE_STREAMING) {
                     QuickActionUtils.createStreamOnClientQuickAction(mParent, mQuickActionView,
                           mRemote, itemId, DownloadItemType.TvSeriesItem, fileName, displayName);

                  }
               } else if (type.equals("movie")) {
                  String movieFile = filePath;
                  if (movieFile != null) {
                     String dirName = "Movies/" + title + "/";
                     String fileName = dirName + Utils.getFileNameWithExtension(movieFile, "\\");

                     File localFile = new File(DownloaderUtils.getBaseDirectory() + "/" + fileName);

                     String displayName = title;
                     String itemId = String.valueOf(_nowPlaying.getMediaInfo().getItemId());

                     if (localFile.exists()) {
                        QuickActionUtils.createPlayOnDeviceQuickAction(mParent, mQuickActionView,
                              localFile, MediaItemType.Video);
                     } else {
                        QuickActionUtils.createDownloadSdCardQuickAction(mParent, mQuickActionView,
                              mRemote, itemId, movieFile, DownloadItemType.MovieItem,
                              MediaItemType.Video, fileName, displayName);
                     }

                     if (Constants.ENABLE_STREAMING) {
                        QuickActionUtils.createStreamOnClientQuickAction(mParent, mQuickActionView,
                              mRemote, itemId, DownloadItemType.MovieItem, fileName, displayName);

                     }

                     mQuickActionView.createActionList();
                  }
               } else if (type.equals("video")) {
                  String movieFile = filePath;
                  String dirName = DownloaderUtils.getMoviePath(title);
                  String fileName = dirName + Utils.getFileNameWithExtension(movieFile, "\\");

                  File localFile = new File(DownloaderUtils.getBaseDirectory() + "/" + fileName);

                  String displayName = title;
                  String itemId = filePath;

                  if (localFile.exists()) {
                     QuickActionUtils.createPlayOnDeviceQuickAction(mParent, mQuickActionView,
                           localFile, MediaItemType.Video);
                  } else {
                     QuickActionUtils.createDownloadSdCardQuickAction(mParent, mQuickActionView,
                           mRemote, itemId, movieFile, DownloadItemType.VideoShareItem,
                           MediaItemType.Video, fileName, displayName);
                  }

                  if (Constants.ENABLE_STREAMING) {
                     QuickActionUtils.createStreamOnClientQuickAction(mParent, mQuickActionView,
                           mRemote, itemId, DownloadItemType.VideoShareItem, fileName, displayName);
                  }
               }

               mQuickActionView.createActionList();
            }
         }
      }
   }

   public void setNowPlaying(RemoteNowPlayingUpdate _nowPlayingMessage) {
      if (_nowPlayingMessage != null && mPositionSlider != null && !mProgressSeekBarChanging) {
         StatusBarActivityHandler.currentIsTvPlaying = _nowPlayingMessage.isTv();
         if (mIgnoreProgressChangedCounter <= 0) {
            int pos = _nowPlayingMessage.getPosition();
            int duration = _nowPlayingMessage.getDuration();

            int progress = 0;
            if (pos != 0 && duration != 0) {
               progress = pos * 100 / duration;
            }
            setProgress(progress);
         } else {
            mIgnoreProgressChangedCounter--;
         }
      }
   }

   public void setPropertiesUpdate(final RemotePropertiesUpdate _property) {
      if (_property != null) {
         mParent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
               if (StatusBarActivityHandler.currentIsTvPlaying) {
                  if (_property.getTag().equals("#TV.View.description")) {
                     setTextDetails(_property.getValue());
                  } else if (_property.getTag().equals("#TV.View.title")) {
                     setStatusText(_property.getValue());
                     setTextTitle(_property.getValue());
                  }
               } else {
                  if (_property.getTag().equals("#Play.Current.Title")) {
                     setStatusText(_property.getValue());
                     setTextTitle(_property.getValue());
                  } else if (_property.getTag().equals("#Play.Current.Plot")) {
                     setTextDetails(_property.getValue());
                  }
               }
               if (_property.getTag().equals("#Play.Current.Thumb")) {

               }
            }
         });
      }
   }

   protected void setTextTitle(String value) {
      StatusBarActivityHandler.currentTitle = value;
      if (mSliderTitleText != null) {
         mSliderTitleText.setText(value);
      }
   }

   protected void setTextDetails(String value) {
      StatusBarActivityHandler.currentDesc = value;
      if (mSliderTextDetails != null) {
         mSliderTextDetails.setText(value);
      }
   }

   protected void setStatusText(String _text) {
      StatusBarActivityHandler.currentStatusString = _text;
      if (mStatusText != null) {
         mStatusText.setText(StatusBarActivityHandler.currentStatusString);
      }
   }

   private void setProgress(int _progress) {
      currentProgress = _progress;
      if (mPositionSlider != null) {
         mPositionSlider.setProgress(_progress);
      }
   }

   public void setStatus(RemoteStatusMessage _statusMessage) {
      StatusBarActivityHandler.currentStatusMessage = _statusMessage;
      if (_statusMessage != null) {
         if (!_statusMessage.isIsPlaying()) {
            mParent.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                  setNowPlayingInfoVisible(false);
               }
            });
         } else {
            setNowPlayingInfoVisible(true);
         }
      }
   }

   public void setVolume(final RemoteVolumeMessage _statusMessage) {
      StatusBarActivityHandler.currentVolumeMessage = _statusMessage;
      if (_statusMessage != null) {
         if (mSeekBar != null) {
            mParent.runOnUiThread(new Runnable() {
               @Override
               public void run() {
                  if (!mVolumeSeekBarChanging) {
                     int volume = _statusMessage.getVolume();
                     mSeekBar.setProgress((int) (volume * 0.2));
                  }
                  if (_statusMessage.isIsMuted()) {
                     mVolumeButton.setImageResource(R.drawable.button_volume_muted);
                  } else {
                     mVolumeButton.setImageResource(R.drawable.button_volume);
                  }
               }
            });
         }
      }
   }

   public boolean isVisible() {
      if (mStatusBar != null) {
         return mStatusBar.getVisibility() == View.VISIBLE;
      }
      return false;
   }

   public void hide() {
      if (mSlider != null && mStatusBar != null) {
         mSlider.setVisibility(View.GONE);
         mStatusBar.setVisibility(View.GONE);
         if (mBottomMarginLayout != null) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,
                  LayoutParams.FILL_PARENT);
            lp.gravity = Gravity.TOP;
            lp.setMargins(0, DpiUtils.getPxFromDpi(mParent, 50), 0, 0);
            mBottomMarginLayout.setLayoutParams(lp);
         }
      }
   }

   public void show() {
      if (mSlider != null && mStatusBar != null) {
         mSlider.setVisibility(View.VISIBLE);
         mStatusBar.setVisibility(View.VISIBLE);

         if (mBottomMarginLayout != null) {
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.FILL_PARENT,
                  LayoutParams.FILL_PARENT);
            lp.gravity = Gravity.TOP;
            lp.setMargins(0, DpiUtils.getPxFromDpi(mParent, 50), 0,
                  DpiUtils.getPxFromDpi(mParent, 90));
            mBottomMarginLayout.setLayoutParams(lp);
         }
      }
   }

   public void setImage(RemoteImageMessage _img) {
      if (_img == null) {
         StatusBarActivityHandler.currentImagePath = null;
         if (mImage != null) {
            mImage.setImageBitmap(null);
         }
      } else {
         StatusBarActivityHandler.currentImagePath = _img.getImagePath();
         byte[] bytes = _img.getImage();
         if (mImage != null) {
            mImage.setAlpha(50);
            mImage.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
         }
      }
   }

   public boolean isNewImage(String _filePath) {
      if (StatusBarActivityHandler.currentImagePath != null && _filePath != null
            && !StatusBarActivityHandler.currentImagePath.equals(_filePath)) {
         return false;
      } else {
         return true;
      }
   }
}
