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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mediaportal.ampdroid.api.IMediaAccessDatabase;
import com.mediaportal.ampdroid.data.CacheItemsSetting;
import com.mediaportal.ampdroid.data.SeriesEpisodeDetails;
import com.mediaportal.ampdroid.data.Movie;
import com.mediaportal.ampdroid.data.MovieFull;
import com.mediaportal.ampdroid.data.Series;
import com.mediaportal.ampdroid.data.SeriesEpisode;
import com.mediaportal.ampdroid.data.SeriesFull;
import com.mediaportal.ampdroid.data.SeriesSeason;
import com.mediaportal.ampdroid.data.WebServiceDescription;
import com.mediaportal.ampdroid.utils.Constants;

public class MediaDatabaseHandler implements IMediaAccessDatabase {
   private final String CLIENT_ID = "ClientId";
   Context mContext;
   private MediaDatabaseHelper mDbHelper;
   private SQLiteDatabase mDatabase;
   private int mClientId;
   private boolean mUseCaching = false;

   public int getClientId() {
      return mClientId;
   }

   public void setClientId(int clientId) {
      mClientId = clientId;
   }

   public MediaDatabaseHandler(Context _context, int _clientId) {
      mContext = _context;
      mDbHelper = new MediaDatabaseHelper(mContext, "ampdroid_media", null, 62);
      mClientId = _clientId;
   }

   @Override
   public void open() {
      try {
         mDatabase = mDbHelper.getWritableDatabase();
      } catch (Exception ex) {
         Log.e(Constants.LOG_CONST, ex.toString());
      }
   }

   @Override
   public void close() {
      mDatabase.close();
   }

