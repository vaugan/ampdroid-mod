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
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import android.util.Log;

import com.mediaportal.ampdroid.api.JsonClient;
import com.mediaportal.ampdroid.api.JsonUtils;
import com.mediaportal.ampdroid.data.MusicAlbum;
import com.mediaportal.ampdroid.data.MusicArtist;
import com.mediaportal.ampdroid.data.MusicTrack;
import com.mediaportal.ampdroid.data.VideoShare;
import com.mediaportal.ampdroid.utils.Constants;

class GmaJsonWebserviceMusicApi {
   private JsonClient mJsonClient;
   private ObjectMapper mJsonObjectMapper;

   private static final String GET_MUSIC_TRACK = "GetMusicTrack";
   private static final String GET_ALL_ALBUMS = "GetAllAlbums";
   private static final String GET_ALBUMS = "GetAlbums";
   private static final String GET_ALBUMS_COUNT = "GetAlbumsCount";
   private static final String GET_ALL_ARTISTS = "GetAllArtists";
   private static final String GET_ARTISTS = "GetArtists";
   private static final String GET_ALBUM = "GetAlbum";
   private static final String GET_ARTISTS_COUNT = "GetArtistsCount";
   private static final String GET_ALBUMS_BY_ARTIST = "GetAlbumsByArtist";
   private static final String GET_SONGS_OF_ALBUM = "GetSongsOfAlbum";
   private static final String FIND_MUSIC_TRACKS = "FindMusicTracks";
   private static final String GET_MUSIC_TRACKS_COUNT = "GetMusicTracksCount";
   private static final String GET_ALL_MUSIC_TRACKS = "GetAllMusicTracks";
   private static final String GET_MUSIC_TRACKS = "GetMusicTracks";
   private static final String GET_MUSIC_SHARES = "GetAllMusicShares";

   public GmaJsonWebserviceMusicApi(JsonClient _jsonClient, ObjectMapper _mapper) {
      mJsonClient = _jsonClient;
      mJsonObjectMapper = _mapper;
   }

   @SuppressWarnings("rawtypes")
   private Object getObjectsFromJson(String _jsonString, Class _class) {
      return JsonUtils.getObjectsFromJson(_jsonString, _class, mJsonObjectMapper);
   }

