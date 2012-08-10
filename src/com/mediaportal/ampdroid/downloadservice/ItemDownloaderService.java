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
package com.mediaportal.ampdroid.downloadservice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.DownloadsActivity;
import com.mediaportal.ampdroid.database.DownloadsDatabaseHandler;
import com.mediaportal.ampdroid.lists.Utils;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.DownloaderUtils;
import com.mediaportal.ampdroid.utils.StringUtils;
import com.mediaportal.ampdroid.utils.Util;

public class ItemDownloaderService extends Service {
   public static final String ITEM_DOWNLOAD_STARTED = "download_started";
   public static final String ITEM_DOWNLOAD_PROGRESSED = "download_progressed";
   public static final String ITEM_DOWNLOAD_FINISHED = "download_finished";
   public static final int NOTIFICATION_ID = 44;
   public static final int UPDATE_INTERVAL = 2000;

   private Intent mIntent;
   private ArrayList<DownloadJob> mDownloadJobs;
   private AsyncTask<String, Integer, HashMap<DownloadState, Integer>> mDownloader;
   private DownloadsDatabaseHandler mDownloadDatabase;
   private HashMap<Integer, DownloadGroup> mDownloadGroups = new HashMap<Integer, DownloadGroup>();

   private class DownloaderTask extends AsyncTask<String, Integer, HashMap<DownloadState, Integer>> {
      private Notification mNotification;
      private NotificationManager mNotificationManager;
      private DownloadJob mCurrentJob;
      private int mNumberOfJobsDone;
      private Context mContext;
      private String mToastMessage;
      private DownloadGroup mCurrentGroup;

      private DownloaderTask(Context _context) {
         mNumberOfJobsDone = 0;
         mContext = _context;
      }

      @Override
      protected HashMap<DownloadState, Integer> doInBackground(String... params) {
         HashMap<DownloadState, Integer> downloadResult = new HashMap<DownloadState, Integer>();
         while (mDownloadJobs != null && mDownloadJobs.size() > 0) {
            DownloadJob topmostTask = null;
            synchronized (mDownloadJobs) {
               topmostTask = mDownloadJobs.get(0);
               mDownloadJobs.remove(0);
            }

            DownloadState state = downloadFile(topmostTask);

            if (topmostTask.getGroupId() == 0
                  || topmostTask.getGroupPart() == topmostTask.getGroupSize() - 1) {
               // either not part of a group or the last part of the group
               // TODO: what if only part of the group fails?
               if (downloadResult.containsKey(state)) {
                  int num = downloadResult.get(state);
                  downloadResult.put(state, num + 1);
               } else {
                  downloadResult.put(state, 1);
               }
            }
         }

         return downloadResult;
      }

