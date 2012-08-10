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

public class SeriesFull extends Series {
   public static final String TABLE_NAME = "SeriesDetails";
   private String SortName;
   private String OrigName;
   private String Status;
   private String[] BannerUrls;
   private String[] FanartUrls;
   private String[] PosterUrls;
   private String ContentRating;
   private String Network;
   private String Summary;
   private String AirsDay;
   private String AirsTime;
   private String[] Actors;
   private int EpisodesUnwatchedCount;
   private int Runtime;
   private Date FirstAired;
   private String EpisodeOrder;
   
   @ColumnProperty(value="SortName", type="text")
   @JsonProperty("SortName")
   public String getSortName() {
      return SortName;
   }
   
   @ColumnProperty(value="SortName", type="text")
   @JsonProperty("SortName")
   public void setSortName(String sortName) {
      SortName = sortName;
   }
   
   @ColumnProperty(value="OrigName", type="text")
   @JsonProperty("OrigName")
   public String getOrigName() {
      return OrigName;
   }
   
   @ColumnProperty(value="OrigName", type="text")
   @JsonProperty("OrigName")
   public void setOrigName(String origName) {
      OrigName = origName;
   }
   
   @ColumnProperty(value="Status", type="text")
   @JsonProperty("Status")
   public String getStatus() {
      return Status;
   }
   
   @ColumnProperty(value="Status", type="text")
   @JsonProperty("Status")
   public void setStatus(String status) {
      Status = status;
   }
   
   @ColumnProperty(value="BannerUrls", type="textarray")
   @JsonProperty("BannerUrls")
   public String[] getBannerUrls() {
      return BannerUrls;
   }
   
   @ColumnProperty(value="BannerUrls", type="textarray")
   @JsonProperty("BannerUrls")
   public void setBannerUrls(String[] bannerUrls) {
      BannerUrls = bannerUrls;
   }
   
   @ColumnProperty(value="FanartUrls", type="textarray")
   @JsonProperty("FanartUrls")
   public String[] getFanartUrls() {
      return FanartUrls;
   }
   
   @ColumnProperty(value="FanartUrls", type="textarray")
   @JsonProperty("FanartUrls")
   public void setFanartUrls(String[] fanartUrls) {
      FanartUrls = fanartUrls;
   }
   
   @ColumnProperty(value="PosterUrls", type="textarray")
   @JsonProperty("PosterUrls")
   public String[] getPosterUrls() {
      return PosterUrls;
   }
   
   @ColumnProperty(value="PosterUrls", type="textarray")
   @JsonProperty("PosterUrls")
   public void setPosterUrls(String[] posterUrls) {
      PosterUrls = posterUrls;
   }
   
   @ColumnProperty(value="ContentRating", type="text")
   @JsonProperty("ContentRating")
   public String getContentRating() {
      return ContentRating;
   }
   
   @ColumnProperty(value="ContentRating", type="text")
   @JsonProperty("ContentRating")
   public void setContentRating(String contentRating) {
      ContentRating = contentRating;
   }
   
   @ColumnProperty(value="Network", type="text")
   @JsonProperty("Network")
   public String getNetwork() {
      return Network;
   }
   
   @ColumnProperty(value="Network", type="text")
   @JsonProperty("Network")
   public void setNetwork(String network) {
      Network = network;
   }
   
   @ColumnProperty(value="Summary", type="text")
   @JsonProperty("Summary")
   public String getSummary() {
      return Summary;
   }
   
   @ColumnProperty(value="Summary", type="text")
   @JsonProperty("Summary")
   public void setSummary(String summary) {
      Summary = summary;
   }
   
   @ColumnProperty(value="AirsDay", type="text")
   @JsonProperty("AirsDay")
   public String getAirsDay() {
      return AirsDay;
   }
   
   @ColumnProperty(value="AirsDay", type="text")
   @JsonProperty("AirsDay")
   public void setAirsDay(String airsDay) {
      AirsDay = airsDay;
   }
   
   @ColumnProperty(value="AirsTime", type="text")
   @JsonProperty("AirsTime")
   public String getAirsTime() {
      return AirsTime;
   }
   
   @ColumnProperty(value="AirsTime", type="text")
   @JsonProperty("AirsTime")
   public void setAirsTime(String airsTime) {
      AirsTime = airsTime;
   }
   
   @ColumnProperty(value="Actors", type="textarray")
   @JsonProperty("Actors")
   public String[] getActors() {
      return Actors;
   }
   
   @ColumnProperty(value="Actors", type="textarray")
   @JsonProperty("Actors")
   public void setActors(String[] actors) {
      Actors = actors;
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
   
   @ColumnProperty(value="Runtime", type="integer")
   @JsonProperty("Runtime")
   public int getRuntime() {
      return Runtime;
   }
   
   @ColumnProperty(value="Runtime", type="integer")
   @JsonProperty("Runtime")
   public void setRuntime(int runtime) {
      Runtime = runtime;
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
   
   @ColumnProperty(value="EpisodeOrder", type="text")
   @JsonProperty("EpisodeOrder")
   public String getEpisodeOrder() {
      return EpisodeOrder;
   }
   
   @ColumnProperty(value="EpisodeOrder", type="text")
   @JsonProperty("EpisodeOrder")
   public void setEpisodeOrder(String episodeOrder) {
      EpisodeOrder = episodeOrder;
   }

}
