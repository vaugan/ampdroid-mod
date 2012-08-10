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

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseActivity;
import com.mediaportal.ampdroid.activities.StatusBarActivityHandler;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.TvChannel;
import com.mediaportal.ampdroid.data.TvSchedule;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.views.TvServerSchedulesDetailsViewItem;
import com.mediaportal.ampdroid.quickactions.ActionItem;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.utils.Util;

public class TvServerSchedulesActivity extends BaseActivity {
   private DataHandler mService;
   private ListView mListView;
   private LazyLoadingAdapter mAdapter;
   private UpdateSchedulesTask mRecordingsUpdater;
   private CancelScheduleTask mCancelScheduleTask;
   private StatusBarActivityHandler mStatusBarHandler;

   private class CancelScheduleTask extends AsyncTask<ILoadingAdapterItem, Boolean, Boolean> {
      private Context mContext;
      private CancelScheduleTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected Boolean doInBackground(ILoadingAdapterItem... _params) {
         ILoadingAdapterItem item = _params[0];
         TvSchedule schedule = (TvSchedule) item.getItem();
         mService.cancelTvScheduleByScheduleId(schedule.getIdSchedule());
         mAdapter.removeItem(item);
         
         return true;
      }

      @Override
      protected void onPostExecute(Boolean _result) {
         if (_result) {
            Util.showToast(mContext, getString(R.string.tvserver_schedulecanceled));

            mAdapter.notifyDataSetChanged();
         } else {
            Util.showToast(mContext, getString(R.string.tvserver_schedulecancel_failed));
         }
      }
   }

   private class UpdateSchedulesTask extends AsyncTask<Integer, Integer, List<TvSchedule>> {
      private HashMap<Integer, TvChannel> mChannels;
      private Context mContext;
      private UpdateSchedulesTask(Context _context){
         mContext = _context;
      }
      
      @Override
      protected List<TvSchedule> doInBackground(Integer... _params) {
         List<TvSchedule> recordings = mService.getTvSchedules();
         mChannels = new HashMap<Integer, TvChannel>();
         for (TvSchedule s : recordings) {
            if(!mChannels.containsKey(s.getIdChannel())){
               TvChannel channel = mService.getTvChannel(s.getIdChannel());
               mChannels.put(s.getIdChannel(), channel);
            }
         }
         
         return recordings;
      }

      @Override
      protected void onPostExecute(List<TvSchedule> _result) {
         if (_result != null) {
            for (TvSchedule s : _result) {
               TvChannel channel = mChannels.get(s.getIdChannel());
               mAdapter.addItem(new TvServerSchedulesDetailsViewItem(mContext, s, channel));
            }

            mListView.setAdapter(mAdapter);
         }
         mAdapter.showLoadingItem(false);
      }
   }

   @Override
   public void onCreate(Bundle _savedInstanceState) {
      setTitle(R.string.title_tvserver_schedules);
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_tvserverschedules);

      mListView = (ListView) findViewById(R.id.ListViewSchedules);

      mService = DataHandler.getCurrentRemoteInstance();
      mAdapter = new LazyLoadingAdapter(this);
      mListView.setAdapter(mAdapter);
      
      mService = DataHandler.getCurrentRemoteInstance();
      mStatusBarHandler = new StatusBarActivityHandler(this, mService);
      mStatusBarHandler.setHome(false);

      mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> _item, View _view, int _pos, long _id) {
            final ILoadingAdapterItem item = (ILoadingAdapterItem) mListView
                  .getItemAtPosition(_pos);
            final QuickAction qa = new QuickAction(_view);

            ActionItem addScheduleAction = new ActionItem();
            addScheduleAction.setTitle(getString(R.string.tvserver_cancel_recording));
            addScheduleAction.setIcon(getResources().getDrawable(R.drawable.quickaction_remove));
            addScheduleAction.setOnClickListener(new OnClickListener() {
               @Override
               public void onClick(View _view) {
                  mCancelScheduleTask = new CancelScheduleTask(_view.getContext());
                  mCancelScheduleTask.execute(item);

                  qa.dismiss();
               }
            });
            qa.addActionItem(addScheduleAction);
            qa.show();
            return true;
         }
      });
      refreshSchedules();
   }

   private void refreshSchedules() {
      mAdapter.showLoadingItem(true);
      mAdapter.setLoadingText(getString(R.string.tvserver_loadschedules));
      mRecordingsUpdater = new UpdateSchedulesTask(this);
      mRecordingsUpdater.execute(0);
   }
}
