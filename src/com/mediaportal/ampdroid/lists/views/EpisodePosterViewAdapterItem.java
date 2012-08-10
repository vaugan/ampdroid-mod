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

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.data.SeriesEpisode;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.SubtextViewHolder;
import com.mediaportal.ampdroid.lists.Utils;
import com.mediaportal.ampdroid.lists.ViewHolder;

public class EpisodePosterViewAdapterItem implements ILoadingAdapterItem {
   private int mSeriesId;
   private SeriesEpisode mEpisode;
   private LazyLoadingImage mImage;
   private Context mContext;

   public EpisodePosterViewAdapterItem(Context _context, int _seriesId, SeriesEpisode _episode) {
      mEpisode = _episode;
      mSeriesId = _seriesId;
      mContext = _context;

      String ext = Utils.getExtension(mEpisode.getBannerUrl());

      int width = 400;
      int height = 200;
      String cacheName = "Series" + File.separator + mSeriesId + File.separator + "Season."
            + mEpisode.getSeasonNumber() + File.separator + "Ep" + _episode.getEpisodeNumber()
            + "_" + width + "x" + height + "." + ext;
      mImage = new LazyLoadingImage(mEpisode.getBannerUrl(), cacheName, width, height);
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
      return mEpisode;
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
      SubtextViewHolder holder = (SubtextViewHolder) _holder;
      if (holder.title != null) {
         holder.title.setTypeface(null, Typeface.BOLD);

         holder.title.setTextColor(Color.WHITE);
         holder.title.setText(mEpisode.getName());
      }

      if (holder.text != null) {
         holder.text.setText(mContext.getString(R.string.media_episode) + " "
               + mEpisode.getEpisodeNumber());
         holder.text.setTextColor(Color.WHITE);
      }

      if (holder.subtext != null) {
         holder.subtext.setText("");
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
      return null;
   }
}
