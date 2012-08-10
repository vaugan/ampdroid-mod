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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.deser.CustomDeserializerFactory;
import org.codehaus.jackson.map.deser.StdDeserializerProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.mediaportal.ampdroid.activities.WebServiceLoginException;
import com.mediaportal.ampdroid.api.CustomDateDeserializer;
import com.mediaportal.ampdroid.api.IMediaAccessApi;
import com.mediaportal.ampdroid.api.JsonClient;
import com.mediaportal.ampdroid.api.JsonUtils;
import com.mediaportal.ampdroid.data.FileInfo;
import com.mediaportal.ampdroid.data.MediaInfo;
import com.mediaportal.ampdroid.data.Movie;
import com.mediaportal.ampdroid.data.MovieFull;
import com.mediaportal.ampdroid.data.MusicAlbum;
import com.mediaportal.ampdroid.data.MusicArtist;
import com.mediaportal.ampdroid.data.MusicTrack;
import com.mediaportal.ampdroid.data.Series;
import com.mediaportal.ampdroid.data.SeriesEpisode;
import com.mediaportal.ampdroid.data.SeriesEpisodeDetails;
import com.mediaportal.ampdroid.data.SeriesFull;
import com.mediaportal.ampdroid.data.SeriesSeason;
import com.mediaportal.ampdroid.data.StreamProfile;
import com.mediaportal.ampdroid.data.StreamTranscodingInfo;
import com.mediaportal.ampdroid.data.VideoShare;
import com.mediaportal.ampdroid.data.WebServiceDescription;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.utils.Constants;

public class GmaJsonWebserviceApi implements IMediaAccessApi {
   private GmaJsonWebserviceMovieApi mMoviesAPI;
   private GmaJsonWebserviceSeriesApi mSeriesAPI;
   private GmaJsonWebserviceVideoApi mVideosAPI;
   private GmaJsonWebserviceMusicApi mMusicAPI;

   private String mServer;
   private int mPort;

   private String mUser;
   private String mPass;
   private boolean mUseAuth;

   private static final String GET_WEBSERVICE_DESC = "GetServiceDescription";
   private static final String GET_VIDEO_SHARES = "GetVideoShares";
   private static final String GET_IMAGE = "GetImage";
   private static final String GET_MEDIA_ITEM = "GetMediaItem";
   private static final String GET_IMAGE_RESIZED = "GetImageResized";
   private static final String GET_DIRECTORY_LIST_BY_PATH = "GetDirectoryListByPath";
   private static final String GET_FILE_INFO = "GetFileInfo";
   private static final String GET_STREAM_INFO = "GetMediaInfo";
   private static final String GET_FILES_FROM_DIRECTORY = "GetFilesFromDirectory";

   private static final String INIT_STREAMING = "InitStream";
   private static final String START_STREAMING = "StartStream";
   private static final String RETRIEVE_STREAMING = "RetrieveStream";
   private static final String STOP_STREAMING = "FinishStream";
   private static final String GET_TRANSCODING_INFO = "GetTranscodingInfo";
   private static final String GET_PROFILES = "GetTranscoderProfilesForTarget";
   private static final String ANDROID_TARGET_NAME = "android";

   private static final String JSON_PREFIX = "http://";
   private static final String JSON_SUFFIX = "/GmaWebService/MediaAccessService/json";
   private static final String STREAM_SUFFIX = "/GmaWebService/MediaAccessService/stream";

   private JsonClient mJsonClient;
   private ObjectMapper mJsonObjectMapper;
   private String mMac;

   @SuppressWarnings("unchecked")
   public GmaJsonWebserviceApi(String _server, int _port, String _mac, String _user, String _pass,
         boolean _auth) {
      mServer = _server;
      mPort = _port;
      mMac = _mac;

      mUser = _user;
      mPass = _pass;
      mUseAuth = _auth;

      mJsonClient = new JsonClient(JSON_PREFIX + mServer + ":" + mPort + JSON_SUFFIX, _user, _pass,
            _auth);
      mJsonObjectMapper = new ObjectMapper();
      mJsonObjectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

      CustomDeserializerFactory sf = new CustomDeserializerFactory();
      mJsonObjectMapper.setDeserializerProvider(new StdDeserializerProvider(sf));
      sf.addSpecificMapping(Date.class, new CustomDateDeserializer());

      mMoviesAPI = new GmaJsonWebserviceMovieApi(mJsonClient, mJsonObjectMapper);
      mSeriesAPI = new GmaJsonWebserviceSeriesApi(mJsonClient, mJsonObjectMapper);
      mVideosAPI = new GmaJsonWebserviceVideoApi(mJsonClient, mJsonObjectMapper);
      mMusicAPI = new GmaJsonWebserviceMusicApi(mJsonClient, mJsonObjectMapper);
      // m_musicAPI = new GmaWebserviceMusicApi(m_wcfService,
      // mJsonObjectMapper);
   }

