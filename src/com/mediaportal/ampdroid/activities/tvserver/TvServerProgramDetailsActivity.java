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
package com.mediaportal.ampdroid.activities.tvserver;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseActivity;
import com.mediaportal.ampdroid.activities.StatusBarActivityHandler;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.TvProgram;
import com.mediaportal.ampdroid.utils.DateTimeHelper;

public class TvServerProgramDetailsActivity extends BaseActivity {
   private LoadProgramDetailsTask mLoadProgramTask;
   private DataHandler mService;
   private ProgressDialog mLoadingDialog;
   private TextView mProgramTitle;
   private TextView mProgramOverview;
   private TextView mProgramStart;
   private TextView mProgramEnd;
   private TextView mProgramDuration;
   private int mProgramId;
   private String mProgramName;
   private StatusBarActivityHandler mStatusBarHandler;
   
   private class LoadProgramDetailsTask extends AsyncTask<Integer, Void, TvProgram> {
      Activity mContext;

      private LoadProgramDetailsTask(Activity _context) {
         mContext = _context;
      }

      @Override
      protected TvProgram doInBackground(Integer... _params) {
         TvProgram prog = mService.getTvEpgDetails(_params[0]);

         return prog;
      }

      @Override
      protected void onPostExecute(TvProgram _result) {
         
         if (_result != null) {
            mProgramTitle.setText(mProgramName);
            
            String desc = _result.getDescription();
            desc = desc.replace('\r', '\n');
            mProgramOverview.setText(desc);
            mProgramStart.setText(DateTimeHelper.getDateString(_result.getStartTime(), true));
            mProgramEnd.setText(DateTimeHelper.getDateString(_result.getEndTime(), true));
            mProgramDuration.setText(String.valueOf(_result.getDurationInMinutes()));
            mLoadingDialog.cancel();
         } else {
            mLoadingDialog.cancel();
            Dialog diag = new Dialog(mContext);
            diag.setTitle(getString(R.string.tvserver_loadprogramdetails_failed));
            diag.setCancelable(true);
            
            diag.show();
            diag.setOnDismissListener(new OnDismissListener() {
               @Override
               public void onDismiss(DialogInterface dialog) {
                  mContext.finish();
                  
               }
            });
         }
      }
   }
   
   @Override
   public void onCreate(Bundle _savedInstanceState) {
      setTitle(R.string.title_tvserver_epg);
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_tvserverprogramdetails);
      
      mService = DataHandler.getCurrentRemoteInstance();
      mStatusBarHandler = new StatusBarActivityHandler(this, mService);
      mStatusBarHandler.setHome(false);
      
      mProgramTitle = (TextView)findViewById(R.id.TextViewProgramName);
      mProgramOverview = (TextView)findViewById(R.id.TextViewProgramDescription);
      mProgramStart = (TextView)findViewById(R.id.TextViewProgramStart);
      mProgramEnd = (TextView)findViewById(R.id.TextViewProgramEnd);
      mProgramDuration = (TextView)findViewById(R.id.TextViewProgramDuration);
   
      Bundle extras = getIntent().getExtras();
      if (extras != null) {
         mProgramId = extras.getInt("program_id");
         mProgramName = extras.getString("program_name");

         if(mProgramTitle != null && mProgramName != null){
            mProgramTitle.setText(mProgramName);
            mProgramTitle.setMovementMethod(new ScrollingMovementMethod());
         }

         mLoadProgramTask = new LoadProgramDetailsTask(this);
         mLoadProgramTask.execute(mProgramId);

         mLoadingDialog = ProgressDialog.show(this, getString(R.string.tvserver_loadprogramdetails),
               getString(R.string.info_loading_title), true);
         mLoadingDialog.setCancelable(true);
      } else {// activity called without movie id (shouldn't happen ;))

      }
   }
}
