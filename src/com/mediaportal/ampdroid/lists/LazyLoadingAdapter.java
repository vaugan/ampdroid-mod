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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.mediaportal.ampdroid.lists.views.LoadingAdapterItem;
import com.mediaportal.ampdroid.lists.views.ViewTypes;
import com.mediaportal.ampdroid.utils.Constants;

public class LazyLoadingAdapter extends BaseAdapter implements SectionIndexer {
   public interface ILoadingListener {
      void EndOfListReached();
   }

   private ArrayList<ILoadingAdapterItem> mCurrentViewData;
   private HashMap<String, Integer> mCurrentAlphaIndexer;
   private List<String> mCurrentSections;
   private ViewTypes mCurrentView;

   private HashMap<Integer, ArrayList<ILoadingAdapterItem>> mViews;
   private HashMap<Integer, HashMap<String, Integer>> mAlphaIndexer;
   private HashMap<Integer, List<String>> mSections;

   private Activity mActivity;
   private static LayoutInflater mInflater = null;
   private boolean mShowLoadingItem;
   public ImageHandler mImageLoader;
   private ILoadingAdapterItem mIoadingIndicator;
   private ILoadingListener mLoadingListener;
   private boolean mFastScrollingInitialised;

   public void setLoadingListener(ILoadingListener _listener) {
      mLoadingListener = _listener;
   }

   public LazyLoadingAdapter(Activity _activity) {
      mActivity = _activity;
      mInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      mImageLoader = new ImageHandler(mActivity.getApplicationContext());
      mIoadingIndicator = new LoadingAdapterItem("");
      mViews = new HashMap<Integer, ArrayList<ILoadingAdapterItem>>();
      mCurrentViewData = new ArrayList<ILoadingAdapterItem>();

      mAlphaIndexer = new HashMap<Integer, HashMap<String, Integer>>();
      mSections = new HashMap<Integer, List<String>>();

      mCurrentAlphaIndexer = new HashMap<String, Integer>();
      mCurrentSections = new ArrayList<String>();
   }

   public LazyLoadingAdapter(Activity _activity, ArrayList<ILoadingAdapterItem> _items) {
      this(_activity);
      mCurrentViewData = _items;
   }

   public void addItem(ILoadingAdapterItem _item) {
      mCurrentViewData.add(_item);
      addItemsToSectionIndexer(_item, mCurrentViewData, mCurrentAlphaIndexer, mCurrentSections);
   }

   public boolean addItem(int _viewId, ILoadingAdapterItem _item) {
      if (mViews.containsKey(_viewId)) {
         mViews.get(_viewId).add(_item);
         addItemsToSectionIndexer(_item, mViews.get(_viewId), mAlphaIndexer.get(_viewId),
               mSections.get(_viewId));
         return true;
      } else {
         return false;
      }
   }

   private void addItemsToSectionIndexer(ILoadingAdapterItem _item,
         ArrayList<ILoadingAdapterItem> _viewData, HashMap<String, Integer> _alphaIndexer,
         List<String> _sections) {
      String section = _item.getSection();
      if (section != null) {
         int index = _viewData.size() - 1;
         // store the FIRST index for the section
         if (_alphaIndexer.containsKey(section)) {
            int existingIndex = _alphaIndexer.get(section);
            if (index < existingIndex) {
               _alphaIndexer.put(section, index);
            }
         } else {
            _alphaIndexer.put(section, index);
            _sections.add(section);
         }
      }
   }

   public boolean setView(ViewTypes _view) {
      mCurrentView = _view;
      int id = _view.ordinal();
      if (mViews.containsKey(id)) {
         mCurrentViewData = mViews.get(id);
         mCurrentAlphaIndexer = mAlphaIndexer.get(id);
         mCurrentSections = mSections.get(id);
         return true;
      } else {
         return false;
      }
   }

   public boolean addView(int _viewId) {
      if (!mViews.containsKey(_viewId)) {
         mViews.put(_viewId, new ArrayList<ILoadingAdapterItem>());
         mSections.put(_viewId, new ArrayList<String>());
         mAlphaIndexer.put(_viewId, new HashMap<String, Integer>());
         return true;
      } else {
         return false;
      }
   }

   public ViewTypes getCurrentView() {
      return mCurrentView;
   }

