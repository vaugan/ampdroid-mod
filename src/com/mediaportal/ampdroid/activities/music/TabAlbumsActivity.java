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
/*
 *       Copyright (C) 2010-2011  Benjamin Gmeiner 
 *       mail.bgmeiner@gmail.com
 *       
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */



package com.mediaportal.ampdroid.activities.music;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

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
import com.mediaportal.ampdroid.api.ApiCredentials;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.FileInfo;
import com.mediaportal.ampdroid.data.MusicAlbum;
import com.mediaportal.ampdroid.data.MusicTrack;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.downloadservice.DownloadJob;
import com.mediaportal.ampdroid.downloadservice.ItemDownloaderHelper;
import com.mediaportal.ampdroid.downloadservice.MediaItemType;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter.ILoadingListener;
import com.mediaportal.ampdroid.lists.Utils;
import com.mediaportal.ampdroid.lists.views.LoadingAdapterItem;
import com.mediaportal.ampdroid.lists.views.MediaListType;
import com.mediaportal.ampdroid.lists.views.MusicAlbumTextViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.MusicAlbumThumbViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.ViewTypes;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.utils.DownloaderUtils;
import com.mediaportal.ampdroid.utils.PlaylistUtils;
import com.mediaportal.ampdroid.utils.QuickActionUtils;
import com.mediaportal.ampdroid.utils.Util;

public class TabAlbumsActivity extends Activity implements ILoadingListener {
   private ListView mListView;
   private LazyLoadingAdapter mAdapter;
   DataHandler mService;
   private LoadMusicTask mMusicLoaderTask;
   private int mItemsLoaded = 0;
   private BaseTabActivity mBaseActivity;
   private StatusBarActivityHandler mStatusBarHandler;
   private String mArtist;
   private String mActivityGroup;
   private int mPreloadItems;

   private class DownloadAlbumTask extends AsyncTask<MusicAlbum, Intent, Boolean> {
      private Context mContext;

      private DownloadAlbumTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected Boolean doInBackground(MusicAlbum... _params) {
         MusicAlbum album = _params[0];
         List<MusicTrack> tracks = mService.getSongsOfAlbum(album.getTitle(),
               album.getAlbumArtistString());
         int albumSize = tracks.size();
         int groupId = new Random().nextInt();
         for (int i = 0; i < albumSize; i++) {
            MusicTrack track = tracks.get(i);

            String url = mService.getDownloadUri(String.valueOf(track.getTrackId()),
                  DownloadItemType.MusicTrackItem);
            FileInfo info = mService.getFileInfo(String.valueOf(track.getTrackId()),
                  DownloadItemType.MusicTrackItem);
            String dirName = DownloaderUtils.getMusicTrackPath(album.getAlbumArtists()[0],
                  album.getTitle());
            final String fileName = dirName
                  + Utils.getFileNameWithExtension(track.getFilePath(), "\\");

            ApiCredentials cred = mService.getDownloadCredentials();
            if (url != null) {
               DownloadJob job = new DownloadJob(groupId);
               job.setUrl(url);
               job.setFileName(fileName);
               job.setDisplayName(track.toString());
               job.setMediaType(MediaItemType.Music);
               job.setGroupName(album.getTitle() + " (" + (i + 1) + "/" + albumSize + ")");
               job.setGroupPart(i);
               job.setGroupSize(albumSize);
               if (info != null) {
                  job.setLength(info.getLength());
               }
               if (cred.useAut()) {
                  job.setAuth(cred.getUsername(), cred.getPassword());
               }

               Intent download = ItemDownloaderHelper.createDownloadIntent(mContext, job);
               publishProgress(download);
            }
         }

         return true;
      }

      @Override
      protected void onProgressUpdate(Intent... values) {
         Intent donwloadIntent = values[0];
         if (donwloadIntent != null) {
            startService(donwloadIntent);
         }
         super.onProgressUpdate(values);
      }

      @Override
      protected void onPostExecute(Boolean _result) {

      }
   }
   
   private class CreateAlbumPlaylistTask extends AsyncTask<MusicAlbum, Intent, Boolean> {
      private Context mContext;

      private CreateAlbumPlaylistTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected Boolean doInBackground(MusicAlbum... _params) {
         MusicAlbum album = _params[0];
         List<MusicTrack> tracks = mService.getSongsOfAlbum(album.getTitle(),
               album.getAlbumArtistString());

         if(mService.isClientControlConnected()){
            mService.createPlaylist(tracks, true, 0);
            return true;
         }
         else{
            return false;
         }
      }

      @Override
      protected void onProgressUpdate(Intent... values) {
         super.onProgressUpdate(values);
      }

      @Override
      protected void onPostExecute(Boolean _result) {
         if(!_result){
            Util.showToast(mContext, getString(R.string.info_remote_notconnected));
         }
      }
   }

   private class LoadMusicTask extends AsyncTask<Integer, List<MusicAlbum>, Boolean> {
      private Context mContext;

      private LoadMusicTask(Context _context) {
         mContext = _context;
      }

