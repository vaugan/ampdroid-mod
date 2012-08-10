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
package com.mediaportal.ampdroid.activities.tvserver;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseActivity;
import com.mediaportal.ampdroid.activities.StatusBarActivityHandler;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.TvChannel;
import com.mediaportal.ampdroid.data.TvChannelDetails;
import com.mediaportal.ampdroid.data.TvChannelGroup;
import com.mediaportal.ampdroid.data.TvRecording;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.views.TvServerChannelDetailsAdapterItem;
import com.mediaportal.ampdroid.quickactions.ActionItem;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.IntentUtils;
import com.mediaportal.ampdroid.utils.QuickActionUtils;
import com.mediaportal.ampdroid.utils.Util;

public class TvServerChannelsActivity extends BaseActivity {
   private DataHandler mService;
   private ListView mListView;
   private Spinner mGroupsSpinner;

   ArrayAdapter<TvChannel> mChannelItems;
   ArrayAdapter<TvChannelGroup> mGroupsItems;
   UpdateGroupsTask mGroupsUpdater;
   UpdateChannelsTask mChannelUpdater;
   ProgressDialog mLoadingDialog;
   String mPlayingUrl;

   boolean mShowAllGroup = false;
   private StatusBarActivityHandler mStatusBarHandler;
   private LazyLoadingAdapter mAdapter;
   protected TvChannelDetails mSelectedTvChannel;

   private class UpdateGroupsTask extends AsyncTask<Integer, Integer, List<TvChannelGroup>> {
      @Override
      protected List<TvChannelGroup> doInBackground(Integer... _group) {
         List<TvChannelGroup> groups = mService.getTvChannelGroups();
         return groups;
      }

      @Override
      protected void onPostExecute(List<TvChannelGroup> _result) {
         mGroupsItems.clear();
         if (_result != null) {
            if (_result.size() == 1 && !mShowAllGroup) {
               Util.showToast(getBaseContext(), getString(R.string.info_no_groups_defined));
            } else {
               for (TvChannelGroup g : _result) {
                  if (mShowAllGroup || g.getIdGroup() != 1) {
                     mGroupsItems.add(g);
                  }
               }
            }
         }
      }
   }

   private class UpdateChannelsTask extends AsyncTask<TvChannelGroup, Integer, List<TvChannelDetails>> {
      @Override
      protected List<TvChannelDetails> doInBackground(TvChannelGroup... _group) {
         List<TvChannelDetails> channels = mService.getTvChannelDetailsForGroup(_group[0].getIdGroup());
         return channels;
      }

      @Override
      protected void onPostExecute(List<TvChannelDetails> _result) {
         if (_result != null) {
            mAdapter.clear();

            for (TvChannelDetails c : _result) {
               mAdapter.addItem(new TvServerChannelDetailsAdapterItem(c));
            }
         }
         mAdapter.notifyDataSetChanged();
         mLoadingDialog.cancel();
      }
   }

   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setTitle(R.string.title_tvserver_channels);

      setContentView(R.layout.activity_tvserverchannels);
      mListView = (ListView) findViewById(R.id.ListViewChannels);

      mAdapter = new LazyLoadingAdapter(this);
      mListView.setAdapter(mAdapter);

      mGroupsSpinner = (Spinner) findViewById(R.id.SpinnerGroups);
      mGroupsItems = new ArrayAdapter<TvChannelGroup>(this, android.R.layout.simple_spinner_item);
      mGroupsItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      mGroupsSpinner.setAdapter(mGroupsItems);

      mService = DataHandler.getCurrentRemoteInstance();
      mStatusBarHandler = new StatusBarActivityHandler(this, mService);
      mStatusBarHandler.setHome(false);

      mGroupsSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
         @Override
         public void onItemSelected(AdapterView<?> _adapter, View _view, int _position, long _id) {
            TvChannelGroup group = (TvChannelGroup) mGroupsSpinner.getItemAtPosition(_position);

            refreshChannelList(group);
         }

         @Override
         public void onNothingSelected(AdapterView<?> _adapter) {

         }
      });

      mListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> _adapter, View _view, int _pos, long _id) {
            ILoadingAdapterItem item = (ILoadingAdapterItem) mListView.getItemAtPosition(_pos);
            mSelectedTvChannel = (TvChannelDetails) item.getItem();

            openDetails(mSelectedTvChannel);

         }
      });

      mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> _item, View _view, int _pos, long _id) {
            // ILoadingAdapterItem item = (ILoadingAdapterItem) mListView
            // .getItemAtPosition(_pos);
            final QuickAction qa = new QuickAction(_view);
            ILoadingAdapterItem item = (ILoadingAdapterItem) mListView.getItemAtPosition(_pos);
            mSelectedTvChannel = (TvChannelDetails) item.getItem();

            ActionItem playOnDeviceAction = new ActionItem();
            playOnDeviceAction.setTitle(getString(R.string.quickactions_streamdevice));
            playOnDeviceAction.setIcon(getResources().getDrawable(R.drawable.quickaction_stream));
            playOnDeviceAction.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View _view) {
                  TvChannel channel = mSelectedTvChannel;
                  startTvStreaming(channel);

                  qa.dismiss();
               }

            });
            qa.addActionItem(playOnDeviceAction);
            
            QuickActionUtils.createPlayOnClientQuickAction(_view.getContext(), qa, mService,
                  new OnClickListener() {
               @Override
               public void onClick(View _view) {
                  TvChannel channel = mSelectedTvChannel;
                  mService.playTvChannelOnClient(channel.getIdChannel(), true);

                  qa.dismiss();
               }
            });
            
            QuickActionUtils.createDetailsQuickAction(_view.getContext(), qa,
                  new View.OnClickListener() {
                     @Override
                     public void onClick(View arg0) {
                        openDetails(mSelectedTvChannel);
                        qa.dismiss();
                     }
                  });

            qa.show();
            return true;
         }
      });

      mShowAllGroup = PreferencesManager.getShowAllChannelsGroup();

      refreshGroups();
   }

   protected void openDetails(TvChannel channel) {
      Intent myIntent = new Intent(this, TvServerChannelDetailsActivity.class);
      myIntent.putExtra("channel_id", channel.getIdChannel());
      myIntent.putExtra("channel_name", channel.getDisplayName());
      Log.d(Constants.LOG_CONST, "Opening channel details for channel: " + channel.getIdChannel());
      startActivity(myIntent);
   }

   private void startTvStreaming(TvChannel _channel) {
      IntentUtils.startTvStreaming(this, mService, _channel);
   }

   private void refreshGroups() {
      mLoadingDialog = ProgressDialog.show(TvServerChannelsActivity.this,
            getString(R.string.tvserver_loadgroups), getString(R.string.info_loading_title), true);
      mLoadingDialog.setCancelable(true);

      mGroupsUpdater = new UpdateGroupsTask();
      mGroupsUpdater.execute(0);
   }

   private void refreshChannelList(TvChannelGroup _group) {
      mLoadingDialog.cancel();
      mLoadingDialog = ProgressDialog
            .show(TvServerChannelsActivity.this, getString(R.string.tvserver_loadchannels),
                  getString(R.string.info_loading_title), true);
      mLoadingDialog.setCancelable(true);

      mChannelUpdater = new UpdateChannelsTask();
      mChannelUpdater.execute(_group);
   }
}
