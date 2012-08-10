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
package com.mediaportal.ampdroid.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.database.DownloadsDatabaseHandler;
import com.mediaportal.ampdroid.downloadservice.DownloadJob;
import com.mediaportal.ampdroid.downloadservice.DownloadState;
import com.mediaportal.ampdroid.downloadservice.ItemDownloaderHelper;
import com.mediaportal.ampdroid.downloadservice.MediaItemType;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.views.DownloadsDetailsAdapterItem;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.DownloaderUtils;
import com.mediaportal.ampdroid.utils.QuickActionUtils;
import com.mediaportal.ampdroid.utils.Util;

public class DownloadsActivity extends Activity {
   private LoadServiceTask mDownloadsUpdaterTask = null;
   private boolean mUpdating;
   private static int UPDATE_INTERVAL = 2000;
   private static int UPDATE_INTERVAL_SLEEP_CYCLES = 20;

   private class LoadServiceTask extends AsyncTask<String, List<DownloadJob>, Intent> {
      Context mContext;
      private DownloadsDatabaseHandler mUpdaterDatabase;
      private boolean mTriggerUpdate = false;
      private LoadServiceTask(Context _context) {
         mContext = _context;
         mUpdaterDatabase = new DownloadsDatabaseHandler(mContext);
      }
      
      public void triggerUpdate(){
         mTriggerUpdate = true;
      }

      @SuppressWarnings("unchecked")
      @Override
      protected Intent doInBackground(String... _params) {
         mUpdaterDatabase.open();
         while (mUpdating) {
            if (mUpdating) {
               List<DownloadJob> downloads = mUpdaterDatabase.getDownloads();
               publishProgress(downloads);
            }

            try {
               for(int i = 0; i < UPDATE_INTERVAL_SLEEP_CYCLES; i++){
                  if(mTriggerUpdate){
                     mTriggerUpdate = false;
                     break;
                  }
                  Thread.sleep(UPDATE_INTERVAL / UPDATE_INTERVAL_SLEEP_CYCLES);
               }
            } catch (InterruptedException e) {
            }
         }
         mUpdaterDatabase.close();
         return null;
      }

      @Override
      protected void onProgressUpdate(List<DownloadJob>... _params) {
         fillListView(_params[0]);
      }

      @Override
      protected void onPostExecute(Intent result) {
         mDownloadsUpdaterTask = null;
         super.onPostExecute(result);
      }

   }

