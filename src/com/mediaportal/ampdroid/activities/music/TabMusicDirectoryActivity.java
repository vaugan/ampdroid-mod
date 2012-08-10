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
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
import com.mediaportal.ampdroid.data.MusicTrack;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.downloadservice.DownloadJob;
import com.mediaportal.ampdroid.downloadservice.ItemDownloaderHelper;
import com.mediaportal.ampdroid.downloadservice.MediaItemType;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter.ILoadingListener;
import com.mediaportal.ampdroid.lists.Utils;
import com.mediaportal.ampdroid.lists.views.MediaListType;
import com.mediaportal.ampdroid.lists.views.MusicFileInfoTextViewAdapterItem;
import com.mediaportal.ampdroid.lists.views.ViewTypes;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.utils.DownloaderUtils;
import com.mediaportal.ampdroid.utils.PlaylistUtils;
import com.mediaportal.ampdroid.utils.QuickActionUtils;
import com.mediaportal.ampdroid.utils.StringUtils;
import com.mediaportal.ampdroid.utils.Util;

public class TabMusicDirectoryActivity extends Activity implements ILoadingListener {
   private ListView mListView;
   private LazyLoadingAdapter mAdapter;
   DataHandler mService;
   private LoadDirectoryTask mDirLoaderTask;
   private BaseTabActivity mBaseActivity;
   private StatusBarActivityHandler mStatusBarHandler;
   private String mActivityGroup;
   private String[] mExtensions;
   private String mCurrentDirectoy;
   private String mCurrentShareName;
   private String mCurrentShareDirectory;

   private class DownloadDirectoryTask extends AsyncTask<FileInfo, Intent, Boolean> {
      private Context mContext;

