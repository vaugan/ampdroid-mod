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

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseTabActivity;
import com.mediaportal.ampdroid.activities.StatusBarActivityHandler;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.Series;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter.ILoadingListener;
import com.mediaportal.ampdroid.lists.views.LoadingAdapterItem;
import com.mediaportal.ampdroid.lists.views.MediaListType;
import com.mediaportal.ampdroid.lists.views.SeriesBannerViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.SeriesPosterViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.SeriesTextViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.SeriesThumbViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.ViewTypes;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.utils.Util;

public class TabSeriesActivity extends Activity implements ILoadingListener {
   private ListView mListView;
   private LazyLoadingAdapter mAdapter;
   DataHandler mService;
   private LoadSeriesTask mSeriesLoaderTask;
   private int mSeriesLoaded = 0;
   private BaseTabActivity mBaseActivity;
   private StatusBarActivityHandler mStatusBarHandler;
   private int mPreloadItems;

   private class LoadSeriesTask extends AsyncTask<Integer, List<Series>, Boolean> {
      private Context mContext;

      private LoadSeriesTask(Context _context) {
         mContext = _context;
      }

      @SuppressWarnings("unchecked")
      @Override
      protected Boolean doInBackground(Integer... _params) {
         int seriesCount = mService.getSeriesCount();
         int loadItems = mSeriesLoaded + _params[0];
         if(_params[0] == 0){
            loadItems = seriesCount;
         }

         if (seriesCount == -99) {
            publishProgress(null, null);
            return false;
         } else {
            while (mSeriesLoaded < loadItems && mSeriesLoaded < seriesCount) {
               int end = mSeriesLoaded + 19;
               if (end >= seriesCount) {
                  end = seriesCount - 1;
               }
               List<Series> series = mService.getSeries(mSeriesLoaded, end);

               publishProgress(series);
               if (series == null) {
                  return false;
               } else {
                  mSeriesLoaded += 20;
               }
            }
         }

         if (mSeriesLoaded < seriesCount) {
            return false;// not yet finished;
         } else {
            return true;// finished
         }
      }

      @Override
      protected void onProgressUpdate(List<Series>... values) {
         if (values != null) {
            List<Series> series = values[0];
            if (series != null) {
               for (Series s : series) {
                  mAdapter.addItem(ViewTypes.TextView.ordinal(), new SeriesTextViewAdapterItem(s));
                  mAdapter.addItem(ViewTypes.PosterView.ordinal(), new SeriesPosterViewAdapterItem(
                        s));
                  mAdapter
                        .addItem(ViewTypes.ThumbView.ordinal(), new SeriesThumbViewAdapterItem(s));
                  mAdapter.addItem(ViewTypes.BannerView.ordinal(), new SeriesBannerViewAdapterItem(
                        s));
               }
               
               if (mAdapter.fastScrollingInitialised()) {
                  mAdapter.resetFastScrolling(mListView);
               }
            } else {
               mAdapter.setLoadingText(getString(R.string.info_loading_failed), false);
               Util.showToast(mContext, getString(R.string.info_loading_failed));
            }
         }

         mAdapter.notifyDataSetChanged();
         super.onProgressUpdate(values);
      }

      @Override
      protected void onPostExecute(Boolean _result) {
         if (_result) {
            mAdapter.showLoadingItem(false);
            mAdapter.notifyDataSetChanged();
         }
         mStatusBarHandler.setLoading(false);

         mSeriesLoaderTask = null;
      }

   }