   @Override
   public ArrayList<Movie> getAllMovies() {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(Movie.TABLE_NAME, null, CLIENT_ID + "=" + mClientId, null,
               null, null, null);
         ArrayList<Movie> movies = null;
         if (result.moveToFirst()) {
            movies = (ArrayList<Movie>) SqliteAnnotationsHelper.getObjectsFromCursor(result,
                  Movie.class, 0);
         }

         result.close();
         return movies;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public List<Movie> getMovies(int _start, int _end) {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(Movie.TABLE_NAME, null, CLIENT_ID + "=" + mClientId, null,
               null, null, null);
         ArrayList<Movie> movies = null;
         if (result.getCount() > _end) {
            result.move(_start + 1);
            movies = (ArrayList<Movie>) SqliteAnnotationsHelper.getObjectsFromCursor(result,
                  Movie.class, _end - _start + 1);
         }
         result.close();
         return movies;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public void saveMovie(Movie movie) {
      try {
         if(!mUseCaching) return;
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(movie,
               Movie.class);
         dbValues.put(CLIENT_ID, mClientId);

         int rows = mDatabase.update(Movie.TABLE_NAME, dbValues, CLIENT_ID + "=" + mClientId
               + " AND Id=" + movie.getId(), null);

         if (rows != 1) {
            mDatabase.insert("Movies", null, dbValues);
         }
         return;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
   }

   @Override
   public CacheItemsSetting getMovieCount() {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(CacheItemsSetting.TABLE_NAME, null, CLIENT_ID + "="
               + mClientId + " AND CacheType=" + Movie.CACHE_ID, null, null, null, null);

         result.moveToFirst();
         CacheItemsSetting setting = (CacheItemsSetting) SqliteAnnotationsHelper
               .getObjectFromCursor(result, CacheItemsSetting.class);
         result.close();
         return setting;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public CacheItemsSetting setMovieCount(int _movieCount) {
      try {
         CacheItemsSetting setting = new CacheItemsSetting(Movie.CACHE_ID, _movieCount, new Date());
         if(!mUseCaching) return setting;
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(setting,
               CacheItemsSetting.class);
         dbValues.put(CLIENT_ID, mClientId);

         int rows = mDatabase.update(CacheItemsSetting.TABLE_NAME, dbValues, CLIENT_ID + "="
               + mClientId + " AND CacheType=" + Movie.CACHE_ID, null);

         if (rows != 1) {
            mDatabase.insert(CacheItemsSetting.TABLE_NAME, null, dbValues);
         }
         return setting;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      return null;
   }

   @Override
   public MovieFull getMovieDetails(int _movieId) {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(MovieFull.TABLE_NAME, null, CLIENT_ID + "=" + mClientId
               + " AND Id=" + _movieId, null, null, null, null);
         MovieFull movie = null;
         if (result.getCount() == 1) {
            result.moveToFirst();
            movie = (MovieFull) SqliteAnnotationsHelper
                  .getObjectFromCursor(result, MovieFull.class);
         }

         result.close();
         return movie;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public void saveMovieDetails(MovieFull _movie) {
      try {
         if(!mUseCaching) return;
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(_movie,
               MovieFull.class);
         dbValues.put(CLIENT_ID, mClientId);

         int rows = mDatabase.update(MovieFull.TABLE_NAME, dbValues, CLIENT_ID + "=" + mClientId
               + " AND Id=" + _movie.getId(), null);

         if (rows != 1) {
            mDatabase.insert(MovieFull.TABLE_NAME, null, dbValues);
         }
         return;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
   }
   


   @Override
   public List<Movie> getAllVideos() {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(Movie.TABLE_NAME_VIDEOS, null, CLIENT_ID + "=" + mClientId, null,
               null, null, null);
         ArrayList<Movie> movies = null;
         if (result.moveToFirst()) {
            movies = (ArrayList<Movie>) SqliteAnnotationsHelper.getObjectsFromCursor(result,
                  Movie.class, 0);
         }

         result.close();
         return movies;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }
   
   @Override
   public List<Movie> getVideos(int _start, int _end) {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(Movie.TABLE_NAME_VIDEOS, null, CLIENT_ID + "=" + mClientId, null,
               null, null, null);
         ArrayList<Movie> movies = null;
         if (result.getCount() > _end) {
            result.move(_start + 1);
            movies = (ArrayList<Movie>) SqliteAnnotationsHelper.getObjectsFromCursor(result,
                  Movie.class, _end - _start + 1);
         }
         result.close();
         return movies;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public void saveVideo(Movie _video) {
      try {
         if(!mUseCaching) return;
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(_video,
               Movie.class);
         dbValues.put(CLIENT_ID, mClientId);

         int rows = mDatabase.update(Movie.TABLE_NAME_VIDEOS, dbValues, CLIENT_ID + "=" + mClientId
               + " AND Id=" + _video.getId(), null);

         if (rows != 1) {
            mDatabase.insert(Movie.TABLE_NAME_VIDEOS, null, dbValues);
         }
         return;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      
   }

   @Override
   public CacheItemsSetting getVideosCount() {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(CacheItemsSetting.TABLE_NAME, null, CLIENT_ID + "="
               + mClientId + " AND CacheType=" + CacheItemsSetting.CACHE_ID_VIDEOS, null, null, null, null);

         result.moveToFirst();
         CacheItemsSetting setting = (CacheItemsSetting) SqliteAnnotationsHelper
               .getObjectFromCursor(result, CacheItemsSetting.class);
         result.close();
         return setting;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public CacheItemsSetting setVideosCount(int _videosCount) {
      try {
         CacheItemsSetting setting = new CacheItemsSetting(CacheItemsSetting.CACHE_ID_VIDEOS, _videosCount, new Date());
         if(!mUseCaching) return setting;
         
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(setting,
               CacheItemsSetting.class);
         dbValues.put(CLIENT_ID, mClientId);

         int rows = mDatabase.update(CacheItemsSetting.TABLE_NAME, dbValues, CLIENT_ID + "="
               + mClientId + " AND CacheType=" + CacheItemsSetting.CACHE_ID_VIDEOS, null);

         if (rows != 1) {
            mDatabase.insert(CacheItemsSetting.TABLE_NAME, null, dbValues);
         }
         return setting;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      return null;
   }

   @Override
   public MovieFull getVideoDetails(int _videoId) {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(MovieFull.TABLE_NAME_VIDEOS, null, CLIENT_ID + "=" + mClientId
               + " AND Id=" + _videoId, null, null, null, null);
         MovieFull movie = null;
         if (result.getCount() == 1) {
            result.moveToFirst();
            movie = (MovieFull) SqliteAnnotationsHelper
                  .getObjectFromCursor(result, MovieFull.class);
         }

         result.close();
         return movie;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public void saveVideoDetails(MovieFull _video) {
      try {
         if(!mUseCaching) return;
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(_video,
               MovieFull.class);
         dbValues.put(CLIENT_ID, mClientId);

         int rows = mDatabase.update(MovieFull.TABLE_NAME_VIDEOS, dbValues, CLIENT_ID + "=" + mClientId
               + " AND Id=" + _video.getId(), null);

         if (rows != 1) {
            mDatabase.insert(MovieFull.TABLE_NAME_VIDEOS, null, dbValues);
         }
         return;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      } 
   }

   @Override
   public List<Series> getSeries(int _start, int _end) {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(Series.TABLE_NAME, null, CLIENT_ID + "=" + mClientId, null, null,
               null, null);
         ArrayList<Series> series = null;
         if (result.getCount() > _end) {
            result.move(_start + 1);
            series = (ArrayList<Series>) SqliteAnnotationsHelper.getObjectsFromCursor(result,
                  Series.class, _end - _start + 1);
         }

         result.close();
         return series;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public void saveSeries(Series _series) {
      try {
         if(!mUseCaching) return;
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(_series,
               Series.class);
         dbValues.put(CLIENT_ID, mClientId);

         int rows = mDatabase.update(Series.TABLE_NAME, dbValues, CLIENT_ID + "=" + mClientId + " AND Id="
               + _series.getId(), null);

         if (rows != 1) {
            mDatabase.insert(Series.TABLE_NAME, null, dbValues);
         }
         return;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
   }

   @Override
   public List<Series> getAllSeries() {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(Series.TABLE_NAME, null, CLIENT_ID + "=" + mClientId, null, null,
               null, null);

         result.moveToFirst();
         ArrayList<Series> series = (ArrayList<Series>) SqliteAnnotationsHelper
               .getObjectsFromCursor(result, Series.class, 0);

         result.close();
         return series;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public CacheItemsSetting getSeriesCount() {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(CacheItemsSetting.TABLE_NAME, null, CLIENT_ID + "="
               + mClientId + " AND CacheType=" + Series.CACHE_ID, null, null, null, null);

         result.moveToFirst();
         CacheItemsSetting setting = (CacheItemsSetting) SqliteAnnotationsHelper
               .getObjectFromCursor(result, CacheItemsSetting.class);
         result.close();
         return setting;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public CacheItemsSetting setSeriesCount(int _seriesCount) {
      try {
         CacheItemsSetting setting = new CacheItemsSetting(Series.CACHE_ID, _seriesCount,
               new Date());
         if(!mUseCaching) return setting;
         
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(setting,
               CacheItemsSetting.class);
         dbValues.put(CLIENT_ID, mClientId);

         int rows = mDatabase.update(CacheItemsSetting.TABLE_NAME, dbValues, "CacheType="
               + Series.CACHE_ID, null);

         if (rows != 1) {
            mDatabase.insert(CacheItemsSetting.TABLE_NAME, null, dbValues);
         }
         return setting;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      return null;
   }

   @Override
   public SeriesFull getFullSeries(int _seriesId) {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(SeriesFull.TABLE_NAME, null, CLIENT_ID + "=" + mClientId
               + " AND Id=" + _seriesId, null, null, null, null);
         SeriesFull series = null;
         if (result.getCount() == 1) {
            result.moveToFirst();
            series = (SeriesFull) SqliteAnnotationsHelper.getObjectFromCursor(result,
                  SeriesFull.class);
         }
         result.close();
         return series;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public void saveSeriesDetails(SeriesFull series) {
      try {
         if(!mUseCaching) return;
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(series,
               SeriesFull.class);
         dbValues.put(CLIENT_ID, mClientId);

         int rows = mDatabase.update(SeriesFull.TABLE_NAME, dbValues, CLIENT_ID + "=" + mClientId
               + " AND Id=" + series.getId(), null);

         if (rows != 1) {
            mDatabase.insert(SeriesFull.TABLE_NAME, null, dbValues);
         }
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
   }

   @Override
   public List<SeriesSeason> getAllSeasons(int _seriesId) {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(SeriesSeason.TABLE_NAME, null, CLIENT_ID + "=" + mClientId
               + " AND SeriesId=" + _seriesId, null, null, null, null);

         result.moveToFirst();
         ArrayList<SeriesSeason> series = (ArrayList<SeriesSeason>) SqliteAnnotationsHelper
               .getObjectsFromCursor(result, SeriesSeason.class, 0);

         result.close();
         return series;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public void saveSeason(SeriesSeason _season) {
      try {
         if(!mUseCaching) return;
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(_season,
               SeriesSeason.class);
         dbValues.put(CLIENT_ID, mClientId);

         int rows = mDatabase.update(SeriesSeason.TABLE_NAME, dbValues, CLIENT_ID + "=" + mClientId
               + " AND Id=\"" + _season.getId() + "\"", null);

         if (rows != 1) {
            mDatabase.insert(SeriesSeason.TABLE_NAME, null, dbValues);
         }
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
   }

   @Override
   public List<SeriesEpisode> getAllEpisodes(int _seriesId) {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(SeriesEpisode.TABLE_NAME, null, CLIENT_ID + "=" + mClientId
               + " AND SeriesId=" + _seriesId, null, null, null, null);

         result.moveToFirst();
         ArrayList<SeriesEpisode> series = (ArrayList<SeriesEpisode>) SqliteAnnotationsHelper
               .getObjectsFromCursor(result, SeriesEpisode.class, 0);

         result.close();
         return series;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public void saveEpisode(int _seriesId, SeriesEpisode _episode) {
      try {
         if(!mUseCaching) return;
         _episode.setSeriesId(_seriesId);
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(_episode,
               SeriesEpisode.class);
         dbValues.put(CLIENT_ID, mClientId);

         int rows = mDatabase.update(SeriesEpisode.TABLE_NAME, dbValues, CLIENT_ID + "=" + mClientId
               + " AND SeriesId=" + _seriesId + " AND Id=" + _episode.getId(), null);

         if (rows != 1) {
            mDatabase.insert(SeriesEpisode.TABLE_NAME, null, dbValues);
         }
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
   }

   @Override
   public List<SeriesEpisode> getAllEpisodesForSeason(int _seriesId, int _seasonNumber) {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(SeriesEpisode.TABLE_NAME, null, CLIENT_ID + "=" + mClientId
               + " AND SeasonNumber=" + _seasonNumber + " AND SeriesId=" + _seriesId, null, null,
               null, null);

         result.moveToFirst();
         ArrayList<SeriesEpisode> series = (ArrayList<SeriesEpisode>) SqliteAnnotationsHelper
               .getObjectsFromCursor(result, SeriesEpisode.class, 0);

         result.close();
         return series;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public WebServiceDescription getServiceDescription() {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(WebServiceDescription.TABLE_NAME, null, CLIENT_ID + "=" + mClientId,
               null, null, null, null);

         WebServiceDescription retObject = null;
         if (result.getCount() == 1) {
            result.moveToFirst();
            retObject = (WebServiceDescription) SqliteAnnotationsHelper.getObjectFromCursor(result,
                  WebServiceDescription.class);
         }

         result.close();
         return retObject;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public void setServiceDescription(WebServiceDescription _supported) {
      try {
         if(!mUseCaching) return ;
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(_supported,
               WebServiceDescription.class);
         dbValues.put(CLIENT_ID, mClientId);

         int rows = mDatabase.update(WebServiceDescription.TABLE_NAME, dbValues, CLIENT_ID + "=" + mClientId,
               null);

         if (rows != 1) {
            mDatabase.insert(WebServiceDescription.TABLE_NAME, null, dbValues);
         }
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }

   }

   @Override
   public SeriesEpisodeDetails getEpisodeDetails(int _seriesId, int _episodeId) {
      try {
         if(!mUseCaching) return null;
         Cursor result = mDatabase.query(SeriesEpisodeDetails.TABLE_NAME, null, CLIENT_ID + "=" + mClientId
               + " AND SeriesId=" + _seriesId + " AND Id=" + _episodeId, null, null, null, null);
         SeriesEpisodeDetails episode = null;
         if (result.getCount() == 1) {
            result.moveToFirst();
            episode = (SeriesEpisodeDetails) SqliteAnnotationsHelper.getObjectFromCursor(result,
                  SeriesEpisodeDetails.class);
         }
         result.close();
         return episode;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   @Override
   public void saveEpisodeDetails(int _seriesId, SeriesEpisodeDetails _episode) {
      try {
         if(!mUseCaching) return;
         _episode.setSeriesId(_seriesId);
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(_episode,
               SeriesEpisodeDetails.class);
         dbValues.put(CLIENT_ID, mClientId);

         int rows = mDatabase.update(SeriesEpisodeDetails.TABLE_NAME, dbValues, CLIENT_ID + "=" + mClientId
               + " AND SeriesId=" + _seriesId + " AND Id=" + _episode.getId(), null);

         if (rows != 1) {
            mDatabase.insert(SeriesEpisodeDetails.TABLE_NAME, null, dbValues);
         }
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
   }


}
