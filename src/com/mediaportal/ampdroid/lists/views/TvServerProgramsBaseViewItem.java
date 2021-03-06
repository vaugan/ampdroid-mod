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

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.data.TvProgramBase;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.SubtextViewHolder;
import com.mediaportal.ampdroid.lists.ViewHolder;
import com.mediaportal.ampdroid.utils.DateTimeHelper;

public class TvServerProgramsBaseViewItem implements ILoadingAdapterItem {

   private TvProgramBase mProgram;
   private String mDateString;
   private boolean mIsCurrent = false;
   
   public TvServerProgramsBaseViewItem(TvProgramBase _program) {
      mProgram = _program;

      
      Date begin = mProgram.getStartTime();
      Date end = mProgram.getEndTime();
      if (begin != null && end != null) {
         String startString = DateTimeHelper.getTimeString(begin);
         Date now = new Date();
         
         if(now.after(begin) && now.before(end)){
            mIsCurrent = true;
         }
         
         mDateString = startString;
      } else {
         mDateString = "";
      }
   }


   @Override
   public LazyLoadingImage getImage() {
      return null;
   }

   @Override
   public int getType() {
      return 1;
   }

   @Override
   public int getXml() {
      return R.layout.listitem_epg;
   }

   @Override
   public Object getItem() {
      return mProgram;
   }

   @Override
   public ViewHolder createViewHolder(View _view) {
      SubtextViewHolder holder = new SubtextViewHolder();
      holder.title = (TextView) _view.findViewById(R.id.TextViewProgramName);
      holder.text = (TextView) _view.findViewById(R.id.TextViewProgramDate);
      holder.image2 = (ImageView) _view.findViewById(R.id.ImageViewRecording);
      return holder;
   }

   @Override
   public void fillViewFromViewHolder(ViewHolder _holder) {
      SubtextViewHolder holder = (SubtextViewHolder) _holder;
      if (holder.title != null) {
         holder.title.setText(mProgram.getTitle());
         
         if(mIsCurrent){
            holder.title.setTextColor(Color.GREEN);
         }
         else if(mProgram.isIsScheduled()){
            holder.title.setTextColor(Color.RED);
         }
            else{
            holder.title.setTextColor(Color.WHITE);
         }
      }

      if (holder.text != null) {
         holder.text.setText(mDateString);
         if(mIsCurrent){
            holder.text.setTextColor(Color.GREEN);
         }
         else if(mProgram.isIsScheduled()){
            holder.text.setTextColor(Color.RED);
         }
         else{
            holder.text.setTextColor(Color.WHITE);
         }
      }
      
      if(holder.image2 != null){
         if(mProgram.isIsScheduled()){
            holder.image2.setImageResource(R.drawable.tvserver_record_button);
         }
         else{
            holder.image2.setImageBitmap(null);
         }
      }
   }

   @Override
   public int getLoadingImageResource() {
      // TODO Auto-generated method stub
      return 0;
   }

   @Override
   public int getDefaultImageResource() {
      // TODO Auto-generated method stub
      return 0;
   }
   
   @Override
   public String getSection() {
      return null;
   }
}
