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

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.mediaportal.ampdroid.api.DataHandler;

public class LazyLoadingGalleryAdapter extends BaseAdapter {
   private Activity activity;
   private ArrayList<ILoadingAdapterItem> data;
   private static LayoutInflater inflater = null;
   private boolean showLoadingItem;
   private int itemLayout = 0;
   private DataHandler service;

   public ImageHandler imageLoader;

   public LazyLoadingGalleryAdapter(Activity _activity, DataHandler _service) {
      activity = _activity;

      inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      imageLoader = new ImageHandler(activity.getApplicationContext());
      data = new ArrayList<ILoadingAdapterItem>();
      service = _service;
   }

   public void AddItem(ILoadingAdapterItem _item) {
      data.add(_item);
   }

   public int getCount() {
      return (showLoadingItem ? data.size() + 1 : data.size());
   }

   public Object getItem(int position) {
      return data.get(position);
   }

   public long getItemId(int position) {
      return position;
   }

   public View getView(int position, View convertView, ViewGroup parent) {
      ImageView imageView;
      if (convertView == null) {
         imageView = new ImageView(activity);
         imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
         imageView.setAdjustViewBounds(false);
         imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
         imageView.setPadding(18, 18, 18, 18);
      } else {
         imageView = (ImageView) convertView;
      }

      Bitmap img = service.getImage(data.get(position).getImage().getImageUrl(), 100, 200);
      imageView.setImageBitmap(img);

      return imageView;
   }
}