      private DownloadDirectoryTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected Boolean doInBackground(FileInfo... _params) {
         FileInfo dir = _params[0];
         List<FileInfo> files = mService.getFilesForFolder(dir.getFullPath());

         int albumSize = files.size();
         int groupId = new Random().nextInt();
         for (int i = 0; i < albumSize; i++) {
            FileInfo track = files.get(i);

            String url = mService.getDownloadUri(track.getFullPath(),
                  DownloadItemType.MusicShareItem);
            FileInfo info = mService.getFileInfo(track.getFullPath(),
                  DownloadItemType.MusicShareItem);

            String localFile = createFileName(dir.getFullPath()) + "/" + track.getName();

            ApiCredentials cred = mService.getDownloadCredentials();
            if (url != null) {
               DownloadJob job = new DownloadJob(groupId);
               job.setUrl(url);
               job.setFileName(localFile);
               job.setDisplayName(track.toString());
               job.setMediaType(MediaItemType.Music);
               job.setGroupName(dir.getName() + " (" + (i + 1) + "/" + albumSize + ")");
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

   private class LoadDirectoryTask extends AsyncTask<Integer, List<FileInfo>, Boolean> {
      private Context mContext;

      private LoadDirectoryTask(Context _context) {
         mContext = _context;
      }

      @SuppressWarnings("unchecked")
      @Override
      protected Boolean doInBackground(Integer... _params) {

         List<FileInfo> folders = mService.getFoldersForFolder(mCurrentDirectoy);
         publishProgress(folders);

         List<FileInfo> files = mService.getFilesForFolder(mCurrentDirectoy);
         publishProgress(files);

         return true;
      }

      @Override
      protected void onProgressUpdate(List<FileInfo>... values) {
         if (values != null) {
            List<FileInfo> items = values[0];
            if (items != null) {
               for (FileInfo f : items) {
                  if (f.isFolder() || StringUtils.containedInArray(f.getExtension(), mExtensions)) {
                     mAdapter.addItem(ViewTypes.TextView.ordinal(),
                           new MusicFileInfoTextViewAdapterItem(f));
                  }
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
         mDirLoaderTask = null;
      }

   }

   private class CreateDirectoryPlaylistTask extends AsyncTask<String, Intent, Boolean> {
      private Context mContext;

      private CreateDirectoryPlaylistTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected Boolean doInBackground(String... _params) {
         String dir = _params[0];
         List<FileInfo> files = mService.getFilesForFolder(dir);

         if (mService.isClientControlConnected()) {
            List<MusicTrack> tracks = new ArrayList<MusicTrack>();
            for (FileInfo f : files) {
               MusicTrack track = new MusicTrack();
               track.setTitle(f.getName());
               track.setFilePath(f.getFullPath());
               tracks.add(track);
            }

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

   public String createFileName(String _path) {
      String fileName = DownloaderUtils.getMusicSharesPath() + mCurrentShareName
            + _path.replace(mCurrentShareDirectory, "").replace("\\", "/");

      return fileName;
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
      mAdapter.setView(PreferencesManager.getDefaultView(MediaListType.MusicShares));
      mAdapter.setLoadingListener(this);

      mListView = (ListView) findViewById(R.id.ListViewVideos);
      mListView.setFastScrollEnabled(true);
      mListView.setAdapter(mAdapter);

      mListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> _adapter, View _view, int _position, long _id) {
            ILoadingAdapterItem selectedItem = (ILoadingAdapterItem) mListView
                  .getItemAtPosition(_position);
            FileInfo selectedFileInfo = (FileInfo) selectedItem.getItem();
            if (selectedFileInfo != null) {
               if (selectedFileInfo.isFolder()) {
                  Intent myIntent = new Intent(_view.getContext(), TabMusicDirectoryActivity.class);
                  myIntent.putExtra("directory", selectedFileInfo.getFullPath());
                  myIntent.putExtra("share_name", mCurrentShareName);
                  myIntent.putExtra("share_dir", mCurrentShareDirectory);
                  myIntent.putExtra("extensions", mExtensions);
                  myIntent.putExtra("activity_group", mActivityGroup);
                  myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                  View view = TabMusicSharesActivityGroup.getGroup().getLocalActivityManager()
                        .startActivity("music_dir" + selectedFileInfo.getFullPath(), myIntent)
                        .getDecorView();

                  TabMusicSharesActivityGroup.getGroup().replaceView(view);
               }
            }
         }
      });

      mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> _item, View _view, final int _position,
               long _id) {
            try {
               final FileInfo selected = (FileInfo) ((ILoadingAdapterItem) _item
                     .getItemAtPosition(_position)).getItem();
               final String trackTitle = selected.getName();
               final String trackPath = selected.getFullPath();
               if (trackTitle != null) {
                  String dirName = DownloaderUtils.getMusicTrackPath();
                  final String fileName = dirName + Utils.getFileNameWithExtension(trackPath, "\\");

                  final QuickAction qa = new QuickAction(_view);

                  final File localFile = new File(DownloaderUtils.getBaseDirectory()
                        + createFileName(trackPath));

                  if (localFile.exists()) {
                     if (!localFile.isDirectory()) {
                        QuickActionUtils.createPlayOnDeviceQuickAction(_view.getContext(), qa,
                              localFile, MediaItemType.Music);
                     } else {
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
                     }
                  } else {
                     if (!selected.isFolder()) {
                        QuickActionUtils.createDownloadSdCardQuickAction(_view.getContext(), qa,
                              mService, trackPath, trackPath, DownloadItemType.MusicShareItem,
                              MediaItemType.Music, fileName, fileName);
                     } else {
                        QuickActionUtils.createDownloadSdCardQuickAction(_view.getContext(), qa,
                              mService, new View.OnClickListener() {
                                 @Override
                                 public void onClick(View _view) {
                                    DownloadDirectoryTask task = new DownloadDirectoryTask(_view
                                          .getContext());
                                    task.execute(selected);
                                    
                                    qa.dismiss();
                                 }
                              });
                     }
                  }

                  if (selected.isFolder()) {
                     QuickActionUtils.createPlayOnClientQuickAction(_view.getContext(), qa,
                           mService, new OnClickListener() {
                              @Override
                              public void onClick(View _view) {
                                 CreateDirectoryPlaylistTask task = new CreateDirectoryPlaylistTask(
                                       _view.getContext());
                                 task.execute(trackPath);

                                 qa.dismiss();
                              }
                           });
                  } else {
                     QuickActionUtils.createPlayOnClientQuickAction(_view.getContext(), qa,
                           mService, new OnClickListener() {
                              @Override
                              public void onClick(View _view) {
                                 mService.playAudioFileOnClient(trackPath, 0);

                                 qa.dismiss();
                              }
                           });
                  }

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

      mAdapter.setLoadingText(getString(R.string.media_shares_loadfiles));
      mAdapter.showLoadingItem(true);

      Bundle extras = getIntent().getExtras();
      if (extras != null) {
         mActivityGroup = extras.getString("activity_group");
         mCurrentDirectoy = extras.getString("directory");
         mCurrentShareName = extras.getString("share_name");
         mCurrentShareDirectory = extras.getString("share_dir");
         mExtensions = extras.getStringArray("extensions");
      }

      loadFurtherItems();
   }

   private void loadFurtherItems() {
      if (mDirLoaderTask == null) {
         mDirLoaderTask = new LoadDirectoryTask(this);
         mStatusBarHandler.setLoading(true);
         mDirLoaderTask.execute(20);
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
      TabMusicSharesActivityGroup.getGroup().back();
      return;
   }

   @Override
   public boolean onKeyDown(int _keyCode, KeyEvent _event) {
      if (_keyCode == KeyEvent.KEYCODE_BACK) {
         TabMusicSharesActivityGroup.getGroup().back();
         return true;
      }
      return super.onKeyDown(_keyCode, _event);
   }
}
