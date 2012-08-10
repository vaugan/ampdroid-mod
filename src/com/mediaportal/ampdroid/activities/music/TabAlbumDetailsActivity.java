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
package com.mediaportal.ampdroid.activities.music;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseTabActivity;
import com.mediaportal.ampdroid.activities.StatusBarActivityHandler;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.Movie;
import com.mediaportal.ampdroid.data.MusicTrack;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.downloadservice.MediaItemType;
import com.mediaportal.ampdroid.lists.ImageHandler;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter;
import com.mediaportal.ampdroid.lists.LazyLoadingAdapter.ILoadingListener;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.Utils;
import com.mediaportal.ampdroid.lists.views.ViewTypes;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.utils.DownloaderUtils;
import com.mediaportal.ampdroid.utils.QuickActionUtils;
import com.mediaportal.ampdroid.utils.Util;

public class TabAlbumDetailsActivity extends Activity implements ILoadingListener {
   private LinearLayout mTracksListView;
   private LazyLoadingAdapter mAdapter;
   private DataHandler mService;
   private LoadAlbumTracksTask mMusicLoaderTask;
   private BaseTabActivity mBaseActivity;
   private StatusBarActivityHandler mStatusBarHandler;
   private String mAlbumArtistsString;
   private String mAlbumName;
   private String mActivityGroup;
   private String mAlbumCover;
   private String[] mAlbumArtists;
   private ImageView mAlbumCoverImage;
   private ImageHandler mImageHandler;
   private TextView mTextViewAlbumName;
   private TextView mTextViewAlbumYear;
   private TextView mTextViewAlbumTracks;
   private TextView mTextViewAlbumLength;
   public List<MusicTrack> mAlbumTracks;

   private class LoadAlbumTracksTask extends AsyncTask<Integer, List<Movie>, List<MusicTrack>> {
      Activity mContext;

      private LoadAlbumTracksTask(Activity _context) {
         mContext = _context;
      }

      @Override
      protected List<MusicTrack> doInBackground(Integer... _params) {
         mAlbumTracks = mService.getSongsOfAlbum(mAlbumName, mAlbumArtistsString);
         return mAlbumTracks;
      }

      @Override
      protected void onPostExecute(List<MusicTrack> _result) {
         if (_result != null) {
            int trackLength = 0;
            int yearMin = Integer.MAX_VALUE;
            int yearMax = 0;
            for (int i = 0; i < _result.size(); i++) {
               View view = Button.inflate(mContext, R.layout.listitem_track, null);
               TextView text = (TextView) view.findViewById(R.id.TextViewTitle);
               ImageView image = (ImageView) view.findViewById(R.id.ImageViewEventImage);
               TextView subtext = (TextView) view.findViewById(R.id.TextViewText);
               final int index = i;

               MusicTrack s = _result.get(i);
               view.setOnTouchListener(new OnTouchListener() {

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
               });

               view.setOnLongClickListener(new OnLongClickListener() {
                  @Override
                  public boolean onLongClick(View _view) {
                     try {
                        MusicTrack selected = (MusicTrack) _view.getTag();
                        final String trackTitle = selected.getTitle();
                        final String trackPath = selected.getFilePath();
                        final String trackId = String.valueOf(selected.getTrackId());
                        
                        if (trackTitle != null) {
                           String dirName = DownloaderUtils.getMusicTrackPath(mAlbumArtists[0],
                                 mAlbumName);
                           final String fileName = dirName
                                 + Utils.getFileNameWithExtension(trackPath, "\\");
                           final String displayName = selected.toString();
                           final QuickAction qa = new QuickAction(_view);

                           final File localFile = new File(DownloaderUtils.getBaseDirectory()
                                 + "/" + fileName);
                           
                           if (localFile.exists()) {
                              QuickActionUtils.createPlayOnDeviceQuickAction(_view.getContext(), qa,
                                    new OnClickListener() {
                                       @Override
                                       public void onClick(View _view) {
                                          Intent playIntent = new Intent(Intent.ACTION_VIEW);
                                          playIntent.setDataAndType(Uri.fromFile(localFile),
                                                "audio/*");
                                          startActivity(playIntent);

                                          qa.dismiss();
                                       }
                                    });
                           } else {
                              QuickActionUtils.createDownloadSdCardQuickAction(_view.getContext(), qa,
                                    mService, trackId, trackId, DownloadItemType.MusicTrackItem,
                                    MediaItemType.Music, fileName, displayName);
                           }

                           QuickActionUtils.createPlayOnClientQuickAction(_view.getContext(), qa,
                                 mService, new OnClickListener() {
                                    @Override
                                    public void onClick(View _view) {
                                       CreateAlbumPlaylistTask task = new CreateAlbumPlaylistTask(_view.getContext());
                                       task.execute(index);
                                       
                                       qa.dismiss();
                                    }
                                 });

                           qa.setAnimStyle(QuickAction.ANIM_AUTO);

                           qa.show();
                        } else {
                           Util.showToast(_view.getContext(), getString(R.string.media_nofile));
                        }
                        return true;
                     } catch (Exception ex) {
                        return false;
                     }
                  }
               });

               text.setText(s.getTitle());
               subtext.setText(s.getArtistsString());
               view.setTag(s);

               trackLength += s.getDuration();

               if (s.getYear() > yearMax) {
                  yearMax = s.getYear();
               }

               if (s.getYear() < yearMin) {
                  yearMin = s.getYear();
               }

               mTracksListView.addView(view);
            }

            if (yearMin == yearMax) {
               mTextViewAlbumYear.setText(String.valueOf(yearMin));
            } else {
               mTextViewAlbumYear.setText(String.valueOf(yearMin)
                     + getText(R.string.music_album_details_year_seperator)
                     + String.valueOf(yearMax));
            }

            if (trackLength > 0) {
               mTextViewAlbumLength.setText(String.valueOf(trackLength / 60));
            }
            mTextViewAlbumTracks.setText(String.valueOf(_result.size()));
         }
      }
   }

