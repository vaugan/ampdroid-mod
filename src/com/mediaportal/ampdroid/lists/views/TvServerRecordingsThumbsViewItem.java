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

import java.util.Date;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.data.TvChannel;
import com.mediaportal.ampdroid.data.TvRecording;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.SubtextViewHolder;
import com.mediaportal.ampdroid.lists.ViewHolder;
import com.mediaportal.ampdroid.utils.DateTimeHelper;
public class TvServerRecordingsThumbsViewItem implements ILoadingAdapterItem {
   TvRecording mRecording;
   TvChannel mChannel;
   private Context mContext;
   public TvServerRecordingsThumbsViewItem(Context _context, TvRecording _recording, TvChannel _channel) {
      mRecording = _recording;
      mChannel = _channel;
      mContext = _context;
   }

   private String getText() {
      if (mChannel != null) {
         return mContext.getString(R.string.tvserver_channel) + ": " + mChannel.getDisplayName();
      } else {
         return mContext.getString(R.string.tvserver_channel) + ": " + mRecording.getIdChannel();
      }
   }

   private String getSubText() {
      Date begin = mRecording.getStartTime();
      Date end = mRecording.getEndTime();
      if (begin != null && end != null) {
         String startString = DateTimeHelper.getDateString(begin, true);
         String endString = DateTimeHelper.getTimeString(end);
         return startString + " - " + endString;
      } else {
         return "Unknown time";
      }
   }

   @Override
   public LazyLoadingImage getImage() {
      return null;
   }

   public String getTitle() {
      return mRecording.getTitle();
   }

   @Override
   public int getType() {
      return 0;
   }

   @Override
   public int getXml() {
      return com.mediaportal.ampdroid.R.layout.listitem_recordings_thumbs;
   }

   @Override
   public Object getItem() {
      return mRecording;
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
         holder.title.setText(getTitle());
      }

      if (holder.text != null) {
         holder.text.setText(getText());
         holder.text.setTextColor(Color.WHITE);
      }

      if (holder.subtext != null) {
         holder.subtext.setText(getSubText());
         holder.subtext.setTextColor(Color.WHITE);
      }
   }

   @Override
   public int getLoadingImageResource() {
      return R.drawable.mplogo;
   }

   @Override
   public int getDefaultImageResource() {
      return R.drawable.mplogo;
   }
   
   @Override
   public String getSection() {
      return null;
   }
}
