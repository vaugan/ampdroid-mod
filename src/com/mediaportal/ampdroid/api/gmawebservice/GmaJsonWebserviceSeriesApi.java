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
package com.mediaportal.ampdroid.api.gmawebservice;

import java.util.ArrayList;
import java.util.Arrays;

import org.codehaus.jackson.map.ObjectMapper;

import android.util.Log;

import com.mediaportal.ampdroid.api.JsonClient;
import com.mediaportal.ampdroid.api.JsonUtils;
import com.mediaportal.ampdroid.data.SeriesEpisodeDetails;
import com.mediaportal.ampdroid.data.Series;
import com.mediaportal.ampdroid.data.SeriesEpisode;
import com.mediaportal.ampdroid.data.SeriesFull;
import com.mediaportal.ampdroid.data.SeriesSeason;
import com.mediaportal.ampdroid.utils.Constants;

public class GmaJsonWebserviceSeriesApi {

   private JsonClient mJsonClient;
   private ObjectMapper mJsonObjectMapper;

   private static final String GET_ALL_SERIES = "GetAllSeries";
   private static final String GET_SERIES = "GetSeries";
   private static final String GET_SERIES_COUNT = "GetSeriesCount";
   private static final String GET_FULL_SERIES = "GetFullSeries";
   private static final String GET_ALL_SEASONS = "GetAllSeasons";
   private static final String GET_SEASON = "GetSeason";
   private static final String GET_ALL_EPISODES = "GetAllEpisodes";
   private static final String GET_EPISODES = "GetEpisodes";
   private static final String GET_EPISODES_COUNT = "GetEpisodesCount";
   private static final String GET_EPISODES_COUNT_FOR_SEASON = "GetEpisodesCountForSeason";
   private static final String GET_ALL_EPISODES_FOR_SEASON = "GetAllEpisodesForSeason";
   private static final String GET_EPISODES_FOR_SEASON = "GetEpisodesForSeason";
   private static final String GET_FULL_EPISODE = "GetFullEpisode";

   public GmaJsonWebserviceSeriesApi(JsonClient _wcfService, ObjectMapper _mapper) {
      mJsonClient = _wcfService;
      mJsonObjectMapper = _mapper;
   }

   @SuppressWarnings("rawtypes")
   private Object getObjectsFromJson(String _jsonString, Class _class) {
      return JsonUtils.getObjectsFromJson(_jsonString, _class, mJsonObjectMapper);
   }

   public ArrayList<Series> getAllSeries() {
      String methodName = GET_ALL_SERIES;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("sort", 0),
            JsonUtils.newPair("order", 0));

