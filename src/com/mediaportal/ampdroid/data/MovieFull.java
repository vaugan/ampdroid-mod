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

public class MovieFull extends Movie {
   public static final String TABLE_NAME = "MovieDetails";
   public static final String TABLE_NAME_VIDEOS = "VideoDetails";
   
   private String DiscId;
   private String Hash;
   private int Part;
   private int Duration;
   private int VideoWidth;
   private int VideoHeight;
   private String VideoResolution;
   private String VideoCodec;
   private String AudioCodec;
   private String AudioChannels;
   private boolean HasSubtitles;
   private String VideoFormat;
   private String AlternateTitlesString;
   private String SortBy;
   private String Directors;
   private String Actors;
   private String Writers;
   private String Certification;
   private String Language;
   private String Summary;
   private double Score;
   private int Popularity;
   private Date DateAdded;
   private int Runtime;
   private String ImdbId;
   private String CoverPathAlternate;
   private String CoverPath;
   

   @ColumnProperty(value="DiscId", type="text")
   @JsonProperty("DiscId")
   public String getDiscId() {
      return DiscId;
   }

   @ColumnProperty(value="DiscId", type="text")
   @JsonProperty("DiscId")
   public void setDiscId(String discId) {
      DiscId = discId;
   }

   @ColumnProperty(value="Hash", type="text")
   @JsonProperty("Hash")
   public String getHash() {
      return Hash;
   }

   @ColumnProperty(value="Hash", type="text")
   @JsonProperty("Hash")
   public void setHash(String hash) {
      Hash = hash;
   }

   @ColumnProperty(value="Part", type="integer")
   @JsonProperty("Part")
   public int getPart() {
      return Part;
   }

   @ColumnProperty(value="Part", type="integer")
   @JsonProperty("Part")
   public void setPart(int part) {
      Part = part;
   }

   @ColumnProperty(value="Duration", type="integer")
   @JsonProperty("Duration")
   public int getDuration() {
      return Duration;
   }

   @ColumnProperty(value="Duration", type="integer")
   @JsonProperty("Duration")
   public void setDuration(int duration) {
      Duration = duration;
   }

   @ColumnProperty(value="VideoWidth", type="integer")
   @JsonProperty("VideoWidth")
   public int getVideoWidth() {
      return VideoWidth;
   }

   @ColumnProperty(value="VideoWidth", type="integer")
   @JsonProperty("VideoWidth")
   public void setVideoWidth(int videoWidth) {
      VideoWidth = videoWidth;
   }

   @ColumnProperty(value="VideoHeight", type="integer")
   @JsonProperty("VideoHeight")
   public int getVideoHeight() {
      return VideoHeight;
   }

   @ColumnProperty(value="VideoHeight", type="integer")
   @JsonProperty("VideoHeight")
   public void setVideoHeight(int videoHeight) {
      VideoHeight = videoHeight;
   }

   @ColumnProperty(value="VideoResolution", type="text")
   @JsonProperty("VideoResolution")
   public String getVideoResolution() {
      return VideoResolution;
   }

   @ColumnProperty(value="VideoResolution", type="text")
   @JsonProperty("VideoResolution")
   public void setVideoResolution(String videoResolution) {
      VideoResolution = videoResolution;
   }

   @ColumnProperty(value="VideoCodec", type="text")
   @JsonProperty("VideoCodec")
   public String getVideoCodec() {
      return VideoCodec;
   }

   @ColumnProperty(value="VideoCodec", type="text")
   @JsonProperty("VideoCodec")
   public void setVideoCodec(String videoCodec) {
      VideoCodec = videoCodec;
   }

   @ColumnProperty(value="AudioCodec", type="text")
   @JsonProperty("AudioCodec")
   public String getAudioCodec() {
      return AudioCodec;
   }

   @ColumnProperty(value="AudioCodec", type="text")
   @JsonProperty("AudioCodec")
   public void setAudioCodec(String audioCodec) {
      AudioCodec = audioCodec;
   }

   @ColumnProperty(value="AudioChannels", type="text")
   @JsonProperty("AudioChannels")
   public String getAudioChannels() {
      return AudioChannels;
   }

   @ColumnProperty(value="AudioChannels", type="text")
   @JsonProperty("AudioChannels")
   public void setAudioChannels(String audioChannels) {
      AudioChannels = audioChannels;
   }

   @ColumnProperty(value="HasSubtitles", type="boolean")
   @JsonProperty("HasSubtitles")
   public boolean isHasSubtitles() {
      return HasSubtitles;
   }

