package com.mediaportal.ampdroid.quickactions;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;

public class QuickActionView extends RelativeLayout implements IQuickActionContainer {

   private LayoutInflater mInflater;
   private Context mContext;
   private ViewGroup mRoot;
   private ViewGroup mTrack;
   private ArrayList<ActionItem> mActionList;

   public QuickActionView(Context _context, AttributeSet _attrs) {
      super(_context, _attrs);
      mContext = _context;
      mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      mRoot = (ViewGroup) mInflater.inflate(R.layout.quickaction_view, null);
      addView(mRoot);

      mTrack = (ViewGroup) mRoot.findViewById(R.id.tracks);
      
      mActionList = new ArrayList<ActionItem>();
   }
   
   /**
    * Add action item
    * 
    * @param _action  {@link ActionItem}
    */
   public void addActionItem(ActionItem _action) {
      mActionList.add(_action); 
   }
   
   /**
    * Create action list
    *     
    */
   public void createActionList() {
      View view;
      String title;
      Drawable icon;
      OnClickListener listener;
      OnLongClickListener longClickListener;
      int index = 1;
      
      for (int i = 0; i < mActionList.size(); i++) {
         title       = mActionList.get(i).getTitle();
         icon     = mActionList.get(i).getIcon();
         listener = mActionList.get(i).getListener();
         longClickListener = mActionList.get(i).getLongClickListener();
         view = getActionItem(title, icon, listener, longClickListener);
      
         view.setFocusable(true);
         view.setClickable(mActionList.get(i).getEnabled());
          
         mTrack.addView(view, index);
         
         index++;
      }
   }
   
   public void clearActionList(){
      for (int i = 0; i < mActionList.size(); i++) {
         mTrack.removeViewAt(1);
      }
      
      mActionList.clear();
   }
   
   /**
    * Get action item {@link View}
    * 
    * @param _title action item title
    * @param _icon {@link Drawable} action item icon
    * @param _listener {@link View.OnClickListener} action item listener
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
   
   @Override
   public void dismiss() {
     //Nothing todo here, but I need it so we can have the dismiss method in the interface
      
   }

}