   private class CreateAlbumPlaylistTask extends AsyncTask<Integer, Intent, Boolean> {
      private Context mContext;

      private CreateAlbumPlaylistTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected Boolean doInBackground(Integer... _params) {
         int index = _params[0];
         List<MusicTrack> tracks = mAlbumTracks;

         if(mService.isClientControlConnected()){
            mService.createPlaylist(tracks, true, index );
            return true;
         }
         else{
            return false;
         }
      }

      @Override
      protected void onProgressUpdate(Intent... values) {
         super.onProgressUpdate(values);
      }

      @Override
      protected void onPostExecute(Boolean _result) {
         if(!_result){
            Util.showToast(mContext, getString(R.string.info_remote_notconnected));
         }
      }
   }
   
   @Override
   public void EndOfListReached() {
      loadFurtherItems();
   }

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_tabalbumdetails);

      mBaseActivity = (BaseTabActivity) getParent().getParent();

      mService = DataHandler.getCurrentRemoteInstance();
      mStatusBarHandler = new StatusBarActivityHandler(mBaseActivity, mService);
      mStatusBarHandler.setHome(false);

      mAdapter = new LazyLoadingAdapter(this);
      mAdapter.addView(ViewTypes.TextView.ordinal());
      mAdapter.setLoadingListener(this);

      mTracksListView = (LinearLayout) findViewById(R.id.LinearLayoutTracks);

      mTextViewAlbumName = (TextView) findViewById(R.id.TextViewAlbumName);
      mTextViewAlbumYear = (TextView) findViewById(R.id.TextViewAlbumYear);
      mTextViewAlbumTracks = (TextView) findViewById(R.id.TextViewAlbumNumTracks);
      mTextViewAlbumLength = (TextView) findViewById(R.id.TextViewAlbumLength);

      mAlbumCoverImage = (ImageView) findViewById(R.id.ImageViewAlbumCover);
      mImageHandler = new ImageHandler(this);
      Bundle extras = getIntent().getExtras();
      if (extras != null) {
         mAlbumArtistsString = extras.getString("album_artists_string");
         mAlbumName = extras.getString("album_name");
         mAlbumCover = extras.getString("album_cover");
         mActivityGroup = extras.getString("activity_group");
         mAlbumArtists = extras.getStringArray("album_artists");
      }

      if (mAlbumCover != null && !mAlbumCover.equals("")) {
         String fileName = Utils.getFileNameWithExtension(mAlbumCover, "\\");
         String cacheName = "Music" + File.separator + mAlbumArtists[0] + File.separator + "Thumbs"
               + File.separator + fileName;
         LazyLoadingImage image = new LazyLoadingImage(mAlbumCover, cacheName, 150, 200);
         mAlbumCoverImage.setTag(mAlbumCover);
         mImageHandler.DisplayImage(image, R.drawable.listview_imageloading_poster, this,
               mAlbumCoverImage);
      }

      mTextViewAlbumName.setText(mAlbumName);

      loadFurtherItems();
   }

   private void loadFurtherItems() {
      if (mMusicLoaderTask == null) {
         mMusicLoaderTask = new LoadAlbumTracksTask(this);
         mMusicLoaderTask.execute(20);
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu _menu) {
      super.onCreateOptionsMenu(_menu);

      return true;
   }

   @Override
   public void onDestroy() {
      super.onDestroy();
   }

   @Override
   public void onBackPressed() {
      if (mActivityGroup.equals("artists")) {
         TabArtistsActivityGroup.getGroup().back();
      } else if (mActivityGroup.equals("albums")) {
         TabAlbumsActivityGroup.getGroup().back();
      }
      return;
   }

   @Override
   public boolean onKeyDown(int _keyCode, KeyEvent _event) {
      if (_keyCode == KeyEvent.KEYCODE_BACK) {
         if (mActivityGroup.equals("artists")) {
            TabArtistsActivityGroup.getGroup().back();
         } else if (mActivityGroup.equals("albums")) {
            TabAlbumsActivityGroup.getGroup().back();
         }
         return true;
      }
      return super.onKeyDown(_keyCode, _event);
   }
}
