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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.data.MusicAlbum;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.SubtextViewHolder;
import com.mediaportal.ampdroid.lists.Utils;
import com.mediaportal.ampdroid.lists.ViewHolder;
import com.mediaportal.ampdroid.utils.Constants;

public class MusicAlbumThumbViewAdapterItem implements ILoadingAdapterItem {
   private MusicAlbum mAlbum;
   private LazyLoadingImage mImage;
   private boolean mShowArtist;
   private String mSection;

   public MusicAlbumThumbViewAdapterItem(MusicAlbum _album, boolean _showArtist) {
      super();
      mAlbum = _album;
      mShowArtist = _showArtist;

      try {
         String artistName = null;
         if (mAlbum.getAlbumArtists() != null && mAlbum.getAlbumArtists().length > 0) {
            artistName = mAlbum.getAlbumArtists()[0];
         } else {
            artistName = "Various";
         }
         String fileName = Utils.getFileNameWithExtension(mAlbum.getCoverPathLarge(), "\\");
         String cacheName = "Music" + File.separator + artistName + File.separator
               + mAlbum.getTitle() + File.separator + "Thumbs" + File.separator + fileName;

         mImage = new LazyLoadingImage(mAlbum.getCoverPathLarge(), cacheName, 200, 100);

         String prettyName = mAlbum.getTitle();
         if (prettyName != null && prettyName.length() > 0) {
            String firstLetter = prettyName.substring(0, 1);
            mSection = firstLetter.toUpperCase();
         }
      } catch (Exception ex) {
         Log.e(Constants.LOG_CONST, ex.toString());
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
      return R.layout.listitem_poster;
   }

   @Override
   public Object getItem() {
      return mAlbum;
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
         holder.title.setText(mAlbum.getTitle());
      }

      if (holder.text != null) {
         holder.text.setText(mAlbum.getAlbumArtistString());
         holder.text.setTextColor(Color.WHITE);
      }

      if (holder.subtext != null) {
         holder.subtext.setText(String.valueOf(mAlbum.getYear()));
      }
   }

   @Override
   public int getLoadingImageResource() {
      return R.drawable.listview_imageloading_poster_2;
   }

   @Override
   public int getDefaultImageResource() {
      return R.drawable.listview_imageloading_poster_2;
   }

   @Override
   public String getSection() {
      return mSection;
   }
}
