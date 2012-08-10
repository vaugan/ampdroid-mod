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
package com.mediaportal.ampdroid.activities.music;

import java.util.ArrayList;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.mediaportal.ampdroid.utils.Constants;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

public class TabArtistsActivityGroup extends ActivityGroup {

   // Keep this in a static variable to make it accessible for all the nesten
   // activities, lets them manipulate the view
   private static TabArtistsActivityGroup mGroup;

   public static TabArtistsActivityGroup getGroup() {
      return mGroup;
   }

   // Need to keep track of the history if you want the back-button to work
   // properly, don't use this if your activities requires a lot of memory.
   private ArrayList<View> mHistory;
   private GoogleAnalyticsTracker mTracker;

   @Override
   protected void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      this.mHistory = new ArrayList<View>();
      mGroup = this;

      Intent intent = new Intent(this, TabArtistsActivity.class);
      intent.putExtra("activity_group", "artists");
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

      // Start the root activity withing the group and get its view
      View view = getLocalActivityManager().startActivity("ArtistsActivity", intent).getDecorView();

      mTracker = GoogleAnalyticsTracker.getInstance();
      mTracker.start(Constants.ANALYTICS_ID, this);
      
      // Replace the view of this ActivityGroup
      replaceView(view);

   }
   
   @Override
   protected void onDestroy() {
      mTracker.stop();
      super.onDestroy();
   }

   public void replaceView(View _view) {
      mTracker.trackPageView("/" + _view.getContext().getClass().getSimpleName());
      // Adds the old one to history
      mHistory.add(_view);
      // Changes this Groups View to the new View.
      setContentView(_view);
   }

   public void back() {
      if (mHistory.size() > 1) {
         mHistory.remove(mHistory.size() - 1);
         setContentView(mHistory.get(mHistory.size() - 1));
      } else {
         finish();
      }
   }

   @Override
   public void onBackPressed() {
      TabArtistsActivityGroup.mGroup.back();
      return;
   }

   @Override
   public boolean onKeyDown(int _keyCode, KeyEvent _event) {
      if (_keyCode == KeyEvent.KEYCODE_BACK) {
         TabArtistsActivityGroup.mGroup.back();
         return true;
      }
      return super.onKeyDown(_keyCode, _event);
   }

}
