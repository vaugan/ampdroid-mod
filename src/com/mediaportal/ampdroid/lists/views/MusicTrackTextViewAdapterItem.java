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
import com.mediaportal.ampdroid.data.MusicTrack;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.SubtextViewHolder;
import com.mediaportal.ampdroid.lists.ViewHolder;

public class MusicTrackTextViewAdapterItem implements ILoadingAdapterItem {
   private MusicTrack mTracks;
   private boolean mShowArtist;
   private String mText;
   private String mSection;

   public MusicTrackTextViewAdapterItem(MusicTrack _track, boolean _showArtist) {
      super();
      mTracks = _track;
      mShowArtist = _showArtist;

      String artistString = "";
      if (mShowArtist && mTracks.getArtists() != null) {
         if (mTracks.getArtists().length == 0) {
            artistString = " - ...";
         } else if (mTracks.getArtists().length == 1) {
            artistString = " - " + mTracks.getArtists()[0];
         } else {
            artistString = " - " + mTracks.getArtists()[0] + ", ...";
         }
      }

      mText = mTracks.getTitle() + artistString;

      if (mText != null && mText.length() > 0) {
         String firstLetter = mText.substring(0, 1);
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
      return mTracks;
   }

   @Override
   public ViewHolder createViewHolder(View _view) {
      SubtextViewHolder holder = new SubtextViewHolder();
      holder.text = (TextView) _view.findViewById(R.id.TextViewText);
      return holder;
   }

   @Override
   public void fillViewFromViewHolder(ViewHolder _holder) {
      SubtextViewHolder holder = (SubtextViewHolder) _holder;

      if (holder.text != null) {
         holder.text.setText(mText);
         holder.text.setTextColor(Color.WHITE);
      }
   }

   @Override
   public String getSection() {
      return mSection;
   }
}
