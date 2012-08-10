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

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseActivity;
import com.mediaportal.ampdroid.activities.StatusBarActivityHandler;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.asynctasks.AddScheduleTask;
import com.mediaportal.ampdroid.asynctasks.CancelScheduleTask;
import com.mediaportal.ampdroid.data.TvChannel;
import com.mediaportal.ampdroid.data.TvProgramBase;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.lists.ILoadingAdapterItem;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.views.TvServerProgramsDetailsViewItem;
import com.mediaportal.ampdroid.quickactions.ActionItem;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.DateTimeHelper;
import com.mediaportal.ampdroid.utils.QuickActionUtils;
import com.mediaportal.ampdroid.utils.Util;

public class TvServerChannelDetailsActivity extends BaseActivity {
   private DataHandler mService;
   private ListView mEpgView;
   private LoadEpgTask mEpgLoaderTask;
   private Spinner mDaysSpinner;
   private Button mPrevDayButton;
   private Button mNextDayButton;
   private ArrayAdapter<EpgDay> mDaysAdapter;
   private LazyLoadingAdapter mEpgAdapter;
   private TextView mChannelNameText;
   private TextView mCurrentDateTextView;
   private int mChannelId;
   private String mChannelName;

   private AddScheduleTask mAddScheduleTask;
   private CancelScheduleTask mCancelScheduleTask;
   private StatusBarActivityHandler mStatusBarHandler;

   private class LoadEpgTask extends AsyncTask<Integer, Integer, List<TvProgramBase>> {
      private Context mContext;

      private LoadEpgTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected List<TvProgramBase> doInBackground(Integer... _params) {
         EpgDay day = mDaysAdapter.getItem(_params[0]);
         Date begin = day.getDayBegin();
         Date end = day.getDayEnd();

         Log.d(Constants.LOG_CONST, String.format("Loading programs for {0} | {1} | {2}", mChannelId, begin, end));
         List<TvProgramBase> programs = mService.getTvBaseEpgForChannel(mChannelId, begin, end);
         Log.d(Constants.LOG_CONST, String.format("Finished loading loading programs for {0} | {1} | {2}", mChannelId, begin, end));
         return programs;
      }

      @Override
      protected void onProgressUpdate(Integer... values) {
         super.onProgressUpdate(values);
      }

      @Override
      protected void onPostExecute(List<TvProgramBase> _result) {
         int selectedPos = 0;
         if (_result != null) {
            Util.showToast(mContext, getString(R.string.tvserver_loadepg_finished));

            for (TvProgramBase p : _result) {
               Date begin = p.getStartTime();
               Date end = p.getEndTime();

               Calendar cal = Calendar.getInstance();
               cal.set(Calendar.DAY_OF_MONTH, begin.getDate());
               Date now = cal.getTime();

               ILoadingAdapterItem item = new TvServerProgramsDetailsViewItem(p);

               mEpgAdapter.addItem(item);

               if (now.after(begin) && now.before(end)) {
                  selectedPos = mEpgAdapter.getCount() - 3;
                  if (selectedPos < 0) {
                     selectedPos = 0;
                  }
               }
            }
         }
         else{
            Log.w(Constants.LOG_CONST, "No programs found");
            Util.showToast(mContext, "Failed to load epg");
         }
         mEpgAdapter.showLoadingItem(false);
         mEpgAdapter.notifyDataSetChanged();

         mEpgView.setSelection(selectedPos);
      }
   }

   private class EpgDay {
      private int mDaysFromToday;
      private Date mDayBegin;
      private Date mDayEnd;

      private EpgDay(int _daysFromToday, Date _begin, Date _end) {
         mDayBegin = _begin;
         mDayEnd = _end;
         mDaysFromToday = _daysFromToday;
      }

      public Date getDayBegin() {
         return mDayBegin;
      }

      public Date getDayEnd() {
         return mDayEnd;
      }

      @Override
      public String toString() {
         switch (mDaysFromToday) {
         case -1:
            return getString(R.string.days_yesterday);
         case 0:
            return getString(R.string.days_today);
         case 1:
            return getString(R.string.days_tomorrow);
         default:
            return DateTimeHelper.getDayOfWeek(mDayBegin);
         }

      }
   }

   @Override
   public void onCreate(Bundle _savedInstanceState) {
      setTitle(R.string.title_tvserver_epg);
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_tvserverchanneldetails);