      @SuppressWarnings("unchecked")
      @Override
      protected Boolean doInBackground(Integer... _params) {
         if (mArtist == null) {
            int albumsCount = mService.getAlbumsCount();
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
                  List<MusicAlbum> items = mService.getAlbums(mItemsLoaded, end);

                  publishProgress(items);
                  if (items == null) {
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
         } else {
            List<MusicAlbum> items = mService.getMusicAlbumsByArtist(mArtist);

            if (items != null) {
               publishProgress(items);
               return true;
            } else {
               return false;
            }
         }
      }

      @Override
      protected void onProgressUpdate(List<MusicAlbum>... values) {
         if (values != null) {
            List<MusicAlbum> series = values[0];
            if (series != null) {
               boolean showArtist = mArtist == null;
               for (MusicAlbum s : series) {
                  mAdapter.addItem(ViewTypes.TextView.ordinal(), new MusicAlbumTextViewAdapterItem(
                        s, showArtist));
                  mAdapter.addItem(ViewTypes.ThumbView.ordinal(),
                        new MusicAlbumThumbViewAdapterItem(s, showArtist));
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
         
         if (mAdapter.fastScrollingInitialised()) {
            mAdapter.resetFastScrolling(mListView);
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
      mAdapter.addView(ViewTypes.ThumbView.ordinal());

      mAdapter.setView(PreferencesManager.getDefaultView(MediaListType.Albums));

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
               MusicAlbum selectedSeries = (MusicAlbum) selectedItem.getItem();
               if (selectedSeries != null) {
                  Intent myIntent = new Intent(_view.getContext(), TabAlbumDetailsActivity.class);
                  myIntent.putExtra("album_artists_string", selectedSeries.getAlbumArtistString());
                  myIntent.putExtra("album_name", selectedSeries.getTitle());
                  myIntent.putExtra("album_artists", selectedSeries.getAlbumArtists());
                  myIntent.putExtra("album_cover", selectedSeries.getCoverPathLarge());
                  myIntent.putExtra("activity_group", mActivityGroup);
                  myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                  if (mArtist != null) {
                     View view = TabArtistsActivityGroup.getGroup().getLocalActivityManager()
                           .startActivity("album_details", myIntent).getDecorView();

                     TabArtistsActivityGroup.getGroup().replaceView(view);
                  } else {
                     View view = TabAlbumsActivityGroup.getGroup().getLocalActivityManager()
                           .startActivity("album_details", myIntent).getDecorView();

                     TabAlbumsActivityGroup.getGroup().replaceView(view);
                  }
               }
            }
         }
      });

      mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> _item, View _view, final int _position,
               long _id) {
            try {
               final MusicAlbum selected = (MusicAlbum) ((ILoadingAdapterItem) _item
                     .getItemAtPosition(_position)).getItem();
               final String trackTitle = selected.getTitle();
              
               if (trackTitle != null) {
                  String dirName = DownloaderUtils.getMusicTrackPath(selected.getAlbumArtists()[0],
                        selected.getTitle());
                  final QuickAction qa = new QuickAction(_view);

                  final File localFile = new File(DownloaderUtils.getBaseDirectory() + "/"
                        + dirName);

                  if (localFile.exists()) {
                     QuickActionUtils.createPlayOnDeviceQuickAction(_view.getContext(), qa,
                           new OnClickListener() {
                              @Override
                              public void onClick(View _view) {
                                 // TODO: play complete album with music
                                 // player
                                 // TODO: where do I get a list of valid
                                 // extensions?
                                 String m3u = PlaylistUtils.createM3UPlaylistFromFolder(
                                       localFile, new String[] { "mp3" });
                                 File playlistFile = new File(localFile.getAbsolutePath() + "/"
                                       + trackTitle + ".m3u");
                                 FileWriter w;
                                 try {
                                    w = new FileWriter(playlistFile);
                                    w.write(m3u);
                                    w.flush();
                                    w.close();
                                 } catch (IOException e) {
                                    e.printStackTrace();
                                 }

                                 Intent playIntent = new Intent(Intent.ACTION_VIEW);
                                 playIntent.setDataAndType(Uri.fromFile(playlistFile), "audio/*");
                                 startActivity(playIntent);

                                 qa.dismiss();
                              }
                           });
                  } else {
                     QuickActionUtils.createDownloadSdCardQuickAction(_view.getContext(), qa,
                           mService, new View.OnClickListener() {
                              @Override
                              public void onClick(View _view) {
                                 DownloadAlbumTask task = new DownloadAlbumTask(_view.getContext());
                                 task.execute(selected);
                                 
                                 qa.dismiss();
                              }
                           });
                  }
                  
                  QuickActionUtils.createPlayOnClientQuickAction(_view.getContext(), qa,
                        mService, new OnClickListener() {
                           @Override
                           public void onClick(View _view) {
                              CreateAlbumPlaylistTask task = new CreateAlbumPlaylistTask(_view.getContext());
                              task.execute(selected);
                              
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

      mAdapter.setLoadingText(getString(R.string.music_albums_loadalbums));
      mAdapter.showLoadingItem(true);

      mPreloadItems = PreferencesManager.getNumItemsToLoad();

      Bundle extras = getIntent().getExtras();
      if (extras != null) {
         mArtist = extras.getString("artist");
         mActivityGroup = extras.getString("activity_group");
      }

      loadFurtherItems();
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
            PreferencesManager.setDefaultView(MediaListType.Albums, currentView);
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
