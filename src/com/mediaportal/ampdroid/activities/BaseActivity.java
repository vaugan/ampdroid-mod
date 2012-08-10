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

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.api.ConnectionState;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.api.IClientControlListener;
import com.mediaportal.ampdroid.asynctasks.ReconnectTask;
import com.mediaportal.ampdroid.data.RemoteClient;
import com.mediaportal.ampdroid.database.SettingsDatabaseHandler;
import com.mediaportal.ampdroid.remote.RemoteAuthenticationResponse;
import com.mediaportal.ampdroid.remote.RemoteImageMessage;
import com.mediaportal.ampdroid.remote.RemoteNowPlaying;
import com.mediaportal.ampdroid.remote.RemoteNowPlayingUpdate;
import com.mediaportal.ampdroid.remote.RemotePropertiesUpdate;
import com.mediaportal.ampdroid.remote.RemoteStatusMessage;
import com.mediaportal.ampdroid.remote.RemoteVolumeMessage;
import com.mediaportal.ampdroid.remote.RemoteWelcomeMessage;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.settings.PreferencesManager.StatusbarAutohide;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.Util;

public class BaseActivity extends Activity implements IClientControlListener {
   protected class ConnectClientControlTask extends AsyncTask<String, String, Boolean> {
      private Context mContext;

      protected ConnectClientControlTask(Context _parent) {
         mContext = _parent;
      }

      @Override
      protected Boolean doInBackground(String... _keys) {
         if (!mService.isClientControlConnected()) {
            return mService.connectClientControl();
         }
         return false;
      }

      @Override
      protected void onPostExecute(Boolean _result) {
         super.onPostExecute(_result);
         if (_result) {
            Util.showToast(mContext, mContext.getString(R.string.connection_successful));
         } else {
            Util.showToast(mContext, mContext.getString(R.string.connection_failed));
         }
         mConnectTask = null;
      }
   }