      Bundle extras = getIntent().getExtras();
      if (extras != null) {
         mChannelId = extras.getInt("channel_id");
         mChannelName = extras.getString("channel_name");
         mEpgView = (ListView) findViewById(R.id.ListViewChannels);
         mEpgAdapter = new LazyLoadingAdapter(this);
         mEpgView.setAdapter(mEpgAdapter);
         mService = DataHandler.getCurrentRemoteInstance();
         mStatusBarHandler = new StatusBarActivityHandler(this, mService);
         mStatusBarHandler.setHome(false);

         mDaysSpinner = (Spinner) findViewById(R.id.SpinnerDay);
         mDaysAdapter = new ArrayAdapter<EpgDay>(this, android.R.layout.simple_spinner_item);
         mDaysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

         Calendar cal = Calendar.getInstance();
         cal.set(Calendar.AM_PM, 0);
         cal.set(Calendar.HOUR, 0);
         cal.set(Calendar.MINUTE, 0);
         cal.set(Calendar.SECOND, 0);

         cal.add(Calendar.DATE, -1);

         Date begin = cal.getTime();
         cal.add(Calendar.DATE, 1);
         Date end = cal.getTime();

         mDaysAdapter.add(new EpgDay(-1, begin, end));

         begin = cal.getTime();
         cal.add(Calendar.DATE, 1);
         end = cal.getTime();

         mDaysAdapter.add(new EpgDay(0, begin, end));

         begin = cal.getTime();
         cal.add(Calendar.DATE, 1);
         end = cal.getTime();

         mDaysAdapter.add(new EpgDay(1, begin, end));

         begin = cal.getTime();
         cal.add(Calendar.DATE, 1);
         end = cal.getTime();

         mDaysAdapter.add(new EpgDay(2, begin, end));

         mDaysSpinner.setAdapter(mDaysAdapter);
         mDaysSpinner.setSelection(1);

         mNextDayButton = (Button) findViewById(R.id.ButtonNextDay);
         mPrevDayButton = (Button) findViewById(R.id.ButtonPrevDay);
         mChannelNameText = (TextView) findViewById(R.id.TextViewChannelName);
         mCurrentDateTextView = (TextView) findViewById(R.id.TextViewCurrentDate);

         mChannelNameText.setText(mChannelName);

         mDaysSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> _adapter, View _view, int _pos, long _id) {
               refreshEpg(_pos);
               mCurrentDateTextView.setText(mDaysAdapter.getItem(_pos).toString());
               setPrevNextButtonEnabled(_pos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> _adapter) {
            }
         });

         mNextDayButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               int pos = mDaysSpinner.getSelectedItemPosition();

               if (pos + 1 < mDaysAdapter.getCount()) {
                  mDaysSpinner.setSelection(pos + 1);
               }
            }
         });

         mPrevDayButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               int pos = mDaysSpinner.getSelectedItemPosition();

               if (pos > 0) {
                  mDaysSpinner.setSelection(pos - 1);
               }
            }
         });

         mEpgView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> _item, View _view, final int _pos,
                  long _id) {
               ILoadingAdapterItem item = (ILoadingAdapterItem) mEpgView.getItemAtPosition(_pos);
               final TvProgramBase program = (TvProgramBase) item.getItem();

               final QuickAction qa = new QuickAction(_view);

               Date now = new Date();
               if (program.isIsScheduled()) {
                  ActionItem addScheduleAction = new ActionItem();
                  addScheduleAction.setTitle(getString(R.string.quickactions_cancel));
                  addScheduleAction.setIcon(getResources().getDrawable(R.drawable.bubble_del));
                  addScheduleAction.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View _view) {
                        mCancelScheduleTask = new CancelScheduleTask(_view.getContext(), mService,
                              mEpgAdapter);
                        mCancelScheduleTask.execute(program);

                        qa.dismiss();
                     }
                  });
                  qa.addActionItem(addScheduleAction);
               } else if(program.getEndTime().after(now)){
                  ActionItem addScheduleAction = new ActionItem();
                  addScheduleAction.setTitle(getString(R.string.quickactions_record));
                  addScheduleAction.setIcon(getResources().getDrawable(R.drawable.quickaction_add));
                  addScheduleAction.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View _view) {
                        mAddScheduleTask = new AddScheduleTask(_view.getContext(), mService,
                              mEpgAdapter);
                        mAddScheduleTask.execute(program);

                        qa.dismiss();
                     }
                  });
                  qa.addActionItem(addScheduleAction);
               }

               if (now.after(program.getStartTime()) && now.before(program.getEndTime())) {
                  QuickActionUtils.createStreamOnClientQuickAction(_view.getContext(), qa,
                        mService, String.valueOf(mChannelId), DownloadItemType.LiveTv,
                        mChannelName, mChannelName);
                  
                  QuickActionUtils.createPlayOnClientQuickAction(_view.getContext(), qa, mService,
                        new OnClickListener() {
                     @Override
                     public void onClick(View _view) {
                        mService.playTvChannelOnClient(mChannelId, true);

                        qa.dismiss();
                     }
                  });
               }

               qa.show();
               return true;
            }
         });

         mEpgView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> _adapter, View _view, int _pos, long _id) {
               ILoadingAdapterItem item = (ILoadingAdapterItem) mEpgAdapter.getItem(_pos);

               // open program detail view
               TvProgramBase prog = (TvProgramBase) item.getItem();

               Intent myIntent = new Intent(_view.getContext(),
                     TvServerProgramDetailsActivity.class);
               myIntent.putExtra("program_id", prog.getIdProgram());
               myIntent.putExtra("program_name", prog.getTitle());
               startActivity(myIntent);

            }
         });
      }
   }

   private void setPrevNextButtonEnabled(int _pos) {
      int size = mDaysAdapter.getCount();

      if (_pos + 1 < size && size != 0) {
         mNextDayButton.setEnabled(true);
      } else {
         mNextDayButton.setEnabled(false);
      }

      if (_pos == 0 || size == 0) {
         mPrevDayButton.setEnabled(false);
      } else {
         mPrevDayButton.setEnabled(true);
      }

   }

   private void refreshEpg(int _position) {
      mEpgAdapter.clear();
      mEpgAdapter.showLoadingItem(true);
      mEpgAdapter.notifyDataSetChanged();
      mEpgLoaderTask = new LoadEpgTask(this);
      mEpgLoaderTask.execute(_position);
   }
}
