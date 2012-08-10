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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseActivity;
import com.mediaportal.ampdroid.activities.StatusBarActivityHandler;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.TvChannel;
import com.mediaportal.ampdroid.data.TvRecording;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.views.TvServerRecordingsThumbsViewItem;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.utils.QuickActionUtils;

public class TvServerRecordingsActivity extends BaseActivity {
   private DataHandler mService;
   private ListView mListView;
   private LazyLoadingAdapter mAdapter;
   private UpdateRecordingsTask mRecordingsUpdater;
   private StatusBarActivityHandler mStatusBarHandler;
   protected TvRecording mSelectedRecording;
   private HashMap<Integer, TvChannel> mChannels;

   private class UpdateRecordingsTask extends AsyncTask<Integer, Integer, List<TvRecording>> {

      private Context mContext;

      private UpdateRecordingsTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected List<TvRecording> doInBackground(Integer... _params) {
         List<TvRecording> recordings = mService.getTvRecordings();
         if (recordings != null) {
            mChannels = new HashMap<Integer, TvChannel>();
            for (TvRecording s : recordings) {
               if (!mChannels.containsKey(s.getIdChannel())) {
                  TvChannel channel = mService.getTvChannel(s.getIdChannel());
                  if (channel != null) {
                     mChannels.put(s.getIdChannel(), channel);
                  }
               }
            }
         }
         return recordings;
      }

      @Override
      protected void onPostExecute(List<TvRecording> _result) {
         if (_result != null) {
            for (TvRecording r : _result) {
               TvChannel channel = mChannels.get(r.getIdChannel());
               mAdapter.addItem(new TvServerRecordingsThumbsViewItem(mContext, r, channel));
            }

            mListView.setAdapter(mAdapter);
         }
         mAdapter.showLoadingItem(false);
      }
   }

   @Override
   public void onCreate(Bundle _savedInstanceState) {
      setTitle(R.string.title_tvserver_recordings);
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_tvserverrecordings);
      mListView = (ListView) findViewById(R.id.ListViewRecordings);
      mListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> _adapter, View _view, int _pos, long _id) {
            ILoadingAdapterItem item = (ILoadingAdapterItem) mListView.getItemAtPosition(_pos);
            mSelectedRecording = (TvRecording) item.getItem();
            openDetails(mSelectedRecording);
         }
      });

      mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> _item, View _view, int _pos, long _id) {
            ILoadingAdapterItem item = (ILoadingAdapterItem) mListView.getItemAtPosition(_pos);
            final QuickAction qa = new QuickAction(_view);
            mSelectedRecording = (TvRecording) item.getItem();

            QuickActionUtils.createPlayOnClientQuickAction(_view.getContext(), qa, mService,
                  mSelectedRecording.getFileName());

            QuickActionUtils.createStreamOnClientQuickAction(_view.getContext(), qa, mService,
                  String.valueOf(mSelectedRecording.getIdRecording()),
                  DownloadItemType.TvRecording, mSelectedRecording.getFileName(),
                  mSelectedRecording.getTitle());

            QuickActionUtils.createDetailsQuickAction(_view.getContext(), qa,
                  new View.OnClickListener() {
                     @Override
                     public void onClick(View arg0) {
                        openDetails(mSelectedRecording);
                        qa.dismiss();
                     }
                  });

            qa.show();
            return true;
         }
      });

      mAdapter = new LazyLoadingAdapter(this);
      mListView.setAdapter(mAdapter);
      mService = DataHandler.getCurrentRemoteInstance();
      mStatusBarHandler = new StatusBarActivityHandler(this, mService);
      mStatusBarHandler.setHome(false);

      refreshRecordings();
   }

   private void openDetails(TvRecording _recording) {
      if (_recording != null) {
         Intent recIntent = new Intent(this, TvServerRecordingDetailsActivity.class);
         recIntent.putExtra("recording_id", _recording.getIdRecording());
         recIntent.putExtra("recording_name", _recording.getTitle());
         recIntent.putExtra("recording_start", _recording.getStartTime());
         recIntent.putExtra("recording_end", _recording.getEndTime());
         if (!mChannels.containsKey(_recording.getIdChannel())) {
            TvChannel channel = mService.getTvChannel(_recording.getIdChannel());
            if (channel != null) {
               recIntent.putExtra("recording_channel", channel.getDisplayName());
            }
         } else {
            recIntent.putExtra("recording_channel", mChannels.get(_recording.getIdChannel())
                  .getDisplayName());
         }
         recIntent.putExtra("recording_description", _recording.getDescription());
         recIntent.putExtra("recording_filename", _recording.getFileName());

         startActivity(recIntent);
      }
   }

   private void refreshRecordings() {
      mAdapter.showLoadingItem(true);
      mAdapter.setLoadingText(getString(R.string.tvserver_loadrecordings));
      mRecordingsUpdater = new UpdateRecordingsTask(this);
      mRecordingsUpdater.execute(0);
   }
}
