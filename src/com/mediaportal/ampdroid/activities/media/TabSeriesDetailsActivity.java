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
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.BaseTabActivity;
import com.mediaportal.ampdroid.api.ApiCredentials;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.controls.ListViewItemOnTouchListener;
import com.mediaportal.ampdroid.data.FileInfo;
import com.mediaportal.ampdroid.data.Movie;
import com.mediaportal.ampdroid.data.SeriesEpisode;
import com.mediaportal.ampdroid.data.SeriesFull;
import com.mediaportal.ampdroid.data.SeriesSeason;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.downloadservice.DownloadJob;
import com.mediaportal.ampdroid.downloadservice.ItemDownloaderHelper;
import com.mediaportal.ampdroid.downloadservice.MediaItemType;
import com.mediaportal.ampdroid.lists.ImageHandler;
import com.mediaportal.ampdroid.lists.LazyLoadingGalleryAdapter;
import com.mediaportal.ampdroid.lists.LazyLoadingImage;
import com.mediaportal.ampdroid.lists.Utils;
import com.mediaportal.ampdroid.quickactions.ActionItem;
import com.mediaportal.ampdroid.quickactions.QuickAction;
import com.mediaportal.ampdroid.utils.DateTimeHelper;
import com.mediaportal.ampdroid.utils.DownloaderUtils;
import com.mediaportal.ampdroid.utils.QuickActionUtils;
import com.mediaportal.ampdroid.utils.Util;

public class TabSeriesDetailsActivity extends Activity {
   private LazyLoadingGalleryAdapter mAdapter;
   private LinearLayout mSeasonLayout;
   private ImageView mSeriesPoster;
   private TextView mTextViewSeriesName;
   private TextView mTextViewSeriesOverview;
   private TextView mTextViewSeriesReleaseDate;
   private TextView mTextViewSeriesRuntime;
   private TextView mTextViewSeriesCertification;
   private TextView mTextViewSeriesActors;
   private RatingBar mSeriesRating;

   private Gallery mPosterGallery;
   private int mSeriesId;
   private SeriesFull mSeries;
   private ImageHandler mImageHandler;
   private LoadSeriesDetailsTask mLoadSeriesTask;
   private LoadSeasonsDetailsTask mLoadSeasonTask;
   private DownloadSeasonTask mSeasonDownloaderTask;
   private PlaySeasonTask mSeasonPlayTask;
   private DataHandler mService;
   private ProgressDialog mLoadingDialog;
   private BaseTabActivity mBaseActivity;
   private String mSeriesName;

   private class DownloadSeasonTask extends AsyncTask<SeriesSeason, Intent, Boolean> {
      private Context mContext;

      private DownloadSeasonTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected Boolean doInBackground(SeriesSeason... _params) {
         SeriesSeason season = _params[0];
         int groupId = new Random().nextInt();

         int epCount = season.getEpisodesCount();
         for (int i = 0; i < epCount; i++) {
            List<SeriesEpisode> episodes = mService.getEpisodesForSeason(mSeriesId,
                  season.getSeasonNumber(), i, i + 1);
            SeriesEpisode ep = episodes.get(0);
            String epFile = ep.getFileName();

            String url = mService.getDownloadUri(String.valueOf(ep.getId()),
                  DownloadItemType.TvSeriesItem);
            FileInfo info = mService.getFileInfo(String.valueOf(ep.getId()),
                  DownloadItemType.TvSeriesItem);
            String dirName = DownloaderUtils.getTvEpisodePath(mSeries.getPrettyName(), ep);
            final String fileName = dirName + Utils.getFileNameWithExtension(epFile, "\\");

            ApiCredentials cred = mService.getDownloadCredentials();
            if (url != null) {
               DownloadJob job = new DownloadJob(groupId);
               job.setUrl(url);
               job.setFileName(fileName);
               job.setDisplayName(mSeriesName + ": " + ep.toString());
               job.setMediaType(MediaItemType.Video);
               job.setGroupName(mSeriesName + ", "
                     + mContext.getString(R.string.media_series_season) + " "
                     + season.getSeasonNumber() + " (" + (i + 1) + "/" + epCount + ")");
               job.setGroupPart(i);
               job.setGroupSize(epCount);
               if (info != null) {
                  job.setLength(info.getLength());
               }
               if (cred.useAut()) {
                  job.setAuth(cred.getUsername(), cred.getPassword());
               }

               Intent download = ItemDownloaderHelper.createDownloadIntent(mContext, job);
               publishProgress(download);
            }
         }

         return true;
      }

