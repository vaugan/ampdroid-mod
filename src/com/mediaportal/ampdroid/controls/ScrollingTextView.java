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
package com.mediaportal.ampdroid.controls;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

public class ScrollingTextView extends TextView {
   public ScrollingTextView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      setSingleLine();
      setEllipsize(null);
      setText("Single-line text view that scrolls automatically if the text is too long to fit in the widget");
  }

  // begin to scroll the text from the original position
  public void startScrolling() {
      scrollHandler.sendEmptyMessage(0);
  }

  private Handler scrollHandler = new Handler() {
      private static final int REFRESH_INTERVAL = 1000;

      public void handleMessage(Message msg) {
          scrollBy(5, 0);
          requestLayout();
          Log.d("ScrollingTextView", "Scrolled to " + getScrollX() + " px");
          sendEmptyMessageDelayed(0, REFRESH_INTERVAL);
      }
  };


}