   public GmaJsonWebserviceApi(String _server, int _port) {
      this(_server, _port, "", "", "", false);
   }

   @Override
   public String getServer() {
      return mServer;
   }

   @Override
   public int getPort() {
      return mPort;
   }

   @Override
   public String getUserName() {
      return mUser;
   }

   @Override
   public String getUserPass() {
      return mPass;
   }

   @Override
   public boolean getUseAuth() {
      return mUseAuth;
   }

   @Override
   public String getMac() {
      return mMac;
   }

   @SuppressWarnings({ "rawtypes", "unchecked" })
   private Object getObjectsFromJson(String _jsonString, Class _class) {
      try {
         Object returnObjects = mJsonObjectMapper.readValue(_jsonString, _class);

         return returnObjects;
      } catch (JsonParseException e) {
         Log.e(Constants.LOG_CONST, e.toString());
      } catch (JsonMappingException e) {
         Log.e(Constants.LOG_CONST, e.toString());
      } catch (IOException e) {
         Log.e(Constants.LOG_CONST, e.toString());
      }
      return null;
   }

   @Override
   public String getAddress() {
      return mServer;
   }

   public WebServiceDescription getServiceDescription() throws WebServiceLoginException{
      String methodName = GET_WEBSERVICE_DESC;
      String response = mJsonClient.Execute(methodName);
      if(mJsonClient.getResponseCode() == 401){
         //unauthorised
         throw new WebServiceLoginException(mUseAuth, mUser, mPass);
      }

      Log.i(Constants.LOG_CONST, "Getting GmaWebservice functions: " + mServer + ":" + mPort + "@"
            + mUser + ":" + mPass);
      if (response != null) {
         WebServiceDescription returnObject = (WebServiceDescription) getObjectsFromJson(response,
               WebServiceDescription.class);

         if (returnObject != null) {
            Log.i(Constants.LOG_CONST, "Successfully connected to GmaWebservice");
            return returnObject;
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   @Override
   public List<VideoShare> getVideoShares() {
      String methodName = GET_VIDEO_SHARES;
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

   @Override
   public List<FileInfo> getFilesForFolder(String _path) {
      String methodName = GET_FILES_FROM_DIRECTORY;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("filepath", _path));

      if (response != null) {
         FileInfo[] returnObject = (FileInfo[]) getObjectsFromJson(response, FileInfo[].class);

         if (returnObject != null) {
            return new ArrayList<FileInfo>(Arrays.asList(returnObject));
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   @Override
   public FileInfo getFileInfo(String _itemId, DownloadItemType _itemType) {
      String methodName = GET_FILE_INFO;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("itemId", _itemId),
            JsonUtils.newPair("type", DownloadItemType.toInt(_itemType)));

      if (response != null) {
         FileInfo returnObject = (FileInfo) getObjectsFromJson(response, FileInfo.class);

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

   @Override
   public MediaInfo getMediaInfo(String _itemId, DownloadItemType _itemType) {
      String methodName = GET_STREAM_INFO;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("itemId", _itemId),
            JsonUtils.newPair("type", DownloadItemType.toInt(_itemType)));

      if (response != null) {
         MediaInfo returnObject = (MediaInfo) getObjectsFromJson(response, MediaInfo.class);

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

   @Override
   public List<FileInfo> getFoldersForFolder(String _path) {
      String methodName = GET_DIRECTORY_LIST_BY_PATH;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("path", _path));

      if (response != null) {
         String[] returnObject = (String[]) getObjectsFromJson(response, String[].class);

         if (returnObject != null) {
            List<FileInfo> retList = new ArrayList<FileInfo>();

            for (String f : returnObject) {
               retList.add(new FileInfo(f, true));
            }

            return retList;
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   @Override
   public List<Movie> getAllMovies() {
      return mMoviesAPI.getAllMovies();
   }

   @Override
   public int getMovieCount() {
      return mMoviesAPI.getMovieCount();
   }

   @Override
   public MovieFull getMovieDetails(int _movieId) {
      return mMoviesAPI.getMovieDetails(_movieId);
   }

   @Override
   public List<Movie> getMovies(int _start, int _end) {
      return mMoviesAPI.getMovies(_start, _end);
   }

   @Override
   public List<Movie> getAllVideos() {
      return mVideosAPI.getAllMovies();
   }

   @Override
   public int getVideosCount() {
      return mVideosAPI.getMovieCount();
   }

   @Override
   public MovieFull getVideoDetails(int _movieId) {
      return mVideosAPI.getMovieDetails(_movieId);
   }

   @Override
   public List<Movie> getVideos(int _start, int _end) {
      return mVideosAPI.getMovies(_start, _end);
   }

   @Override
   public List<Series> getAllSeries() {
      return mSeriesAPI.getAllSeries();
   }

   @Override
   public List<Series> getSeries(int _start, int _end) {
      return mSeriesAPI.getSeries(_start, _end);
   }

   @Override
   public SeriesFull getFullSeries(int _seriesId) {
      return mSeriesAPI.getFullSeries(_seriesId);
   }

   @Override
   public int getSeriesCount() {
      return mSeriesAPI.getSeriesCount();
   }

   @Override
   public List<SeriesSeason> getAllSeasons(int _seriesId) {
      return mSeriesAPI.getAllSeasons(_seriesId);
   }

   @Override
   public List<SeriesEpisode> getAllEpisodes(int _seriesId) {
      return mSeriesAPI.getAllEpisodes(_seriesId);
   }

   @Override
   public List<SeriesEpisode> getAllEpisodesForSeason(int _seriesId, int _seasonNumber) {
      return mSeriesAPI.getAllEpisodesForSeason(_seriesId, _seasonNumber);
   }

   @Override
   public List<SeriesEpisode> getEpisodesForSeason(int _seriesId, int _seasonNumber, int _begin,
         int _end) {
      return mSeriesAPI.getEpisodesForSeason(_seriesId, _seasonNumber, _begin, _end);
   }

   @Override
   public int getEpisodesCountForSeason(int _seriesId, int _seasonNumber) {
      return mSeriesAPI.getEpisodesCountForSeason(_seriesId, _seasonNumber);
   }

   @Override
   public SeriesEpisodeDetails getEpisode(int _seriesId, int _episodeId) {
      return mSeriesAPI.getFullEpisode(_seriesId, _episodeId);
   }

   @Override
   public Bitmap getBitmap(String _url) {
      URL myFileUrl = null;
      Bitmap bmImg = null;
      try {
         myFileUrl = new URL(JSON_PREFIX + mServer + ":" + mPort + STREAM_SUFFIX + "/" + GET_IMAGE
               + "?path=" + URLEncoder.encode(_url, "UTF-8"));

         if (mUseAuth) {
            Authenticator.setDefault(new Authenticator() {
               protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(mUser, mPass.toCharArray());
               }
            });
         }

         HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
         conn.setDoInput(true);
         conn.connect();
         InputStream is = conn.getInputStream();

         bmImg = BitmapFactory.decodeStream(is);
      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }

      return bmImg;
   }

   @Override
   public Bitmap getBitmapFromMedia(DownloadItemType _itemType, String _itemId, int _position,
         int _maxWidth, int _maxHeight) {
      URL myFileUrl = null;
      Bitmap bmImg = null;
      try {
         myFileUrl = new URL(JSON_PREFIX + mServer + ":" + mPort + STREAM_SUFFIX + "/"
               + "ExtractImageResized" + "?type=" + DownloadItemType.toInt(_itemType) + "&itemId="
               + URLEncoder.encode(_itemId, "UTF-8") + "&position=" + _position + "&maxWidth="
               + _maxWidth + "&maxHeight=" + _maxHeight);

         if (mUseAuth) {
            Authenticator.setDefault(new Authenticator() {
               protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(mUser, mPass.toCharArray());
               }
            });
         }

         HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
         conn.setDoInput(true);
         conn.connect();
         InputStream is = conn.getInputStream();

         bmImg = BitmapFactory.decodeStream(is);
      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }

      return bmImg;
   }

   @Override
   public Bitmap getBitmap(String _url, int _maxWidth, int _maxHeight) {
      URL myFileUrl = null;
      Bitmap bmImg = null;
      try {
         myFileUrl = new URL(JSON_PREFIX + mServer + ":" + mPort + STREAM_SUFFIX + "/"
               + GET_IMAGE_RESIZED + "?path=" + URLEncoder.encode(_url, "UTF-8") + "&maxWidth="
               + _maxWidth + "&maxHeight=" + _maxHeight);

         if (mUseAuth) {
            Authenticator.setDefault(new Authenticator() {
               protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(mUser, mPass.toCharArray());
               }
            });
         }

         HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
         conn.setDoInput(true);
         conn.connect();
         InputStream is = conn.getInputStream();

         bmImg = BitmapFactory.decodeStream(is);
      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }

      return bmImg;
   }

   @Override
   public String getDownloadUri(String _itemId, DownloadItemType _itemType) {
      String fileUrl = null;
      try {
         fileUrl = JSON_PREFIX + mServer + ":" + mPort + STREAM_SUFFIX + "/" + GET_MEDIA_ITEM
               + "?type=" + DownloadItemType.toInt(_itemType) + "&itemId="
               + URLEncoder.encode(_itemId, "UTF-8");
      } catch (UnsupportedEncodingException e) {
         e.printStackTrace();
      }
      return fileUrl;
   }

   @Override
   public void initStreaming(String _id, String _client, DownloadItemType _itemType,
         String _itemId) {
      mJsonClient.Execute(INIT_STREAMING, 40000,
            JsonUtils.newPair("type", DownloadItemType.toInt(_itemType)),
            JsonUtils.newPair("itemId", _itemId),
            JsonUtils.newPair("clientDescription", _client), JsonUtils.newPair("identifier", _id));
   }

   @Override
   public String startStreaming(String _id, String _profile, long _position) {
      mJsonClient.Execute(START_STREAMING, 40000,
            JsonUtils.newPair("startPosition", String.valueOf(_position)),
            JsonUtils.newPair("profileName", _profile), JsonUtils.newPair("identifier", _id));

      String fileUrl = JSON_PREFIX + mServer + ":" + mPort + STREAM_SUFFIX + "/"
            + RETRIEVE_STREAMING + "?identifier=" + _id;
      return fileUrl;
   }

   @Override
   public StreamTranscodingInfo getTransocdingInfo(String _id) {
      String methodName = GET_TRANSCODING_INFO;
      String response = mJsonClient.Execute(methodName, JsonUtils.newPair("identifier", _id));

      if (response != null && !response.equals("")) {
         StreamTranscodingInfo returnObject = (StreamTranscodingInfo) getObjectsFromJson(response,
               StreamTranscodingInfo.class);

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

   @Override
   public List<StreamProfile> getTranscoderProfiles() {
      String methodName = GET_PROFILES;
      String response = mJsonClient.Execute(methodName,
            JsonUtils.newPair("target", ANDROID_TARGET_NAME));

      if (response != null) {
         StreamProfile[] returnObject = (StreamProfile[]) getObjectsFromJson(response,
               StreamProfile[].class);

         if (returnObject != null) {
            List<StreamProfile> retList = new ArrayList<StreamProfile>();

            for (StreamProfile p : returnObject) {
               retList.add(p);
            }

            return retList;
         } else {
            Log.e(Constants.LOG_CONST, "Error parsing result from JSON method " + methodName);
         }
      } else {
         Log.e(Constants.LOG_CONST, "Error retrieving data for method" + methodName);
      }
      return null;
   }

   @Override
   public void stopStreaming(String _id) {
      String methodName = STOP_STREAMING;
      mJsonClient.Execute(methodName, JsonUtils.newPair("identifier", _id));
   }

   @Override
   public List<VideoShare> getMusicShares() {
      return mMusicAPI.getMusicShares();

   }

   @Override
   public MusicTrack getMusicTrack(int trackId) {
      return mMusicAPI.getMusicTrack(trackId);
   }

   @Override
   public MusicAlbum getAlbum(String albumArtistName, String albumName) {
      return mMusicAPI.getAlbum(albumArtistName, albumName);
   }

   @Override
   public List<MusicAlbum> getAllAlbums() {
      return mMusicAPI.getAllAlbums();
   }

   @Override
   public List<MusicAlbum> getAlbums(int _start, int _end) {
      return mMusicAPI.getAlbums(_start, _end);
   }

   @Override
   public int getAlbumsCount() {
      return mMusicAPI.getAlbumsCount();
   }

   @Override
   public List<MusicArtist> getAllArtists() {
      return mMusicAPI.getAllArtists();
   }

   @Override
   public List<MusicArtist> getArtists(int _start, int _end) {
      return mMusicAPI.getArtists(_start, _end);
   }

   @Override
   public int getArtistsCount() {
      return mMusicAPI.getArtistsCount();
   }

   @Override
   public List<MusicAlbum> getAlbumsByArtist(String artistName) {
      return mMusicAPI.getAlbumsByArtist(artistName);
   }

   @Override
   public List<MusicTrack> getSongsOfAlbum(String albumName, String albumArtistName) {
      return mMusicAPI.getSongsOfAlbum(albumName, albumArtistName);
   }

   @Override
   public List<MusicTrack> findMusicTracks(String album, String artist, String title) {
      return mMusicAPI.findMusicTracks(album, artist, title);
   }

   @Override
   public int getMusicTracksCount() {
      return mMusicAPI.getMusicTracksCount();
   }

   @Override
   public List<MusicTrack> getMusicTracks(int _start, int _end) {
      return mMusicAPI.getMusicTracks(_start, _end);
   }

   @Override
   public List<MusicTrack> getAllMusicTracks() {
      return mMusicAPI.getAllMusicTracks();
   }

   @Override
   public List<MusicAlbum> getMusicAlbumsByArtist(String _artist) {
      return mMusicAPI.getAlbumsByArtist(_artist);
   }
}
