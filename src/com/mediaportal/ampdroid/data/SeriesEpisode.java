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

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.mediaportal.ampdroid.database.ColumnProperty;

public class SeriesEpisode {
   public static final String TABLE_NAME = "SeriesEpisodes";
   
   private int SeriesId;
	private int Id;
	private String Name;
	private int SeasonNumber;
	private int EpisodeNumber;
	private int Watched;
	private Date FirstAired;
	private String BannerUrl;
	private float Rating;
	private int RatingCount;
	private boolean HasLocalFile;
	private String FileName;
	
	public SeriesEpisode() {

	}

	@Override
	public String toString() {
		if (getName() != null) {
			return getName();
		} else
			return super.toString();
	}
   
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
   
   @ColumnProperty(value="SeriesId", type="integer")
   public void setSeriesId(int seriesId) {
      SeriesId = seriesId;
   }

   @ColumnProperty(value="SeriesId", type="integer")
   public int getSeriesId() {
      return SeriesId;
   }
   
   @ColumnProperty(value="Name", type="text")
   @JsonProperty("Name")
   public String getName() {
      return Name;
   }
   
   @ColumnProperty(value="Name", type="text")
   @JsonProperty("Name")
   public void setName(String name) {
      Name = name;
   }
   
   @ColumnProperty(value="SeasonNumber", type="integer")
   @JsonProperty("SeasonNumber")
   public int getSeasonNumber() {
      return SeasonNumber;
   }
   
   @ColumnProperty(value="SeasonNumber", type="integer")
   @JsonProperty("SeasonNumber")
   public void setSeasonNumber(int seasonNumber) {
      SeasonNumber = seasonNumber;
   }
   
   @ColumnProperty(value="EpisodeNumber", type="integer")
   @JsonProperty("EpisodeNumber")
   public int getEpisodeNumber() {
      return EpisodeNumber;
   }
   
   @ColumnProperty(value="EpisodeNumber", type="integer")
   @JsonProperty("EpisodeNumber")
   public void setEpisodeNumber(int episodeNumber) {
      EpisodeNumber = episodeNumber;
   }
   
   @ColumnProperty(value="Watched", type="integer")
   @JsonProperty("Watched")
   public int getWatched() {
      return Watched;
   }
   
   @ColumnProperty(value="Watched", type="integer")
   @JsonProperty("Watched")
   public void setWatched(int watched) {
      Watched = watched;
   }
   
   @ColumnProperty(value="FirstAired", type="date")
   @JsonProperty("FirstAired")
   public Date getFirstAired() {
      return FirstAired;
   }
   
   @ColumnProperty(value="FirstAired", type="date")
   @JsonProperty("FirstAired")
   public void setFirstAired(Date firstAired) {
      FirstAired = firstAired;
   }
   
   @ColumnProperty(value="BannerUrl", type="text")
   @JsonProperty("BannerUrl")
   public String getBannerUrl() {
      return BannerUrl;
   }
   
   @ColumnProperty(value="BannerUrl", type="text")
   @JsonProperty("BannerUrl")
   public void setBannerUrl(String bannerUrl) {
      BannerUrl = bannerUrl;
   }
   
   @ColumnProperty(value="Rating", type="float")
   @JsonProperty("Rating")
   public float getRating() {
      return Rating;
   }
   
   @ColumnProperty(value="Rating", type="float")
   @JsonProperty("Rating")
   public void setRating(float rating) {
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
   
   @ColumnProperty(value="HasLocalFile", type="boolean")
   @JsonProperty("HasLocalFile")
   public boolean isHasLocalFile() {
      return HasLocalFile;
   }
   
   @ColumnProperty(value="HasLocalFile", type="boolean")
   @JsonProperty("HasLocalFile")
   public void setHasLocalFile(boolean hasLocalFile) {
      HasLocalFile = hasLocalFile;
   }

   @ColumnProperty(value="FileName", type="text")
   @JsonProperty("FileName")
   public void setFileName(String fileName) {
      FileName = fileName;
   }

   @ColumnProperty(value="FileName", type="text")
   @JsonProperty("FileName")
   public String getFileName() {
      return FileName;
   }

}
