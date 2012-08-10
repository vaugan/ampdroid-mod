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
package com.mediaportal.ampdroid.utils;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.videoplayback.StreamingDetailsActivity;
import com.mediaportal.ampdroid.activities.videoplayback.VideoStreamingPlayerActivity;
import com.mediaportal.ampdroid.api.ApiCredentials;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.FileInfo;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.downloadservice.DownloadJob;
import com.mediaportal.ampdroid.downloadservice.ItemDownloaderHelper;
import com.mediaportal.ampdroid.downloadservice.MediaItemType;
import com.mediaportal.ampdroid.quickactions.ActionItem;
import com.mediaportal.ampdroid.quickactions.IQuickActionContainer;
import com.mediaportal.ampdroid.settings.PreferencesManager;

public class QuickActionUtils {

   public static void createQuickAction(Context _context, IQuickActionContainer _parent,
         int _titleStringId, int _iconResId, OnClickListener _onClickListener) {
      ActionItem quickAction = new ActionItem();
      quickAction.setTitle(_context.getString(_titleStringId));
      quickAction.setIcon(_context.getResources().getDrawable(_iconResId));
      quickAction.setOnClickListener(_onClickListener);
      _parent.addActionItem(quickAction);
   }

   public static void createPlayOnClientQuickAction(Context _context,
         IQuickActionContainer _parent, DataHandler _service, String _file) {

      final String playFile = _file;
      final DataHandler service = _service;
      final IQuickActionContainer parent = _parent;
      final Context context = _context;

      ActionItem actionItem = new ActionItem();
      actionItem.setTitle(_context.getString(R.string.quickactions_playclient));
      if (_service.isClientControlConnected()) {
         actionItem.setIcon(_context.getResources().getDrawable(R.drawable.quickaction_play_pc));
         actionItem.setEnabled(true);
      } else {
         actionItem.setIcon(_context.getResources().getDrawable(
               R.drawable.quickaction_play_pc_disabled));
         actionItem.setEnabled(false);
      }
      actionItem.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View _view) {
            service.playVideoFileOnClient(playFile, 0);

            parent.dismiss();
         }
      });

      _parent.addActionItem(actionItem);
   }

   public static void createPlayOnClientQuickAction(Context _context,
         IQuickActionContainer _parent, DataHandler _service, OnClickListener _onClickListener) {
      ActionItem actionItem = new ActionItem();
      actionItem.setTitle(_context.getString(R.string.quickactions_playclient));
      if (_service.isClientControlConnected()) {
         actionItem.setIcon(_context.getResources().getDrawable(R.drawable.quickaction_play_pc));
         actionItem.setEnabled(true);
      } else {
         actionItem.setIcon(_context.getResources().getDrawable(
               R.drawable.quickaction_play_pc_disabled));
         actionItem.setEnabled(false);
      }
      actionItem.setOnClickListener(_onClickListener);

      _parent.addActionItem(actionItem);
   }

   public static void createDownloadSdCardQuickAction(final Context _context,
         IQuickActionContainer _parent, DataHandler _service, View.OnClickListener _onClickListener) {
      ActionItem actionItem = new ActionItem();
      actionItem.setTitle(_context.getString(R.string.quickactions_downloadsd));
      actionItem.setIcon(_context.getResources().getDrawable(R.drawable.quickaction_download));
      actionItem.setOnClickListener(_onClickListener);

      _parent.addActionItem(actionItem);
   }

   public static void createDownloadSdCardQuickAction(final Context _context,
         IQuickActionContainer _parent, DataHandler _service, String _itemId, String _epFile,
         DownloadItemType _itemType, MediaItemType _mediaType, String _fileName, String _displayName) {

      final DataHandler service = _service;
      final IQuickActionContainer parent = _parent;
      final Context context = _context;
      final String itemId = _itemId;
      final String epFile = _epFile;
      final DownloadItemType itemType = _itemType;
      final MediaItemType mediaType = _mediaType;
      final String fileName = _fileName;
      final String displayName = _displayName;

      ActionItem actionItem = new ActionItem();
      actionItem.setTitle(_context.getString(R.string.quickactions_downloadsd));
      actionItem.setIcon(_context.getResources().getDrawable(R.drawable.quickaction_download));
      actionItem.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View _view) {
            String url = service.getDownloadUri(itemId, itemType);
            FileInfo info = service.getFileInfo(itemId, itemType);
            ApiCredentials cred = service.getDownloadCredentials();
            if (url != null) {
               DownloadJob job = new DownloadJob();
               job.setUrl(url);
               job.setFileName(fileName);
               job.setDisplayName(displayName);
               job.setMediaType(mediaType);
               if (info != null) {
                  job.setLength(info.getLength());
               }
               if (cred.useAut()) {
                  job.setAuth(cred.getUsername(), cred.getPassword());
               }

               Intent download = ItemDownloaderHelper.createDownloadIntent(_view.getContext(), job);
               context.startService(download);

               parent.dismiss();
            }
         }
      });

      _parent.addActionItem(actionItem);
   }

   public static void createPlayOnDeviceQuickAction(Context _context,
         IQuickActionContainer _parent, File _localFile, MediaItemType _mediaType) {

      final IQuickActionContainer parent = _parent;
      final Context context = _context;
      final File localFile = _localFile;
      final MediaItemType mediaType = _mediaType;

      ActionItem actionItem = new ActionItem();
      actionItem.setTitle(_context.getString(R.string.quickactions_playdevice));
      actionItem.setIcon(_context.getResources().getDrawable(R.drawable.quickaction_play));
      actionItem.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View _view) {
            Intent playIntent = new Intent(Intent.ACTION_VIEW);
            playIntent.setDataAndType(Uri.fromFile(localFile),
                  MediaItemType.getIntentMimeType(mediaType));
            context.startActivity(playIntent);

            parent.dismiss();
         }
      });

      _parent.addActionItem(actionItem);
   }

   public static void createPlayOnDeviceQuickAction(Context _context,
         IQuickActionContainer _parent, OnClickListener _onClickListener) {
      ActionItem actionItem = new ActionItem();
      actionItem.setTitle(_context.getString(R.string.quickactions_playdevice));
      actionItem.setIcon(_context.getResources().getDrawable(R.drawable.quickaction_play));
      actionItem.setOnClickListener(_onClickListener);

      _parent.addActionItem(actionItem);
   }

   public static void createStreamOnClientQuickAction(Context _context,
         IQuickActionContainer _parent, DataHandler _service, String _itemId,
         DownloadItemType _itemType, String _fileName, String _displayName) {
      final IQuickActionContainer parent = _parent;
      final Context context = _context;
      final String itemId = _itemId;
      final DownloadItemType itemType = _itemType;
      final String displayName = _displayName;

      ActionItem actionItem = new ActionItem();
      actionItem.setTitle(_context.getString(R.string.quickactions_streamdevice));
      actionItem.setIcon(_context.getResources().getDrawable(R.drawable.quickaction_stream));
      actionItem.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View _view) {
            Intent download = new Intent(_view.getContext(), StreamingDetailsActivity.class);
            download.putExtra("video_id", itemId);
            download.putExtra("video_type", DownloadItemType.toInt(itemType));
            download.putExtra("video_name", displayName);
            context.startActivity(download);

            parent.dismiss();
         }
      });

      actionItem.setOnLongClickListener(new OnLongClickListener() {
         @Override
         public boolean onLongClick(View _view) {
            String defaultProfile = null;
            if (itemType == DownloadItemType.LiveTv) {
               defaultProfile = PreferencesManager.getDefaultProfile(true);
            } else {
               defaultProfile = PreferencesManager.getDefaultProfile(false);
            }
            if (defaultProfile != null) {
               Intent download = new Intent(_view.getContext(), VideoStreamingPlayerActivity.class);
               download.putExtra("video_id", itemId);
               download.putExtra("video_type", DownloadItemType.toInt(itemType));
               download.putExtra("video_name", displayName);
               context.startActivity(download);
            } else {
               Util.showToast(context, "no default profile defained yet");
            }

            parent.dismiss();
            return false;
         }
      });

      _parent.addActionItem(actionItem);
   }

   public static void createDetailsQuickAction(Context _context, IQuickActionContainer _parent,
         OnClickListener _onClickListener) {
      ActionItem actionItem = new ActionItem();
      actionItem.setTitle(_context.getString(R.string.quickactions_details));
      actionItem.setIcon(_context.getResources().getDrawable(R.drawable.quickaction_details));
      actionItem.setOnClickListener(_onClickListener);

      _parent.addActionItem(actionItem);
   }
}