   private ListView mListView;
   private DownloadsDatabaseHandler mDatabase;
   private LazyLoadingAdapter mAdapter;
   List<DownloadJob> mDownloads;

   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_downloads);
      setTitle(getString(R.string.downloads_title));

      mListView = (ListView) findViewById(R.id.ListViewItems);
      mDatabase = new DownloadsDatabaseHandler(this);

      mAdapter = new LazyLoadingAdapter(this);
      mListView.setAdapter(mAdapter);
      
      Bundle extras = getIntent().getExtras();
      if (extras != null) {
       if(extras.containsKey("notification_id")){
          int id = extras.getInt("notification_id");
          NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(
                Context.NOTIFICATION_SERVICE);
          manager.cancel(id);
       }

      
      }
      mListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> _adapter, View _view, int _position, long _id) {
            ILoadingAdapterItem selectedItem = (ILoadingAdapterItem) mListView
                  .getItemAtPosition(_position);
            DownloadJob job = (DownloadJob) selectedItem.getItem();
            File file = new File(DownloaderUtils.getBaseDirectory() + "/" + job.getFileName());

            String mimeString = MediaItemType.getIntentMimeType(job.getMediaType());
            Intent playIntent = new Intent(Intent.ACTION_VIEW);
            playIntent.setDataAndType(Uri.fromFile(file), mimeString);

            startActivity(playIntent);
         }
      });

      mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
         @Override
         public boolean onItemLongClick(AdapterView<?> _item, View _view, int _pos, long _id) {
            final ILoadingAdapterItem item = (ILoadingAdapterItem) mListView
                  .getItemAtPosition(_pos);

            if (item != null) {
               final QuickAction qa = new QuickAction(_view);
               final DownloadJob job = (DownloadJob) item.getItem();
               final String file = DownloaderUtils.getBaseDirectory() + "/" + job.getFileName();
               final File downloadFile = new File(file);

               if (job.getState() == DownloadState.Running
                     || job.getState() == DownloadState.Paused
                     || job.getState() == DownloadState.Queued) {

                  QuickActionUtils.createQuickAction(_view.getContext(), qa,
                        R.string.quickactions_cancel,
                        R.drawable.quickaction_remove,
                        new OnClickListener() {
                           @Override
                           public void onClick(View _view) {
                              Log.d(Constants.LOG_CONST, "User request download cancellation");
                              job.setRequestCancelation(true);
                              if(job.getState() == DownloadState.Queued){
                                 job.setState(DownloadState.Stopped);
                              }
                              mDatabase.open();
                              mDatabase.updateDownloads(job);
                              mDatabase.close();
                              mDownloadsUpdaterTask.triggerUpdate();
                              qa.dismiss();
                           }
                        });
               } else {
                  // the job is already done (or failed or aborted)
                  // give the option to remove the download from the list
                  QuickActionUtils.createQuickAction(_view.getContext(), qa,
                        R.string.quickactions_remove,
                        R.drawable.quickaction_remove,
                        new OnClickListener() {
                           @Override
                           public void onClick(View _view) {
                              Log.d(Constants.LOG_CONST, "User request download removal");
                              mAdapter.removeItem(item);
                              mDatabase.open();
                              mDatabase.removeDownload(job);
                              mDatabase.close();
                              mAdapter.notifyDataSetChanged();
                              qa.dismiss();
                           }
                        });
                  if (downloadFile.exists()) {
                     // Download not running (or paused) any more and the file
                     // exists
                     // Give the user an option to delete it
                     QuickActionUtils.createQuickAction(_view.getContext(), qa,
                          R.string.quickactions_delete,
                           R.drawable.quickaction_delete,
                           new OnClickListener() {
                              @Override
                              public void onClick(View _view) {
                                 try {
                                    Log.i(Constants.LOG_CONST, "Deleting file " + file);
                                    if (downloadFile.delete()) {
                                       Util.showToast(_view.getContext(),
                                             getString(R.string.downloads_delete_file_success));
                                    } else {
                                       Util.showToast(_view.getContext(),
                                             getString(R.string.downloads_delete_file_failed));
                                    }
                                 } catch (Exception ex) {
                                    Util.showToast(_view.getContext(),
                                          getString(R.string.downloads_delete_file_failed));
                                    Log.e(Constants.LOG_CONST, ex.toString());
                                 }
                                 qa.dismiss();
                              }
                           });
                  }
               }

               if (downloadFile.exists()) {
                  // File exists -> give user the option to play locally. File
                  // might still be
                  // downloading, but some files can be played even if not
                  // downloaded completely
                  QuickActionUtils.createQuickAction(_view.getContext(), qa,
                        R.string.quickactions_playdevice,
                        R.drawable.quickaction_play,
                        new OnClickListener() {
                           @Override
                           public void onClick(View _view) {
                              File file = new File(DownloaderUtils.getBaseDirectory() + "/" + job.getFileName());

                              String mimeString = MediaItemType.getIntentMimeType(job.getMediaType());
                              Intent playIntent = new Intent(Intent.ACTION_VIEW);
                              playIntent.setDataAndType(Uri.fromFile(file), mimeString);

                              startActivity(playIntent);
                              qa.dismiss();
                           }
                        });
               } else {
                  QuickActionUtils.createQuickAction(_view.getContext(), qa,
                        R.string.quickactions_redownload,
                        R.drawable.quickaction_download,
                        new OnClickListener() {
                           @Override
                           public void onClick(View _view) {
                              Intent download = ItemDownloaderHelper.createDownloadIntent(
                                    _view.getContext(), job);
                              startService(download);
                              qa.dismiss();
                           }
                        });
               }
               qa.show();
            }
            return true;
         }
      });
   }

   public void fillListView(DownloadJob[] _params) {
      List<DownloadJob> listDownloads = new ArrayList<DownloadJob>();
      for (DownloadJob j : _params) {
         listDownloads.add(j);
      }
      fillListView(listDownloads);
   }

   private void fillListView(List<DownloadJob> _downloads) {
      if (mDownloads == null) {
         mDownloads = _downloads;
         if (mDownloads != null) {
            for (DownloadJob j : mDownloads) {
               mAdapter.addItem(new DownloadsDetailsAdapterItem(j, this));
            }
            mAdapter.notifyDataSetChanged();
         }
      } else {
         // update
         boolean updateRequired = false;
         boolean insertRequired = false;
         if (_downloads != null) {
            for (DownloadJob newJob : _downloads) {
               boolean found = false;
               for (int i = 0; i < mAdapter.getCount(); i++) {
                  DownloadsDetailsAdapterItem item = (DownloadsDetailsAdapterItem) mAdapter
                        .getItem(i);
                  DownloadJob oldJob = (DownloadJob) item.getItem();
                  if (newJob.getId() == oldJob.getId()) {
                     long oldTime = oldJob.getDateLastUpdated().getTime();
                     long newTime = newJob.getDateLastUpdated().getTime();
                     if (oldTime < newTime) {
                        // job has been updated
                        oldJob.updateValues(newJob);
                        item.updateViewValues();
                        updateRequired = true;
                     }
                     found = true;
                     break;
                  }
               }
               if (!found) {
                  mAdapter.addItem(new DownloadsDetailsAdapterItem(newJob, this));
                  insertRequired = true;
               }
            }
            
            for (int i = 0; i < mAdapter.getCount(); i++) {
               DownloadsDetailsAdapterItem item = (DownloadsDetailsAdapterItem) mAdapter
                     .getItem(i);
               DownloadJob oldJob = (DownloadJob) item.getItem();
               boolean found = false;
               for (DownloadJob newJob : _downloads) {
                  if (newJob.getId() == oldJob.getId()) {
                     found = true;
                     break;
                  }
               }
               
               if(!found){
                  //item no longer in database -> remove from adapter
                  mAdapter.removeItem(item);
                  updateRequired = true;
               }
            }

            if (updateRequired) {
               mAdapter.notifyDataSetChanged();
            }

            if (insertRequired) {
               mAdapter.notifyDataSetChanged();
            }
         }
         else{
            mDownloads = null;
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
         }
      }
   }

   @Override
   protected void onPause() {
      mUpdating = false;
      super.onPause();
   }

   @Override
   protected void onResume() {
      if (mDownloadsUpdaterTask == null) {
         mUpdating = true;
         mDownloadsUpdaterTask = new LoadServiceTask(this);
         mDownloadsUpdaterTask.execute();
      }
      super.onResume();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu _menu) {
      super.onCreateOptionsMenu(_menu);

      MenuItem clearDownloadsItem = _menu.add(0, Menu.FIRST, Menu.NONE,
            getString(R.string.menu_clear_downloads));
      clearDownloadsItem.setIcon(R.drawable.ic_menu_close_clear_cancel);
      clearDownloadsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            if (mDownloads != null) {
               boolean hasQueued = false;
               for (DownloadJob j : mDownloads) {
                  if (j.getState() == DownloadState.Queued) {
                     hasQueued = true;
                     break;
                  }
               }
               
               if(hasQueued){
                  confirmDeleteQueued();
               }
               else{
                  deleteJobs(false);
               }
            }
            return true;
         }
      });
      
      return true;
   }

   protected void deleteJobs(boolean _deleteQueued) {
      mDatabase.open();
      for (DownloadJob j : mDownloads) {
         if (j.getState() != DownloadState.Running && j.getState() != DownloadState.Paused
             && (_deleteQueued || j.getState() != DownloadState.Queued)) {
            mDatabase.removeDownload(j);
         }
      }
      mDatabase.close();
      mDownloadsUpdaterTask.triggerUpdate();
   }

   protected void confirmDeleteQueued() {
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
      builder.setTitle(getString(R.string.downloads_also_remove_queued_title));
      builder.setMessage(getString(R.string.downloads_also_remove_queued_text));
      builder.setPositiveButton(getString(R.string.dialog_yes),
            new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                  deleteJobs(true);
                  dialog.cancel();
               }
            });
      builder.setNegativeButton(getString(R.string.dialog_no),
            new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                  deleteJobs(false);
                  dialog.cancel();
               }
            });
      builder.create().show();
   }
}
