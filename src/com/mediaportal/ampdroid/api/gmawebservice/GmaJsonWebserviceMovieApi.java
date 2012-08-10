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
import com.mediaportal.ampdroid.data.Movie;
import com.mediaportal.ampdroid.data.MovieFull;
import com.mediaportal.ampdroid.utils.Constants;

class GmaJsonWebserviceMovieApi {
   private JsonClient mJsonClient;
   private ObjectMapper mJsonObjectMapper;

   private static final String GET_ALL_MOVIES = "GetAllMovies";
   private static final String GET_MOVIES_COUNT = "GetMovieCount";
   private static final String GET_MOVIES = "GetMovies";
   private static final String GET_MOVIE_DETAILS = "GetFullMovie";
   private static final String SEARCH_FOR_MOVIE = "SearchForMovie";

   public GmaJsonWebserviceMovieApi(JsonClient _jsonClient, ObjectMapper _mapper) {
      mJsonClient = _jsonClient;
      mJsonObjectMapper = _mapper;
   }

   @SuppressWarnings("rawtypes")
   private Object getObjectsFromJson(String _jsonString, Class _class) {
      return JsonUtils.getObjectsFromJson(_jsonString, _class, mJsonObjectMapper);
   }

   ArrayList<Movie> getAllMovies() {
      String methodName = GET_ALL_MOVIES;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("sort", 0),
            JsonUtils.newPair("order", 0));

      if (response != null) {
         Movie[] returnObject = (Movie[]) getObjectsFromJson(response, Movie[].class);

         if (returnObject != null) {
            return new ArrayList<Movie>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   int getMovieCount() {
      String methodName = GET_MOVIES_COUNT;
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

   MovieFull getMovieDetails(int _movieId) {
      String methodName = GET_MOVIE_DETAILS;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("movieId", _movieId));

      if (response != null) {
         MovieFull returnObject = (MovieFull) getObjectsFromJson(response, MovieFull.class);

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

   ArrayList<Movie> getMovies(int _start, int _end) {
      String methodName = GET_MOVIES;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("startIndex", _start),
            JsonUtils.newPair("endIndex", _end), JsonUtils.newPair("sort", 0),
            JsonUtils.newPair("order", 0));

      if (response != null) {
         Movie[] returnObject = (Movie[]) getObjectsFromJson(response, Movie[].class);

         if (returnObject != null) {
            return new ArrayList<Movie>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   ArrayList<Movie> searchForMovie(String _searchString) {
      String methodName = SEARCH_FOR_MOVIE;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("searchString", _searchString));

      if (response != null) {
         Movie[] returnObject = (Movie[]) getObjectsFromJson(response, Movie[].class);

         if (returnObject != null) {
            return new ArrayList<Movie>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }
}
