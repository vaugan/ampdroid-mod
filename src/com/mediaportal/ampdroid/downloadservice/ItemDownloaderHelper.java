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

import android.content.Context;
import android.content.Intent;

public class ItemDownloaderHelper {

   public static Intent createDownloadIntent(Context _context, DownloadJob _job) {
      Intent download = new Intent(_context, ItemDownloaderService.class);
      download.putExtra("url", _job.getUrl());
      download.putExtra("file_name", _job.getFileName());
      download.putExtra("display_name", _job.getDisplayName());
      download.putExtra("length", _job.getLength());
      download.putExtra("item_type", _job.getMediaTypeInt());

      download.putExtra("useAuth", _job.isUseAut());
      download.putExtra("username", _job.getUsername());
      download.putExtra("password", _job.getPassword());
      download.putExtra("job_id", _job.getId());

      download.putExtra("group_name", _job.getGroupName());
      download.putExtra("group_id", _job.getGroupId());
      download.putExtra("group_part", _job.getGroupPart());
      download.putExtra("group_size", _job.getGroupSize());

      return download;
   }

   public static DownloadJob getDownloadJobFromIntent(Intent intent) {
      if (intent != null) {
         String url = intent.getStringExtra("url");
         String fileName = intent.getStringExtra("file_name");
         String displayName = intent.getStringExtra("display_name");
         long length = intent.getLongExtra("length", 0);
         boolean useAuth = intent.getBooleanExtra("useAuth", false);
         String username = intent.getStringExtra("username");
         String password = intent.getStringExtra("password");
         int itemType = intent.getIntExtra("item_type", 0);
         int id = intent.getIntExtra("job_id", 0);

         String groupName = intent.getStringExtra("group_name");
         int groupId = intent.getIntExtra("group_id", 0);
         int groupPart = intent.getIntExtra("group_part", 0);
         int groupSize = intent.getIntExtra("group_size", 0);

         DownloadJob job = new DownloadJob();
         job.setId(id);
         job.setUrl(url);
         job.setFileName(fileName);
         job.setDisplayName(displayName);
         job.setLength(length);
         job.setMediaTypeInt(itemType);

         job.setGroupId(groupId);
         job.setGroupName(groupName);
         job.setGroupPart(groupPart);
         job.setGroupSize(groupSize);

         if (useAuth) {
            job.setAuth(username, password);
         }
         return job;
      }
      return null;
   }

}