      private DownloadState downloadFile(DownloadJob _job) {
         File downloadFile = null;
         mToastMessage = null;
         long lastUpdated = new Date().getTime();
         boolean cancelRequested = false;

         mDownloadDatabase.open();
         DownloadJob downloadJob = mDownloadDatabase.getDownload(_job.getId());
         DownloadGroup group = null;

         if (downloadJob == null || downloadJob.getState() != DownloadState.Queued) {
            // check if the download has been stopped in the meantime (between
            // creation and start)
            return DownloadState.Stopped;
         }

         if (downloadJob.getGroupId() != 0 && mDownloadGroups.containsKey(downloadJob.getGroupId())) {
            group = mDownloadGroups.get(downloadJob.getGroupId());
         }

         if (group == null || downloadJob.getGroupPart() == 0) {
            mNumberOfJobsDone++;
         }

         try {
            mCurrentJob = downloadJob;
            mCurrentGroup = group;
            URL myFileUrl = new URL(downloadJob.getUrl());
            String myFileName = downloadJob.getFileName();

            Intent onClickIntent = new Intent(getApplicationContext(), DownloadsActivity.class);

            downloadJob.setState(DownloadState.Running);
            downloadJob.setDateStarted(new Date());

            mDownloadDatabase.updateDownloads(downloadJob);

            // configure the intent
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                  onClickIntent, 0);

            // This is who should be launched if the user selects the app icon
            // in the notification.
            // Intent appIntent = new Intent(getApplicationContext(),
            // HomeActivity.class);

            // configure the notification
            mNotification = new Notification(R.drawable.quickaction_download, "Download",
                  System.currentTimeMillis());
            mNotification.flags = mNotification.flags | Notification.FLAG_ONGOING_EVENT;
            mNotification.contentView = new RemoteViews(getApplicationContext().getPackageName(),
                  R.layout.download_progress);
            mNotification.contentIntent = pendingIntent;
            mNotification.contentView.setImageViewResource(R.id.status_icon, R.drawable.icon);

            if (group == null) {
               createNotificationText(1, 0);
            } else {
               createNotificationText(1, group.getPercentageDone());
            }

            mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(
                  Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);

            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();

            if (downloadJob.isUseAut()) {
               final String username = downloadJob.getUsername();
               final String password = downloadJob.getPassword();
               Authenticator.setDefault(new Authenticator() {
                  protected PasswordAuthentication getPasswordAuthentication() {
                     return new PasswordAuthentication(username, password.toCharArray());
                  }
               });
            } else {
               Authenticator.setDefault(null);
            }

            conn.setDoInput(true);
            conn.connect();

            InputStream inputStream = conn.getInputStream();

            downloadFile = new File(DownloaderUtils.getBaseDirectory() + "/" + myFileName);
            File donwloadDir = new File(Utils.getFolder(downloadFile.toString(), "/"));

            if (!donwloadDir.exists()) {
               if (donwloadDir.mkdirs()) {
                  Log.d(Constants.LOG_CONST_ITEMDOWNLOADER, "created directory on sd card");
               }
            }

            OutputStream out = new FileOutputStream(downloadFile);
            byte buf[] = new byte[1024];

            long fileSize = downloadJob.getLength();
            if (fileSize == -1) {
               // if no filesize was specified, try to get the filesize from the
               // connection
               fileSize = conn.getContentLength();
            }

            long read = 0;
            int currentProgress = 0;
            int len;
            while (!cancelRequested && (len = inputStream.read(buf)) > 0) {
               out.write(buf, 0, len);

               read += len;
               if (group != null) {
                  group.addRead(len);
               }

               long curTime = new Date().getTime();
               if (curTime > lastUpdated + UPDATE_INTERVAL) {
                  int progress = 0;
                  if (fileSize == -99 || fileSize == -1 || fileSize == 0) {
                     // we don't know the filesize
                     publishProgress(0, (int) read);
                  } else {
                     if (group == null) {
                        if (fileSize == 0) {
                           progress = -99;
                        } else {
                           progress = (int) (read * 100 / fileSize);
                        }
                     } else {
                        long totalLength = group.getGroupContentSize();
                        long totalDone = group.getTotalDone();
                        if (totalLength == 0) {
                           progress = -99;
                        } else {
                           progress = (int) (totalDone * 100 / totalLength);
                        }
                        group.setPercentageDone(progress);
                     }

                     currentProgress = progress;

                     downloadJob.setProgress(currentProgress);

                     mDownloadDatabase.updateProgress(downloadJob.getId(),
                           downloadJob.getProgress());

                     publishProgress(1, progress);
                  }
                  cancelRequested = mDownloadDatabase.getCancelRequest(downloadJob);
                  //Log.d(Constants.LOG_CONST_ITEMDOWNLOADER, "Cancel Requested: " + cancelRequested);
                  lastUpdated = curTime;
               }
            }
            try {
               if (cancelRequested) {
                  Log.d(Constants.LOG_CONST_ITEMDOWNLOADER, "Disconnecting socket");
                  conn.disconnect();
               }
               Log.d(Constants.LOG_CONST_ITEMDOWNLOADER, "Closing in stream");
               inputStream.close();
            } catch (Exception ex) {
               Log.d(Constants.LOG_CONST_ITEMDOWNLOADER, ex.toString());
            }

            Log.d(Constants.LOG_CONST_ITEMDOWNLOADER, "Closing out stream");
            out.flush();
            out.close();

            if (cancelRequested) {
               Log.d(Constants.LOG_CONST_ITEMDOWNLOADER, "Download Cancel requested");
               if (downloadFile != null) {
                  deleteFile(downloadFile);
               }
               downloadJob.setState(DownloadState.Stopped);
               downloadJob.setErrorMessage("Cancelled by User");
            } else {
               Log.d(Constants.LOG_CONST_ITEMDOWNLOADER, "Download Finished");
               downloadJob.setState(DownloadState.Finished);
               downloadJob.setProgress(100);
            }
            downloadJob.setDateFinished(new Date());

            mDownloadDatabase.updateDownloads(downloadJob);

            mDownloadDatabase.close();
            // return true only if download finished
            return downloadJob.getState();
         } catch (MalformedURLException e) {
            Log.w(Constants.LOG_CONST_ITEMDOWNLOADER, e.toString());
            if (e != null) {
               mToastMessage = e.getMessage();
               publishProgress(-1);
            }
         } catch (UnsupportedEncodingException e) {
            Log.w(Constants.LOG_CONST_ITEMDOWNLOADER, e.toString());
            if (e != null) {
               mToastMessage = e.getMessage();
               publishProgress(-1);
            }
         } catch (IOException e) {
            Log.w(Constants.LOG_CONST_ITEMDOWNLOADER, e.toString());
            if (e != null) {
               mToastMessage = e.getMessage();
               publishProgress(-1);
            }
         } catch (Exception e) {
            Log.w(Constants.LOG_CONST_ITEMDOWNLOADER, e.toString());
            if (e != null) {
               mToastMessage = e.getMessage();
               publishProgress(-1);
            }
         }

