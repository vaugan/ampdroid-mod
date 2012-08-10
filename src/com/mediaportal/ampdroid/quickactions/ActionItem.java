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
package com.mediaportal.ampdroid.quickactions;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;

/**
 * Action item, displayed as menu with icon and text.
 * 
 * @author Lorensius. W. L. T
 *
 */
public class ActionItem {
	private Drawable mIcon = null;
	private String mTitle = null;
	private OnClickListener mListener = null;
   private boolean mEnabled = true;
   private OnLongClickListener mLongClickListener = null;
	
	/**
	 * Constructor
	 */
	public ActionItem() {}
	
	/**
	 * Constructor
	 * 
	 * @param _icon {@link Drawable} action icon
	 */
	public ActionItem(Drawable _icon) {
		this.mIcon = _icon;
	}
	
	/**
	 * Set action title
	 * 
	 * @param _title action title
	 */
	public void setTitle(String _title) {
		this.mTitle = _title;
	}
	
	/**
	 * Get action title
	 * 
	 * @return action title
	 */
	public String getTitle() {
		return this.mTitle;
	}
	
	/**
	 * Set action icon
	 * 
	 * @param _icon {@link Drawable} action icon
	 */
	public void setIcon(Drawable _icon) {
		this.mIcon = _icon;
	}
	
	/**
	 * Get action icon
	 * @return  {@link Drawable} action icon
	 */
	public Drawable getIcon() {
		return this.mIcon;
	}
	
	/**
	 * Set on click listener
	 * 
	 * @param _listener on click listener {@link View.OnClickListener}
	 */
	public void setOnClickListener(OnClickListener _listener) {
		this.mListener = _listener;
	}
	
	  /**
    * Set on click listener
    * 
    * @param _listener on click listener {@link View.OnClickListener}
    */
   public void setOnLongClickListener(OnLongClickListener _listener) {
      this.mLongClickListener  = _listener;
   }
   
   public OnLongClickListener getLongClickListener() {
      return this.mLongClickListener;
   }
	
	/**
	 * Get on click listener
	 * 
	 * @return on click listener {@link View.OnClickListener}
	 */
	public OnClickListener getListener() {
		return this.mListener;
	}

   public void setEnabled(boolean _enabled) {
      mEnabled = _enabled;
   }

   public boolean getEnabled() {
      return mEnabled;
   }
}
