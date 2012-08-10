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
package com.mediaportal.ampdroid.activities.media;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.Movie;
import com.mediaportal.ampdroid.data.MovieFull;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.downloadservice.MediaItemType;
import com.mediaportal.ampdroid.lists.ImageHandler;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.Utils;
import com.mediaportal.ampdroid.quickactions.QuickActionView;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.DownloaderUtils;
import com.mediaportal.ampdroid.utils.QuickActionUtils;

public class TabVideoDetailsActivity extends Activity {
   private DataHandler mService;
   MovieFull mMovie;
   int mVideoId;

   private ImageHandler mImageHandler;
   private LoadMovieDetailsTask mLoadVideoTask;
   private ProgressDialog mLoadingDialog;
   private ImageView mImageViewVideoPoster;
   private TextView mTextViewVideoName;
   private TextView mTextViewVideoReleaseDate;
   private RatingBar mRatingBarVideoRating;
   private TextView mTextViewVideoOverview;
   private TextView mTextViewVideoRuntime;
   private TextView mTextViewVideoActors;
   private String mVideoName;

   private class LoadMovieDetailsTask extends AsyncTask<Integer, List<Movie>, MovieFull> {
      Activity mContext;

      private LoadMovieDetailsTask(Activity _context) {
         mContext = _context;
      }

      @Override
      protected MovieFull doInBackground(Integer... _params) {
         mMovie = mService.getVideoDetails(mVideoId);
         
         return mMovie;
      }

      @Override
      protected void onPostExecute(MovieFull _result) {
         if (_result != null) {
            String videosPoster = _result.getCoverThumbPath();
            if (videosPoster != null && !videosPoster.equals("")) {
               String fileName = Utils.getFileNameWithExtension(_result.getCoverThumbPath(), "\\");
               String cacheName = "Videos" + File.separator + mVideoId + File.separator
                     + "LargePoster" + File.separator + fileName;

               LazyLoadingImage image = new LazyLoadingImage(videosPoster, cacheName, 200, 400);
               mImageViewVideoPoster.setTag(videosPoster);
               mImageHandler.DisplayImage(image, R.drawable.listview_imageloading_poster, mContext,
                     mImageViewVideoPoster);

            }

            mTextViewVideoReleaseDate.setText(String.valueOf(_result.getYear()));

            int runtime = _result.getRuntime();
            if (runtime != 0) {
               mTextViewVideoRuntime.setText(String.valueOf(runtime));
            } else {
               mTextViewVideoRuntime.setText("-");
            }

            String actorsString = _result.getActorsString();
            if (actorsString != null) {
               mTextViewVideoActors.setText(actorsString);
            } else {
               mTextViewVideoActors.setText("-");
            }

            int rating = (int) _result.getScore();
            mRatingBarVideoRating.setRating(rating);

            mTextViewVideoOverview.setText(_result.getSummary());
            
            String movieFile = mMovie.getFilename();
            if (movieFile != null) {
               String dirName = DownloaderUtils.getMoviePath(mMovie);
               String fileName = dirName + Utils.getFileNameWithExtension(movieFile, "\\");

               File localFile = new File(DownloaderUtils.getBaseDirectory() + "/" + fileName);

               QuickActionView view = (QuickActionView) findViewById(R.id.QuickActionViewItemActions);

               String displayName = mMovie.toString();
               String itemId = String.valueOf(mVideoId);

               if (localFile.exists()) {
                  QuickActionUtils.createPlayOnDeviceQuickAction(mContext, view, localFile,
                        MediaItemType.Video);
               } else {
                  QuickActionUtils.createDownloadSdCardQuickAction(mContext, view, mService,
                        itemId, movieFile, DownloadItemType.VideoDatabaseItem, MediaItemType.Video,
                        fileName, displayName);
               }

               if (Constants.ENABLE_STREAMING) {
                  QuickActionUtils.createStreamOnClientQuickAction(mContext, view, mService,
                        itemId, DownloadItemType.VideoDatabaseItem, fileName, displayName);

               }

               QuickActionUtils.createPlayOnClientQuickAction(mContext, view, mService, movieFile);
               view.createActionList();
            }
            
            mLoadingDialog.cancel();
         } else {
            mLoadingDialog.cancel();
            Dialog diag = new Dialog(getParent());
            diag.setTitle(getString(R.string.media_videos_loadingerror));
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
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_tabmoviedetails);

      Bundle extras = getIntent().getExtras();
      if (extras != null) {
         mVideoId = extras.getInt("video_id");
         mVideoName = extras.getString("video_name");

         mService = DataHandler.getCurrentRemoteInstance();

         mTextViewVideoName = (TextView) findViewById(R.id.TextViewMovieName);
         mTextViewVideoName.setText(mVideoName);

         mImageViewVideoPoster = (ImageView) findViewById(R.id.ImageViewMoviePoster);
         mTextViewVideoReleaseDate = (TextView) findViewById(R.id.TextViewMovieRelease);
         mRatingBarVideoRating = (RatingBar) findViewById(R.id.RatingBarMovieRating);
         mTextViewVideoOverview = (TextView) findViewById(R.id.TextViewMovieOverview);
         mTextViewVideoRuntime = (TextView) findViewById(R.id.TextViewMovieRuntime);
         mTextViewVideoActors = (TextView) findViewById(R.id.TextViewMovieActors);

        mImageViewVideoPoster.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               mService.playVideoFileOnClient(mMovie.getFilename(), 0);
            }
         });
         
         mImageHandler = new ImageHandler(this);

         mLoadVideoTask = new LoadMovieDetailsTask(this);
         mLoadVideoTask.execute(mVideoId);

         // mPosterGallery.setSpacing(-10);
         // mPosterGallery.setAdapter(mAdapter);

         mLoadingDialog = ProgressDialog.show(getParent(), getString(R.string.media_videos_loadvideodetails),
               getString(R.string.info_loading_title), true);
         mLoadingDialog.setCancelable(true);
      } else {// activity called without movie id (shouldn't happen ;))

      }
   }
}
