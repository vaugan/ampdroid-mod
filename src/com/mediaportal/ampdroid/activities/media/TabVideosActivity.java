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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseTabActivity;
import com.mediaportal.ampdroid.activities.StatusBarActivityHandler;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.Movie;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.downloadservice.MediaItemType;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter.ILoadingListener;
import com.mediaportal.ampdroid.lists.Utils;
import com.mediaportal.ampdroid.lists.views.LoadingAdapterItem;
import com.mediaportal.ampdroid.lists.views.MediaListType;
import com.mediaportal.ampdroid.lists.views.MoviePosterViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.MovieTextViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.MovieThumbViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.ViewTypes;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.DownloaderUtils;
import com.mediaportal.ampdroid.utils.QuickActionUtils;
import com.mediaportal.ampdroid.utils.Util;

public class TabVideosActivity extends Activity implements ILoadingListener {
   private ListView mListView;
   private LazyLoadingAdapter mAdapter;
   DataHandler mService;
   private LoadVideosTask mVideosLoaderTask;
   private int mVideosLoaded = 0;
   private BaseTabActivity mBaseActivity;
   private StatusBarActivityHandler mStatusBarHandler;
   private int mPreloadItems;

   private class LoadVideosTask extends AsyncTask<Integer, List<Movie>, Boolean> {
      private Context mContext;

      private LoadVideosTask(Context _context) {
         mContext = _context;
      }

      @SuppressWarnings("unchecked")
      @Override
      protected Boolean doInBackground(Integer... _params) {
         int loadItems = mVideosLoaded + _params[0];
         int videosCount = mService.getVideosCount();
         if (_params[0] == 0) {
            loadItems = videosCount;
         }

         if (videosCount == -99) {
            publishProgress(null, null);
            return false;
         } else {
            while (mVideosLoaded < loadItems && mVideosLoaded < videosCount) {
               int end = mVideosLoaded + 19;
               if (end >= videosCount) {
                  end = videosCount - 1;
               }
               List<Movie> videos = mService.getVideos(mVideosLoaded, end);

               publishProgress(videos);
               if (videos == null) {
                  return false;
               } else {
                  mVideosLoaded += 20;
               }
            }
            if (mVideosLoaded < videosCount) {
               return false;// not yet finished;
            } else {
               return true;// finished
            }
         }
      }

      @Override
      protected void onProgressUpdate(List<Movie>... values) {
         if (values != null) {
            List<Movie> movies = values[0];
            if (movies != null) {
               for (Movie m : movies) {
                  mAdapter.addItem(ViewTypes.TextView.ordinal(), new MovieTextViewAdapterItem(m));
                  mAdapter.addItem(ViewTypes.PosterView.ordinal(),
                        new MoviePosterViewAdapterItem(m));
                  mAdapter.addItem(ViewTypes.ThumbView.ordinal(), new MovieThumbViewAdapterItem(m));
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
         mVideosLoaderTask = null;
      }
   }

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_tabmovies);

      mBaseActivity = (BaseTabActivity) getParent().getParent();

      mService = DataHandler.getCurrentRemoteInstance();

      if (mBaseActivity != null && mService != null) {
         mStatusBarHandler = new StatusBarActivityHandler(mBaseActivity, mService);
         mStatusBarHandler.setHome(false);
      }

      mAdapter = new LazyLoadingAdapter(this);
      mAdapter.addView(ViewTypes.TextView.ordinal());
      mAdapter.addView(ViewTypes.PosterView.ordinal());
      mAdapter.addView(ViewTypes.ThumbView.ordinal());

      mAdapter.setView(PreferencesManager.getDefaultView(MediaListType.Videos));

      mAdapter.setLoadingListener(this);

      mPreloadItems = PreferencesManager.getNumItemsToLoad();

      mListView = (ListView) findViewById(R.id.ListViewVideos);
      mListView.setFastScrollEnabled(true);
      mListView.setAdapter(mAdapter);

      mListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            ILoadingAdapterItem item = (ILoadingAdapterItem) mListView.getItemAtPosition(position);

            if (item.getClass().equals(LoadingAdapterItem.class)) {
               loadFurtherMovieItems();
            } else {
               Movie selectedMovie = (Movie) item.getItem();
               openDetails(selectedMovie);
            }
         }
      });

      mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> _item, View _view, final int _position,
               long _id) {
            try {
               final Movie selected = (Movie) ((ILoadingAdapterItem) _item.getItemAtPosition(_position))
                     .getItem();
               // EpisodeDetails details = mService.getEpisode(mSeriesId,
               // selected.getId());
               final String movieFile = selected.getFilename();
               if (movieFile != null) {
                  String dirName = DownloaderUtils.getMoviePath(selected);
                  final String fileName = dirName + Utils.getFileNameWithExtension(movieFile, "\\");
                  final String displayName = selected.toString();
                  final String itemId = String.valueOf(selected.getId());
                  final QuickAction qa = new QuickAction(_view);

                  final File localFile = new File(DownloaderUtils.getBaseDirectory() + "/"
                        + fileName);

                  if (localFile.exists()) {
                     QuickActionUtils.createPlayOnDeviceQuickAction(_view.getContext(), qa,
                           localFile, MediaItemType.Video);
                  } else {
                     QuickActionUtils.createDownloadSdCardQuickAction(_view.getContext(), qa,
                           mService, itemId, movieFile, DownloadItemType.VideoDatabaseItem,
                           MediaItemType.Video, fileName, displayName);
                  }

                  if (Constants.ENABLE_STREAMING) {
                     // experimental support for streaming, disable for
                     // releases
                     QuickActionUtils.createStreamOnClientQuickAction(_view.getContext(), qa,
                           mService, itemId, DownloadItemType.VideoDatabaseItem, fileName,
                           displayName);

                  }

                  QuickActionUtils.createPlayOnClientQuickAction(_view.getContext(), qa, mService,
                        movieFile);
                  
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

      loadFurtherMovieItems();
   }

   protected void openDetails(Movie selectedMovie) {
      Intent myIntent = new Intent(this, TabVideoDetailsActivity.class);
      myIntent.putExtra("video_id", selectedMovie.getId());
      myIntent.putExtra("video_name", selectedMovie.getName());

      myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

      // Create the view using FirstGroup's LocalActivityManager
      View view = TabVideosActivityGroup.getGroup().getLocalActivityManager()
            .startActivity("video_details", myIntent).getDecorView();

      // Again, replace the view
      TabVideosActivityGroup.getGroup().replaceView(view);
   }

   @Override
   public void EndOfListReached() {
      loadFurtherMovieItems();
   }

   private void loadFurtherMovieItems() {
      if (mVideosLoaderTask == null) {
         mAdapter.setLoadingText(getString(R.string.media_videos_loadvideos));
         mAdapter.showLoadingItem(true);

         mVideosLoaderTask = new LoadVideosTask(this);
         mStatusBarHandler.setLoading(true);
         mVideosLoaderTask.execute(mPreloadItems);
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

      MenuItem setDefaultViewItem = _menu.add(0, Menu.FIRST + 1, Menu.NONE,
            getString(R.string.menu_set_default_view));
      setDefaultViewItem.setIcon(R.drawable.ic_menu_set_as);
      setDefaultViewItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            ViewTypes currentView = mAdapter.getCurrentView();
            PreferencesManager.setDefaultView(MediaListType.Videos, currentView);
            return true;
         }
      });

      return true;
   }
}