      @Override
      protected void onProgressUpdate(Intent... values) {
         Intent donwloadIntent = values[0];
         if (donwloadIntent != null) {
            startService(donwloadIntent);
         }
         super.onProgressUpdate(values);
      }

      @Override
      protected void onPostExecute(Boolean _result) {

      }
   }

   private class PlaySeasonTask extends AsyncTask<SeriesSeason, Intent, Boolean> {
      private Context mContext;

      private PlaySeasonTask(Context _context) {
         mContext = _context;
      }

      @Override
      protected Boolean doInBackground(SeriesSeason... _params) {
         SeriesSeason season = _params[0];
         List<SeriesEpisode> episodes = mService.getAllEpisodesForSeason(mSeriesId,
               season.getSeasonNumber());

         mService.createPlaylistWithEpisodes(episodes, true, 0);
         return true;
      }

      @Override
      protected void onProgressUpdate(Intent... values) {
         super.onProgressUpdate(values);
      }

      @Override
      protected void onPostExecute(Boolean _result) {
         mSeasonPlayTask = null;
      }
   }

   private class LoadSeriesDetailsTask extends AsyncTask<Integer, List<Movie>, SeriesFull> {
      Activity mContext;

      private LoadSeriesDetailsTask(Activity _context) {
         mContext = _context;
      }

      @Override
      protected SeriesFull doInBackground(Integer... _params) {
         mSeries = mService.getFullSeries(mSeriesId);

         return mSeries;
      }

      @Override
      protected void onPostExecute(SeriesFull _result) {
         if (_result != null) {
            String seriesPoster = _result.getCurrentPosterUrl();
            if (seriesPoster != null && !seriesPoster.equals("")) {
               String fileName = Utils
                     .getFileNameWithExtension(_result.getCurrentPosterUrl(), "\\");
               String cacheName = "Series" + File.separator + mSeriesId + File.separator
                     + "LargePoster" + File.separator + fileName;

               LazyLoadingImage image = new LazyLoadingImage(seriesPoster, cacheName, 150, 200);
               mSeriesPoster.setTag(seriesPoster);
               mImageHandler.DisplayImage(image, R.drawable.listview_imageloading_poster, mContext,
                     mSeriesPoster);

            }

            Date firstAired = _result.getFirstAired();

            if (firstAired != null) {
               String date = DateTimeHelper.getDateString(firstAired, false);
               mTextViewSeriesReleaseDate.setText(date);
            }

            int runtime = _result.getRuntime();
            if (runtime != 0) {
               mTextViewSeriesRuntime.setText(String.valueOf(runtime));
            } else {
               mTextViewSeriesRuntime.setText("-");
            }

            String[] actors = _result.getActors();
            if (actors != null) {
               String actorsString = "";
               for (String a : actors) {
                  actorsString += " - " + a + "\n";
               }
               mTextViewSeriesActors.setText(actorsString);
            } else {
               mTextViewSeriesActors.setText("-");
            }

            int rating = (int) _result.getRating();
            mSeriesRating.setRating(rating);

            mTextViewSeriesOverview.setText(_result.getSummary());
            if (mBaseActivity.getIsActive()) {
               mLoadingDialog.cancel();
            }
         } else {
            if (mBaseActivity.getIsActive()) {
               mLoadingDialog.cancel();
               Dialog diag = new Dialog(getParent());
               diag.setTitle(getString(R.string.media_series_loadingerror));
               diag.setCancelable(true);

               diag.show();
               diag.setOnDismissListener(new OnDismissListener() {
                  @Override
                  public void onDismiss(DialogInterface dialog) {

                  }
               });
            }
         }

         mLoadSeasonTask = new LoadSeasonsDetailsTask(mContext);
         mLoadSeasonTask.execute(mSeriesId);
      }
   }

   private class LoadSeasonsDetailsTask extends AsyncTask<Integer, List<Movie>, List<SeriesSeason>> {
      Activity mContext;

