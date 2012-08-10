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
package com.mediaportal.ampdroid.lists.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.downloadservice.DownloadJob;
import com.mediaportal.ampdroid.downloadservice.DownloadState;
import com.mediaportal.ampdroid.lists.DownloadsViewHolder;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.ViewHolder;
import com.mediaportal.ampdroid.utils.DateTimeHelper;

public class DownloadsDetailsAdapterItem implements ILoadingAdapterItem {
   private DownloadJob mDownload;
   private String mFinished;
   private Context mContext;

   public DownloadsDetailsAdapterItem(DownloadJob _job, Context _context) {
      mDownload = _job;
      mContext = _context;
      
      updateViewValues();
   }

   public void updateViewValues() {
      if (mDownload.getState() == DownloadState.Finished) {
         mFinished = DateTimeHelper.getDateString(mDownload.getDateFinished(), true);
      }else if(mDownload.getState() == DownloadState.Running){
         mFinished = String.valueOf(mDownload.getProgress()) + " %";
      } else if(mDownload.getState() == DownloadState.Queued){
         mFinished = DateTimeHelper.getDateString(mDownload.getDateAdded(), true);
      }else {
         mFinished = DateTimeHelper.getDateString(mDownload.getDateFinished(), true) + 
                     " (" + String.valueOf(mDownload.getProgress()) + " %)";
      }
   }

   @Override
   public LazyLoadingImage getImage() {
      return null;
   }

   @Override
   public int getLoadingImageResource() {
      return 0;
   }

   @Override
   public int getDefaultImageResource() {
      return 0;
   }

   @Override
   public int getType() {
      return 0;
   }

   @Override
   public int getXml() {
      return R.layout.listitem_download_details;
   }

   @Override
   public Object getItem() {
      return mDownload;
   }

   @Override
   public ViewHolder createViewHolder(View _view) {
      DownloadsViewHolder holder = new DownloadsViewHolder();
      holder.state = (TextView) _view.findViewById(R.id.TextViewDownloadState);
      holder.finished = (TextView) _view.findViewById(R.id.TextViewDownloadFinished);
      holder.filename = (TextView) _view.findViewById(R.id.TextViewDownloadFilename);
      holder.text = (TextView) _view.findViewById(R.id.TextViewDownloadName);

      return holder;
   }

   @Override
   public void fillViewFromViewHolder(ViewHolder _holder) {
      DownloadsViewHolder holder = (DownloadsViewHolder) _holder;
      holder.state.setText(mDownload.getState().toStringLocalised(mContext));
      holder.finished.setText(mFinished);
      holder.text.setText(mDownload.getDisplayName());
      holder.filename.setText(mDownload.getFileName());
   }

   @Override
   public String getSection() {
      return null;
   }

}
