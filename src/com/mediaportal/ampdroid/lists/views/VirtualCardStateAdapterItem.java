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

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.data.TvVirtualCard;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.SubtextViewHolder;
import com.mediaportal.ampdroid.lists.ViewHolder;

public class VirtualCardStateAdapterItem implements ILoadingAdapterItem {
   Context mContext;
   TvVirtualCard mCard;
   public VirtualCardStateAdapterItem(Context _context, TvVirtualCard _card){
      mCard = _card;
      mContext = _context;
   }
   
   @Override
   public LazyLoadingImage getImage() {
      return null;
   }

   @Override
   public int getType() {
      return 0;
   }

   @Override
   public int getXml() {
      return R.layout.listitem_virtualcard;
   }

   @Override
   public Object getItem() {
      return mCard;
   }

   @Override
   public ViewHolder createViewHolder(View _view) {
      SubtextViewHolder holder = new SubtextViewHolder();
      holder.title = (TextView) _view.findViewById(R.id.TextViewChannelName);
      holder.text = (TextView) _view.findViewById(R.id.TextViewState);
      holder.subtext = (TextView) _view.findViewById(R.id.TextViewCardUser);
      return holder;
   }

   @Override
   public void fillViewFromViewHolder(ViewHolder _holder) {
      SubtextViewHolder holder = (SubtextViewHolder) _holder;
      if (holder.title != null) {
         holder.title.setTypeface(null, Typeface.BOLD);
         holder.title.setTextColor(Color.WHITE);
         holder.title.setText(mCard.getChannelName());
      }

      if (holder.text != null) {
         String stateString = null;
         if(mCard.isIsRecording()){
            stateString = mContext.getString(R.string.state_recording);
         }
         else if(mCard.isIsTimeShifting()){
            stateString = mContext.getString(R.string.state_timeshifting);
         }
         else if(mCard.isIsGrabbingEpg()){
            stateString = mContext.getString(R.string.state_grabbingEpg);
         }
         
         holder.text.setText(stateString);
         holder.text.setTextColor(Color.WHITE);
      }

      if (holder.subtext != null) {
         String subtextString = mCard.getName() + " (" + mCard.getUser().getName() + ")";
         holder.subtext.setText(subtextString);
         holder.subtext.setTextColor(Color.WHITE);
      }
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
   public String getSection() {
      return null;
   }
}
