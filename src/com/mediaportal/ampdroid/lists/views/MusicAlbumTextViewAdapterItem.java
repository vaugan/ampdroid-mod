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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.data.MusicAlbum;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.SubtextViewHolder;
import com.mediaportal.ampdroid.lists.ViewHolder;
import com.mediaportal.ampdroid.utils.Constants;

public class MusicAlbumTextViewAdapterItem implements ILoadingAdapterItem {
   private MusicAlbum mAlbum;
   private boolean mShowArtist;
   private String mText;
   private String mSection;

   public MusicAlbumTextViewAdapterItem(MusicAlbum _album, boolean _showArtist) {
      super();
      mAlbum = _album;
      mShowArtist = _showArtist;

      try {
         String artistString = "";
         if (mShowArtist && mAlbum.getAlbumArtists() != null) {
            if (mAlbum.getAlbumArtists().length == 0) {
               artistString = " - ...";
            } else if (mAlbum.getAlbumArtists().length == 1) {
               artistString = " - " + mAlbum.getAlbumArtists()[0];
            } else {
               artistString = " - " + mAlbum.getAlbumArtists()[0] + ", ...";
            }
         }

         mText = mAlbum.getTitle() + artistString;

         if (mText != null && mText.length() > 0) {
            String firstLetter = mText.substring(0, 1);
            mSection = firstLetter.toUpperCase();
         }
      } catch (Exception ex) {
         Log.e(Constants.LOG_CONST, ex.toString());
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
      return mAlbum;
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

      try {
         if (holder.text != null) {
            holder.text.setText(mText);
            holder.text.setTextColor(Color.WHITE);
         }
      } catch (Exception ex) {
         Log.e(Constants.LOG_CONST, ex.toString());
      }
   }

   @Override
   public String getSection() {
      return mSection;
   }
}
