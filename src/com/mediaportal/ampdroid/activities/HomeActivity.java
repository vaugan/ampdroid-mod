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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.api.ConnectionState;
import com.mediaportal.ampdroid.data.WebServiceDescription;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.settings.PreferencesManager.StatusbarAutohide;
import com.mediaportal.ampdroid.utils.AdUtils;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.DpiUtils;
import com.mediaportal.ampdroid.utils.Util;
import com.mediaportal.ampdroid.utils.WakeOnLan;

public class HomeActivity extends BaseActivity {
   private LoadServiceTask mServiceLoaderTask = null;

   private class LoadServiceTask extends AsyncTask<String, String, Object> {
      Context mContext;

      private LoadServiceTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected Object doInBackground(String... _params) {
         String loader = _params[0];

         if (loader.equals("media")) {
            try {
               WebServiceDescription functions = mService.getServiceDescription();

               if (functions != null) {
                  Log.d(Constants.LOG_CONST,
                        "Connected to WebService version " + functions.getServiceVersion());
                  if (functions.getMovingPicturesApiVersion() == 1
                        && functions.getTvSeriesApiVersion() == 1
                        && functions.getVideoApiVersion() == 1) {
                     Intent myIntent = new Intent(mContext, MediaActivity.class);

                     myIntent.putExtra("service_connected", true);

                     myIntent.putExtra("supports_video", functions.supportsVideo());
                     myIntent.putExtra("supports_series", functions.supportsTvSeries());
                     myIntent.putExtra("supports_movingpictures",
                           functions.supportsMovingPictures());
                     return myIntent;
                  } else {
                      return "Wrong service version";
                  }
               } else {
                  publishProgress(loader);
               }
            } catch (WebServiceLoginException ex) {
               return ex;
            }

         } else if (loader.equals("music")) {
            try {
               WebServiceDescription functions = mService.getServiceDescription();

               if (mIsActive) {
                  // the activity is still in the foreground
                  if (functions != null) {
                     Log.d(Constants.LOG_CONST,
                           "Connected to WebService version " + functions.getServiceVersion());
                     if (functions.getMusicApiVersion() == 1) {
                        Intent myIntent = new Intent(mContext, MusicActivity.class);
                        return myIntent;
                     } else {
                        return "Wrong service version";
                     }
                  } else {
                     publishProgress(loader);
                  }
               }
            } catch (WebServiceLoginException ex) {
               return ex;
            }
         } else if (loader.equals("tvservice")) {
            try {
               if (mService.isTvServiceActive()) {
                  Intent myIntent = new Intent(mContext, TvServerOverviewActivity.class);
                  return myIntent;
               } else {
                  publishProgress(loader);
               }
            } catch (WebServiceLoginException ex) {
               return ex;
            }
         }

         return null;
      }