      if (response != null) {
         Series[] returnObject = (Series[]) getObjectsFromJson(response, Series[].class);

         if (returnObject != null) {
            return new ArrayList<Series>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public ArrayList<Series> getSeries(int _start, int _end) {
      String methodName = GET_SERIES;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("startIndex", _start),
            JsonUtils.newPair("endIndex", _end), JsonUtils.newPair("sort", 0),
            JsonUtils.newPair("order", 0));

      if (response != null) {
         Series[] returnObject = (Series[]) getObjectsFromJson(response, Series[].class);

         if (returnObject != null) {
            return new ArrayList<Series>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public SeriesFull getFullSeries(int _seriesId) {
      String methodName = GET_FULL_SERIES;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("seriesId", _seriesId));

      if (response != null) {
         SeriesFull returnObject = (SeriesFull) getObjectsFromJson(response, SeriesFull.class);

         if (returnObject != null) {
            return returnObject;
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public int getSeriesCount() {
      String methodName = GET_SERIES_COUNT;
      String response = mJsonClient.Execute(methodName);

      if (response != null) {
         Integer returnObject = (Integer) getObjectsFromJson(response, Integer.class);

         if (returnObject != null) {
            return returnObject;
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return -99;
   }

   public ArrayList<SeriesSeason> getAllSeasons(int _seriesId) {
      String methodName = GET_ALL_SEASONS;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("seriesId", _seriesId),
            JsonUtils.newPair("sort", 0), JsonUtils.newPair("order", 0));

      if (response != null) {
         SeriesSeason[] returnObject = (SeriesSeason[]) getObjectsFromJson(response,
               SeriesSeason[].class);

         if (returnObject != null) {
            return new ArrayList<SeriesSeason>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public SeriesSeason getSeason(int _seriesId, int _seasonNumber) {
      String methodName = GET_SEASON;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("seriesId", _seriesId),
            JsonUtils.newPair("seasonNumber", _seasonNumber));

      if (response != null) {
         SeriesSeason returnObject = (SeriesSeason) getObjectsFromJson(response, SeriesSeason.class);

         if (returnObject != null) {
            return returnObject;
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public ArrayList<SeriesEpisode> getAllEpisodes(int _seriesId) {
      String methodName = GET_ALL_EPISODES;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("seriesId", _seriesId),
            JsonUtils.newPair("sort", GmaSortOptions.EpisodeNumber), JsonUtils.newPair("order", 0));

      if (response != null) {
         SeriesEpisode[] returnObject = (SeriesEpisode[]) getObjectsFromJson(response,
               SeriesEpisode[].class);

         if (returnObject != null) {
            return new ArrayList<SeriesEpisode>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public ArrayList<SeriesEpisode> getEpisodes(int _seriesId, int _start, int _end) {
      String methodName = GET_EPISODES;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("seriesId", _seriesId),
            JsonUtils.newPair("startIndex", _start), JsonUtils.newPair("endIndex", _end),
            JsonUtils.newPair("sort", GmaSortOptions.EpisodeNumber), JsonUtils.newPair("order", 0));

      if (response != null) {
         SeriesEpisode[] returnObject = (SeriesEpisode[]) getObjectsFromJson(response,
               SeriesEpisode[].class);

         if (returnObject != null) {
            return new ArrayList<SeriesEpisode>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public int getEpisodesCount() {
      String methodName = GET_EPISODES_COUNT;
      String response = mJsonClient.Execute(methodName);

      if (response != null) {
         Integer returnObject = (Integer) getObjectsFromJson(response, Integer.class);

         if (returnObject != null) {
            return returnObject;
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return -99;
   }

   public ArrayList<SeriesEpisode> getAllEpisodesForSeason(int _seriesId, int _seasonNumber) {
      String methodName = GET_ALL_EPISODES_FOR_SEASON;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("seriesId", _seriesId),
            JsonUtils.newPair("seasonNumber", _seasonNumber),
            JsonUtils.newPair("sort", GmaSortOptions.EpisodeNumber), JsonUtils.newPair("order", 0));

      if (response != null) {
         SeriesEpisode[] returnObject = (SeriesEpisode[]) getObjectsFromJson(response,
               SeriesEpisode[].class);

         if (returnObject != null) {
            return new ArrayList<SeriesEpisode>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public int getEpisodesCountForSeason(int _seriesId, int _seasonNumber) {
      String methodName = GET_EPISODES_COUNT_FOR_SEASON;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("seriesId", _seriesId),
            JsonUtils.newPair("season", _seasonNumber));

      if (response != null) {
         Integer returnObject = (Integer) getObjectsFromJson(response, Integer.class);

         if (returnObject != null) {
            return returnObject;
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return -99;
   }

   public ArrayList<SeriesEpisode> getEpisodesForSeason(int _seriesId, int _seasonId, int _start,
         int _end) {
      String methodName = GET_EPISODES_FOR_SEASON;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("seriesId", _seriesId),
            JsonUtils.newPair("seasonId", _seasonId), JsonUtils.newPair("startIndex", _start),
            JsonUtils.newPair("endIndex", _end), JsonUtils.newPair("sort", GmaSortOptions.EpisodeNumber),
            JsonUtils.newPair("order", 0));

      if (response != null) {
         SeriesEpisode[] returnObject = (SeriesEpisode[]) getObjectsFromJson(response,
               SeriesEpisode[].class);

         if (returnObject != null) {
            return new ArrayList<SeriesEpisode>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public SeriesEpisodeDetails getFullEpisode(int _seriesId, int _episodeId) {
      String methodName = GET_FULL_EPISODE;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("seriesId", _seriesId),
            JsonUtils.newPair("episodeId", _episodeId));

      if (response != null) {
         SeriesEpisodeDetails returnObject = (SeriesEpisodeDetails) getObjectsFromJson(response,
               SeriesEpisodeDetails.class);

         if (returnObject != null) {
            return returnObject;
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }
}
