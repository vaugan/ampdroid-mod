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

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseTabActivity;
import com.mediaportal.ampdroid.activities.StatusBarActivityHandler;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.MusicTrack;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.downloadservice.MediaItemType;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter.ILoadingListener;
import com.mediaportal.ampdroid.lists.Utils;
import com.mediaportal.ampdroid.lists.views.LoadingAdapterItem;
import com.mediaportal.ampdroid.lists.views.MediaListType;
import com.mediaportal.ampdroid.lists.views.MusicTrackTextViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.MusicTrackThumbViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.ViewTypes;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.utils.DownloaderUtils;
import com.mediaportal.ampdroid.utils.QuickActionUtils;
import com.mediaportal.ampdroid.utils.Util;

public class TabTracksActivity extends Activity implements ILoadingListener {
   private ListView mListView;
   private LazyLoadingAdapter mAdapter;
   DataHandler mService;
   private LoadMusicTask mMusicLoaderTask;
   private int mItemsLoaded = 0;
   private BaseTabActivity mBaseActivity;
   private StatusBarActivityHandler mStatusBarHandler;
   private String mActivityGroup;
   private int mPreloadItems;

   private class LoadMusicTask extends AsyncTask<Integer, List<MusicTrack>, Boolean> {
      private Context mContext;

      private LoadMusicTask(Context _context) {
         mContext = _context;
      }

      @SuppressWarnings("unchecked")
      @Override
      protected Boolean doInBackground(Integer... _params) {
         int albumsCount = mService.getMusicTracksCount();
         int loadItems = mItemsLoaded + _params[0];
         if (_params[0] == 0) {
            loadItems = albumsCount;
         }

         if (albumsCount == -99) {
            publishProgress(null, null);
            return false;
         } else {
            while (mItemsLoaded < loadItems && mItemsLoaded < albumsCount) {
               int end = mItemsLoaded + 19;
               if (end >= albumsCount) {
                  end = albumsCount - 1;
               }
               List<MusicTrack> series = mService.getMusicTracks(mItemsLoaded, end);

               publishProgress(series);
               if (series == null) {
                  return false;
               } else {
                  mItemsLoaded += 20;
               }
            }
         }

         if (mItemsLoaded < albumsCount) {
            return false;// not yet finished;
         } else {
            return true;// finished
         }
      }

      @Override
      protected void onProgressUpdate(List<MusicTrack>... values) {
         if (values != null) {
            List<MusicTrack> items = values[0];
            if (items != null) {
               for (MusicTrack t : items) {
                  mAdapter.addItem(ViewTypes.TextView.ordinal(), new MusicTrackTextViewAdapterItem(
                        t, true));
                  mAdapter.addItem(ViewTypes.ThumbView.ordinal(),
                        new MusicTrackThumbViewAdapterItem(t, true));
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
         mMusicLoaderTask = null;
      }

   }

   @Override
   public void EndOfListReached() {
      loadFurtherItems();
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
      // mAdapter.addView(ViewTypes.ThumbView.ordinal());
      mAdapter.setView(PreferencesManager.getDefaultView(MediaListType.Songs));
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
               loadFurtherItems();
            } else {
               MusicTrack selectedTrack = (MusicTrack) selectedItem.getItem();
               if (selectedTrack != null) {
                  // TODO: Open track detail view
               }
            }
         }
      });

      mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> _item, View _view, final int _position,
               long _id) {
            try {
               MusicTrack selected = (MusicTrack) ((ILoadingAdapterItem) _item
                     .getItemAtPosition(_position)).getItem();
               final String trackTitle = selected.getTitle();
               final String trackPath = selected.getFilePath();
               final String trackId = String.valueOf(selected.getTrackId());
               if (trackTitle != null) {
                  String dirName = DownloaderUtils.getMusicTrackPath();
                  final String fileName = dirName + Utils.getFileNameWithExtension(trackPath, "\\");
                  final String displayName = selected.toString();
                  final QuickAction qa = new QuickAction(_view);

                  final File localFile = new File(DownloaderUtils.getBaseDirectory() + "/"
                        + fileName);
                  
                  if (localFile.exists()) {
                     QuickActionUtils.createPlayOnDeviceQuickAction(_view.getContext(), qa,
                           new OnClickListener() {
                              @Override
                              public void onClick(View _view) {
                                 Intent playIntent = new Intent(Intent.ACTION_VIEW);
                                 playIntent.setDataAndType(Uri.fromFile(localFile),
                                       "audio/*");
                                 startActivity(playIntent);

                                 qa.dismiss();
                              }
                           });
                  } else {
                     QuickActionUtils.createDownloadSdCardQuickAction(_view.getContext(), qa,
                           mService, trackId, trackId, DownloadItemType.MusicTrackItem,
                           MediaItemType.Music, fileName, displayName);
                  }

                  QuickActionUtils.createPlayOnClientQuickAction(_view.getContext(), qa,
                        mService, new OnClickListener() {
                           @Override
                           public void onClick(View _view) {
                              mService.playAudioFileOnClient(trackPath, 0);

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

      mAdapter.setLoadingText(getString(R.string.music_tracks_loadtracks));
      mAdapter.showLoadingItem(true);

      mPreloadItems = PreferencesManager.getNumItemsToLoad();

      loadFurtherItems();

      Bundle extras = getIntent().getExtras();
      if (extras != null) {
         mActivityGroup = extras.getString("activity_group");
      }
   }

   private void loadFurtherItems() {
      if (mMusicLoaderTask == null) {
         mMusicLoaderTask = new LoadMusicTask(this);
         mStatusBarHandler.setLoading(true);
         mMusicLoaderTask.execute(mPreloadItems);
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
      // MenuItem thumbsSettingsItem = viewItem.add(0, Menu.FIRST + 3,
      // Menu.NONE, getString(R.string.media_views_thumbs));

      textSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mAdapter.setView(ViewTypes.TextView);
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
            PreferencesManager.setDefaultView(MediaListType.Songs, currentView);
            return true;
         }
      });

      return true;
   }

   @Override
   public void onDestroy() {
      mAdapter.mImageLoader.stopThread();
      mListView.setAdapter(null);
      super.onDestroy();
   }

   @Override
   public void onBackPressed() {
      TabTracksActivityGroup.getGroup().back();
      return;
   }

   @Override
   public boolean onKeyDown(int _keyCode, KeyEvent _event) {
      if (_keyCode == KeyEvent.KEYCODE_BACK) {
         TabTracksActivityGroup.getGroup().back();
         return true;
      }
      return super.onKeyDown(_keyCode, _event);
   }
}