      @Override
      protected void onProgressUpdate(String... _params) {
         String loader = _params[0];
         String mac = null;
         if (loader.equals("media") || loader.equals("music")) {
            mac = mService.getRemoteAccessApi().getMac();
         } else if (loader.equals("tvservice")) {
            mac = mService.getTvApi().getMac();
         }

         final String macString = mac;// we need a final for the dialog

         boolean autoConnect = PreferencesManager.isWolAutoConnect();

         if (autoConnect) {
            if (mac != null && !mac.equals("")) {
               // we have a valid mac and user wants autoWOL -> start WOL
               Util.showToast(mContext, getString(R.string.home_wol_running));
               WakeOnLan.sendMagicPacket(mac);

            } else {
               // user wants wol but hasn't entered a valid mac -> ask for it
               Util.showToast(mContext, getString(R.string.home_wol_nomac));
            }
         }
         if (!autoConnect) {
            if (mac != null & !mac.equals("")) {
               // no autoconnect, but valid mac -> ask user if WOL
               if (mIsActive) {
                  AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                  builder.setTitle(mContext.getString(R.string.home_wol_confirmation_title));
                  builder.setMessage(mContext.getString(R.string.home_wol_confirmation_text));
                  builder.setPositiveButton(mContext.getString(R.string.dialog_yes),
                        new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                              // user wants to send WOL
                              Util.showToast(mContext, getString(R.string.home_wol_running));
                              WakeOnLan.sendMagicPacket(macString);
                           }
                        });
                  builder.setNegativeButton(mContext.getString(R.string.dialog_no),
                        new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                              Util.showToast(mContext, getString(R.string.info_no_connection));
                              dialog.cancel();
                           }
                        });
                  builder.create().show();
               }

            } else {
               Util.showToast(mContext, getString(R.string.info_no_connection));
            }
         }

         super.onProgressUpdate(_params);
      }

      @Override
      protected void onPostExecute(Object _result) {
         if (_result != null) {
            if (_result.getClass() == Intent.class) {
               startActivity((Intent) _result);
            } else if (_result.getClass() == WebServiceLoginException.class) {
               WebServiceLoginException ex = (WebServiceLoginException) _result;
               Util.showToast(mContext, getString(R.string.info_wrong_username_password));
               Log.d(Constants.LOG_CONST, ex.toString());
            } else if (_result.getClass() == String.class){
               Util.showToast(mContext, (String) _result);
            }
         } else {

         }
         mStatusBarHandler.setLoading(false);
         mServiceLoaderTask = null;
      }
   }

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_homescreen);

      final ImageButton buttonRemote = (ImageButton) findViewById(R.id.ImageButtonRemote);
      buttonRemote.setOnClickListener(new View.OnClickListener() {
         public void onClick(View _view) {
            Util.Vibrate(_view.getContext(), 50);
            Intent myIntent = new Intent(_view.getContext(), RemoteControlActivity.class);
            startActivity(myIntent);
         }
      });

      final ImageButton buttonMusic = (ImageButton) findViewById(R.id.ImageButtonMusic);
      buttonMusic.setOnClickListener(new View.OnClickListener() {
         public void onClick(View _view) {
            Util.Vibrate(_view.getContext(), 50);
            if (mServiceLoaderTask == null) {
               mServiceLoaderTask = new LoadServiceTask(_view.getContext());
               mServiceLoaderTask.execute("music");
               mStatusBarHandler.setLoading(true);
            }
         }
      });

      final ImageButton buttonTv = (ImageButton) findViewById(R.id.ImageButtonTv);
      buttonTv.setOnClickListener(new View.OnClickListener() {
         public void onClick(View _view) {
            Util.Vibrate(_view.getContext(), 50);
            if (mServiceLoaderTask == null) {
               mServiceLoaderTask = new LoadServiceTask(_view.getContext());
               mServiceLoaderTask.execute("tvservice");
               mStatusBarHandler.setLoading(true);
            }
         }
      });

      final ImageButton buttonVideos = (ImageButton) findViewById(R.id.ImageButtonVideos);
      buttonVideos.setOnClickListener(new View.OnClickListener() {
         public void onClick(View _view) {
            Util.Vibrate(_view.getContext(), 50);
            if (mServiceLoaderTask == null) {
               mServiceLoaderTask = new LoadServiceTask(_view.getContext());
               mServiceLoaderTask.execute("media");
               mStatusBarHandler.setLoading(true);
            }
         }
      });

      final ImageButton buttonPictures = (ImageButton) findViewById(R.id.ImageButtonPictures);
      buttonPictures.setOnClickListener(new View.OnClickListener() {
         public void onClick(View _view) {
            Util.Vibrate(_view.getContext(), 50);
            Util.showToast(_view.getContext(), getString(R.string.info_not_implemented));
         }
      });

      final ImageButton buttonPlugins = (ImageButton) findViewById(R.id.ImageButtonPlugins);
      buttonPlugins.setOnClickListener(new View.OnClickListener() {
         public void onClick(View _view) {
            Util.Vibrate(_view.getContext(), 50);
            /*
             * if (mService.isClientControlConnected()) {
             * mService.removePlaylistItem("music", 0);
             * 
             * mService.movePlaylistItem("music", 0, 5);
             * 
             * mService.requestPlaylist("music");
             * 
             * mService.clearPlaylistItems("music"); }
             * 
             * /* Intent download = new Intent(_view.getContext(),
             * StreamingDetailsActivity.class); download.putExtra("video_id",
             * "1084101"); download.putExtra("video_type", 2);
             * download.putExtra("video_name",
             * "Community: Introduction to Film"); startActivity(download);
             */

            // Intent myIntent = new Intent(_view.getContext(),
            // VideoPlayerActivity.class);
            // startActivity(myIntent);
            // Util.showToast(_view.getContext(),
            // getString(R.string.info_not_implemented));

            // DownloadJob job = new DownloadJob();
            // job.setUrl("http://10.0.0.14:44321/GmaWebService/MediaAccessService/stream/StartStreaming?source=c:\\got.avi&profileName=android");
            // job.setFileName("got.mpeg");
            // job.setDisplayName("Streaming");
            // job.setMediaType(MediaItemType.Video);
            //
            // job.setAuth("admin", "admin");
            //
            // Intent download =
            // ItemDownloaderHelper.createDownloadIntent(_view.getContext(),
            // job);
            // startService(download);

            // try {
            // PackageManager mgr = getPackageManager();
            //
            // Intent intent = new Intent(Intent.ACTION_RUN);
            // intent.setClassName("com.quoord.tapatalkpro.activity",
            // "com.quoord.tapatalkpro.activity.ForumNavigationActivity");
            // intent.putExtra("url", "http://forum.team-mediaportal.com");
            // intent.putExtra("forumName", "MediaPortal");
            // intent.putExtra("forumId", "4549");
            // startActivity(intent);
            // } catch (Exception ex) {
            // Log.e(Constants.LOG_CONST, ex.toString());
            // }

            // List list = mgr.queryIntentActivities(intent,
            // PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
            //
            // if (list.size() > 0) {
            // Log.i("Log", "Have application" + list.size());
            // intent.putExtra("url", "http://forum.team-mediaportal.com");
            // intent.putExtra("forumName", "MediaPortal");
            // intent.putExtra("forumId", "4549");
            // startActivity(intent);
            // } else {
            // Log.i("Log", "None application");
            // }
         }
      });
      
      AdUtils.createAdForView(this, R.id.LinearLayoutAdMob);
   }

   private void setAdPosition() {
      LinearLayout layout = (LinearLayout) findViewById(R.id.LinearLayoutAdMob);
      FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
      lp.gravity = Gravity.BOTTOM;

      StatusbarAutohide hideMode = PreferencesManager.getStatusbarAutohide();
      if ((hideMode == StatusbarAutohide.ShowWhenConnected && mService != null && mService.isClientControlConnected()) ||
           hideMode == StatusbarAutohide.AlwaysShow
          ) {
         Log.d(Constants.LOG_CONST, "Position -> up");
         lp.setMargins(0, 0, 0, DpiUtils.getPxFromDpi(this, 90));
      } else {
         Log.d(Constants.LOG_CONST, "Position -> down");
         lp.setMargins(0, 0, 0, 0);
      }

      layout.setLayoutParams(lp);
   }

   @Override
   protected void onStart() {
      setAdPosition();
      
      super.onStart();
   }

   @Override
   public void stateChanged(final ConnectionState _state) {
      super.stateChanged(_state);
      runOnUiThread(new Runnable() {
         @Override
         public void run() {
            setAdPosition();
         }
      });
   }

   @Override
   public boolean onCreateOptionsMenu(Menu _menu) {
      super.onCreateOptionsMenu(_menu);
      MenuItem logoutItem = _menu.add(0, Menu.FIRST, Menu.NONE,
            getString(R.string.menu_quit_client));
      logoutItem.setIcon(R.drawable.ic_menu_exit);
      logoutItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            finish();
            return true;
         }
      });

      return true;
   }
}