      private LoadSeasonsDetailsTask(Activity _context) {
         mContext = _context;
      }

      @Override
      protected List<SeriesSeason> doInBackground(Integer... _params) {
         List<SeriesSeason> seasons = mService.getAllSeasons(mSeriesId);

         return seasons;
      }

      @Override
      protected void onPostExecute(List<SeriesSeason> _result) {
         if (_result != null) {
            for (int i = 0; i < _result.size(); i++) {
               SeriesSeason s = _result.get(i);
               if (s.getEpisodesCount() > 0) {
                  View view = Button.inflate(mContext, R.layout.listitem_poster, null);
                  TextView text = (TextView) view.findViewById(R.id.TextViewTitle);
                  ImageView image = (ImageView) view.findViewById(R.id.ImageViewEventImage);
                  TextView subtext = (TextView) view.findViewById(R.id.TextViewText);

                  String seasonBanner = s.getSeasonBanner();
                  if (seasonBanner != null && !seasonBanner.equals("")) {
                     String fileName = Utils.getFileNameWithExtension(seasonBanner, "\\");
                     String cacheName = "Series" + File.separator + mSeriesId + File.separator
                           + "LargePoster" + File.separator + fileName;

                     LazyLoadingImage bannerImage = new LazyLoadingImage(seasonBanner, cacheName,
                           75, 100);
                     image.setTag(seasonBanner);
                     mImageHandler.DisplayImage(bannerImage,
                           R.drawable.listview_imageloading_poster, mContext, image);
                  }

                  view.setOnTouchListener(new ListViewItemOnTouchListener());
                  view.setOnClickListener(new OnClickListener() {
                     @Override
                     public void onClick(View _view) {
                        SeriesSeason s = (SeriesSeason) _view.getTag();

                        openDetails(s);
                     }
                  });

                  view.setOnLongClickListener(new OnLongClickListener() {
                     @Override
                     public boolean onLongClick(View _view) {
                        try {
                           final SeriesSeason s = (SeriesSeason) _view.getTag();

                           if (s != null) {
                              final QuickAction qa = new QuickAction(_view);

                              ActionItem sdCardAction = new ActionItem();
                              sdCardAction.setTitle(getString(R.string.quickactions_downloadsd));
                              sdCardAction.setIcon(getResources().getDrawable(
                                    R.drawable.quickaction_download));
                              sdCardAction.setOnClickListener(new OnClickListener() {
                                 @Override
                                 public void onClick(final View _view) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(
                                          mBaseActivity);
                                    builder
                                          .setTitle(getString(R.string.media_series_loadmultiplewarning_title));
                                    builder
                                          .setMessage(getString(R.string.media_series_loadmultiplewarning_text_begin)
                                                + s.getEpisodesCount()
                                                + getString(R.string.media_series_loadmultiplewarning_text_end));
                                    builder.setCancelable(false);
                                    builder.setPositiveButton(getString(R.string.dialog_yes),
                                          new DialogInterface.OnClickListener() {
                                             public void onClick(DialogInterface dialog, int id) {
                                                mSeasonDownloaderTask = new DownloadSeasonTask(
                                                      _view.getContext());
                                                mSeasonDownloaderTask.execute(s);
                                             }
                                          });

                                    builder.setNegativeButton(getString(R.string.dialog_no),
                                          new DialogInterface.OnClickListener() {
                                             public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                             }
                                          });
                                    AlertDialog alert = builder.create();
                                    alert.show();

                                    qa.dismiss();
                                 }
                              });
                              qa.addActionItem(sdCardAction);

                              QuickActionUtils.createPlayOnClientQuickAction(_view.getContext(),
                                    qa, mService, new OnClickListener() {
                                       @Override
                                       public void onClick(View _view) {
                                          mSeasonPlayTask = new PlaySeasonTask(_view.getContext());
                                          mSeasonPlayTask.execute(s);

                                          Util.showToast(_view.getContext(),
                                                getString(R.string.info_not_implemented));
                                          // mService.playFileOnClient(epFile);

                                          qa.dismiss();
                                       }
                                    });

                              QuickActionUtils.createDetailsQuickAction(_view.getContext(), qa,
                                    new View.OnClickListener() {
                                       @Override
                                       public void onClick(View arg0) {
                                          openDetails(s);
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

                  text.setText(getString(R.string.media_series_season) + " " + s.getSeasonNumber());
                  subtext.setText(s.getEpisodesCount() + " " + getString(R.string.media_episodes));
                  view.setTag(s);

                  mSeasonLayout.addView(view);
               }
            }
         }
      }
   }

   private void openDetails(SeriesSeason s) {
      Intent myIntent = new Intent(this, TabEpisodesActivity.class);
      myIntent.putExtra("series_id", s.getSeriesId());
      myIntent.putExtra("season_number", s.getSeasonNumber());
      myIntent.putExtra("series_name", mSeries.getPrettyName());

      myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

      // Create the view using FirstGroup's
      // LocalActivityManager
      View view = TabSeriesActivityGroup.getGroup().getLocalActivityManager()
            .startActivity("season_episodes", myIntent).getDecorView();

      // Again, replace the view
      TabSeriesActivityGroup.getGroup().replaceView(view);
   }

   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_tabseriesdetails);
      mSeasonLayout = (LinearLayout) findViewById(R.id.LinearLayoutSeasons);
      mSeriesPoster = (ImageView) findViewById(R.id.ImageViewSeriesPoster);
      mTextViewSeriesName = (TextView) findViewById(R.id.TextViewSeriesName);
      mTextViewSeriesOverview = (TextView) findViewById(R.id.TextViewOverview);
      mPosterGallery = (Gallery) findViewById(R.id.GalleryAllMoviePosters);
      mTextViewSeriesReleaseDate = (TextView) findViewById(R.id.TextViewSeriesRelease);
      mTextViewSeriesRuntime = (TextView) findViewById(R.id.TextViewSeriesRuntime);
      mTextViewSeriesCertification = (TextView) findViewById(R.id.TextViewSeriesCertification);
      mTextViewSeriesActors = (TextView) findViewById(R.id.TextViewSeriesActors);
      mSeriesRating = (RatingBar) findViewById(R.id.RatingBarSeriesRating);
      mSeriesRating.setNumStars(10);

      mBaseActivity = (BaseTabActivity) getParent().getParent();

      Bundle extras = getIntent().getExtras();
      if (extras != null) {
         mSeriesId = extras.getInt("series_id");
         mSeriesName = extras.getString("series_name");
         mTextViewSeriesName.setText(mSeriesName);

         mService = DataHandler.getCurrentRemoteInstance();
         mImageHandler = new ImageHandler(this);

         mLoadSeriesTask = new LoadSeriesDetailsTask(this);
         mLoadSeriesTask.execute(mSeriesId);

         // mPosterGallery.setSpacing(-10);
         // mPosterGallery.setAdapter(mAdapter);

         mLoadingDialog = ProgressDialog.show(getParent(),
               getString(R.string.media_series_loadseriesdetails),
               getString(R.string.info_loading_title), true);
         mLoadingDialog.setCancelable(true);
      } else {// activity called without movie id (shouldn't happen ;))

      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu _menu) {
      super.onCreateOptionsMenu(_menu);
      SubMenu viewItem = _menu.addSubMenu(0, Menu.FIRST + 1, Menu.NONE,
            getString(R.string.media_views));

      if (mSeries != null) {
         String[] actors = mSeries.getActors();
         if (actors != null) {
            for (String a : actors) {
               MenuItem imdbActorsLookupMenu = viewItem.add(0, Menu.FIRST + 3, Menu.NONE,
                     getString(R.string.media_views_thumbs));

               imdbActorsLookupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem item) {

                     return true;
                  }
               });
            }
         }
      }

      MenuItem imdbLookupMenu = _menu.add(0, Menu.FIRST + 1, Menu.NONE, "imdb");
      // getString(R.string.menu_set_default_view));
      imdbLookupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            String imdb = mSeries.getImdbId();
            Intent i = new Intent();
            i.setData(Uri.parse("imdb:///title/" + imdb + "/"));
            i.setAction("android.intent.action.VIEW");
            startActivity(i);

            return true;
         }
      });

      return true;
   }

   @Override
   public void onDestroy() {
      // adapter.imageLoader.stopThread();
      // m_listView.setAdapter(null);
      super.onDestroy();
   }
}