         // delete unfinished files
         if (downloadFile != null) {
            deleteFile(downloadFile);
         }
         downloadJob.setState(DownloadState.Error);
         downloadJob.setErrorMessage(mToastMessage);
         downloadJob.setDateFinished(new Date());
         mDownloadDatabase.updateDownloads(downloadJob);
         mDownloadDatabase.close();

         return downloadJob.getState();
      }

      private void deleteFile(File _file) {
         try {
            if (!_file.getPath().endsWith("_debug.ts")) {
               _file.delete();
            }
         } catch (Exception ex) {
            Log.e(Constants.LOG_CONST_ITEMDOWNLOADER, ex.toString());
         }

      }

      private void createNotificationText(int _type, int _progress) {
         int totalDownloads = mNumberOfJobsDone + getNumberOfTotalDownloads();
         String filename = null;
         if (mCurrentGroup == null) {
            filename = mCurrentJob.getDisplayName();
         } else {
            filename = mCurrentJob.getGroupName();
         }

         String overview = (totalDownloads > 0 ? mNumberOfJobsDone + "/" + totalDownloads : "");

         String progressText = "";
         if (_type == 0) {
            progressText = StringUtils.formatFileSize(_progress);
         } else if (_type == 1) {
            progressText = _progress + " %";
         }

         mNotification.contentView.setTextViewText(R.id.TextViewNotificationFileName, filename);
         mNotification.contentView.setTextViewText(R.id.TextViewNotificationOverview, overview);

         mNotification.contentView.setTextViewText(R.id.TextViewNotificationProgressText,
               progressText);

         if (_progress != -99) {
            mNotification.contentView.setProgressBar(R.id.ProgressBarNotificationTransferStatus,
                  100, _progress, false);
         } else {
            mNotification.contentView.setProgressBar(R.id.ProgressBarNotificationTransferStatus,
                  100, 0, true);
         }

      }

      /***
       * Calculates the number of downloads in the queue (takes in accounts that
       * multiple items may belong to one group)
       * 
       * @return Number of total downloads
       */
      private int getNumberOfTotalDownloads() {
         int numDownloads = 0;
         synchronized (mDownloadJobs) {
            for (DownloadJob j : mDownloadJobs) {
               if (j.getGroupId() == 0 || j.getGroupPart() == 0) {
                  numDownloads++;
               }
            }
         }

         return numDownloads;
      }

