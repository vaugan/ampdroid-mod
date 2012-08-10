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
import android.widget.ImageView;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.data.TvChannel;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.SubtextViewHolder;
import com.mediaportal.ampdroid.lists.ViewHolder;

public class TvServerChannelAdapterItem implements ILoadingAdapterItem {
   TvChannel mChannel;
   
   public TvServerChannelAdapterItem(TvChannel _channel){
      mChannel = _channel;
   }
   
   @Override
   public LazyLoadingImage getImage() {
      return null;
   }

   @Override
   public int getLoadingImageResource() {
      return R.drawable.mp_logo_2;
   }

   @Override
   public int getDefaultImageResource() {
      return R.drawable.mp_logo_2;
   }

   @Override
   public int getType() {
      return 0;
   }

   @Override
   public int getXml() {
      return R.layout.listitem_channel;
   }

   @Override
   public Object getItem() {
      return mChannel;
   }

   @Override
   public ViewHolder createViewHolder(View _view) {
      SubtextViewHolder holder = new SubtextViewHolder();
      holder.text = (TextView) _view.findViewById(R.id.TextViewChannelName);
      holder.image = (ImageView) _view.findViewById(R.id.ImageViewLogo);
      return holder;
   }

   @Override
   public void fillViewFromViewHolder(ViewHolder _holder) {
      if (_holder.text != null) {
         _holder.text.setText(mChannel.getDisplayName());
      }
   }
   
   @Override
   public String getSection() {
      return null;
   }
}
