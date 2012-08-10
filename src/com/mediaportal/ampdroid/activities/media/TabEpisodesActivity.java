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
package com.mediaportal.ampdroid.activities.media;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.SeriesEpisode;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.downloadservice.MediaItemType;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.Utils;
import com.mediaportal.ampdroid.lists.views.EpisodePosterViewAdapterItem;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.DownloaderUtils;
import com.mediaportal.ampdroid.utils.QuickActionUtils;
import com.mediaportal.ampdroid.utils.Util;

public class TabEpisodesActivity extends Activity {
   private ListView mListView;
   private LazyLoadingAdapter mAdapter;
   MediaPlayer mMediaPlayer;
   DataHandler mService;

   private String mSeriesName;
   private int mSeriesId;
   private int mSeasonNumber;

   private LoadEpisodesTask mEpisodesLoaderTask;
   private TextView mTextViewSeriesName;
   private TextView mTextViewSeason;

   private class LoadEpisodesTask extends AsyncTask<Integer, List<SeriesEpisode>, Boolean> {
      private Context mContext;

      private LoadEpisodesTask(Context _context) {
         mContext = _context;
      }

      @SuppressWarnings("unchecked")
      @Override
      protected Boolean doInBackground(Integer... _params) {
         List<SeriesEpisode> episodes = mService.getAllEpisodesForSeason(mSeriesId, mSeasonNumber);
         publishProgress(episodes);
         return true;
      }

      @Override
      protected void onProgressUpdate(List<SeriesEpisode>... values) {
         if (values != null) {
            List<SeriesEpisode> episodes = values[0];
            if (episodes != null) {
               for (SeriesEpisode e : episodes) {
                  // EpisodeDetails details = mService.getEpisode(e.getId());
                  mAdapter.addItem(new EpisodePosterViewAdapterItem(mContext, mSeriesId, e));
               }
            }
            Log.d(Constants.LOG_CONST, "Finished adding " + episodes.size() + " episodes to listview");
         }
         mAdapter.notifyDataSetChanged();
         super.onProgressUpdate(values);
      }

      @Override
      protected void onPostExecute(Boolean _result) {
         Log.d(Constants.LOG_CONST, "Episode loading complete, set adapter loading to false");
         mAdapter.showLoadingItem(false);
         mAdapter.notifyDataSetChanged();
      }
   }

   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_tabepisodes);
      mListView = (ListView) findViewById(R.id.ListView);
      mTextViewSeriesName = (TextView) findViewById(R.id.TextViewSeriesName);
      mTextViewSeason = (TextView) findViewById(R.id.TextViewSeason);
      mMediaPlayer = new MediaPlayer();

      Bundle extras = getIntent().getExtras();
      if (extras != null) {
         mSeriesId = extras.getInt("series_id");
         mSeriesName = extras.getString("series_name");
         mSeasonNumber = extras.getInt("season_number");

         mTextViewSeriesName.setText(mSeriesName);
         mTextViewSeason.setText(getString(R.string.media_series_season) + " " + mSeasonNumber);
         mService = DataHandler.getCurrentRemoteInstance();

         mAdapter = new LazyLoadingAdapter(this);
         mListView.setAdapter(mAdapter);

         refreshEpisodes();

      } else {// activity called without bundle infos (shouldn't happen ;))

      }

      mListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> _item, View _view, int _position, long _id) {
            Object obj = mListView.getItemAtPosition(_position);
            ILoadingAdapterItem selectedItem = (ILoadingAdapterItem) obj;
            SeriesEpisode selectedEp = (SeriesEpisode) selectedItem.getItem();

            openDetails(selectedEp);
         }
      });

      mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> _item, View _view, final int _position,
               long _id) {
            try {
               final SeriesEpisode selected = (SeriesEpisode) ((ILoadingAdapterItem) _item
                     .getItemAtPosition(_position)).getItem();
               // EpisodeDetails details = mService.getEpisode(mSeriesId,
               // selected.getId());
               final String epFile = selected.getFileName();
               if (epFile != null) {
                  String dirName = DownloaderUtils.getTvEpisodePath(mSeriesName, selected);
                  final String fileName = dirName + Utils.getFileNameWithExtension(epFile, "\\");
                  final String displayName = mSeriesName + ": " + selected.toString();
                  final String itemId = String.valueOf(selected.getId());
                  final QuickAction qa = new QuickAction(_view);

                  final File localFile = new File(DownloaderUtils.getBaseDirectory() + "/"
                        + fileName);

                  if (localFile.exists()) {
                     QuickActionUtils.createPlayOnDeviceQuickAction(_view.getContext(), qa,
                           localFile, MediaItemType.Video);
                  } else {
                     QuickActionUtils.createDownloadSdCardQuickAction(_view.getContext(), qa,
                           mService, itemId, epFile, DownloadItemType.TvSeriesItem,
                           MediaItemType.Video, fileName, displayName);
                  }

                  if (Constants.ENABLE_STREAMING) {
                     QuickActionUtils.createStreamOnClientQuickAction(_view.getContext(), qa,
                           mService, itemId, DownloadItemType.TvSeriesItem, fileName, displayName);

                  }

                  QuickActionUtils.createPlayOnClientQuickAction(_view.getContext(), qa, mService,
                        epFile);
                  
                  QuickActionUtils.createDetailsQuickAction(_view.getContext(), qa,
                        new View.OnClickListener() {
                           @Override
                           public void onClick(View arg0) {
                              openDetails(selected);
                              qa.dismiss();
                           }
                        });

                  qa.setAnimStyle(QuickAction.ANIM_AUTO);

                  qa.show();
               } else {
                  Util.showToast(_view.getContext(), getString(R.string.media_nofile));
               }
               return true;
            } catch (Exception ex) {
               return false;
            }
         }
      });
   }

   protected void openDetails(SeriesEpisode selectedEp) {
      Intent myIntent = new Intent(this, TabEpisodeDetailsActivity.class);
      myIntent.putExtra("series_id", mSeriesId);
      myIntent.putExtra("series_name", mSeriesName);
      myIntent.putExtra("episode_id", selectedEp.getId());
      myIntent.putExtra("episode_name", selectedEp.getName());
      myIntent.putExtra("episode_season", selectedEp.getSeasonNumber());
      myIntent.putExtra("episode_nr", selectedEp.getEpisodeNumber());
      myIntent.putExtra("episode_banner", selectedEp.getBannerUrl());

      myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

      // Create the view using FirstGroup's LocalActivityManager
      View view = TabSeriesActivityGroup.getGroup().getLocalActivityManager()
            .startActivity("episode_details", myIntent).getDecorView();

      // Again, replace the view
      TabSeriesActivityGroup.getGroup().replaceView(view);
   }

   private void refreshEpisodes() {
      mAdapter.setLoadingText(getString(R.string.media_episodes_loading));
      mAdapter.showLoadingItem(true);
      mEpisodesLoaderTask = new LoadEpisodesTask(this);
      mEpisodesLoaderTask.execute(0);

   }

   /*@Override
   public void onDestroy() {
      mAdapter.mImageLoader.stopThread();
      mListView.setAdapter(null);
      super.onDestroy();
   }*/

   @Override
   public void onBackPressed() {
      TabSeriesActivityGroup.getGroup().back();
      return;
   }

   @Override
   public boolean onKeyDown(int _keyCode, KeyEvent _event) {
      if (_keyCode == KeyEvent.KEYCODE_BACK) {
         TabSeriesActivityGroup.getGroup().back();
         return true;
      }
      return super.onKeyDown(_keyCode, _event);
   }
}
