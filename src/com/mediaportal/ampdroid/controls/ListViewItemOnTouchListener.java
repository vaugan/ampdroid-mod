package com.mediaportal.ampdroid.controls;

import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class ListViewItemOnTouchListener implements OnTouchListener{
   @Override
   public boolean onTouch(View v, MotionEvent event) {
      if (event.getAction() == MotionEvent.ACTION_DOWN) {
         v.setBackgroundResource(android.R.drawable.list_selector_background);
      } else if (event.getAction() == MotionEvent.ACTION_CANCEL) {
         v.setBackgroundColor(Color.TRANSPARENT);
      } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

      } else {
         v.setBackgroundColor(Color.TRANSPARENT);
      }

      return false;
   }
}