      @Override
      protected void onPostExecute(HashMap<DownloadState, Integer> _result) {
         if (mNotificationManager != null) {
            stopSelf();
            Log.d(Constants.LOG_CONST_ITEMDOWNLOADER, "Download ended, cancel progress item");
            mNotificationManager.cancel(NOTIFICATION_ID);

            Intent onClickIntent = new Intent(getApplicationContext(), DownloadsActivity.class);
            onClickIntent.putExtra("notification_id", 49);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                  onClickIntent, PendingIntent.FLAG_CANCEL_CURRENT);// PendingIntent.FLAG_CANCEL_CURRENT
            Notification notification = new Notification(R.drawable.mp_logo_2,
                  mContext.getString(R.string.notification_title), System.currentTimeMillis());
            if (_result != null) {
               notification.setLatestEventInfo(getApplicationContext(),
                     mContext.getString(R.string.notification_title),
                     createFinishedNotificationText(_result), pendingIntent);
            } else {
               notification.setLatestEventInfo(getApplicationContext(),
                     mContext.getString(R.string.notification_title),
                     mContext.getString(R.string.notification_download_failed), pendingIntent);
            }

            mNotificationManager.notify(49, notification);
         }
         super.onPostExecute(_result);
      }

      private String createFinishedNotificationText(HashMap<DownloadState, Integer> _result) {
         int numFinished = 0;
         if (_result.containsKey(DownloadState.Finished)) {
            numFinished += _result.get(DownloadState.Finished);
         }
         numFinished = mNumberOfJobsDone;

         int numFailed = 0;
         if (_result.containsKey(DownloadState.Stopped)) {
            numFailed += _result.get(DownloadState.Stopped);
         }
         if (_result.containsKey(DownloadState.Error)) {
            numFailed += _result.get(DownloadState.Error);
         }

         // Will result in smth. like: "3 Download(s) finished (1 failed)"
         String retString = numFinished
               + mContext.getString(R.string.notification_download_succeeded);
         if (numFailed > 0) {
            retString += " (" + numFailed
                  + mContext.getString(R.string.notification_download_failed) + ")";
         }

         return retString;
      }

      @Override
      protected void onProgressUpdate(Integer... values) {
         int type = values[0];

         if (type != -1) {
            int progress = values[1];
            createNotificationText(type, progress);
            // inform the progress bar of updates in progress
            mNotificationManager.notify(NOTIFICATION_ID, mNotification);
         } else {
            Util.showToast(mContext, mToastMessage);
         }
      }

   }

   @Override
   public IBinder onBind(Intent intent) {
      mIntent = intent;
      return null;
   }

   @Override
   public void onCreate() {
      mDownloadJobs = new ArrayList<DownloadJob>();
   }

   @Override
   public int onStartCommand(Intent intent, int flags, int startId) {
      if (mDownloadDatabase == null) {
         mDownloadDatabase = new DownloadsDatabaseHandler(this);
      }

      if ((flags & START_FLAG_RETRY) == 0) {

      } else {

      }

      DownloadJob job = ItemDownloaderHelper.getDownloadJobFromIntent(intent);
      if (job == null) {
         Log.d(Constants.LOG_CONST_ITEMDOWNLOADER, "Couldn't get job from intent");
         return Service.START_STICKY;
      }
      DownloadsDatabaseHandler db = new DownloadsDatabaseHandler(this);
      db.open();
      job.setDateAdded(new Date());
      job.setState(DownloadState.Queued);
      if (!db.updateDownloads(job)) {
         db.addDownload(job);
      }
      db.close();

      synchronized (mDownloadJobs) {
         mDownloadJobs.add(job);
      }

      // Toast notification if job either not part of a group or first part of a
      // group
      if (job.getGroupId() == 0) {
         if (job.getDisplayName() != null) {
            Util.showToast(this, "Added " + job.getDisplayName() + " to download list");
         } else {
            Util.showToast(this, "Added " + job.getFileName() + " to download list");
         }
      } else if (job.getGroupPart() == 0) {
         Util.showToast(this, "Added " + job.getGroupName() + " to download list");
      }

      int groupId = job.getGroupId();
      if (groupId != 0) {
         // part of a group
         if (!mDownloadGroups.containsKey(groupId)) {
            DownloadGroup group = new DownloadGroup(groupId);
            mDownloadGroups.put(groupId, group);
         }
         mDownloadGroups.get(groupId).addItem(job);
      }

      if (mDownloader == null || mDownloader.isCancelled()) {
         mDownloader = new DownloaderTask(this).execute();
      }

      return Service.START_STICKY;
   }
}
