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
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseTabActivity;
import com.mediaportal.ampdroid.activities.StatusBarActivityHandler;
import com.mediaportal.ampdroid.activities.videoplayback.StreamingDetailsActivity;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.asynctasks.ReconnectTask;
import com.mediaportal.ampdroid.data.FileInfo;
import com.mediaportal.ampdroid.data.VideoShare;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.downloadservice.MediaItemType;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.settings.PreferencesManager;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.DownloaderUtils;
import com.mediaportal.ampdroid.utils.QuickActionUtils;
import com.mediaportal.ampdroid.utils.StringUtils;

public class TabSharesActivity extends Activity {
   private ListView mListView;
   private ArrayAdapter<VideoShare> mListItems;
   private ArrayAdapter<FileInfo> mFileItems;
   private List<String> mBreadCrumb;
   private VideoShare mCurrentShare;
   private LoadSharesTask mSeriesLoaderTask;
   private DataHandler mService;
   private LoadFolderTask mFilesLoaderTask;
   private ProgressDialog mLoadingDialog;
   private BaseTabActivity mBaseActivity;
   private StatusBarActivityHandler mStatusBarHandler;
   private GoogleAnalyticsTracker mTracker;

   private class LoadSharesTask extends AsyncTask<Integer, Integer, List<VideoShare>> {
      @Override
      protected List<VideoShare> doInBackground(Integer... _params) {
         List<VideoShare> shares = mService.getAllVideoShares();

         return shares;
      }

      @Override
      protected void onPostExecute(List<VideoShare> _result) {
         mListItems.clear();

         if (_result != null) {
            for (VideoShare s : _result) {
               mListItems.add(s);
            }
         }

         mListView.setAdapter(mListItems);
         mListItems.notifyDataSetChanged();
         if (mLoadingDialog != null && mBaseActivity.getIsActive()) {
            mLoadingDialog.dismiss();
         }
      }
   }

   private class LoadFolderTask extends AsyncTask<String, Integer, List<FileInfo>> {
      @Override
      protected List<FileInfo> doInBackground(String... _params) {
         String path = _params[0];
         List<FileInfo> retList = new ArrayList<FileInfo>();

         List<FileInfo> folders = mService.getFoldersForFolder(path);
         List<FileInfo> files = mService.getFilesForFolder(path);

         if (folders != null) {
            retList.addAll(folders);
         }

         if (files != null) {
            retList.addAll(files);
         }
         return retList;
      }

