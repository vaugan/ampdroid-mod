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

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.data.Series;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.SubtextViewHolder;
import com.mediaportal.ampdroid.lists.Utils;
import com.mediaportal.ampdroid.lists.ViewHolder;

public class SeriesThumbViewAdapterItem implements ILoadingAdapterItem {
   private Series mSeries;
   private LazyLoadingImage mImage;
   private String mSection;
   public SeriesThumbViewAdapterItem(Series _series) {
      super();
      this.mSeries = _series;
      
      String fileName = Utils.getFileNameWithExtension(mSeries.getCurrentFanartUrl(), "\\");
      String cacheName =   "Series" + File.separator + mSeries.getId() + File.separator + "Thumbs" + File.separator + fileName;

      mImage = new LazyLoadingImage(mSeries.getCurrentFanartUrl(), cacheName, 200, 100);
      
      String prettyName = mSeries.getPrettyName();
      if(prettyName != null && prettyName.length() > 0){
         String firstLetter = prettyName.substring(0, 1);
         mSection = firstLetter.toUpperCase();
      }
   }

   @Override
   public LazyLoadingImage getImage() {
      return mImage;
   }
   
   @Override
   public int getType() {
      return ViewTypes.ThumbView.ordinal();
   }

   @Override
   public int getXml() {
      return R.layout.listitem_thumb;
   }
   
   @Override
   public Object getItem() {
      return mSeries;
   }
   
   @Override
   public ViewHolder createViewHolder(View _view) {
      SubtextViewHolder holder = new SubtextViewHolder();
      holder.text = (TextView) _view.findViewById(R.id.TextViewText);
      holder.image = (ImageView) _view.findViewById(R.id.ImageViewEventImage);
      holder.subtext = (TextView) _view.findViewById(R.id.TextViewSubtext);
      holder.title = (TextView) _view.findViewById(R.id.TextViewTitle);
      return holder;
   }

   @Override
   public void fillViewFromViewHolder(ViewHolder _holder) {
      SubtextViewHolder holder = (SubtextViewHolder)_holder;
      if (holder.title != null) {
         holder.title.setTypeface(null, Typeface.BOLD);

         holder.title.setTextColor(Color.WHITE);
         holder.title.setText(mSeries.getPrettyName());
      }

      if (holder.text != null) {
         holder.text.setText(mSeries.getGenreString());
         holder.text.setTextColor(Color.WHITE);
      }

      if (holder.subtext != null) {
         holder.subtext.setText(mSeries.getGenreString());
      }
   }

   @Override
   public int getLoadingImageResource() {
      return R.drawable.listview_imageloading_thumb;
   }

   @Override
   public int getDefaultImageResource() {
      return R.drawable.listview_imageloading_thumb;
   }
   
   @Override
   public String getSection() {
      return mSection;
   }
}
