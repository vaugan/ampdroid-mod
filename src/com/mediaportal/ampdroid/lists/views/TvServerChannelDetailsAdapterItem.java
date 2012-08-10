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

import java.io.File;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.data.TvChannel;
import com.mediaportal.ampdroid.data.TvChannelDetails;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.SubtextViewHolder;
import com.mediaportal.ampdroid.lists.ViewHolder;
import com.mediaportal.ampdroid.lists.LazyLoadingImage.ImageType;

public class TvServerChannelDetailsAdapterItem implements ILoadingAdapterItem {
   TvChannel mChannel;
   private String mNowText;
   private String mNextText;
   private LazyLoadingImage mImage;

   public TvServerChannelDetailsAdapterItem(TvChannelDetails _channel) {
      mChannel = _channel;
      if (_channel.getCurrentProgram() != null) {
         mNowText = _channel.getCurrentProgram().getTitle();
      }
      if (_channel.getNextProgram() != null) {
         mNextText = _channel.getNextProgram().getTitle();
      }

      String cacheName = "ChannelLogos" + File.separator + _channel.getIdChannel() + ".jpg";

      mImage = new LazyLoadingImage(String.valueOf(_channel.getIdChannel()), cacheName, 100, 100);
      mImage.setImageType(ImageType.TvLogo);
   }

   @Override
   public LazyLoadingImage getImage() {
      return mImage;
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
      return R.layout.listitem_channeldetails;
   }

   @Override
   public Object getItem() {
      return mChannel;
   }

   @Override
   public ViewHolder createViewHolder(View _view) {
      SubtextViewHolder holder = new SubtextViewHolder();
      holder.title = (TextView) _view.findViewById(R.id.TextViewChannelName);
      holder.text = (TextView) _view.findViewById(R.id.TextViewEpgNowText);
      holder.subtext = (TextView) _view.findViewById(R.id.TextViewEpgNextText);
      holder.image = (ImageView) _view.findViewById(R.id.ImageViewLogo);
      return holder;
   }

   @Override
   public void fillViewFromViewHolder(ViewHolder _holder) {
      SubtextViewHolder holder = (SubtextViewHolder) _holder;
      if (holder.title != null) {
         holder.title.setText(mChannel.getDisplayName());
      }
      if (holder.text != null) {
         holder.text.setText(mNowText);
      }
      if (holder.subtext != null) {
         holder.subtext.setText(mNextText);
      }
   }

   @Override
   public String getSection() {
      return null;
   }
}