   public int getCount() {
      return (mShowLoadingItem ? mCurrentViewData.size() + 1 : mCurrentViewData.size());
   }

   public Object getItem(int position) {
      if (position == mCurrentViewData.size()) {
         return mIoadingIndicator;
      } else {
         return mCurrentViewData.get(position);
      }

   }

   public long getItemId(int position) {
      return position;
   }

   @Override
   public int getItemViewType(int position) {
      if (position == mCurrentViewData.size()) {
         return mIoadingIndicator.getType();
      } else {
         return mCurrentViewData.get(position).getType();
      }
   }

   @Override
   public int getViewTypeCount() {
      return 10;
   }

   public View getView(int position, View convertView, ViewGroup parent) {
      View vi = convertView;
      ViewHolder holder = null;
      if (mCurrentViewData != null) {
         ILoadingAdapterItem item = null;
         if (position >= mCurrentViewData.size()) {
            item = mIoadingIndicator;
            if (mLoadingListener != null) {
               mLoadingListener.EndOfListReached();
            }
         } else {
            item = mCurrentViewData.get(position);
         }

         if (convertView == null) {
            try {
               vi = mInflater.inflate(item.getXml(), null);
               holder = item.createViewHolder(vi);
               vi.setTag(holder);
            } catch (Exception ex) {
               Log.d("Adapter", ex.getMessage());
            }
         } else {
            holder = (ViewHolder) vi.getTag();
         }

         if (holder != null) {
            item.fillViewFromViewHolder(holder);

            if (holder.image != null) {
               LazyLoadingImage image = item.getImage();
               int defaultImage = item.getDefaultImageResource();
               int loadingImage = item.getLoadingImageResource();

               try {
                  if (image != null && !image.equals("")) {
                     holder.image.setTag(image.getImageUrl());
                     mImageLoader.DisplayImage(image, loadingImage, mActivity, holder.image);
                  } else {
                     if (defaultImage == 0) {// show nothing as default image
                        holder.image.setImageBitmap(null);
                     } else {
                        holder.image.setImageResource(defaultImage);
                     }
                  }
               } catch (Exception ex) {
                  Log.w(Constants.LOG_CONST, "Exception on image setting: " + ex.toString());
               }
            }
         }
      }

      return vi;
   }

   public void showLoadingItem(boolean showLoadingItem) {
      this.mShowLoadingItem = showLoadingItem;
   }

   public boolean isLoadingItemShown() {
      return mShowLoadingItem;
   }

   public void setLoadingText(String _text) {
      setLoadingText(_text, true);
   }

   public void setLoadingText(String _text, boolean _loading) {
      ((LoadingAdapterItem) mIoadingIndicator).setLoadingText(_text);
      ((LoadingAdapterItem) mIoadingIndicator).setLoadingVisible(_loading);
   }

   public void clear() {
      mCurrentViewData.clear();
   }

   public void removeItem(ILoadingAdapterItem _item) {
      mCurrentViewData.remove(_item);
   }

   @Override
   public int getPositionForSection(int section) {
      String sectionString = mCurrentSections.get(section);
      return mCurrentAlphaIndexer.get(sectionString);
   }

   @Override
   public int getSectionForPosition(int position) {
      // you will notice it will be never called (right?)
      Log.v("getSectionForPosition", "called");
      return 0;
   }

   @Override
   public Object[] getSections() {
      mFastScrollingInitialised = true;
      return mCurrentSections.toArray(); // to string will be called each
                                         // object, to display
      // the letter
   }

   public boolean fastScrollingInitialised() {
      return mFastScrollingInitialised;
   }

   private boolean FLAG_THUMB_PLUS = false;

   public void resetFastScrolling(ListView _listView) {
      mFastScrollingInitialised = false;
      _listView.setFastScrollEnabled(false);
      _listView.setFastScrollEnabled(true);

      if (_listView.getWidth() <= 0)
         return;

      int newWidth = FLAG_THUMB_PLUS ? _listView.getWidth() - 1 : _listView.getWidth() + 1;
      ViewGroup.LayoutParams params = _listView.getLayoutParams();
      params.width = newWidth;
      _listView.setLayoutParams(params);

      FLAG_THUMB_PLUS = !FLAG_THUMB_PLUS;
   }
}
