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

public class TabMovieDetailsActivity extends Activity {
   private DataHandler mService;
   MovieFull mMovie;
   int mMovieId;

   private ImageHandler mImageHandler;
   private LoadVideoDetailsTask mLoadMovieTask;
   private ProgressDialog mLoadingDialog;
   private ImageView mImageViewMoviePoster;
   private TextView mTextViewMovieName;
   private TextView mTextViewMovieReleaseDate;
   private RatingBar mRatingBarMovieRating;
   private TextView mTextViewMovieOverview;
   private TextView mTextViewMovieRuntime;
   private TextView mTextViewMovieActors;
   private String mMovieName;

   private class LoadVideoDetailsTask extends AsyncTask<Integer, List<Movie>, MovieFull> {
      Activity mContext;

      private LoadVideoDetailsTask(Activity _context) {
         mContext = _context;
      }

      @Override
      protected MovieFull doInBackground(Integer... _params) {
         mMovie = mService.getMovieDetails(mMovieId);

         return mMovie;
      }

      @Override
      protected void onPostExecute(MovieFull _result) {
         if (_result != null) {
            String seriesPoster = _result.getCoverThumbPath();
            if (seriesPoster != null && !seriesPoster.equals("")) {
               String fileName = Utils.getFileNameWithExtension(_result.getCoverThumbPath(), "\\");
               String cacheName = "Movies" + File.separator + mMovieId + File.separator
                     + "LargePoster" + File.separator + fileName;

               LazyLoadingImage image = new LazyLoadingImage(seriesPoster, cacheName, 200, 400);
               mImageViewMoviePoster.setTag(seriesPoster);
               mImageHandler.DisplayImage(image, R.drawable.listview_imageloading_poster, mContext,
                     mImageViewMoviePoster);

            }

            mTextViewMovieReleaseDate.setText(String.valueOf(_result.getYear()));

            int runtime = _result.getRuntime();
            if (runtime != 0) {
               mTextViewMovieRuntime.setText(String.valueOf(runtime));
            } else {
               mTextViewMovieRuntime.setText("-");
            }

            String[] actors = _result.getActorsString().split("\\|");
            if (actors != null) {
               String actorsString = "";
               for (String a : actors) {
                  if (a != null && !a.equals("")) {
                     actorsString += " - " + a + "\n";
                  }
               }
               mTextViewMovieActors.setText(actorsString);
            } else {
               mTextViewMovieActors.setText("-");
            }

            int rating = (int) _result.getScore();
            mRatingBarMovieRating.setRating(rating);

            mTextViewMovieOverview.setText(_result.getSummary());

            String movieFile = mMovie.getFilename();
            if (movieFile != null) {
               String dirName = DownloaderUtils.getMoviePath(mMovie);
               String fileName = dirName + Utils.getFileNameWithExtension(movieFile, "\\");

               File localFile = new File(DownloaderUtils.getBaseDirectory() + "/" + fileName);

               QuickActionView view = (QuickActionView) findViewById(R.id.QuickActionViewItemActions);

               String displayName = mMovie.toString();
               String itemId = String.valueOf(mMovieId);

               if (localFile.exists()) {
                  QuickActionUtils.createPlayOnDeviceQuickAction(mContext, view, localFile,
                        MediaItemType.Video);
               } else {
                  QuickActionUtils.createDownloadSdCardQuickAction(mContext, view, mService,
                        itemId, movieFile, DownloadItemType.MovieItem, MediaItemType.Video,
                        fileName, displayName);
               }

               if (Constants.ENABLE_STREAMING) {
                  QuickActionUtils.createStreamOnClientQuickAction(mContext, view, mService,
                        itemId, DownloadItemType.MovieItem, fileName, displayName);

               }

               QuickActionUtils.createPlayOnClientQuickAction(mContext, view, mService, movieFile);
               view.createActionList();
            }

            mLoadingDialog.cancel();
         } else {
            mLoadingDialog.cancel();
            Dialog diag = new Dialog(getParent());
            diag.setTitle(getString(R.string.media_movie_loadingerror));
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
         mMovieId = extras.getInt("movie_id");
         mMovieName = extras.getString("movie_name");

         mService = DataHandler.getCurrentRemoteInstance();

         mTextViewMovieName = (TextView) findViewById(R.id.TextViewMovieName);
         mTextViewMovieName.setText(mMovieName);

         mImageViewMoviePoster = (ImageView) findViewById(R.id.ImageViewMoviePoster);
         mTextViewMovieReleaseDate = (TextView) findViewById(R.id.TextViewMovieRelease);
         mRatingBarMovieRating = (RatingBar) findViewById(R.id.RatingBarMovieRating);
         mTextViewMovieOverview = (TextView) findViewById(R.id.TextViewMovieOverview);
         mTextViewMovieRuntime = (TextView) findViewById(R.id.TextViewMovieRuntime);
         mTextViewMovieActors = (TextView) findViewById(R.id.TextViewMovieActors);
         mImageViewMoviePoster.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               mService.playVideoFileOnClient(mMovie.getFilename(), 0);
            }
         });

         mImageHandler = new ImageHandler(this);

         mLoadMovieTask = new LoadVideoDetailsTask(this);
         mLoadMovieTask.execute(mMovieId);

         // mPosterGallery.setSpacing(-10);
         // mPosterGallery.setAdapter(mAdapter);

         mLoadingDialog = ProgressDialog.show(getParent(),
               getString(R.string.media_movie_loadmoviedetails),
               getString(R.string.info_loading_title), true);
         mLoadingDialog.setCancelable(true);
      } else {// activity called without movie id (shouldn't happen ;))

      }
   }
}
