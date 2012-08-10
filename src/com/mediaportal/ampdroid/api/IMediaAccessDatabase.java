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
import java.util.List;

import com.mediaportal.ampdroid.data.CacheItemsSetting;
import com.mediaportal.ampdroid.data.SeriesEpisodeDetails;
import com.mediaportal.ampdroid.data.Movie;
import com.mediaportal.ampdroid.data.MovieFull;
import com.mediaportal.ampdroid.data.Series;
import com.mediaportal.ampdroid.data.SeriesEpisode;
import com.mediaportal.ampdroid.data.SeriesFull;
import com.mediaportal.ampdroid.data.SeriesSeason;
import com.mediaportal.ampdroid.data.WebServiceDescription;

public interface IMediaAccessDatabase {
   //general
   WebServiceDescription getServiceDescription();
   void setServiceDescription(WebServiceDescription supported);
   public void open();
   public void close();
   
   //Movies
   ArrayList<Movie> getAllMovies();
   List<Movie> getMovies(int _start, int _end);
   void saveMovie(Movie _movie);
   CacheItemsSetting getMovieCount();
   CacheItemsSetting setMovieCount(int movieCount);
   MovieFull getMovieDetails(int _movieId);
   void saveMovieDetails(MovieFull _movie);
   
   //Series
   List<Series> getSeries(int _start, int _end);
   void saveSeries(Series _series);
   List<Series> getAllSeries();
   CacheItemsSetting getSeriesCount();
   CacheItemsSetting setSeriesCount(int _seriesCount);
   
   SeriesFull getFullSeries(int _seriesId);
   void saveSeriesDetails(SeriesFull series);
   
   List<SeriesSeason> getAllSeasons(int _seriesId);
   void saveSeason(SeriesSeason s);
   
   List<SeriesEpisode> getAllEpisodes(int _seriesId);
   void saveEpisode(int _seriesId, SeriesEpisode _episode);
   List<SeriesEpisode> getAllEpisodesForSeason(int _seriesId, int _seasonNumber);
   //CacheItemsSetting getEpisodesCountForSeason(int _seriesId, int _seasonNumber);
   //CacheItemsSetting setEpisodesCountForSeason(int _seriesId, int _seasonNumber, int _episodesCount);
   
   SeriesEpisodeDetails getEpisodeDetails(int _seriesId, int _episodeId);
   void saveEpisodeDetails(int _seriesId, SeriesEpisodeDetails episode);
   
   
   MovieFull getVideoDetails(int _movieId);
   List<Movie> getAllVideos();
   void saveVideo(Movie m);
   CacheItemsSetting getVideosCount();
   CacheItemsSetting setVideosCount(int movieCount);
   List<Movie> getVideos(int _start, int _end);
   void saveVideoDetails(MovieFull movie);
   

}
