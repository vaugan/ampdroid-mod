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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.bugsense.trace.BugSenseHandler;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.settings.SettingsActivity;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.RemoteClient;
import com.mediaportal.ampdroid.database.SettingsDatabaseHandler;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.utils.Util;

public class WelcomeScreenActivity extends Activity {
   private class StartupTask extends AsyncTask<RemoteClient, String, Boolean> {

      private Context mContext;

      public StartupTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected Boolean doInBackground(RemoteClient... _clients) {

         PreferencesManager.intitialisePreferencesManager(mContext);

         DataHandler.setupRemoteHandler(_clients[0], mContext, false);

         if (PreferencesManager.connectOnStartup()) {
            DataHandler client = DataHandler.getCurrentRemoteInstance();
            boolean success = client.connectClientControl();
            return success;
         }
         return true;
      }

      @Override
      protected void onProgressUpdate(String... _result) {

      }

      @Override
      protected void onPostExecute(Boolean _result) {
         if (PreferencesManager.connectOnStartup()) {
            if (_result) {
               Util.showToast(mContext, mContext.getString(R.string.connection_successful));
            } else {
               Util.showToast(mContext, mContext.getString(R.string.connection_failed));
            }
         }
         Intent myIntent = new Intent(mContext, HomeActivity.class);
         startActivity(myIntent);
      }
   }

   GoogleAnalyticsTracker mTracker;
   
   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_welcomescreen);
      BugSenseHandler.setup(this, "4434a1f4");
      //AdUtils.createAdForView(this, R.id.LinearLayoutAdMob);

      mTracker = GoogleAnalyticsTracker.getInstance();
      mTracker.start("UA-24774210-1", 300, this);
      
      mTracker.trackPageView("/WelcomeScreen");
   }

   @Override
   protected void onStart() {
      // disconnect client if one is currently connected
      DataHandler client = DataHandler.getCurrentRemoteInstance();
      if (client != null) {
         client.disconnectClientControl();
      }

      final ProgressBar progress = (ProgressBar) findViewById(R.id.ProgressBarWelcomeScreen);
      progress.setVisibility(View.INVISIBLE);
      final Button connectButton = (Button) findViewById(R.id.ButtonConnect);
      connectButton.setEnabled(true);

      final Spinner spinner = (Spinner) findViewById(R.id.SpinnerSelectClients);

      SettingsDatabaseHandler remoteClientsDb = new SettingsDatabaseHandler(this);
      remoteClientsDb.open();
      List<RemoteClient> clients = remoteClientsDb.getClients();
      remoteClientsDb.close();

      connectButton.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View _view) {
            StartupTask task = new StartupTask(_view.getContext());
            RemoteClient client = (RemoteClient) spinner.getSelectedItem();
            if (client != null) {
               task.execute(client);
               progress.setVisibility(View.VISIBLE);
               progress.setIndeterminate(true);
               connectButton.setEnabled(false);
            } else {
               Util.showToast(_view.getContext(), getString(R.string.welcome_no_client));
            }
         }
      });

      if (clients != null && clients.size() > 0) {
         ArrayAdapter<RemoteClient> adapter = new ArrayAdapter<RemoteClient>(this,
               android.R.layout.simple_spinner_item, clients);
         adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         spinner.setAdapter(adapter);
      } else {
         Util.showToast(this, getString(R.string.welcome_no_clients));
      }

      super.onStart();
   }
   
   

   @Override
   protected void onDestroy() {
      mTracker.stop();
      super.onDestroy();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu _menu) {
      MenuItem settingsItem = _menu
            .add(0, Menu.FIRST, Menu.NONE, getString(R.string.menu_settings));
      settingsItem.setIcon(R.drawable.ic_menu_preferences);
      settingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            startSettings();
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

   private void startSettings() {
      Intent settingsIntent = new Intent(this, SettingsActivity.class);
      startActivity(settingsIntent);
   }

   @Override
   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      Window window = getWindow();
      window.setFormat(PixelFormat.RGBA_8888);
   }
}
