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

public class SeriesSeason{
   public static final String TABLE_NAME = "SeriesSeasons";
   public static final int CACHE_ID = 2;
	private String Id;
	private int SeriesId;
	private int SeasonNumber;
	private int EpisodesCount;
	private int EpisodesUnwatchedCount;
	private String SeasonBanner;
	private String[] AlternateSeasonBanners;
   
   @ColumnProperty(value="Id", type="text")
	@JsonProperty("Id")
	public String getId() {
      return Id;
   }
   
   @ColumnProperty(value="Id", type="text")
   @JsonProperty("Id")
   public void setId(String id) {
      Id = id;
   }
   
   @ColumnProperty(value="SeriesId", type="integer")
   @JsonProperty("SeriesId")
   public int getSeriesId() {
      return SeriesId;
   }
   
   @ColumnProperty(value="SeriesId", type="integer")
   @JsonProperty("SeriesId")
   public void setSeriesId(int seriesId) {
      SeriesId = seriesId;
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
   
   @ColumnProperty(value="EpisodesCount", type="integer")
   @JsonProperty("EpisodesCount")
   public int getEpisodesCount() {
      return EpisodesCount;
   }
   
   @ColumnProperty(value="EpisodesCount", type="integer")
   @JsonProperty("EpisodesCount")
   public void setEpisodesCount(int episodesCount) {
      EpisodesCount = episodesCount;
   }
   
   @ColumnProperty(value="EpisodesUnwatchedCount", type="integer")
   @JsonProperty("EpisodesUnwatchedCount")
   public int getEpisodesUnwatchedCount() {
      return EpisodesUnwatchedCount;
   }
   
   @ColumnProperty(value="EpisodesUnwatchedCount", type="integer")
   @JsonProperty("EpisodesUnwatchedCount")
   public void setEpisodesUnwatchedCount(int episodesUnwatchedCount) {
      EpisodesUnwatchedCount = episodesUnwatchedCount;
   }
   
   @ColumnProperty(value="SeasonBanner", type="text")
   @JsonProperty("SeasonBanner")
   public String getSeasonBanner() {
      return SeasonBanner;
   }
   
   @ColumnProperty(value="SeasonBanner", type="text")
   @JsonProperty("SeasonBanner")
   public void setSeasonBanner(String seasonBanner) {
      SeasonBanner = seasonBanner;
   }
   
   @ColumnProperty(value="AlternateSeasonBanners", type="textarray")
   @JsonProperty("AlternateSeasonBanners")
   public String[] getAlternateSeasonBanners() {
      return AlternateSeasonBanners;
   }
   
   @ColumnProperty(value="AlternateSeasonBanners", type="textarray")
   @JsonProperty("AlternateSeasonBanners")
   public void setAlternateSeasonBanners(String[] alternateSeasonBanners) {
      AlternateSeasonBanners = alternateSeasonBanners;
   }
}
