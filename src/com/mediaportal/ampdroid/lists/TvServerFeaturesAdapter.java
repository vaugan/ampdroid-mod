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
package com.mediaportal.ampdroid.lists;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.mediaportal.ampdroid.R;
public class TvServerFeaturesAdapter extends BaseAdapter {

   
   private List<TvServerFeature> mFeatures = null;
   private LayoutInflater mInflater = null;
   private Activity mActivity = null;
   
   public TvServerFeaturesAdapter(Activity _activity){
      mActivity = _activity;
      mFeatures = new ArrayList<TvServerFeature>();
      mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   }
   
   public TvServerFeaturesAdapter(Activity _activity, List<TvServerFeature> _features){
      mActivity = _activity;
      mFeatures = _features;
      mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   }
   
   public void addFeature(TvServerFeature _feature){
      mFeatures.add(_feature);
   }
   
   public List<TvServerFeature> getFeatures(){
      return mFeatures;
   }

   @Override
   public int getCount() {
      return mFeatures.size();
   }

   @Override
   public Object getItem(int _position) {
      return mFeatures.get(_position);
   }

   @Override
   public long getItemId(int _position) {
      return _position;
   }

   @Override
   public View getView(int _position, View _convertView, ViewGroup _parent) {
      View vi = _convertView;
      SubtextViewHolder holder;

      if (_convertView == null) {
         try {
            vi = mInflater.inflate(R.layout.listitem_serverfeature, null);
         } catch (Exception ex) {
            Log.d("Adapter", ex.getMessage());
         }
         holder = new SubtextViewHolder();
         holder.text = (TextView) vi.findViewById(R.id.TextViewText);
         holder.image = (ImageView) vi.findViewById(R.id.ImageViewEventImage);
         holder.title = (TextView) vi.findViewById(R.id.TextViewTitle);
         vi.setTag(holder);
      } else {
         holder = (SubtextViewHolder) vi.getTag();
      }
      
      TvServerFeature feature = (TvServerFeature) getItem(_position);
      
      holder.title.setText(feature.getName());
      holder.text.setText(feature.getDescription());
      holder.image.setImageResource(feature.getIcon());
      
      
      return vi;
   }

}
