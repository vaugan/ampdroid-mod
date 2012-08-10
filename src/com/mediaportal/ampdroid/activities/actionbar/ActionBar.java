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
/*
 * Copyright (C) 2010 Johan Nilsson <http://markupartist.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mediaportal.ampdroid.activities.actionbar;

import com.mediaportal.ampdroid.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActionBar extends RelativeLayout implements OnClickListener {

   private LayoutInflater mInflater;
   private RelativeLayout mBarView;
   private ImageView mLogoView;
   // private View mHomeView;
   private TextView mTitleView;
   private ImageButton mHomeBtn;
   private RelativeLayout mHomeLayout;

   private ProgressBar mProgressBar;
   private ImageButton mChangeClientBtn;
   private ImageButton mSearchBtn;

   public ProgressBar getProgressBar() {
      return mProgressBar;
   }

   public ImageButton getChangeClientButton() {
      return mChangeClientBtn;
   }

   public ImageButton getSearchButton() {
      return mSearchBtn;
   }
   
   public ImageButton getHomeButton(){
      return mHomeBtn;
   }
   
   public ImageButton getLogoutButton(){
      return mLogoutBtn;
   }

   private boolean mIsInitialised = false;
   private boolean mLoading;
   private ImageButton mLogoutBtn;
   

   public ActionBar(Context _context, AttributeSet _attrs) {
      super(_context, _attrs);

      mInflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      mBarView = (RelativeLayout) mInflater.inflate(R.layout.actionbar, null);
      addView(mBarView);

      //mLogoView = (ImageView) mBarView.findViewById(R.id.actionbar_home_logo);
      mHomeLayout = (RelativeLayout) mBarView.findViewById(R.id.actionbar_home_bg);
      mHomeBtn = (ImageButton) mBarView.findViewById(R.id.actionbar_home_btn);

      mTitleView = (TextView) mBarView.findViewById(R.id.actionbar_title);

      mChangeClientBtn = (ImageButton) mBarView.findViewById(R.id.actionbar_item_clients);
      mSearchBtn = (ImageButton) mBarView.findViewById(R.id.actionbar_item_search);
      mLogoutBtn = (ImageButton)mBarView.findViewById(R.id.actionbar_item_logout);
      
      mProgressBar = (ProgressBar) mBarView.findViewById(R.id.actionbar_item_progress);

      if (mLoading) {
         mProgressBar.setVisibility(View.VISIBLE);
      } else {
         mProgressBar.setVisibility(View.INVISIBLE);
      }
   }

   public void setHome(boolean _home) {
      if (!_home) {
         mHomeBtn.setVisibility(View.VISIBLE);
      } else {
         mHomeBtn.setVisibility(View.GONE);
      }

   }

   public void setInitialised(boolean isInitialised) {
      this.mIsInitialised = isInitialised;
   }

   public boolean isInitialised() {
      return mIsInitialised;
   }

   /**
    * Shows the provided logo to the left in the action bar.
    * 
    * This is ment to be used instead of the setHomeAction and does not draw a
    * divider to the left of the provided logo.
    * 
    * @param _resId
    *           The drawable resource id
    */
   public void setHomeLogo(int _resId) {
      // TODO: Add possibility to add an IntentAction as well.
      mLogoView.setImageResource(_resId);
      mLogoView.setVisibility(View.VISIBLE);
      mHomeLayout.setVisibility(View.GONE);
   }

   public void setTitle(CharSequence _title) {
      mTitleView.setText(_title);
   }

   public void setTitle(int _resid) {
      mTitleView.setText(_resid);
   }

   @Override
   public void onClick(View v) {

   }

   public boolean getLoading() {
      return mProgressBar.getVisibility() == View.VISIBLE;
   }

   public void setLoading(boolean _loading) {
      mLoading = _loading;

      if (mProgressBar != null) {
         if (_loading) {
            mProgressBar.setVisibility(View.VISIBLE);
         } else {
            mProgressBar.setVisibility(View.INVISIBLE);
         }
      }
   }
}