   protected DataHandler mService;
   protected StatusBarActivityHandler mStatusBarHandler;
   private ConnectClientControlTask mConnectTask;
   private MenuItem mConnectItem;
   protected ReconnectTask mReconnectTask;
   protected boolean mIsActive;
   protected GoogleAnalyticsTracker mTracker;

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);

      PreferencesManager.intitialisePreferencesManager(this);

      if (PreferencesManager.isFullscreen()) {
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
               WindowManager.LayoutParams.FLAG_FULLSCREEN);
      }
      
      mTracker = GoogleAnalyticsTracker.getInstance();
      mTracker.start(Constants.ANALYTICS_ID, this);
   }

   @Override
   protected void onStart() {
      super.onStart();
      mService = DataHandler.getCurrentRemoteInstance();
      if (mService != null) {
         mStatusBarHandler = new StatusBarActivityHandler(this, mService);
         mService.addClientControlListener(this);
         handleAutoHide();
         mStatusBarHandler.setConnected(mService.isClientControlConnected());
      }
   }

   private void handleAutoHide() {
      StatusbarAutohide autohide = PreferencesManager.getStatusbarAutohide();

      if (mStatusBarHandler != null) {
         switch (autohide) {
         case AlwaysHide:
            mStatusBarHandler.hide();
            break;
         case AlwaysShow:
            mStatusBarHandler.show();
            break;
         case ShowWhenConnected:
            if (mService.isClientControlConnected()) {
               mStatusBarHandler.show();
            } else {
               mStatusBarHandler.hide();
            }
            break;
         }
      }
   }

   @Override
   protected void onStop() {
      mIsActive = false;
      if(mReconnectTask != null){
         mReconnectTask.cancelReConnect();
      }
      if (mService != null) {
         mService.removeClientControlListener(this);
      }
      super.onStop();
   }

   @Override
   protected void onResume() {
      mTracker.trackPageView("/" + this.getLocalClassName());
      mIsActive = true;
      if (PreferencesManager.getAutoReconnect()) {
         mReconnectTask = new ReconnectTask();
         mReconnectTask.execute(mService);
      }
      super.onResume();
   }
   
   @Override
   protected void onDestroy() {
      mTracker.stop();
      super.onDestroy();
   }
   
   public boolean getIsActive() {
      return mIsActive;
   }

   @Override
   public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      if (v.getTag() != null && v.getTag().equals("switchclients")) {
         menu.setHeaderTitle(getString(R.string.menu_clients));

         SettingsDatabaseHandler remoteClientsDb = new SettingsDatabaseHandler(this);
         remoteClientsDb.open();
         List<RemoteClient> clients = remoteClientsDb.getClients();
         remoteClientsDb.close();

         for (int i = 0; i < clients.size(); i++) {
            MenuItem item = menu.add(0, Menu.FIRST, Menu.NONE, clients.get(i).getClientName());
            item.setOnMenuItemClickListener(new OnMenuItemClickListener() {

               @Override
               public boolean onMenuItemClick(MenuItem item) {
                  // TODO: Client switching
                  notImplemented();
                  return false;
               }
            });
         }
      }
   }

   protected void notImplemented() {
      Util.showToast(this, getString(R.string.info_not_implemented));
   }

   @Override
   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      Window window = getWindow();
      window.setFormat(PixelFormat.RGBA_8888);
   }

   @Override
   public boolean onPrepareOptionsMenu(Menu menu) {
      if (mConnectItem != null) {
         String title = null;
         if (!mService.isClientControlConnected()) {
            title = getString(R.string.menu_connect);
            mConnectItem.setIcon(R.drawable.ic_menu_connect);
         } else {
            title = getString(R.string.menu_disconnect);
            mConnectItem.setIcon(R.drawable.ic_menu_disconnect);
         }
         mConnectItem.setTitle(title);
      }
      return super.onPrepareOptionsMenu(menu);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu _menu) {
      // MenuItem settingsItem = _menu
      // .add(0, Menu.FIRST, Menu.NONE, getString(R.string.menu_settings));
      // settingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
      // @Override
      // public boolean onMenuItemClick(MenuItem item) {
      // startSettings();
      // return true;
      // }
      // });

      String title = null;
      if (!mService.isClientControlConnected()) {
         title = getString(R.string.menu_connect);
      } else {
         title = getString(R.string.menu_disconnect);
      }

      mConnectItem = _menu.add(0, Menu.FIRST, Menu.NONE, title);
      mConnectItem.setIcon(R.drawable.ic_menu_connect);
      mConnectItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            if (!mService.isClientControlConnected()) {
               connectClientControl();
            } else {
               disconnectClientControl();
            }
            return true;
         }
      });

      MenuItem downloadsItem = _menu.add(0, Menu.FIRST, Menu.NONE,
            getString(R.string.menu_downloads));
      downloadsItem.setIcon(R.drawable.ic_menu_save);
      downloadsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            startDownloads();
            return true;
         }

      });

      return true;
   }

   private void startDownloads() {
      Intent myIntent = new Intent(this, DownloadsActivity.class);
      startActivity(myIntent);
   }

   protected void connectClientControl() {
      if (mConnectTask == null) {
         mConnectTask = new ConnectClientControlTask(this);
         mConnectTask.execute();
      }
   }

   private void disconnectClientControl() {
      mService.disconnectClientControl();
   }

   @Override
   public void messageReceived(Object _message) {
      if (_message.getClass().equals(RemoteStatusMessage.class)) {
         mStatusBarHandler.setStatus((RemoteStatusMessage) _message);
      } else if (_message.getClass().equals(RemoteNowPlaying.class)) {
         mStatusBarHandler.setNowPlaying((RemoteNowPlaying) _message);
      } else if (_message.getClass().equals(RemoteNowPlayingUpdate.class)) {
         mStatusBarHandler.setNowPlaying((RemoteNowPlayingUpdate) _message);
      } else if (_message.getClass().equals(RemotePropertiesUpdate.class)) {
         if (((RemotePropertiesUpdate) _message).getTag().equals("#Play.Current.Thumb")) {
            String filePath = ((RemotePropertiesUpdate) _message).getValue();
            if (filePath == null || filePath.equals("")) {
               mStatusBarHandler.setImage(null);
            } else if (mStatusBarHandler.isNewImage(filePath)) {
               mService.getClientImage(filePath);
            }
         }
         mStatusBarHandler.setPropertiesUpdate((RemotePropertiesUpdate) _message);
      } else if (_message.getClass().equals(RemoteWelcomeMessage.class)) {
         RemoteVolumeMessage vol = ((RemoteWelcomeMessage) _message).getVolume();
         mStatusBarHandler.setVolume(vol);
      } else if (_message.getClass().equals(RemoteVolumeMessage.class)) {
         RemoteVolumeMessage vol = ((RemoteVolumeMessage) _message);
         mStatusBarHandler.setVolume(vol);
      } else if (_message.getClass().equals(RemoteImageMessage.class)) {
         RemoteImageMessage img = (RemoteImageMessage) _message;
         mStatusBarHandler.setImage(img);
      } else if (_message.getClass().equals(RemoteAuthenticationResponse.class)) {
         RemoteAuthenticationResponse auth = (RemoteAuthenticationResponse) _message;
         if (!auth.isSuccess()) {
            Util.showToast(this, auth.getErrorMessage());
         }
      }
   }

   @Override
   public void stateChanged(final ConnectionState _state) {
      runOnUiThread(new Runnable() {
         @Override
         public void run() {
            if (mIsActive) {
               handleAutoHide();
               if (_state == ConnectionState.Disconnected) {
                  if (mConnectItem != null) {
                     mConnectItem.setTitle(getString(R.string.menu_connect));
                     mConnectItem.setIcon(R.drawable.ic_menu_connect);
                  }
                  if (mStatusBarHandler != null) {
                     mStatusBarHandler.setNowPlayingInfoVisible(false);
                     mStatusBarHandler.setConnected(false);
                  }

                  if (PreferencesManager.getAutoReconnect()) {
                     mReconnectTask = new ReconnectTask();
                     mReconnectTask.execute(mService);
                  }
               } else if (_state == ConnectionState.Connected) {
                  if (mConnectItem != null) {
                     mConnectItem.setTitle(getString(R.string.menu_disconnect));
                     mConnectItem.setIcon(R.drawable.ic_menu_disconnect);
                  }
                  if (mStatusBarHandler != null) {
                     mStatusBarHandler.setNowPlayingInfoVisible(true);
                     mStatusBarHandler.setConnected(true);
                  }
               }
            }
         }
      });
   }
}
