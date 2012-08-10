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

import java.util.List;

import android.graphics.Bitmap;

import com.mediaportal.ampdroid.activities.WebServiceLoginException;
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

public interface IMediaAccessApi extends IApiInterface {
   List<Movie> getAllMovies();

   int getMovieCount();

   List<Movie> getMovies(int _start, int _end);

   MovieFull getMovieDetails(int _movieId);

   Bitmap getBitmap(String _id);

   Bitmap getBitmap(String _url, int _maxWidth, int _maxHeight);

   List<Series> getAllSeries();

   List<Series> getSeries(int _start, int _end);

   SeriesFull getFullSeries(int _seriesId);

   int getSeriesCount();

   List<SeriesSeason> getAllSeasons(int seriesId);

   List<SeriesEpisode> getAllEpisodes(int seriesId);

   List<SeriesEpisode> getAllEpisodesForSeason(int seriesId, int seasonNumber);
   
   List<SeriesEpisode> getEpisodesForSeason(int _seriesId, int _seasonNumber, int _begin,
         int _end);
   
   int getEpisodesCountForSeason(int _seriesId, int _seasonNumber);

   WebServiceDescription getServiceDescription() throws WebServiceLoginException;

   String getDownloadUri(String _itemId, DownloadItemType _itemType);

   List<VideoShare> getVideoShares();

   SeriesEpisodeDetails getEpisode(int _seriesId, int _episodeId);

   List<FileInfo> getFilesForFolder(String _path);

   List<FileInfo> getFoldersForFolder(String _path);

   List<Movie> getAllVideos();

   int getVideosCount();

   MovieFull getVideoDetails(int _movieId);

   List<Movie> getVideos(int _start, int _end);

   FileInfo getFileInfo(String _itemId, DownloadItemType _itemType);

   int getAlbumsCount();
   
   List<MusicAlbum> getAllAlbums();

   List<MusicAlbum> getAlbums(int _start, int _end);
   
   MusicTrack getMusicTrack(int trackId);

   MusicAlbum getAlbum(String albumArtistName, String albumName);

   int getArtistsCount();

   List<MusicArtist> getAllArtists();

   List<MusicArtist> getArtists(int _start, int _end);

   List<MusicAlbum> getAlbumsByArtist(String artistName);

   List<MusicTrack> getSongsOfAlbum(String albumName, String albumArtistName);

   List<MusicTrack> findMusicTracks(String album, String artist, String title);

   int getMusicTracksCount();

   List<MusicTrack> getMusicTracks(int _start, int _end);

   List<MusicTrack> getAllMusicTracks();

   List<VideoShare> getMusicShares();

   List<MusicAlbum> getMusicAlbumsByArtist(String _artist);

   void initStreaming(String _id, String _client, DownloadItemType _itemType, String _itemId);
   
   String startStreaming(String _id, String _profile, long _position);

   void stopStreaming(String _file);

   MediaInfo getMediaInfo(String _itemId, DownloadItemType _itemType);

   Bitmap getBitmapFromMedia(DownloadItemType _itemType, String _itemId, int _position, int _maxWidth, int _maxHeight);

   StreamTranscodingInfo getTransocdingInfo(String _id);

   List<StreamProfile> getTranscoderProfiles();

}