   @Override
   public void EndOfListReached() {
      loadFurtherSeriesItems();
   }

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_tabseries);

      mBaseActivity = (BaseTabActivity) getParent().getParent();

      mService = DataHandler.getCurrentRemoteInstance();
      mStatusBarHandler = new StatusBarActivityHandler(mBaseActivity, mService);
      mStatusBarHandler.setHome(false);

      mAdapter = new LazyLoadingAdapter(this);
      mAdapter.addView(ViewTypes.TextView.ordinal());
      mAdapter.addView(ViewTypes.PosterView.ordinal());
      mAdapter.addView(ViewTypes.ThumbView.ordinal());
      mAdapter.addView(ViewTypes.BannerView.ordinal());
      
      mAdapter.setView(PreferencesManager.getDefaultView(MediaListType.Series));
      mPreloadItems = PreferencesManager.getNumItemsToLoad();
      
      mAdapter.setLoadingListener(this);

      mListView = (ListView) findViewById(R.id.ListViewVideos);
      mListView.setFastScrollEnabled(true);
      mListView.setAdapter(mAdapter);

      mListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> _adapter, View _view, int _position, long _id) {
            ILoadingAdapterItem selectedItem = (ILoadingAdapterItem) mListView
                  .getItemAtPosition(_position);

            if (selectedItem.getClass().equals(LoadingAdapterItem.class)) {
               loadFurtherSeriesItems();
            } else {
               Series selectedSeries = (Series) selectedItem.getItem();
               if (selectedSeries != null) {
                  Intent myIntent = new Intent(_view.getContext(), TabSeriesDetailsActivity.class);
                  myIntent.putExtra("series_id", selectedSeries.getId());
                  myIntent.putExtra("series_name", selectedSeries.getPrettyName());
                  myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                  // Create the view using FirstGroup's LocalActivityManager
                  View view = TabSeriesActivityGroup.getGroup().getLocalActivityManager()
                        .startActivity("series_details", myIntent).getDecorView();

                  // Again, replace the view
                  TabSeriesActivityGroup.getGroup().replaceView(view);
               }
            }
         }
      });

      loadFurtherSeriesItems();
   }

   private void loadFurtherSeriesItems() {
      if (mSeriesLoaderTask == null) {
         mAdapter.setLoadingText(getString(R.string.media_series_loadseries));
         mAdapter.showLoadingItem(true);

         mSeriesLoaderTask = new LoadSeriesTask(this);
         mStatusBarHandler.setLoading(true);
         mSeriesLoaderTask.execute(mPreloadItems);
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu _menu) {
      super.onCreateOptionsMenu(_menu);

      SubMenu viewItem = _menu.addSubMenu(0, Menu.FIRST + 1, Menu.NONE,
            getString(R.string.media_views));
      viewItem.setIcon(R.drawable.ic_menu_slideshow);

      MenuItem textSettingsItem = viewItem.add(0, Menu.FIRST + 1, Menu.NONE,
            getString(R.string.media_views_text));
      MenuItem posterSettingsItem = viewItem.add(0, Menu.FIRST + 2, Menu.NONE,
            getString(R.string.media_views_poster));
      MenuItem thumbsSettingsItem = viewItem.add(0, Menu.FIRST + 3, Menu.NONE,
            getString(R.string.media_views_thumbs));
      MenuItem bannerSettingsItem = viewItem.add(0, Menu.FIRST + 4, Menu.NONE,
            getString(R.string.media_views_banner));

      textSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mAdapter.setView(ViewTypes.TextView);
            mAdapter.notifyDataSetInvalidated();
            return true;
         }
      });

      posterSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mAdapter.setView(ViewTypes.PosterView);
            mAdapter.notifyDataSetInvalidated();
            return true;
         }
      });

      thumbsSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mAdapter.setView(ViewTypes.ThumbView);
            mAdapter.notifyDataSetInvalidated();
            return true;
         }
      });

      bannerSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mAdapter.setView(ViewTypes.BannerView);
            mAdapter.notifyDataSetInvalidated();
            return true;
         }
      });
      
      MenuItem setDefaultViewItem = _menu.add(0, Menu.FIRST + 1, Menu.NONE,
            getString(R.string.menu_set_default_view));
      setDefaultViewItem.setIcon(R.drawable.ic_menu_set_as);
      setDefaultViewItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            ViewTypes currentView = mAdapter.getCurrentView();
            PreferencesManager.setDefaultView(MediaListType.Series, currentView);
            return true;
         }
      });

      return true;
   }
}
