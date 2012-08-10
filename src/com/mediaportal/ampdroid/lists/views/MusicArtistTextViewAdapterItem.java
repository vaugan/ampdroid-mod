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

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.data.MusicArtist;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.SubtextViewHolder;
import com.mediaportal.ampdroid.lists.ViewHolder;

public class MusicArtistTextViewAdapterItem implements ILoadingAdapterItem {
   private MusicArtist mArtist;
   private String mSection;
   public MusicArtistTextViewAdapterItem(MusicArtist _artist) {
      super();
      this.mArtist = _artist;
      
      String prettyName = mArtist.getTitle();
      if(prettyName != null && prettyName.length() > 0){
         String firstLetter = prettyName.substring(0, 1);
         mSection = firstLetter.toUpperCase();
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
      return R.layout.listitem_text;
   }

   @Override
   public Object getItem() {
      return mArtist;
   }

   @Override
   public ViewHolder createViewHolder(View _view) {
      SubtextViewHolder holder = new SubtextViewHolder();
      holder.text = (TextView) _view.findViewById(R.id.TextViewText);
      return holder;
   }

   @Override
   public void fillViewFromViewHolder(ViewHolder _holder) {
      SubtextViewHolder holder = (SubtextViewHolder)_holder;

      if (holder.text != null) {
         holder.text.setText(mArtist.getTitle());
         holder.text.setTextColor(Color.WHITE);
      }
   }
   
   @Override
   public String getSection() {
      return mSection;
   }

}
