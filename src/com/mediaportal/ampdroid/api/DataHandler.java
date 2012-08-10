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
package com.mediaportal.ampdroid.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;

import com.mediaportal.ampdroid.activities.WebServiceLoginException;
import com.mediaportal.ampdroid.data.CacheItemsSetting;
import com.mediaportal.ampdroid.data.FileInfo;
import com.mediaportal.ampdroid.data.MediaInfo;
import com.mediaportal.ampdroid.data.Movie;
import com.mediaportal.ampdroid.data.MovieFull;
import com.mediaportal.ampdroid.data.MusicAlbum;
import com.mediaportal.ampdroid.data.MusicArtist;
import com.mediaportal.ampdroid.data.MusicTrack;
import com.mediaportal.ampdroid.data.RemoteClient;
import com.mediaportal.ampdroid.data.Series;
import com.mediaportal.ampdroid.data.SeriesEpisode;
import com.mediaportal.ampdroid.data.SeriesEpisodeDetails;
import com.mediaportal.ampdroid.data.SeriesFull;
import com.mediaportal.ampdroid.data.SeriesSeason;
import com.mediaportal.ampdroid.data.StreamProfile;
import com.mediaportal.ampdroid.data.StreamTranscodingInfo;
import com.mediaportal.ampdroid.data.TvCardDetails;
import com.mediaportal.ampdroid.data.TvChannel;
import com.mediaportal.ampdroid.data.TvChannelDetails;
import com.mediaportal.ampdroid.data.TvChannelGroup;
import com.mediaportal.ampdroid.data.TvProgram;
import com.mediaportal.ampdroid.data.TvProgramBase;
import com.mediaportal.ampdroid.data.TvRecording;
import com.mediaportal.ampdroid.data.TvSchedule;
import com.mediaportal.ampdroid.data.TvVirtualCard;
import com.mediaportal.ampdroid.data.VideoShare;
import com.mediaportal.ampdroid.data.WebServiceDescription;
import com.mediaportal.ampdroid.data.commands.RemoteKey;
import com.mediaportal.ampdroid.database.MediaDatabaseHandler;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;

public class DataHandler {
   private RemoteClient client;
   private RemoteFunctions functions;
   private static List<RemoteClient> clientList = new ArrayList<RemoteClient>();
   private static DataHandler dataHandler;
   private IMediaAccessDatabase mediaDatabase;

   private DataHandler(RemoteClient _client, Context _context) {
      client = _client;

      mediaDatabase = new MediaDatabaseHandler(_context, _client.getClientId());
   }

   public static boolean setupRemoteHandler(RemoteClient _client, Context _context,
         boolean _checkConnection) {
      dataHandler = new DataHandler(_client, _context);
      setFunctions(_client, false);

      return true;
   }

   private static void setFunctions(RemoteClient _client, boolean _checkConnection) {
      dataHandler.functions = new RemoteFunctions();

      ITvServiceApi tvApi = _client.getTvControlApi();
      if (tvApi != null) {
         dataHandler.functions.setTvEnabled(true);
         if (_checkConnection) {
            try {
               dataHandler.functions.setTvAvailable(tvApi.TestConnectionToTVService());
            } catch (WebServiceLoginException e) {
               e.printStackTrace();
            }
         }
      } else {
         dataHandler.functions.setTvEnabled(false);
      }

      IMediaAccessApi mediaApi = _client.getRemoteAccessApi();
      if (mediaApi != null) {
         dataHandler.functions.setMediaEnabled(true);
         if (_checkConnection) {
            // todo: test function for media access
            dataHandler.functions.setMediaAvailable(true);
         }
      } else {
         dataHandler.functions.setMediaEnabled(false);
      }

      IMediaAccessApi remoteApi = _client.getRemoteAccessApi();
      if (remoteApi != null) {
         dataHandler.functions.setRemoteEnabled(true);
         if (_checkConnection) {
            // todo: test function for remote access
            dataHandler.functions.setRemoteAvailable(true);
         }
      } else {
         dataHandler.functions.setRemoteEnabled(false);
      }
   }

   public static DataHandler getCurrentRemoteInstance() {
      return dataHandler;
   }

