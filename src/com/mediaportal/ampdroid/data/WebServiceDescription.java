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
package com.mediaportal.ampdroid.data;

import org.codehaus.jackson.annotate.JsonProperty;

import com.mediaportal.ampdroid.database.ColumnProperty;

public class WebServiceDescription {
   public static final String TABLE_NAME = "SupportedFunctions";

   private boolean SupportsVideos;
   private boolean SupportsMusic;
   private boolean SupportsPictures;
   private boolean SupportsTvSeries;
   private boolean SupportsMovingPictures;
   private boolean SupportsMyFilms;

   private int VideoApiVersion;
   private int MusicApiVersion;
   private int PicturesApiVersion;
   private int TvSeriesApiVersion;
   private int MovingPicturesApiVersion;
   private int MyFilmsApiVersion;
   private int StreamingApiVersion;

   private String ServiceVersion;
   
   @ColumnProperty(value = "SupportsMyFilms", type = "boolean")
   @JsonProperty("SupportsMyFilms")
   public void setSupportsMyFilms(boolean supportsMyFilms) {
      SupportsMyFilms = supportsMyFilms;
   }
   
   @ColumnProperty(value = "SupportsMyFilms", type = "boolean")
   @JsonProperty("SupportsMyFilms")
   public boolean isSupportsMyFilms() {
      return SupportsMyFilms;
   }
   
   @ColumnProperty(value = "SupportsVideos", type = "boolean")
   @JsonProperty("SupportsVideos")
   public boolean supportsVideo() {
      return SupportsVideos;
   }

   @ColumnProperty(value = "SupportsVideos", type = "boolean")
   @JsonProperty("SupportsVideos")
   public void setSupportsVideo(boolean supportsVideo) {
      SupportsVideos = supportsVideo;
   }

   @ColumnProperty(value = "SupportsMusic", type = "boolean")
   @JsonProperty("SupportsMusic")
   public boolean supportsMusic() {
      return SupportsMusic;
   }

   @ColumnProperty(value = "SupportsMusic", type = "boolean")
   @JsonProperty("SupportsMusic")
   public void setSupportsMusic(boolean supportsMusic) {
      SupportsMusic = supportsMusic;
   }

   @ColumnProperty(value = "SupportsPictures", type = "boolean")
   @JsonProperty("SupportsPictures")
   public boolean supportsPictures() {
      return SupportsPictures;
   }

   @ColumnProperty(value = "SupportsPictures", type = "boolean")
   @JsonProperty("SupportsPictures")
   public void setSupportsPictures(boolean supportsPictures) {
      SupportsPictures = supportsPictures;
   }

   @ColumnProperty(value = "SupportsTvSeries", type = "boolean")
   @JsonProperty("SupportsTvSeries")
   public boolean supportsTvSeries() {
      return SupportsTvSeries;
   }

   @ColumnProperty(value = "SupportsTvSeries", type = "boolean")
   @JsonProperty("SupportsTvSeries")
   public void setSupportsTvSeries(boolean supportsTvSeries) {
      SupportsTvSeries = supportsTvSeries;
   }

   @ColumnProperty(value = "SupportsMovingPictures", type = "boolean")
   @JsonProperty("SupportsMovingPictures")
   public boolean supportsMovingPictures() {
      return SupportsMovingPictures;
   }

   @ColumnProperty(value = "SupportsMovingPictures", type = "boolean")
   @JsonProperty("SupportsMovingPictures")
   public void setSupportsMovingPictures(boolean supportsMovies) {
      SupportsMovingPictures = supportsMovies;
   }

   @ColumnProperty(value = "VideoApiVersion", type = "integer")
   @JsonProperty("VideoApiVersion")
   public int getVideoApiVersion() {
      return VideoApiVersion;
   }

   @ColumnProperty(value = "VideoApiVersion", type = "integer")
   @JsonProperty("VideoApiVersion")
   public void setVideoApiVersion(int videoApiVersion) {
      VideoApiVersion = videoApiVersion;
   }

   @ColumnProperty(value = "MusicApiVersion", type = "integer")
   @JsonProperty("MusicApiVersion")
   public int getMusicApiVersion() {
      return MusicApiVersion;
   }

   @ColumnProperty(value = "MusicApiVersion", type = "integer")
   @JsonProperty("MusicApiVersion")
   public void setMusicApiVersion(int musicApiVersion) {
      MusicApiVersion = musicApiVersion;
   }

   @ColumnProperty(value = "PicturesApiVersion", type = "integer")
   @JsonProperty("PicturesApiVersion")
   public int getPicturesApiVersion() {
      return PicturesApiVersion;
   }

   @ColumnProperty(value = "PicturesApiVersion", type = "integer")
   @JsonProperty("PicturesApiVersion")
   public void setPicturesApiVersion(int picturesApiVersion) {
      PicturesApiVersion = picturesApiVersion;
   }

   @ColumnProperty(value = "TvSeriesApiVersion", type = "integer")
   @JsonProperty("TvSeriesApiVersion")
   public int getTvSeriesApiVersion() {
      return TvSeriesApiVersion;
   }

   @ColumnProperty(value = "TvSeriesApiVersion", type = "integer")
   @JsonProperty("TvSeriesApiVersion")
   public void setTvSeriesApiVersion(int tvSeriesApiVersion) {
      TvSeriesApiVersion = tvSeriesApiVersion;
   }

   @ColumnProperty(value = "MovingPicturesApiVersion", type = "integer")
   @JsonProperty("MovingPicturesApiVersion")
   public int getMovingPicturesApiVersion() {
      return MovingPicturesApiVersion;
   }

   @ColumnProperty(value = "MovingPicturesApiVersion", type = "integer")
   @JsonProperty("MovingPicturesApiVersion")
   public void setMovingPicturesApiVersion(int movingPicturesApiVersion) {
      MovingPicturesApiVersion = movingPicturesApiVersion;
   }

   @ColumnProperty(value = "MyFilmsApiVersion", type = "integer")
   @JsonProperty("MyFilmsApiVersion")
   public int getMyFilmsApiVersion() {
      return MyFilmsApiVersion;
   }

   @ColumnProperty(value = "MyFilmsApiVersion", type = "integer")
   @JsonProperty("MyFilmsApiVersion")
   public void setMyFilmsApiVersion(int myFilmsApiVersion) {
      MyFilmsApiVersion = myFilmsApiVersion;
   }

   @ColumnProperty(value = "StreamingApiVersion", type = "integer")
   @JsonProperty("StreamingApiVersion")
   public int getStreamingApiVersion() {
      return StreamingApiVersion;
   }

   @ColumnProperty(value = "StreamingApiVersion", type = "integer")
   @JsonProperty("StreamingApiVersion")
   public void setStreamingApiVersion(int streamingApiVersion) {
      StreamingApiVersion = streamingApiVersion;
   }

   @ColumnProperty(value = "ServiceVersion", type = "text")
   @JsonProperty("ServiceVersion")
   public void setServiceVersion(String serviceVersion) {
      ServiceVersion = serviceVersion;
   }

   @ColumnProperty(value = "ServiceVersion", type = "text")
   @JsonProperty("ServiceVersion")
   public String getServiceVersion() {
      return ServiceVersion;
   }



}
