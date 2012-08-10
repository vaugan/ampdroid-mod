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


import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;
import com.mediaportal.ampdroid.R;
/**
 * This class does most of the work of wrapping the {@link PopupWindow} so it's simpler to use. 
 * Edited by Lorensius. W. L. T
 * 
 * @author qberticus
 * 
 */
public class CustomPopupWindow {
	protected final View mAnchor;
	protected final PopupWindow mWindow;
	private View mRoot;
	private Drawable mBackground = null;
	protected final WindowManager mWindowManager;
	
	/**
	 * Create a QuickAction
	 * 
	 * @param _anchor
	 *            the view that the QuickAction will be displaying 'from'
	 */
	public CustomPopupWindow(View _anchor) {
		this.mAnchor = _anchor;
		this.mWindow = new PopupWindow(_anchor.getContext());

		// when a touch even happens outside of the window
		// make the window go away
		mWindow.setTouchInterceptor(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					CustomPopupWindow.this.mWindow.dismiss();
					
					return true;
				}
				
				return false;
			}
		});

		mWindowManager = (WindowManager) _anchor.getContext().getSystemService(Context.WINDOW_SERVICE);
		
		onCreate();
	}

	/**
	 * Anything you want to have happen when created. Probably should create a view and setup the event listeners on
	 * child views.
	 */
	protected void onCreate() {}

	/**
	 * In case there is stuff to do right before displaying.
	 */
	protected void onShow() {}

	protected void preShow() {
		if (mRoot == null) {
			throw new IllegalStateException("setContentView was not called with a view to display.");
		}
		
		onShow();

		if (mBackground == null) {
			mWindow.setBackgroundDrawable(new BitmapDrawable());
		} else {
			mWindow.setBackgroundDrawable(mBackground);
		}

		// if using PopupWindow#setBackgroundDrawable this is the only values of the width and hight that make it work
		// otherwise you need to set the background of the root viewgroup
		// and set the popupwindow background to an empty BitmapDrawable
		
		mWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
		mWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		mWindow.setTouchable(true);
		mWindow.setFocusable(true);
		mWindow.setOutsideTouchable(true);

		mWindow.setContentView(mRoot);
	}

	public void setBackgroundDrawable(Drawable _background) {
		this.mBackground = _background;
	}

	/**
	 * Sets the content view. Probably should be called from {@link onCreate}
	 * 
	 * @param _root
	 *            the view the popup will display
	 */
	public void setContentView(View _root) {
		this.mRoot = _root;
		
		mWindow.setContentView(_root);
	}

	/**
	 * Will inflate and set the view from a resource id
	 * 
	 * @param _layoutResID
	 */
	public void setContentView(int _layoutResID) {
		LayoutInflater inflator =
				(LayoutInflater) mAnchor.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		setContentView(inflator.inflate(_layoutResID, null));
	}

	/**
	 * If you want to do anything when {@link dismiss} is called
	 * 
	 * @param _listener
	 */
	public void setOnDismissListener(PopupWindow.OnDismissListener _listener) {
		mWindow.setOnDismissListener(_listener);
	}

	/**
	 * Displays like a popdown menu from the anchor view
	 */
	public void showDropDown() {
		showDropDown(0, 0);
	}

	/**
	 * Displays like a popdown menu from the anchor view.
	 * 
	 * @param _xOffset
	 *            offset in X direction
	 * @param _yOffset
	 *            offset in Y direction
	 */
	public void showDropDown(int _xOffset, int _yOffset) {
		preShow();

		mWindow.setAnimationStyle(R.style.Animations_PopDownMenu_Left);

		mWindow.showAsDropDown(mAnchor, _xOffset, _yOffset);
	}

	/**
	 * Displays like a QuickAction from the anchor view.
	 */
	public void showLikeQuickAction() {
		showLikeQuickAction(0, 0);
	}

	/**
	 * Displays like a QuickAction from the anchor view.
	 * 
	 * @param _xOffset
	 *            offset in the X direction
	 * @param _yOffset
	 *            offset in the Y direction
	 */
	public void showLikeQuickAction(int _xOffset, int _yOffset) {
		preShow();

		mWindow.setAnimationStyle(R.style.Animations_PopUpMenu_Center);

		int[] location = new int[2];
		mAnchor.getLocationOnScreen(location);

		Rect anchorRect =
				new Rect(location[0], location[1], location[0] + mAnchor.getWidth(), location[1]
					+ mAnchor.getHeight());

		mRoot.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mRoot.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		int rootWidth 		= mRoot.getMeasuredWidth();
		int rootHeight 		= mRoot.getMeasuredHeight();

		int screenWidth 	= mWindowManager.getDefaultDisplay().getWidth();
		//int screenHeight 	= windowManager.getDefaultDisplay().getHeight();

		int xPos 			= ((screenWidth - rootWidth) / 2) + _xOffset;
		int yPos	 		= anchorRect.top - rootHeight + _yOffset;

		// display on bottom
		if (rootHeight > anchorRect.top) {
			yPos = anchorRect.bottom + _yOffset;
			
			mWindow.setAnimationStyle(R.style.Animations_PopDownMenu_Center);
		}

		mWindow.showAtLocation(mAnchor, Gravity.NO_GRAVITY, xPos, yPos);
	}
	
	public void dismiss() {
		mWindow.dismiss();
	}
}
