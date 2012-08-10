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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseActivity;
import com.mediaportal.ampdroid.activities.StatusBarActivityHandler;
import com.mediaportal.ampdroid.activities.videoplayback.VideoStreamingPlayerActivity;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.TvChannel;
import com.mediaportal.ampdroid.data.TvChannelGroup;
import com.mediaportal.ampdroid.data.TvVirtualCard;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.views.VirtualCardStateAdapterItem;
import com.mediaportal.ampdroid.quickactions.ActionItem;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.Util;

public class TvServerStateActivity extends BaseActivity {
   private ListView mListView;
   private Button mStartTimeshift;
   private Button mStartRecording;
   private Spinner mGroups;
   private Spinner mChannels;
   private LazyLoadingAdapter mListItems;
   private DataHandler mService;
   ArrayAdapter<TvChannel> mChannelItems;
   ArrayAdapter<TvChannelGroup> mGroupsItems;

   UpdateGroupsTask mGroupsUpdater;
   UpdateChannelsTask mChannelUpdater;
   UpdateCardsTask mCardsUpdater;
   StartTimeshiftTask mTimeshiftStarter;
   StopTimeshiftTask mTimeshiftStopper;
   ProgressDialog mLoadingDialog;

   boolean mShowAllGroup = false;
   private StatusBarActivityHandler mStatusBarHandler;

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
            for (TvChannelGroup g : _result) {
               if (mShowAllGroup || g.getIdGroup() != 1) {
                  mGroupsItems.add(g);
               }
            }
         }
      }
   }

   private class UpdateChannelsTask extends AsyncTask<TvChannelGroup, Integer, List<TvChannel>> {
      @Override
      protected List<TvChannel> doInBackground(TvChannelGroup... _group) {
         List<TvChannel> channels = mService.getTvChannelsForGroup(_group[0].getIdGroup()); // get
         return channels;
      }

      @Override
      protected void onPostExecute(List<TvChannel> _result) {
         if (_result != null) {
            mChannelItems.clear();

            for (TvChannel c : _result) {
               mChannelItems.add(c);
            }
         }

         if (mIsActive) {
            mLoadingDialog.cancel();
         }
      }
   }

   private class UpdateCardsTask extends AsyncTask<Integer, Integer, List<TvVirtualCard>> {
      private Context mContext;

      UpdateCardsTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected List<TvVirtualCard> doInBackground(Integer... _params) {
         List<TvVirtualCard> cards = mService.getTvCardsActive();
         return cards;
      }

      @Override
      protected void onPostExecute(List<TvVirtualCard> _result) {
         super.onPostExecute(_result);
         mListItems.clear();
         if (_result != null) {
            for (TvVirtualCard c : _result) {
               mListItems.addItem(new VirtualCardStateAdapterItem(mContext, c));
            }
         }
         mListItems.notifyDataSetChanged();
      }
   }

   private class StartTimeshiftTask extends AsyncTask<TvChannel, Integer, String> {
      Context mContext;

      public StartTimeshiftTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected String doInBackground(TvChannel... _params) {
         if (_params != null) {
            try {
               String url = mService.startTimeshift(_params[0].getIdChannel(),
                     PreferencesManager.getClientName());
               return url;
            } catch (Exception ex) {
               Log.e(Constants.LOG_CONST, "Error during start timeshift: " + ex.toString());
               return null;
            }
         }
         return null;
      }

      @Override
      protected void onPostExecute(String _result) {
         if (_result != null) {
            Util.showToast(mContext, _result);
         } else {
            Util.showToast(mContext, getString(R.string.tvserver_starttimeshift_failed));
         }
         refreshActiveCards();
      }
   }

   private class StopTimeshiftTask extends AsyncTask<String, Integer, Boolean> {
      Context mContext;

      public StopTimeshiftTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected Boolean doInBackground(String... _params) {
         boolean result = mService.stopTimeshift(_params[0]);

         return result;
      }

      @Override
      protected void onPostExecute(Boolean _result) {
         if (_result) {
            Util.showToast(mContext, getString(R.string.tvserver_stoptimeshift));
         } else {
            Util.showToast(mContext, getString(R.string.tvserver_stoptimeshift_failed));
         }
         refreshActiveCards();
      }
   }

   @Override
   public void onCreate(Bundle _savedInstanceState) {
      setTitle(R.string.title_tvserver_state);
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_tvserverstate);
      mListView = (ListView) findViewById(R.id.ListViewCards);
      mStartTimeshift = (Button) findViewById(R.id.ButtonStartTimeshift);
      mStartRecording = (Button) findViewById(R.id.ButtonStartRecording);
      mChannels = (Spinner) findViewById(R.id.SpinnerChannels);
      mGroups = (Spinner) findViewById(R.id.SpinnerGroups);

      mGroups.setOnItemSelectedListener(new OnItemSelectedListener() {

         @Override
         public void onItemSelected(AdapterView<?> _adapter, View _view, int _position, long _id) {
            Object selected = mGroups.getItemAtPosition(_position);
            fillChannelList((TvChannelGroup) selected);
         }

         @Override
         public void onNothingSelected(AdapterView<?> _adapter) {

         }
      });

      mListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> _adapter, View _view, int _pos, long _id) {
            ILoadingAdapterItem selected = (ILoadingAdapterItem) mListView.getItemAtPosition(_pos);
            TvVirtualCard card = (TvVirtualCard) selected.getItem();
            Util.showToast(_view.getContext(), card.getRTSPUrl());
         }
      });

      mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> _item, View _view, final int _position,
               long _id) {
            final QuickAction qa = new QuickAction(_view);
            ActionItem sdCardAction = new ActionItem();

            sdCardAction.setTitle(getString(R.string.tvserver_stoptimeshift));
            sdCardAction.setIcon(getResources().getDrawable(R.drawable.quickaction_delete));

            VirtualCardStateAdapterItem selected = (VirtualCardStateAdapterItem) mListView
                  .getItemAtPosition(_position);
            final TvVirtualCard card = (TvVirtualCard) selected.getItem();

            sdCardAction.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View _view) {
                  stopTimeshift(card.getUser().getName());
                  qa.dismiss();
               }
            });
            qa.addActionItem(sdCardAction);

            ActionItem playOnDeviceAction = new ActionItem();
            playOnDeviceAction.setTitle(getString(R.string.quickactions_playdevice));
            playOnDeviceAction.setIcon(getResources().getDrawable(R.drawable.quickaction_play));
            playOnDeviceAction.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View _view) {
                  String playingUrl = card.getRTSPUrl();
                  if (playingUrl != null) {
                     try {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setDataAndType(Uri.parse(playingUrl), "video/*");
                        startActivityForResult(i, 1);
                     } catch (Exception ex) {
                        Log.e(Constants.LOG_CONST, ex.toString());
                     }
                  } else {
                     Util.showToast(_view.getContext(), getString(R.string.tvserver_errorplaying));
                  }

                  qa.dismiss();
               }
            });
            qa.addActionItem(playOnDeviceAction);

            qa.setAnimStyle(QuickAction.ANIM_AUTO);

            qa.show();
            return true;
         }
      });

      mStartTimeshift.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            startTimeshift();
         }
      });

      mStartRecording.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View v) {
            startRecording();
         }
      });

      mService = DataHandler.getCurrentRemoteInstance();
      mStatusBarHandler = new StatusBarActivityHandler(this, mService);
      mStatusBarHandler.setHome(false);

      mListItems = new LazyLoadingAdapter(this);
      mListView.setAdapter(mListItems);

      mChannelItems = new ArrayAdapter<TvChannel>(this, android.R.layout.simple_spinner_item);
      mChannelItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      mChannels.setAdapter(mChannelItems);

      mGroupsItems = new ArrayAdapter<TvChannelGroup>(this, android.R.layout.simple_spinner_item);
      mGroupsItems.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      mGroups.setAdapter(mGroupsItems);

      SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
      mShowAllGroup = prefs.getBoolean("tvserver_showall", true);

      refreshActiveCards();

      refreshGroups();
   }

   private void refreshGroups() {
      mLoadingDialog = ProgressDialog.show(TvServerStateActivity.this,
            getString(R.string.tvserver_loadgroups), getString(R.string.info_loading_title), true);
      mLoadingDialog.setCancelable(true);

      mGroupsUpdater = new UpdateGroupsTask();
      mGroupsUpdater.execute(0);
   }

   @Override
   protected void onStart() {
      super.onStart();
   }

   private void startTimeshift() {
      TvChannel channel = (TvChannel) mChannels.getSelectedItem();

      mTimeshiftStarter = new StartTimeshiftTask(this);
      mTimeshiftStarter.execute(channel);
   }

   private void stopTimeshift(String _name) {
      mTimeshiftStopper = new StopTimeshiftTask(this);
      mTimeshiftStopper.execute(_name);
   }

   private void startRecording() {
      Util.showToast(this, getString(R.string.info_not_implemented));
   }

   private void fillChannelList(TvChannelGroup _group) {
      mLoadingDialog.cancel();
      mLoadingDialog = ProgressDialog
            .show(TvServerStateActivity.this, getString(R.string.tvserver_loadchannels),
                  getString(R.string.info_loading_title), true);
      mLoadingDialog.setCancelable(true);

      mChannelUpdater = new UpdateChannelsTask();
      mChannelUpdater.execute(_group);
   }

   private void refreshActiveCards() {
      mCardsUpdater = new UpdateCardsTask(this);
      mCardsUpdater.execute(0);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu _menu) {
      MenuItem refreshItem = _menu.add(0, Menu.FIRST, Menu.NONE, getString(R.string.menu_refresh));
      refreshItem.setIcon(R.drawable.ic_menu_refresh);
      refreshItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            refreshActiveCards();
            return true;
         }
      });

      return true;
   }
}
