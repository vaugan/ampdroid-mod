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
package com.mediaportal.ampdroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.mediaportal.ampdroid.data.CacheItemsSetting;
import com.mediaportal.ampdroid.data.SeriesEpisodeDetails;
import com.mediaportal.ampdroid.data.Movie;
import com.mediaportal.ampdroid.data.MovieFull;
import com.mediaportal.ampdroid.data.Series;
import com.mediaportal.ampdroid.data.SeriesEpisode;
import com.mediaportal.ampdroid.data.SeriesFull;
import com.mediaportal.ampdroid.data.SeriesSeason;
import com.mediaportal.ampdroid.data.WebServiceDescription;
import com.mediaportal.ampdroid.downloadservice.DownloadJob;

public class MediaDatabaseHelper extends SQLiteOpenHelper {
   private String mSeriesTableString;
   private String mMovieTableString;
   private String mMovieDetailsTableString;
   private String mSeriesDetailsTableString;
   private String mSeriesSeasonTableString;
   private String mSeriesEpisodeTableString;
   private String mSeriesEpisodeDetailsTableString;
   private String mCacheSettingsString;
   private String mSupportedFunctionsString;
   private String mVideosTableString;
   private String mVideoDetailsTableString;
   
   public MediaDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
      super(context, name, factory, version);
      mSeriesTableString = SqliteAnnotationsHelper
            .getCreateTableStringFromClass(Series.TABLE_NAME, Series.class, true);
      mSeriesDetailsTableString = SqliteAnnotationsHelper.getCreateTableStringFromClass(SeriesFull.TABLE_NAME, 
            SeriesFull.class, true);
      mSeriesSeasonTableString = SqliteAnnotationsHelper.getCreateTableStringFromClass(SeriesSeason.TABLE_NAME, 
            SeriesSeason.class, true);
      mSeriesEpisodeTableString = SqliteAnnotationsHelper.getCreateTableStringFromClass(SeriesEpisode.TABLE_NAME, 
            SeriesEpisode.class, true);
      mSeriesEpisodeDetailsTableString = SqliteAnnotationsHelper.getCreateTableStringFromClass(SeriesEpisodeDetails.TABLE_NAME, 
            SeriesEpisodeDetails.class, true);

      mMovieTableString = SqliteAnnotationsHelper.getCreateTableStringFromClass(Movie.TABLE_NAME, Movie.class, true);
      mMovieDetailsTableString = SqliteAnnotationsHelper.getCreateTableStringFromClass(MovieFull.TABLE_NAME,
            MovieFull.class, true);
      
      mVideosTableString = SqliteAnnotationsHelper.getCreateTableStringFromClass(Movie.TABLE_NAME_VIDEOS, Movie.class, true);
      mVideoDetailsTableString = SqliteAnnotationsHelper.getCreateTableStringFromClass(MovieFull.TABLE_NAME_VIDEOS, 
            MovieFull.class, true);
      
      mCacheSettingsString = SqliteAnnotationsHelper.getCreateTableStringFromClass(CacheItemsSetting.TABLE_NAME, 
            CacheItemsSetting.class, true);
      
      mSupportedFunctionsString = SqliteAnnotationsHelper.getCreateTableStringFromClass(WebServiceDescription.TABLE_NAME, 
            WebServiceDescription.class, true);
      
   }

   @Override
   public void onCreate(SQLiteDatabase _db) {
      _db.execSQL(mSeriesTableString);
      _db.execSQL(mSeriesDetailsTableString);
      _db.execSQL(mSeriesSeasonTableString);
      _db.execSQL(mSeriesEpisodeTableString);
      _db.execSQL(mSeriesEpisodeDetailsTableString);
      
      _db.execSQL(mMovieTableString);
      _db.execSQL(mMovieDetailsTableString);
      
      _db.execSQL(mVideosTableString);
      _db.execSQL(mVideoDetailsTableString);
      
      _db.execSQL(mCacheSettingsString);
      _db.execSQL(mSupportedFunctionsString);
   }

   @Override
   public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
      _db.execSQL("DROP TABLE IF EXISTS " + "Series");
      _db.execSQL("DROP TABLE IF EXISTS " + "SeriesDetails");
      _db.execSQL("DROP TABLE IF EXISTS " + "SeriesSeason");
      _db.execSQL("DROP TABLE IF EXISTS " + "SeriesSeasons");
      _db.execSQL("DROP TABLE IF EXISTS " + "SeriesEpisodes");
      _db.execSQL("DROP TABLE IF EXISTS " + "SeriesEpisodeDetails");
      _db.execSQL("DROP TABLE IF EXISTS " + "SeriesDetails");
      
      _db.execSQL("DROP TABLE IF EXISTS " + "Movies");
      _db.execSQL("DROP TABLE IF EXISTS " + "Movie");
      _db.execSQL("DROP TABLE IF EXISTS " + "MovieDetails");
      
      _db.execSQL("DROP TABLE IF EXISTS " + "Videos");
      _db.execSQL("DROP TABLE IF EXISTS " + "VideosDetails");
      _db.execSQL("DROP TABLE IF EXISTS " + "VideoDetails");
      
      _db.execSQL("DROP TABLE IF EXISTS " + "CacheSettings");
      _db.execSQL("DROP TABLE IF EXISTS " + "SupportedFunctions");
      
      onCreate(_db);

   }
}
