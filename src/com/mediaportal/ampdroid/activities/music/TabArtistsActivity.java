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

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.mediaportal.ampdroid.data.MusicAlbum;
import com.mediaportal.ampdroid.data.MusicArtist;
import com.mediaportal.ampdroid.data.MusicTrack;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter.ILoadingListener;
import com.mediaportal.ampdroid.lists.views.LoadingAdapterItem;
import com.mediaportal.ampdroid.lists.views.MediaListType;
import com.mediaportal.ampdroid.lists.views.MusicArtistTextViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.MusicArtistThumbViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.ViewTypes;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.utils.QuickActionUtils;
import com.mediaportal.ampdroid.utils.Util;

public class TabArtistsActivity extends Activity implements ILoadingListener {
   private ListView mListView;
   private LazyLoadingAdapter mAdapter;
   DataHandler mService;
   private LoadMusicTask mMusicLoaderTask;
   private int mItemsLoaded = 0;
   private BaseTabActivity mBaseActivity;
   private StatusBarActivityHandler mStatusBarHandler;
   private String mActivityGroup;
   private int mPreloadItems;

   private class LoadMusicTask extends AsyncTask<Integer, List<MusicArtist>, Boolean> {
      private Context mContext;

      private LoadMusicTask(Context _context) {
         mContext = _context;
      }

      @SuppressWarnings("unchecked")
      @Override
      protected Boolean doInBackground(Integer... _params) {
         int albumsCount = mService.getArtistsCount();
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
               List<MusicArtist> series = mService.getArtists(mItemsLoaded, end);

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
      protected void onProgressUpdate(List<MusicArtist>... values) {
         if (values != null) {
            List<MusicArtist> series = values[0];
            if (series != null) {
               for (MusicArtist s : series) {
                  if (s != null && s.getTitle() != null && s.getTitle().length() > 0) {
                     mAdapter.addItem(ViewTypes.TextView.ordinal(),
                           new MusicArtistTextViewAdapterItem(s));
                     mAdapter.addItem(ViewTypes.ThumbView.ordinal(),
                           new MusicArtistThumbViewAdapterItem(s));
                  }
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

   private class CreateArtistPlaylistTask extends AsyncTask<MusicArtist, Intent, Boolean> {
      private Context mContext;

      private CreateArtistPlaylistTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected Boolean doInBackground(MusicArtist... _params) {
         MusicArtist artist = _params[0];

         List<MusicAlbum> albums = mService.getMusicAlbumsByArtist(artist.getTitle());
         List<MusicTrack> tracks = new ArrayList<MusicTrack>();
         for (MusicAlbum a : albums) {
            List<MusicTrack> albumTracks = mService.getSongsOfAlbum(a.getTitle(),
                  a.getAlbumArtistString());
            if(albumTracks != null){
               tracks.addAll(albumTracks);
            }
         }

         if (mService.isClientControlConnected()) {
            mService.createPlaylist(tracks, true, 0);
            return true;
         } else {
            return false;
         }
      }

      @Override
      protected void onProgressUpdate(Intent... values) {
         super.onProgressUpdate(values);
      }

      @Override
      protected void onPostExecute(Boolean _result) {
         if (!_result) {
            Util.showToast(mContext, getString(R.string.info_remote_notconnected));
         }
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
      mAdapter.setView(PreferencesManager.getDefaultView(MediaListType.Artists));
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
               MusicArtist selectedArtist = (MusicArtist) selectedItem.getItem();
               if (selectedArtist != null) {
                  Intent myIntent = new Intent(_view.getContext(), TabAlbumsActivity.class);
                  myIntent.putExtra("artist", selectedArtist.getTitle());
                  myIntent.putExtra("activity_group", mActivityGroup);
                  myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                  // Create the view using FirstGroup's LocalActivityManager
                  View view = TabArtistsActivityGroup.getGroup().getLocalActivityManager()
                        .startActivity("album_artist", myIntent).getDecorView();

                  // Again, replace the view
                  TabArtistsActivityGroup.getGroup().replaceView(view);
               }
            }
         }
      });
      
      mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> _item, View _view, final int _position,
               long _id) {
            try {
               final MusicArtist selected = (MusicArtist) ((ILoadingAdapterItem) _item
                     .getItemAtPosition(_position)).getItem();
               final QuickAction qa = new QuickAction(_view);
               
               QuickActionUtils.createPlayOnClientQuickAction(_view.getContext(), qa,
                     mService, new OnClickListener() {
                        @Override
                        public void onClick(View _view) {
                           CreateArtistPlaylistTask task = new CreateArtistPlaylistTask(_view.getContext());
                           task.execute(selected);
                           
                           qa.dismiss();
                        }
                     });

               qa.setAnimStyle(QuickAction.ANIM_AUTO);
               qa.show();
               return true;
            } catch (Exception ex) {
               return false;
            }
         }
      });

      mAdapter.setLoadingText(getString(R.string.music_artists_loadartists));
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
            PreferencesManager.setDefaultView(MediaListType.Artists, currentView);
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
      if (mActivityGroup.equals("artists")) {
         TabArtistsActivityGroup.getGroup().back();
      } else if (mActivityGroup.equals("albums")) {
         TabAlbumsActivityGroup.getGroup().back();
      }
      return;
   }

   @Override
   public boolean onKeyDown(int _keyCode, KeyEvent _event) {
      if (_keyCode == KeyEvent.KEYCODE_BACK) {
         if (mActivityGroup.equals("artists")) {
            TabArtistsActivityGroup.getGroup().back();
         } else if (mActivityGroup.equals("albums")) {
            TabAlbumsActivityGroup.getGroup().back();
         }
         return true;
      }
      return super.onKeyDown(_keyCode, _event);
   }
}
