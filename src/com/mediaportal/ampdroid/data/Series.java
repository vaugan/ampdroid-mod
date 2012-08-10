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

public class Series {
   public static final int CACHE_ID = 1;
   public static final String TABLE_NAME = "Series";
   
   private int Id;
   private String PrettyName;
   private int EpisodeCount;
   private String ImdbId;
   private double Rating;
   private int RatingCount;
   private String CurrentFanartUrl;
   private String CurrentBannerUrl;
   private String CurrentPosterUrl;
   private String GenreString;
   private String[] Genres;

   @ColumnProperty(value="Id", type="integer")
   @JsonProperty("Id")
   public int getId() {
      return Id;
   }
   
   @ColumnProperty(value="Id", type="integer")
   @JsonProperty("Id")
   public void setId(int id) {
      Id = id;
   }
   
   @ColumnProperty(value="PrettyName", type="text")
   @JsonProperty("PrettyName")
   public String getPrettyName() {
      return PrettyName;
   }
   
   @ColumnProperty(value="PrettyName", type="text")
   @JsonProperty("PrettyName")
   public void setPrettyName(String prettyName) {
      PrettyName = prettyName;
   }
   
   @ColumnProperty(value="EpisodeCount", type="integer")
   @JsonProperty("EpisodeCount")
   public int getEpisodeCount() {
      return EpisodeCount;
   }
   
   @ColumnProperty(value="EpisodeCount", type="integer")
   @JsonProperty("EpisodeCount")
   public void setEpisodeCount(int episodeCount) {
      EpisodeCount = episodeCount;
   }
   
   @ColumnProperty(value="ImdbId", type="text")
   @JsonProperty("ImdbId")
   public String getImdbId() {
      return ImdbId;
   }
   
   @ColumnProperty(value="ImdbId", type="text")
   @JsonProperty("ImdbId")
   public void setImdbId(String imdbId) {
      ImdbId = imdbId;
   }
   
   @ColumnProperty(value="Rating", type="double")
   @JsonProperty("Rating")
   public double getRating() {
      return Rating;
   }
   
   @ColumnProperty(value="Rating", type="double")
   @JsonProperty("Rating")
   public void setRating(double rating) {
      Rating = rating;
   }
   
   @ColumnProperty(value="RatingCount", type="integer")
   @JsonProperty("RatingCount")
   public int getRatingCount() {
      return RatingCount;
   }
   
   @ColumnProperty(value="RatingCount", type="integer")
   @JsonProperty("RatingCount")
   public void setRatingCount(int ratingCount) {
      RatingCount = ratingCount;
   }
   
   @ColumnProperty(value="CurrentFanartUrl", type="text")
   @JsonProperty("CurrentFanartUrl")
   public String getCurrentFanartUrl() {
      return CurrentFanartUrl;
   }
   
   @ColumnProperty(value="CurrentFanartUrl", type="text")
   @JsonProperty("CurrentFanartUrl")
   public void setCurrentFanartUrl(String currentFanartUrl) {
      CurrentFanartUrl = currentFanartUrl;
   }
   
   @ColumnProperty(value="CurrentBannerUrl", type="text")
   @JsonProperty("CurrentBannerUrl")
   public String getCurrentBannerUrl() {
      return CurrentBannerUrl;
   }
   
   @ColumnProperty(value="CurrentBannerUrl", type="text")
   @JsonProperty("CurrentBannerUrl")
   public void setCurrentBannerUrl(String currentBannerUrl) {
      CurrentBannerUrl = currentBannerUrl;
   }
   
   @ColumnProperty(value="CurrentPosterUrl", type="text")
   @JsonProperty("CurrentPosterUrl")
   public String getCurrentPosterUrl() {
      return CurrentPosterUrl;
   }
   
   @ColumnProperty(value="CurrentPosterUrl", type="text")
   @JsonProperty("CurrentPosterUrl")
   public void setCurrentPosterUrl(String currentPosterUrl) {
      CurrentPosterUrl = currentPosterUrl;
   }
   
   @ColumnProperty(value="GenreString", type="text")
   @JsonProperty("GenreString")
   public String getGenreString() {
      return GenreString;
   }
   
   @ColumnProperty(value="GenreString", type="text")
   @JsonProperty("GenreString")
   public void setGenreString(String genreString) {
      GenreString = genreString;
   }
   
   @ColumnProperty(value="Genres", type="textarray")
   @JsonProperty("Genres")
   public String[] getGenres() {
      return Genres;
   }
   
   @ColumnProperty(value="Genres", type="textarray")
   @JsonProperty("Genres")
   public void setGenres(String[] genres) {
      Genres = genres;
   }
}