   public static void clearRemotClients() {
      clientList.clear();
   }

   public static void addRemoteClient(RemoteClient _client) {
      clientList.add(_client);
   }

   public static List<RemoteClient> getRemoteClients() {
      return clientList;
   }

   public static void updateRemoteClient(RemoteClient mClient) {
      for (int i = 0; i < clientList.size(); i++) {
         if (clientList.get(i).getClientId() == mClient.getClientId()) {
            clientList.set(i, mClient);
         }
      }
   }

   public static void setCurrentRemoteInstance(int _id) {
      for (RemoteClient c : clientList) {
         dataHandler.client = c;
         setFunctions(dataHandler.client, false);
      }
   }

   public List<VideoShare> getAllVideoShares() {
      return client.getRemoteAccessApi().getVideoShares();
   }

   public List<FileInfo> getFilesForFolder(String _path) {
      return client.getRemoteAccessApi().getFilesForFolder(_path);
   }

   public List<FileInfo> getFoldersForFolder(String _path) {
      return client.getRemoteAccessApi().getFoldersForFolder(_path);
   }

   public FileInfo getFileInfo(String _itemId, DownloadItemType _itemType) {
      return client.getRemoteAccessApi().getFileInfo(_itemId, _itemType);
   }

   public Date getMovieDatabaseLastUpdated() {
      return new Date();
   }

   public void forceMovieListUpdate() {

   }

   public void forceMovieDetailsUpdate(int _movieId) {

   }

   public List<Movie> getAllMovies() {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();

      mediaDatabase.open();
      List<Movie> movies = mediaDatabase.getAllMovies();

      if (movies == null || movies.size() == 0) {
         movies = remoteAccess.getAllMovies();
         if (movies != null) {
            for (Movie m : movies) {
               mediaDatabase.saveMovie(m);
            }
         }
      }

      mediaDatabase.close();
      return movies;
   }

   public int getMovieCount() {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();

      mediaDatabase.open();
      CacheItemsSetting setting = mediaDatabase.getMovieCount();

      if (setting == null) {
         int movieCount = remoteAccess.getMovieCount();
         setting = mediaDatabase.setMovieCount(movieCount);
      }

      mediaDatabase.close();
      if (setting != null) {
         return setting.getCacheCount();
      } else {
         return 0;
      }
   }

   public List<Movie> getMovies(int _start, int _end) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();

      mediaDatabase.open();
      List<Movie> movies = mediaDatabase.getMovies(_start, _end);

      if (movies == null || movies.size() == 0) {
         movies = remoteAccess.getMovies(_start, _end);
         if (movies != null) {
            for (Movie m : movies) {
               mediaDatabase.saveMovie(m);
            }
         }
      }

