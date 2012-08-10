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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.tvserver.TvServerChannelsActivity;
import com.mediaportal.ampdroid.activities.tvserver.TvServerEpgActivity;
import com.mediaportal.ampdroid.activities.tvserver.TvServerRecordingsActivity;
import com.mediaportal.ampdroid.activities.tvserver.TvServerSchedulesActivity;
import com.mediaportal.ampdroid.activities.tvserver.TvServerStateActivity;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.lists.TvServerFeature;
import com.mediaportal.ampdroid.lists.TvServerFeaturesAdapter;

public class TvServerOverviewActivity extends BaseActivity {
   private ListView mListView;
   private TvServerFeaturesAdapter mFeaturesAdapter;
   private DataHandler mService;
   private StatusBarActivityHandler statusBarHandler;

   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_tvserver);

      mService = DataHandler.getCurrentRemoteInstance();
      statusBarHandler = new StatusBarActivityHandler(this, mService);
      statusBarHandler.setHome(false);

      mListView = (ListView) findViewById(R.id.ListViewItems);
      mListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> _adapter, View _view, int _position, long _id) {
            if (_position == 0) {
               Intent myIntent = new Intent(_view.getContext(), TvServerStateActivity.class);
               startActivity(myIntent);
            }

            if (_position == 1) {
               Intent myIntent = new Intent(_view.getContext(), TvServerEpgActivity.class);
               startActivity(myIntent);
            }

            if (_position == 2) {
               Intent myIntent = new Intent(_view.getContext(), TvServerChannelsActivity.class);
               startActivity(myIntent);
            }

            if (_position == 3) {
               Intent myIntent = new Intent(_view.getContext(), TvServerSchedulesActivity.class);
               startActivity(myIntent);
            }

            if (_position == 4) {
               Intent myIntent = new Intent(_view.getContext(), TvServerRecordingsActivity.class);
               startActivity(myIntent);
            }
         }
      });

      mFeaturesAdapter = new TvServerFeaturesAdapter(this);
      mFeaturesAdapter.addFeature(new TvServerFeature(getString(R.string.tvserver_manual_title),
            getString(R.string.tvserver_manual_desc), R.drawable.tvserver_manual));
      mFeaturesAdapter.addFeature(new TvServerFeature(getString(R.string.tvserver_epg_title),
            getString(R.string.tvserver_epg_desc), R.drawable.tvserver_tv));
      mFeaturesAdapter.addFeature(new TvServerFeature(getString(R.string.tvserver_channel_title),
            getString(R.string.tvserver_channel_desc), R.drawable.tvserver_tv));
      mFeaturesAdapter.addFeature(new TvServerFeature(getString(R.string.tvserver_schedules_title),
            getString(R.string.tvserver_schedules_desc), R.drawable.tvserver_schedules));
      mFeaturesAdapter.addFeature(new TvServerFeature(
            getString(R.string.tvserver_recordings_title),
            getString(R.string.tvserver_recordings_desc), R.drawable.tvserver_recordings));

      mListView.setAdapter(mFeaturesAdapter);
   }

}
