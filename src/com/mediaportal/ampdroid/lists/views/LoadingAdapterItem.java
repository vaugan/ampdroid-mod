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

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.SubtextViewHolder;
import com.mediaportal.ampdroid.lists.ViewHolder;

public class LoadingAdapterItem implements ILoadingAdapterItem {

   private String mLoadingText;
   private SubtextViewHolder mHolder;
   private boolean mLoading;

   public LoadingAdapterItem(String _loadingText) {
      setLoadingText(_loadingText);
   }

   @Override
   public LazyLoadingImage getImage() {
      return null;
   }

   @Override
   public int getType() {
      return -99;
   }

   @Override
   public int getXml() {
      return R.layout.listitem_loadingindicator;
   }

   @Override
   public Object getItem() {
      return null;
   }

   @Override
   public ViewHolder createViewHolder(View _view) {
      SubtextViewHolder holder = new SubtextViewHolder();
      holder.text = (TextView) _view.findViewById(R.id.TextViewLoadingText);
      holder.progressBar = (ProgressBar) _view.findViewById(R.id.ProgressBarLoading);
      return holder;
   }

   @Override
   public void fillViewFromViewHolder(ViewHolder _holder) {
      mHolder = (SubtextViewHolder) _holder;
      handleLoadingText();
      handleProgressVisibility();
   }

   private void handleProgressVisibility() {
      if (mHolder != null) {
         if (mLoading) {
            mHolder.progressBar.setVisibility(View.VISIBLE);
         } else {
            mHolder.progressBar.setVisibility(View.INVISIBLE);
         }
      }
   }

   private void handleLoadingText() {
      if (mHolder != null) {
         mHolder.text.setText(mLoadingText);
      }
   }

   @Override
   public int getLoadingImageResource() {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public int getDefaultImageResource() {
      // TODO Auto-generated method stub
      return 0;
   }

   public void setLoadingText(String mLoadingText) {
      this.mLoadingText = mLoadingText;
      handleLoadingText();
   }

   public String getLoadingText() {
      return mLoadingText;
   }

   public void setLoadingVisible(boolean _loading) {
      mLoading = _loading;
      handleProgressVisibility();
   }
   
   @Override
   public String getSection() {
      return null;
   }
}
