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

import java.util.Date;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseActivity;
import com.mediaportal.ampdroid.activities.StatusBarActivityHandler;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.quickactions.QuickActionView;
import com.mediaportal.ampdroid.utils.DateTimeHelper;
import com.mediaportal.ampdroid.utils.QuickActionUtils;

public class TvServerRecordingDetailsActivity extends BaseActivity {
   private DataHandler mService;
   private ProgressDialog mLoadingDialog;
   private TextView mTextViewTitle;
   private TextView mTextViewOverview;
   private TextView mTextViewStart;
   private TextView mTextViewEnd;
   private TextView mTextViewDuration;
   private int mRecordingId;
   private String mProgramName;
   private StatusBarActivityHandler mStatusBarHandler;
   private String mRecordingName;
   private Date mRecordingStart;
   private Date mRecordingEnd;
   private String mRecordingChannel;
   private String mRecordingOverview;
   private String mRecordingFilename;
   private TextView mTextViewRecordingChannel;
   
   @Override
   public void onCreate(Bundle _savedInstanceState) {
      setTitle(R.string.title_tvserver_epg);
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_recordingdetails);

      mService = DataHandler.getCurrentRemoteInstance();
      mStatusBarHandler = new StatusBarActivityHandler(this, mService);
      mStatusBarHandler.setHome(false);

      mTextViewTitle = (TextView) findViewById(R.id.TextViewRecordingName);
      mTextViewOverview = (TextView) findViewById(R.id.TextViewRecordingOverview);
      mTextViewStart = (TextView) findViewById(R.id.TextViewRecordingStarttime);
      mTextViewEnd = (TextView) findViewById(R.id.TextViewRecordingEndtime);
      mTextViewDuration = (TextView) findViewById(R.id.TextViewRecordingRuntime);
      mTextViewRecordingChannel = (TextView)findViewById(R.id.TextViewRecordingChannel);

      Bundle extras = getIntent().getExtras();
      if (extras != null) {
         mRecordingId = extras.getInt("recording_id");
         mRecordingName = extras.getString("recording_name");
         mRecordingStart = (Date) extras.get("recording_start");
         mRecordingEnd = (Date) extras.get("recording_end");
         mRecordingChannel = extras.getString("recording_channel");
         mRecordingOverview = extras.getString("recording_description");
         mRecordingFilename = extras.getString("recording_filename");
         
         if(mTextViewTitle != null){
            mTextViewTitle.setText(mRecordingName);
         }
         
         if(mTextViewOverview != null){
            mTextViewOverview.setText(mRecordingOverview);
         }
         
         if(mTextViewStart != null){
            mTextViewStart.setText(DateTimeHelper.getDateString(mRecordingStart, true));
         }
         
         if(mTextViewEnd != null){
            mTextViewEnd.setText(DateTimeHelper.getDateString(mRecordingEnd, true));
         }
         
         if(mTextViewDuration != null){
            long ms = mRecordingEnd.getTime() - mRecordingStart.getTime();
            mTextViewDuration.setText(DateTimeHelper.getTimeStringFromMs(ms));
         }
         
         if(mTextViewRecordingChannel != null){
            mTextViewRecordingChannel.setText(mRecordingChannel);
         }
         
         QuickActionView view = (QuickActionView) findViewById(R.id.QuickActionViewItemActions);

         QuickActionUtils.createPlayOnClientQuickAction(this, view, mService,
               mRecordingFilename);

         QuickActionUtils.createStreamOnClientQuickAction(this, view, mService,
               String.valueOf(mRecordingId),
               DownloadItemType.TvRecording, mRecordingFilename,
               mRecordingName);
         
         view.createActionList();
         

      } else {
         // activity called without movie id (shouldn't happen ;))
      }
   }
}