      mediaDatabase.close();
      return movies;
   }

   public MovieFull getMovieDetails(int _movieId) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();

      mediaDatabase.open();
      MovieFull movie = mediaDatabase.getMovieDetails(_movieId);

      if (movie == null) {
         movie = remoteAccess.getMovieDetails(_movieId);
         if (movie != null) {
            mediaDatabase.saveMovieDetails(movie);
         }
      }

      mediaDatabase.close();
      return movie;
   }

   public List<Movie> getAllVideos() {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();

      mediaDatabase.open();
      List<Movie> movies = mediaDatabase.getAllVideos();

      if (movies == null || movies.size() == 0) {
         movies = remoteAccess.getAllVideos();
         if (movies != null) {
            for (Movie m : movies) {
               mediaDatabase.saveVideo(m);
            }
         }
      }

      mediaDatabase.close();
      return movies;
   }

   public int getVideosCount() {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();

      mediaDatabase.open();
      CacheItemsSetting setting = mediaDatabase.getVideosCount();
      int movieCount = 0;
      if (setting == null) {
         movieCount = remoteAccess.getVideosCount();
         setting = mediaDatabase.setVideosCount(movieCount);
      }

      mediaDatabase.close();
      if (setting != null) {
         return setting.getCacheCount();
      } else {
         return movieCount;
      }
   }

   public List<Movie> getVideos(int _start, int _end) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();

      mediaDatabase.open();
      List<Movie> movies = mediaDatabase.getVideos(_start, _end);

      if (movies == null || movies.size() == 0) {
         movies = remoteAccess.getVideos(_start, _end);
         if (movies != null) {
            for (Movie m : movies) {
               mediaDatabase.saveVideo(m);
            }
         }
      }

      mediaDatabase.close();
      return movies;
   }

   public MovieFull getVideoDetails(int _movieId) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();

      mediaDatabase.open();
      MovieFull movie = mediaDatabase.getVideoDetails(_movieId);

      if (movie == null) {
         movie = remoteAccess.getVideoDetails(_movieId);
         if (movie != null) {
            mediaDatabase.saveVideoDetails(movie);
         }
      }

      mediaDatabase.close();
      return movie;
   }

   public Bitmap getBitmap(String _id) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getBitmap(_id);
   }

   public List<Series> getAllSeries() {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      mediaDatabase.open();
      List<Series> series = mediaDatabase.getAllSeries();

      if (series == null || series.size() == 0) {
         series = remoteAccess.getAllSeries();
         if (series != null) {
            for (Series s : series) {
               mediaDatabase.saveSeries(s);
            }
         }
      }

      mediaDatabase.close();
      return series;
   }

   public List<Series> getSeries(int _start, int _end) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      mediaDatabase.open();
      List<Series> series = mediaDatabase.getSeries(_start, _end);

      if (series == null || series.size() == 0) {
         series = remoteAccess.getSeries(_start, _end);
         if (series != null) {
            for (Series s : series) {
               mediaDatabase.saveSeries(s);
            }
         }
      }

      mediaDatabase.close();
      return series;
   }

   public SeriesFull getFullSeries(int _seriesId) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      mediaDatabase.open();
      SeriesFull series = mediaDatabase.getFullSeries(_seriesId);

      if (series == null) {
         series = remoteAccess.getFullSeries(_seriesId);
         if (series != null) {
            mediaDatabase.saveSeriesDetails(series);
         }
      }

      mediaDatabase.close();
      return series;
   }

   public int getSeriesCount() {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      mediaDatabase.open();
      CacheItemsSetting setting = mediaDatabase.getSeriesCount();

      if (setting == null) {
         int seriesCount = remoteAccess.getSeriesCount();
         setting = mediaDatabase.setSeriesCount(seriesCount);
      }

      mediaDatabase.close();
      if (setting != null) {
         return setting.getCacheCount();
      } else {
         return 0;
      }
   }

   public List<SeriesSeason> getAllSeasons(int _seriesId) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      mediaDatabase.open();
      List<SeriesSeason> seasons = mediaDatabase.getAllSeasons(_seriesId);

      if (seasons == null || seasons.size() == 0) {
         seasons = remoteAccess.getAllSeasons(_seriesId);
         if (seasons != null) {
            for (SeriesSeason s : seasons) {
               mediaDatabase.saveSeason(s);
            }
         }
      }

      mediaDatabase.close();
      return seasons;
   }

   public List<SeriesEpisode> getAllEpisodes(int _seriesId) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      mediaDatabase.open();
      List<SeriesEpisode> episodes = mediaDatabase.getAllEpisodes(_seriesId);

      if (episodes == null || episodes.size() == 0) {
         episodes = remoteAccess.getAllEpisodes(_seriesId);

         if (episodes != null) {
            for (SeriesEpisode e : episodes) {
               mediaDatabase.saveEpisode(_seriesId, e);
            }
         }
      }

      mediaDatabase.close();
      return episodes;
   }

   public List<SeriesEpisode> getAllEpisodesForSeason(int _seriesId, int _seasonNumber) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      mediaDatabase.open();
      List<SeriesEpisode> episodes = mediaDatabase
            .getAllEpisodesForSeason(_seriesId, _seasonNumber);

      if (episodes == null || episodes.size() == 0) {
         episodes = remoteAccess.getAllEpisodesForSeason(_seriesId, _seasonNumber);
         if (episodes != null) {
            for (SeriesEpisode e : episodes) {
               mediaDatabase.saveEpisode(_seriesId, e);
            }
         }
      }

      mediaDatabase.close();
      return episodes;
   }

   public int getEpisodesCountForSeason(int _seriesId, int _seasonNumber) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getEpisodesCountForSeason(_seriesId, _seasonNumber);
   }

   public List<SeriesEpisode> getEpisodesForSeason(int _seriesId, int _seasonNumber, int _begin,
         int _end) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getEpisodesForSeason(_seriesId, _seasonNumber, _begin, _end);
   }

   public SeriesEpisodeDetails getEpisode(int _seriesId, int _episodeId) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();

      mediaDatabase.open();
      SeriesEpisodeDetails episode = mediaDatabase.getEpisodeDetails(_seriesId, _episodeId);

      if (episode == null) {
         episode = remoteAccess.getEpisode(_seriesId, _episodeId);
         mediaDatabase.saveEpisodeDetails(_seriesId, episode);
      }

      mediaDatabase.close();
      return episode;
   }

   public WebServiceDescription getServiceDescription() throws WebServiceLoginException{
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      mediaDatabase.open();
      WebServiceDescription supported = mediaDatabase.getServiceDescription();

      if (supported == null) {
         supported = remoteAccess.getServiceDescription();
         if (supported != null) {
            mediaDatabase.setServiceDescription(supported);
         }
      }

      mediaDatabase.close();
      return supported;
   }

   public int getAlbumsCount() {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();

      int albumsCount = remoteAccess.getAlbumsCount();

      return albumsCount;
   }

   public List<VideoShare> getAllMusicShares() {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getMusicShares();
   }

   public List<MusicAlbum> getAllAlbums() {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getAllAlbums();
   }

   public List<MusicAlbum> getAlbums(int _start, int _end) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getAlbums(_start, _end);
   }

   public int getArtistsCount() {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();

      int albumsCount = remoteAccess.getArtistsCount();

      return albumsCount;
   }

   public List<MusicArtist> getArtists(int _start, int _end) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getArtists(_start, _end);
   }

   public int getMusicTracksCount() {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getMusicTracksCount();
   }

   public List<MusicTrack> getMusicTracks(int _start, int _end) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getMusicTracks(_start, _end);
   }

   public List<MusicTrack> getAllMusicTracks() {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getAllMusicTracks();
   }

   public List<MusicAlbum> getMusicAlbumsByArtist(String _artist) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getMusicAlbumsByArtist(_artist);
   }

   public List<MusicTrack> getSongsOfAlbum(String albumName, String albumArtistName) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getSongsOfAlbum(albumName, albumArtistName);
   }

   public Bitmap getImage(String _url) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getBitmap(_url);
   }

   public Bitmap getImage(String _url, int _maxWidth, int _maxHeight) {
      IMediaAccessApi remoteAccess = client.getRemoteAccessApi();
      return remoteAccess.getBitmap(_url, _maxWidth, _maxHeight);
   }

   public List<TvChannelGroup> getTvChannelGroups() {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.GetGroups();
   }

   public List<TvChannel> getTvChannelsForGroup(int _groupId) {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.GetChannels(_groupId);
   }
   
   public List<TvChannelDetails> getTvChannelDetailsForGroup(int _groupId) {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.GetChannelsDetails(_groupId);
   }

   public List<TvChannel> getTvChannelsForGroup(int _groupId, int _startIndex, int _endIndex) {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.GetChannels(_groupId, _startIndex, _endIndex);
   }

   public int getTvChannelsCount(int _groupId) {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.GetChannelsCount(_groupId);
   }

   public TvChannel getTvChannel(int _channelId) {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.GetChannelById(_channelId);
   }

   public List<TvCardDetails> getTvCards() {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.GetCards();
   }

   public List<TvVirtualCard> getTvCardsActive() {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.GetActiveCards();
   }

   public String startTimeshift(int _channelId, String _clientName) {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.SwitchTVServerToChannelAndGetStreamingUrl(_clientName, _channelId);
   }

   public boolean isTvServiceActive() throws WebServiceLoginException {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.TestConnectionToTVService();
   }

   public boolean stopTimeshift(String _user) {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.CancelCurrentTimeShifting(_user);
   }

   public List<TvRecording> getTvRecordings() {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.GetRecordings();
   }

   public List<TvSchedule> getTvSchedules() {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.GetSchedules();
   }

   public List<TvProgramBase> getTvBaseEpgForChannel(int _channelId, Date _begin, Date _end) {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.GetProgramsForChannel(_channelId, _begin, _end);
   }

   public void addTvSchedule(int _channelId, String _title, Date _startTime, Date _endTime) {
      ITvServiceApi tvApi = client.getTvControlApi();
      tvApi.AddSchedule(_channelId, _title, _startTime, _endTime, 0);
   }

   public void cancelTvScheduleByProgramId(int _programId) {
      ITvServiceApi tvApi = client.getTvControlApi();
      tvApi.cancelScheduleByProgramId(_programId);
   }

   public void cancelTvScheduleByScheduleId(int _scheduleId) {
      ITvServiceApi tvApi = client.getTvControlApi();
      tvApi.cancelScheduleByScheduleId(_scheduleId);
   }

   public TvProgram getTvEpgDetails(Integer _id) {
      ITvServiceApi tvApi = client.getTvControlApi();
      return tvApi.GetProgramById(_id);
   }

   public void setFunctions(RemoteFunctions functions) {
      this.functions = functions;
   }

   public RemoteFunctions getFunctions() {
      return functions;
   }

   public void addClientControlListener(IClientControlListener _listener) {
      client.getClientControlApi().addApiListener(_listener);
   }

   public void clearClientControlListener() {
      client.getClientControlApi().clearApiListener();
   }

   public void removeClientControlListener(IClientControlListener _listener) {
      client.getClientControlApi().removeApiListener(_listener);
   }

   public boolean connectClientControl() {
      return client.getClientControlApi().connect();
   }

   public void disconnectClientControl() {
      client.getClientControlApi().disconnect();
   }

   public boolean isClientControlConnected() {
      return client.getClientControlApi().isConnected();
   }

   public void sendRemoteButton(RemoteKey _button) {
      client.getClientControlApi().sendKeyCommand(_button);
   }

   public void sendClientVolume(int _level) {
      client.getClientControlApi().setVolume(_level);
   }

   public int getClientVolume() {
      return client.getClientControlApi().getVolume();
   }

   public String getDownloadUri(String _itemId, DownloadItemType _itemType) {
      return client.getRemoteAccessApi().getDownloadUri(_itemId, _itemType);
   }

   public ApiCredentials getDownloadCredentials() {
      ApiCredentials cred = new ApiCredentials();
      cred.setUseAut(client.getRemoteAccessApi().getUseAuth());
      cred.setUsername(client.getRemoteAccessApi().getUserName());
      cred.setPassword(client.getRemoteAccessApi().getUserPass());
      return cred;
   }

   public void playVideoFileOnClient(String _fileName, int _pos) {
      client.getClientControlApi().startVideo(_fileName, _pos);
   }

   public void playAudioFileOnClient(String _fileName, int _pos) {
      client.getClientControlApi().startAudio(_fileName, _pos);
   }

   public void sendRemoteButtonDown(RemoteKey _remoteKey, int _pause) {
      client.getClientControlApi().sendKeyDownCommand(_remoteKey, _pause);

   }

   public void sendRemoteButtonUp() {
      client.getClientControlApi().sendKeyUpCommand();
   }

   public void playTvChannelOnClient(int _channel, boolean _fullscreen) {
      client.getClientControlApi().playTvChannelOnClient(_channel, _fullscreen);
   }

   public void sendSetPowerModeCommand(PowerModes _mode) {
      client.getClientControlApi().sendPowerMode(_mode);
   }

   public void sendOpenWindowMessage(int _windowId, String _parameter) {
      client.getClientControlApi().openWindow(_windowId, _parameter);
   }

   public void requestPlugins() {
      client.getClientControlApi().requestPlugins();
   }

   public void sendClientPosition(int _position) {
      client.getClientControlApi().sendPosition(_position);
   }

   public void sendRemoteKey(int keyCode, int i) {
      client.getClientControlApi().sendRemoteKey(keyCode, i);
   }

   public void getClientImage(String filePath) {
      client.getClientControlApi().getClientImage(filePath);
   }

   public IApiInterface getRemoteAccessApi() {
      return client.getRemoteAccessApi();
   }

   public IApiInterface getRemoteControlApi() {
      return client.getClientControlApi();
   }

   public IApiInterface getTvApi() {
      return client.getTvControlApi();
   }
   
   public String startTvStreaming(String _id, long _position) {
      return client.getTvControlApi().startTvStreaming(_id, _position);
      }

   public boolean initTvStreaming(String _id, String _client, int _channel,
         String _profile) {
      return client.getTvControlApi().initTvStreaming(_id, _client, _channel, _profile);
         
   }
   
   public void stopTvStreaming(String _file) {
      client.getTvControlApi().stopTvStreaming(_file);
   }
   
   public List<StreamProfile> getTvTranscoderProfiles() {
      return client.getTvControlApi().getTvTranscoderProfiles();
   }

   public String startStreaming(String _id, String _profile, long _position) {
      return client.getRemoteAccessApi().startStreaming(_id, _profile, _position);
   }

   public void initStreaming(String _id, String _client, DownloadItemType _itemType,
         String _itemId) {
      client.getRemoteAccessApi().initStreaming(_id, _client, _itemType, _itemId);
   }

   public StreamTranscodingInfo getTransocdingInfo(String _id) {
      return client.getRemoteAccessApi().getTransocdingInfo(_id);
   }

   public void stopStreaming(String _file) {
      client.getRemoteAccessApi().stopStreaming(_file);
   }
   
   

   public MediaInfo getMediaInfo(String _itemId, DownloadItemType _itemType) {
      return client.getRemoteAccessApi().getMediaInfo(_itemId, _itemType);
   }

   public Bitmap getBitmapFromMedia(DownloadItemType _itemType, String _itemId, int _position, int _maxWidth, int _maxHeight) {
      return client.getRemoteAccessApi().getBitmapFromMedia(_itemType, _itemId, _position, _maxWidth, _maxHeight);
   }

   public List<StreamProfile> getTranscoderProfiles() {
      return client.getRemoteAccessApi().getTranscoderProfiles();
   }

   public void createPlaylist(List<MusicTrack> _tracks, boolean _autoPlay, int _startPos) {
      client.getClientControlApi().createPlaylistWithSongs(_tracks, _autoPlay, _startPos);
   }
   
   public void createPlaylistWithEpisodes(List<SeriesEpisode> episodes, boolean _autoPlay, int _startPos) {
      client.getClientControlApi().createPlaylistWithEpisodes(episodes, _autoPlay, _startPos);
   }

   public void requestPlaylist(String _type) {
      client.getClientControlApi().requestPlaylist(_type);
   }

   public void movePlaylistItem(String _type, int _oldIndex, int _newIndex) {
      client.getClientControlApi().movePlaylistItem(_type, _oldIndex, _newIndex);
   }

   public void playPlaylistItem(String _type, int _index) {
      client.getClientControlApi().playPlaylistItem(_type, _index);
   }

   public void clearPlaylistItems(String _type) {
      client.getClientControlApi().clearPlaylistItems(_type);
   }

   public void removePlaylistItem(String _type, int _index) {
      client.getClientControlApi().removePlaylistItem(_type, _index);
   }

   public boolean initRecordingStreaming(String _id, String _client, int _recordingId, String _profile, int _startPosition) {
      return client.getTvControlApi().initRecordingStreaming(_id, _client, _recordingId, _profile, _startPosition);
   }
   
   public String startRecordingStreaming(String _id)
   {
      return client.getTvControlApi().startRecordingStreaming(_id);
   }
   
   void stopRecordingStreaming(String _id){
      client.getTvControlApi().stopRecordingStreaming(_id);
   }

   public MediaInfo getRecordingMediaInfo(int _recordingId) {
      return client.getTvControlApi().getRecordingMediaInfo(_recordingId);
   }

   public Bitmap getTvLogoImage(String _channelId) {
      return client.getTvControlApi().getTvChannelLogo(_channelId);
   }
}
