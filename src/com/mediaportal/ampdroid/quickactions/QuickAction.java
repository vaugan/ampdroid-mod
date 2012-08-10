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

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;

/**
 * Popup window, shows action list as icon and text (QuickContact / Twitter
 * app).
 * 
 * @author Lorensius. W. T
 */
public class QuickAction extends CustomPopupWindow implements IQuickActionContainer {
   private final View mRoot;
   private final ImageView mArrowUp;
   private final ImageView mArrowDown;
   private final Animation mTrackAnim;
   private final LayoutInflater mInflater;
   private final Context mContext;

   public static final int ANIM_GROW_FROM_LEFT = 1;
   public static final int ANIM_GROW_FROM_RIGHT = 2;
   public static final int ANIM_GROW_FROM_CENTER = 3;
   public static final int ANIM_AUTO = 4;

   private int mAnimStyle;
   private boolean mAnimateTrack;
   private ViewGroup mTrack;
   private ArrayList<ActionItem> mActionList;

   /**
    * Constructor
    * 
    * @param _anchor
    *           {@link View} on where the popup should be displayed
    */
   public QuickAction(View _anchor) {
      super(_anchor);

      mActionList = new ArrayList<ActionItem>();
      mContext = _anchor.getContext();
      mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

      mRoot = (ViewGroup) mInflater.inflate(R.layout.quickaction, null);

      mArrowDown = (ImageView) mRoot.findViewById(R.id.arrow_down);
      mArrowUp = (ImageView) mRoot.findViewById(R.id.arrow_up);

      setContentView(mRoot);

      mTrackAnim = AnimationUtils.loadAnimation(_anchor.getContext(), R.anim.rail);

      mTrackAnim.setInterpolator(new Interpolator() {
         public float getInterpolation(float t) {
            // Pushes past the target area, then snaps back into place.
            // Equation for graphing: 1.2-((x*1.6)-1.1)^2
            final float inner = (t * 1.55f) - 1.1f;

            return 1.2f - inner * inner;
         }
      });

      mTrack = (ViewGroup) mRoot.findViewById(R.id.tracks);
      mAnimStyle = ANIM_AUTO;
      mAnimateTrack = true;
   }

   /**
    * Animate track
    * 
    * @param _animateTrack
    *           flag to animate track
    */
   public void animateTrack(boolean _animateTrack) {
      this.mAnimateTrack = _animateTrack;
   }

   /**
    * Set animation style
    * 
    * @param _animStyle
    *           animation style, default is set to ANIM_AUTO
    */
   public void setAnimStyle(int _animStyle) {
      this.mAnimStyle = _animStyle;
   }

   /**
    * Add action item
    * 
    * @param _action
    *           {@link ActionItem}
    */
   public void addActionItem(ActionItem _action) {
      mActionList.add(_action);
   }

   /**
    * Show popup window
    */
   public void show() {
      preShow();

      int[] location = new int[2];

      mAnchor.getLocationOnScreen(location);

      Rect anchorRect = new Rect(location[0], location[1], location[0] + mAnchor.getWidth(),
            location[1] + mAnchor.getHeight());

      mRoot.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
      mRoot.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

      int rootWidth = mRoot.getMeasuredWidth();
      int rootHeight = mRoot.getMeasuredHeight();

      int screenWidth = mWindowManager.getDefaultDisplay().getWidth();
      // int screenHeight = windowManager.getDefaultDisplay().getHeight();

      int xPos = (screenWidth - rootWidth) / 2;
      int yPos = anchorRect.top - rootHeight;

      boolean onTop = true;

      // display on bottom
      if (rootHeight > anchorRect.top) {
         yPos = anchorRect.bottom;
         onTop = false;
      }

      showArrow(((onTop) ? R.id.arrow_down : R.id.arrow_up), anchorRect.centerX());

      setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);

      createActionList();

      mWindow.showAtLocation(this.mAnchor, Gravity.NO_GRAVITY, xPos, yPos);