   public List<VideoShare> getMusicShares() {
      String methodName = GET_MUSIC_SHARES;
      String response = mJsonClient.Execute(methodName);

      if (response != null) {
         VideoShare[] returnObject = (VideoShare[]) getObjectsFromJson(response, VideoShare[].class);

         if (returnObject != null) {
            return new ArrayList<VideoShare>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public MusicTrack getMusicTrack(int trackId) {
      String methodName = GET_MUSIC_TRACK;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("trackId", trackId));

      if (response != null) {
         MusicTrack returnObject = (MusicTrack) getObjectsFromJson(response, MusicTrack.class);

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

   public List<MusicTrack> getMusicTracks(int _start, int _end) {
      String methodName = GET_MUSIC_TRACKS;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("startIndex", _start),
            JsonUtils.newPair("endIndex", _end), JsonUtils.newPair("sort", 0),
            JsonUtils.newPair("order", 0));

      if (response != null) {
         MusicTrack[] returnObject = (MusicTrack[]) getObjectsFromJson(response, MusicTrack[].class);

         if (returnObject != null) {
            return new ArrayList<MusicTrack>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public List<MusicTrack> getAllMusicTracks() {
      String methodName = GET_ALL_MUSIC_TRACKS;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("sort", 0),
            JsonUtils.newPair("order", 0));

      if (response != null) {
         MusicTrack[] returnObject = (MusicTrack[]) getObjectsFromJson(response, MusicTrack[].class);

         if (returnObject != null) {
            return new ArrayList<MusicTrack>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public int getMusicTracksCount() {
      String methodName = GET_MUSIC_TRACKS_COUNT;
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

   public MusicAlbum getAlbum(String albumArtistName, String albumName) {
      String methodName = GET_ALBUM;
      String response = mJsonClient.Execute(methodName,
            JsonUtils.newPair("albumArtistName", albumArtistName),
            JsonUtils.newPair("albumName", albumName));

      if (response != null) {
         MusicAlbum returnObject = (MusicAlbum) getObjectsFromJson(response, MusicAlbum.class);

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

   public List<MusicAlbum> getAllAlbums() {
      String methodName = GET_ALL_ALBUMS;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("sort", 0),
            JsonUtils.newPair("order", 0));

      if (response != null) {
         MusicAlbum[] returnObject = (MusicAlbum[]) getObjectsFromJson(response, MusicAlbum[].class);

         if (returnObject != null) {
            return new ArrayList<MusicAlbum>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public List<MusicAlbum> getAlbums(int _start, int _end) {
      String methodName = GET_ALBUMS;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("startIndex", _start),
            JsonUtils.newPair("endIndex", _end), JsonUtils.newPair("sort", 0),
            JsonUtils.newPair("order", 0));

      if (response != null) {
         MusicAlbum[] returnObject = (MusicAlbum[]) getObjectsFromJson(response, MusicAlbum[].class);

         if (returnObject != null) {
            return new ArrayList<MusicAlbum>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public int getAlbumsCount() {
      String methodName = GET_ALBUMS_COUNT;
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

   public List<MusicArtist> getAllArtists() {
      String methodName = GET_ALL_ARTISTS;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("sort", 0),
            JsonUtils.newPair("order", 0));

      if (response != null) {
         MusicArtist[] returnObject = (MusicArtist[]) getObjectsFromJson(response,
               MusicArtist[].class);

         if (returnObject != null) {
            return new ArrayList<MusicArtist>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public List<MusicArtist> getArtists(int _start, int _end) {
      String methodName = GET_ARTISTS;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("startIndex", _start),
            JsonUtils.newPair("endIndex", _end), JsonUtils.newPair("sort", 0),
            JsonUtils.newPair("order", 0));

      if (response != null) {
         MusicArtist[] returnObject = (MusicArtist[]) getObjectsFromJson(response,
               MusicArtist[].class);

         if (returnObject != null) {
            return new ArrayList<MusicArtist>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public int getArtistsCount() {
      String methodName = GET_ARTISTS_COUNT;
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

   public List<MusicAlbum> getAlbumsByArtist(String artistName) {
      String methodName = GET_ALBUMS_BY_ARTIST;
      String response = mJsonClient.Execute(methodName,
            JsonUtils.newPair("artistName", artistName), JsonUtils.newPair("sort", 0),
            JsonUtils.newPair("order", 0));

      if (response != null) {
         MusicAlbum[] returnObject = (MusicAlbum[]) getObjectsFromJson(response, MusicAlbum[].class);

         if (returnObject != null) {
            return new ArrayList<MusicAlbum>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public List<MusicTrack> getSongsOfAlbum(String albumName, String albumArtistName) {
      String methodName = GET_SONGS_OF_ALBUM;

      if (albumArtistName != null) {
         albumArtistName = albumArtistName.trim();
         if (albumArtistName.startsWith("|")) {
            albumArtistName = albumArtistName.substring(1);
         }
         if (albumArtistName.endsWith("|")) {
            albumArtistName = albumArtistName.substring(0, albumArtistName.length() - 2);
         }
         albumArtistName = albumArtistName.trim();
      }

      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("albumName", albumName),
            JsonUtils.newPair("albumArtistName", albumArtistName), JsonUtils.newPair("sort", 0),
            JsonUtils.newPair("order", 0));

      if (response != null) {
         MusicTrack[] returnObject = (MusicTrack[]) getObjectsFromJson(response, MusicTrack[].class);

         if (returnObject != null) {
            return new ArrayList<MusicTrack>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   public List<MusicTrack> findMusicTracks(String album, String artist, String title) {
      String methodName = FIND_MUSIC_TRACKS;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("album", album),
            JsonUtils.newPair("artist", artist), JsonUtils.newPair("title", title),
            JsonUtils.newPair("sort", 0), JsonUtils.newPair("order", 0));

      if (response != null) {
         MusicTrack[] returnObject = (MusicTrack[]) getObjectsFromJson(response, MusicTrack[].class);

         if (returnObject != null) {
            return new ArrayList<MusicTrack>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }
}