      @Override
      protected void onPostExecute(List<FileInfo> _result) {
         mFileItems.clear();

         if (_result != null) {
            for (FileInfo f : _result) {
               if (f.isFolder()
                     || StringUtils.containedInArray(f.getExtension(), mCurrentShare.Extensions)) {
                  mFileItems.add(f);
               }
            }
         }

         mListView.setAdapter(mFileItems);
         mFileItems.notifyDataSetChanged();
         mLoadingDialog.dismiss();
      }
   }

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_tabshares);

      mListView = (ListView) findViewById(R.id.ListViewShares);
      mListView.setFastScrollEnabled(true);

      mFileItems = new ArrayAdapter<FileInfo>(this, android.R.layout.simple_list_item_1);

      mListItems = new ArrayAdapter<VideoShare>(this, android.R.layout.simple_list_item_1);
      mListView.setAdapter(mListItems);
      mListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> _adapter, View _view, int _pos, long _id) {
            if (_adapter.getAdapter().equals(mListItems)) {
               VideoShare selected = (VideoShare) mListView.getItemAtPosition(_pos);
               mCurrentShare = selected;
               loadFiles(selected.Path);
            } else if (_adapter.getAdapter().equals(mFileItems)) {
               FileInfo selected = (FileInfo) mListView.getItemAtPosition(_pos);
               if (selected.isFolder()) {
                  loadFiles(selected.getFullPath());
               } else {
                  Intent download = new Intent(_view.getContext(), StreamingDetailsActivity.class);
                  download.putExtra("video_id", selected.getFullPath());
                  download.putExtra("video_type", DownloadItemType.toInt(DownloadItemType.VideoShareItem));
                  download.putExtra("video_name", selected.getName());
                  startActivity(download);
               }
            }
         }
      });

      mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> _adapter, View _view, final int _pos,
               long _id) {
            try {
               if (_adapter.getAdapter().equals(mFileItems)) {
                  final FileInfo selected = (FileInfo) mListView.getItemAtPosition(_pos);
                  final String fileName = DownloaderUtils.getVideoPath(mCurrentShare, selected);

                  final QuickAction qa = new QuickAction(_view);

                  final File localFile = new File(DownloaderUtils.getBaseDirectory() + "/"
                        + fileName);

                  if (!selected.isFolder()) {
                     if (localFile.exists()) {
                        QuickActionUtils.createPlayOnDeviceQuickAction(_view.getContext(), qa,
                              localFile, MediaItemType.Video);
                     } else {
                        QuickActionUtils.createDownloadSdCardQuickAction(_view.getContext(), qa,
                              mService, selected.getFullPath(), fileName,
                              DownloadItemType.VideoShareItem, MediaItemType.Video, fileName,
                              selected.getName());
                     }

                     if (Constants.ENABLE_STREAMING) {
                        // experimental support for streaming, disable for
                        // releases
                        QuickActionUtils.createStreamOnClientQuickAction(_view.getContext(), qa,
                              mService, selected.getFullPath(), DownloadItemType.VideoShareItem,
                              fileName, selected.getName());

                     }

                     QuickActionUtils.createPlayOnClientQuickAction(_view.getContext(), qa,
                           mService, selected.getFullPath());
                  } else {
                     QuickActionUtils.createDetailsQuickAction(_view.getContext(), qa,
                           new View.OnClickListener() {
                              @Override
                              public void onClick(View arg0) {
                                 loadFiles(selected.getFullPath());
                              }
                           });
                  }
                  qa.setAnimStyle(QuickAction.ANIM_AUTO);

                  qa.show();

               } else if (_adapter.getAdapter().equals(mListItems)) {
                  final VideoShare selected = (VideoShare) mListView.getItemAtPosition(_pos);
                  mCurrentShare = selected;
                  final QuickAction qa = new QuickAction(_view);
                  QuickActionUtils.createDetailsQuickAction(_view.getContext(), qa,
                        new View.OnClickListener() {
                           @Override
                           public void onClick(View arg0) {
                              loadFiles(selected.Path);
                           }
                        });

                  qa.setAnimStyle(QuickAction.ANIM_AUTO);

                  qa.show();
               }
            } catch (Exception ex) {
               return false;
            }
            return false;
         }
      });

      mBreadCrumb = new ArrayList<String>();

      mBaseActivity = (BaseTabActivity) getParent();
      mService = DataHandler.getCurrentRemoteInstance();

      if (mBaseActivity != null && mService != null) {
         mStatusBarHandler = new StatusBarActivityHandler(mBaseActivity, mService);
         mStatusBarHandler.setHome(false);
      }

      loadShares();
      
      mTracker = GoogleAnalyticsTracker.getInstance();
      mTracker.start(Constants.ANALYTICS_ID, this);
   }
   
   @Override
   protected void onResume() {
      mTracker.trackPageView("/" + this.getLocalClassName());
      super.onResume();
   }
   
   @Override
   protected void onDestroy() {
      mTracker.stop();
      super.onDestroy();
   }

   private void loadShares() {
      mLoadingDialog = ProgressDialog.show(getParent(),
            getString(R.string.media_shares_loadshares), getString(R.string.info_loading_title),
            true);
      mLoadingDialog.setCancelable(true);

      mSeriesLoaderTask = new LoadSharesTask();
      mSeriesLoaderTask.execute(0);
   }

   protected void loadFiles(String _path) {
      mLoadingDialog = ProgressDialog.show(getParent(), getString(R.string.media_shares_loadfiles),
            getString(R.string.info_loading_title), true);
      mLoadingDialog.setCancelable(true);
      mBreadCrumb.add(_path);

      mFilesLoaderTask = new LoadFolderTask();
      mFilesLoaderTask.execute(_path);
   }

   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (keyCode == KeyEvent.KEYCODE_BACK) {
         if (!mListView.getAdapter().equals(mListItems)) {
            if (mBreadCrumb.size() == 1) {
               mBreadCrumb.clear();

               loadShares();
            } else {
               mBreadCrumb.remove(mBreadCrumb.size() - 1);// remove current
                                                          // directory
               String lastFolder = mBreadCrumb.get(mBreadCrumb.size() - 1); // get
                                                                            // the
                                                                            // previous
                                                                            // directory
               mBreadCrumb.remove(mBreadCrumb.size() - 1); // remove prev
                                                           // directory since it
                                                           // will be readded on
                                                           // loading
               loadFiles(lastFolder);

            }
            return true;
         }

      }
      return super.onKeyDown(keyCode, event);
   }

}
