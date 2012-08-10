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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.actionbar.ActionBar;
import com.mediaportal.ampdroid.activities.media.TabMoviesActivityGroup;
import com.mediaportal.ampdroid.activities.media.TabSeriesActivityGroup;
import com.mediaportal.ampdroid.activities.media.TabSharesActivity;
import com.mediaportal.ampdroid.activities.media.TabVideosActivityGroup;

public class MediaActivity extends BaseTabActivity {
   TabHost mTabHost;

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_media);

      ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
      actionBar.setTitle(getString(R.string.title_media));

      Bundle extras = getIntent().getExtras();
      if (extras != null) {
         boolean serviceConnected = extras.getBoolean("service_connected");
         if (serviceConnected) {
            boolean supportsVideo = extras.getBoolean("supports_video");
            boolean supportsMovies = extras.getBoolean("supports_movingpictures");
            boolean supportsTvSeries = extras.getBoolean("supports_series");
            
            mTabHost = getTabHost();
            mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
            if (supportsVideo) {
               /** tid1 is firstTabSpec Id. Its used to access outside. */
               TabSpec sharesTabSpec = mTabHost.newTabSpec("tid0");
               View tab = createTabView(this, getString(R.string.media_tabs_shares));
               sharesTabSpec.setIndicator(tab);
               sharesTabSpec.setContent(new Intent(this, TabSharesActivity.class));
               mTabHost.addTab(sharesTabSpec);

               TabSpec videosTabSpec = mTabHost.newTabSpec("tid1");
               videosTabSpec
                     .setIndicator(createTabView(this, getString(R.string.media_tabs_videos)));
               videosTabSpec.setContent(new Intent(this, TabVideosActivityGroup.class));
               mTabHost.addTab(videosTabSpec);
            }

            if (supportsTvSeries) {
               TabSpec seriesTabSpec = mTabHost.newTabSpec("tid2");
               seriesTabSpec
                     .setIndicator(createTabView(this, getString(R.string.media_tabs_series)));
               seriesTabSpec.setContent(new Intent(this, TabSeriesActivityGroup.class));
               mTabHost.addTab(seriesTabSpec);
            }

            if (supportsMovies) {
               TabSpec moviesTabSpec = mTabHost.newTabSpec("tid3");
               moviesTabSpec
                     .setIndicator(createTabView(this, getString(R.string.media_tabs_movies)));
               moviesTabSpec.setContent(new Intent(this, TabMoviesActivityGroup.class));
               mTabHost.addTab(moviesTabSpec);
            }
         } else {
            
            finish();
         }
      }
   }

   private static View createTabView(final Context _context, final String _text) {
      try {
         View view = LayoutInflater.from(_context).inflate(R.layout.tabs_bg, null);
         TextView tv = (TextView) view.findViewById(R.id.tabsText);
         tv.setText(_text);
         return view;
      } catch (Exception ex) {
         Log.d("Tab:", ex.toString());
         return null;
      }
   }
}