      if (mAnimateTrack)
         mTrack.startAnimation(mTrackAnim);
   }

   /**
    * Set animation style
    * 
    * @param _screenWidth
    *           Screen width
    * @param _requestedX
    *           distance from left screen
    * @param _onTop
    *           flag to indicate where the popup should be displayed. Set TRUE
    *           if displayed on top of anchor and vice versa
    */
   private void setAnimationStyle(int _screenWidth, int _requestedX, boolean _onTop) {
      int arrowPos = _requestedX - mArrowUp.getMeasuredWidth() / 2;

      switch (mAnimStyle) {
      case ANIM_GROW_FROM_LEFT:
         mWindow.setAnimationStyle((_onTop) ? R.style.Animations_PopUpMenu_Left
               : R.style.Animations_PopDownMenu_Left);
         break;

      case ANIM_GROW_FROM_RIGHT:
         mWindow.setAnimationStyle((_onTop) ? R.style.Animations_PopUpMenu_Right
               : R.style.Animations_PopDownMenu_Right);
         break;

      case ANIM_GROW_FROM_CENTER:
         mWindow.setAnimationStyle((_onTop) ? R.style.Animations_PopUpMenu_Center
               : R.style.Animations_PopDownMenu_Center);
         break;

      case ANIM_AUTO:
         if (arrowPos <= _screenWidth / 4) {
            mWindow.setAnimationStyle((_onTop) ? R.style.Animations_PopUpMenu_Left
                  : R.style.Animations_PopDownMenu_Left);
         } else if (arrowPos > _screenWidth / 4 && arrowPos < 3 * (_screenWidth / 4)) {
            mWindow.setAnimationStyle((_onTop) ? R.style.Animations_PopUpMenu_Center
                  : R.style.Animations_PopDownMenu_Center);
         } else {
            mWindow.setAnimationStyle((_onTop) ? R.style.Animations_PopDownMenu_Right
                  : R.style.Animations_PopDownMenu_Right);
         }

         break;
      }
   }

   /**
    * Create action list
    * 
    */
   private void createActionList() {
      View view;
      String title;
      Drawable icon;
      OnClickListener listener;
      OnLongClickListener longClickListener;
      int index = 1;

      for (int i = 0; i < mActionList.size(); i++) {
         title = mActionList.get(i).getTitle();
         icon = mActionList.get(i).getIcon();
         listener = mActionList.get(i).getListener();
         longClickListener = mActionList.get(i).getLongClickListener();
         view = getActionItem(title, icon, listener, longClickListener);

         view.setFocusable(true);
         view.setClickable(mActionList.get(i).getEnabled());

         mTrack.addView(view, index);

         index++;
      }
   }

   /**
    * Get action item {@link View}
    * 
    * @param _title
    *           action item title
    * @param _icon
    *           {@link Drawable} action item icon
    * @param _listener
    *           {@link View.OnClickListener} action item listener
    * @return action item {@link View}
    */
   private View getActionItem(String _title, Drawable _icon, OnClickListener _listener,
         OnLongClickListener _longClickListener) {
      LinearLayout container = (LinearLayout) mInflater.inflate(R.layout.quickaction_action_item,
            null);
      ImageView img = (ImageView) container.findViewById(R.id.icon);
      TextView text = (TextView) container.findViewById(R.id.title);

      if (_icon != null) {
         img.setImageDrawable(_icon);
      } else {
         img.setVisibility(View.GONE);
      }

      if (_title != null) {
         text.setText(_title);
      } else {
         text.setVisibility(View.GONE);
      }

      if (_listener != null) {
         container.setOnClickListener(_listener);
      }

      if (_longClickListener != null) {
         container.setOnLongClickListener(_longClickListener);
      }

      return container;
   }

   /**
    * Show arrow
    * 
    * @param _whichArrow
    *           arrow type resource id
    * @param _requestedX
    *           distance from left screen
    */
   private void showArrow(int _whichArrow, int _requestedX) {
      final View showArrow = (_whichArrow == R.id.arrow_up) ? mArrowUp : mArrowDown;
      final View hideArrow = (_whichArrow == R.id.arrow_up) ? mArrowDown : mArrowUp;

      final int arrowWidth = mArrowUp.getMeasuredWidth();

      showArrow.setVisibility(View.VISIBLE);

      ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) showArrow
            .getLayoutParams();

      param.leftMargin = _requestedX - arrowWidth / 2;

      hideArrow.setVisibility(View.INVISIBLE);
   }
}