   @ColumnProperty(value="HasSubtitles", type="boolean")
   @JsonProperty("HasSubtitles")
   public void setHasSubtitles(boolean hasSubtitles) {
      HasSubtitles = hasSubtitles;
   }

   @ColumnProperty(value="VideoFormat", type="text")
   @JsonProperty("VideoFormat")
   public String getVideoFormat() {
      return VideoFormat;
   }

   @ColumnProperty(value="VideoFormat", type="text")
   @JsonProperty("VideoFormat")
   public void setVideoFormat(String videoFormat) {
      VideoFormat = videoFormat;
   }

   @ColumnProperty(value="AlternateTitlesString", type="text")
   @JsonProperty("AlternateTitlesString")
   public String getAlternateTitlesString() {
      return AlternateTitlesString;
   }

   @ColumnProperty(value="AlternateTitlesString", type="text")
   @JsonProperty("AlternateTitlesString")
   public void setAlternateTitlesString(String alternateTitlesString) {
      AlternateTitlesString = alternateTitlesString;
   }

   @ColumnProperty(value="SortBy", type="text")
   @JsonProperty("SortBy")
   public String getSortBy() {
      return SortBy;
   }

   @ColumnProperty(value="SortBy", type="text")
   @JsonProperty("SortBy")
   public void setSortBy(String sortBy) {
      SortBy = sortBy;
   }

   @ColumnProperty(value="Directors", type="text")
   @JsonProperty("Directors")
   public String getDirectorsString() {
      return Directors;
   }

   @ColumnProperty(value="Directors", type="text")
   @JsonProperty("Directors")
   public void setDirectorsString(String directorsString) {
      Directors = directorsString;
   }

   @ColumnProperty(value="Actors", type="text")
   @JsonProperty("Actors")
   public String getActorsString() {
      return Actors;
   }

   @ColumnProperty(value="Actors", type="text")
   @JsonProperty("Actors")
   public void setActorsString(String actorsString) {
      Actors = actorsString;
   }

   @ColumnProperty(value="Writers", type="text")
   @JsonProperty("Writers")
   public String getWritersString() {
      return Writers;
   }

   @ColumnProperty(value="Writers", type="text")
   @JsonProperty("Writers")
   public void setWritersString(String writersString) {
      Writers = writersString;
   }

   @ColumnProperty(value="Certification", type="text")
   @JsonProperty("Certification")
   public String getCertification() {
      return Certification;
   }

   @ColumnProperty(value="Certification", type="text")
   @JsonProperty("Certification")
   public void setCertification(String certification) {
      Certification = certification;
   }

   @ColumnProperty(value="Language", type="text")
   @JsonProperty("Language")
   public String getLanguage() {
      return Language;
   }

   @ColumnProperty(value="Language", type="text")
   @JsonProperty("Language")
   public void setLanguage(String language) {
      Language = language;
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

   @ColumnProperty(value="Score", type="double")
   @JsonProperty("Score")
   public double getScore() {
      return Score;
   }

   @ColumnProperty(value="Score", type="double")
   @JsonProperty("Score")
   public void setScore(double score) {
      Score = score;
   }

   @ColumnProperty(value="Popularity", type="integer")
   @JsonProperty("Popularity")
   public int getPopularity() {
      return Popularity;
   }

   @ColumnProperty(value="Popularity", type="integer")
   @JsonProperty("Popularity")
   public void setPopularity(int popularity) {
      Popularity = popularity;
   }

   @ColumnProperty(value="DateAdded", type="date")
   @JsonProperty("DateAdded")
   public Date getDateAdded() {
      return DateAdded;
   }

   @ColumnProperty(value="DateAdded", type="date")
   @JsonProperty("DateAdded")
   public void setDateAdded(Date dateAdded) {
      DateAdded = dateAdded;
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

   @ColumnProperty(value="CoverPathAlternate", type="text")
   @JsonProperty("CoverPathAlternate")
   public String getCoverPathAlternate() {
      return CoverPathAlternate;
   }

   @ColumnProperty(value="CoverPathAlternate", type="text")
   @JsonProperty("CoverPathAlternate")
   public void setCoverPathAlternate(String coverPathAlternate) {
      CoverPathAlternate = coverPathAlternate;
   }

   @ColumnProperty(value="CoverPath", type="text")
   @JsonProperty("CoverPath")
   public String getCoverPath() {
      return CoverPath;
   }

   @ColumnProperty(value="CoverPath", type="text")
   @JsonProperty("CoverPath")
   public void setCoverPath(String coverPath) {
      CoverPath = coverPath;
   }

}
