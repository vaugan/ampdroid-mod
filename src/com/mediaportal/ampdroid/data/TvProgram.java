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

public class TvProgram extends TvProgramBase {
	String Classification;
	boolean HasConflict;
	int IdProgram;
	boolean IsPartialRecordingSeriesPending;
	boolean IsRecording;
	boolean IsRecordingManual;
	boolean IsRecordingOnce;
	boolean IsRecordingOncePending;
	boolean IsRecordingSeries;
	boolean IsRecordingSeriesPending;
	boolean Notify;
	Date OriginalAirDate;
	int ParentalRating;
	int StarRating;
	
   String SeriesNum;
   boolean IsChanged;
   String EpisodeName;
   String EpisodeNum;
   String EpisodeNumber;
   String EpisodePart;
   String Genre;
   
   
	
	@Override
	public String toString(){
	   return Title;
	}
	
   @ColumnProperty(value="IsChanged", type="boolean")
   @JsonProperty("IsChanged")
   public boolean isIsChanged() {
      return IsChanged;
   }   
   
   @ColumnProperty(value="IsChanged", type="boolean")
   @JsonProperty("IsChanged")
   public void setIsChanged(boolean isChanged) {
      IsChanged = isChanged;
   }
   
   @ColumnProperty(value="EpisodeName", type="text")
   @JsonProperty("EpisodeName")
   public String getEpisodeName() {
      return EpisodeName;
   }   
   
   @ColumnProperty(value="EpisodeName", type="text")
   @JsonProperty("EpisodeName")
   public void setEpisodeName(String episodeName) {
      EpisodeName = episodeName;
   }   
   
   @ColumnProperty(value="EpisodeNum", type="integer")
   @JsonProperty("EpisodeNum")
   public String getEpisodeNum() {
      return EpisodeNum;
   }   
   
   @ColumnProperty(value="EpisodeNum", type="integer")
   @JsonProperty("EpisodeNum")
   public void setEpisodeNum(String episodeNum) {
      EpisodeNum = episodeNum;
   }   
   
   @ColumnProperty(value="EpisodeNumber", type="text")
   @JsonProperty("EpisodeNumber")
   public String getEpisodeNumber() {
      return EpisodeNumber;
   }   
   
   @ColumnProperty(value="EpisodeNumber", type="text")
   @JsonProperty("EpisodeNumber")
   public void setEpisodeNumber(String episodeNumber) {
      EpisodeNumber = episodeNumber;
   }   
   
   @ColumnProperty(value="EpisodePart", type="text")
   @JsonProperty("EpisodePart")
   public String getEpisodePart() {
      return EpisodePart;
   }   
   
   @ColumnProperty(value="EpisodePart", type="text")
   @JsonProperty("EpisodePart")
   public void setEpisodePart(String episodePart) {
      EpisodePart = episodePart;
   }   
   
   @ColumnProperty(value="Genre", type="text")
   @JsonProperty("Genre")
   public String getGenre() {
      return Genre;
   }   
   
   @ColumnProperty(value="Genre", type="text")
   @JsonProperty("Genre")
   public void setGenre(String genre) {
      Genre = genre;
   } 
   
   @ColumnProperty(value="SeriesNum", type="text")
   @JsonProperty("SeriesNum")
   public String getSeriesNum() {
      return SeriesNum;
   }   
   
   @ColumnProperty(value="SeriesNum", type="text")
   @JsonProperty("SeriesNum")
   public void setSeriesNum(String seriesNum) {
      SeriesNum = seriesNum;
   }
   
   @ColumnProperty(value="Classification", type="text")
	@JsonProperty("Classification")
   public String getClassification() {
      return Classification;
   }
   
   @ColumnProperty(value="Classification", type="text")
   @JsonProperty("Classification")
   public void setClassification(String classification) {
      Classification = classification;
   }
   
   @ColumnProperty(value="HasConflict", type="boolean")
   @JsonProperty("HasConflict")
   public boolean isHasConflict() {
      return HasConflict;
   }
   
   @ColumnProperty(value="HasConflict", type="boolean")
   @JsonProperty("HasConflict")
   public void setHasConflict(boolean hasConflict) {
      HasConflict = hasConflict;
   }
   
   @ColumnProperty(value="IdProgram", type="integer")
   @JsonProperty("IdProgram")
   public int getIdProgram() {
      return IdProgram;
   }
   
   @ColumnProperty(value="IdProgram", type="integer")
   @JsonProperty("IdProgram")
   public void setIdProgram(int idProgram) {
      IdProgram = idProgram;
   }
   
   @ColumnProperty(value="IsPartialRecordingSeriesPending", type="boolean")
   @JsonProperty("IsPartialRecordingSeriesPending")
   public boolean isIsPartialRecordingSeriesPending() {
      return IsPartialRecordingSeriesPending;
   }
   
   @ColumnProperty(value="IsPartialRecordingSeriesPending", type="boolean")
   @JsonProperty("IsPartialRecordingSeriesPending")
   public void setIsPartialRecordingSeriesPending(boolean isPartialRecordingSeriesPending) {
      IsPartialRecordingSeriesPending = isPartialRecordingSeriesPending;
   }
   
   @ColumnProperty(value="IsRecording", type="boolean")
   @JsonProperty("IsRecording")
   public boolean isIsRecording() {
      return IsRecording;
   }
   
   @ColumnProperty(value="IsRecording", type="boolean")
   @JsonProperty("IsRecording")
   public void setIsRecording(boolean isRecording) {
      IsRecording = isRecording;
   }
   
   @ColumnProperty(value="IsRecordingManual", type="boolean")
   @JsonProperty("IsRecordingManual")
   public boolean isIsRecordingManual() {
      return IsRecordingManual;
   }
   
   @ColumnProperty(value="IsRecordingManual", type="boolean")
   @JsonProperty("IsRecordingManual")
   public void setIsRecordingManual(boolean isRecordingManual) {
      IsRecordingManual = isRecordingManual;
   }
   
   @ColumnProperty(value="IsRecordingOnce", type="boolean")
   @JsonProperty("IsRecordingOnce")
   public boolean isIsRecordingOnce() {
      return IsRecordingOnce;
   }
   
   @ColumnProperty(value="IsRecordingOnce", type="boolean")
   @JsonProperty("IsRecordingOnce")
   public void setIsRecordingOnce(boolean isRecordingOnce) {
      IsRecordingOnce = isRecordingOnce;
   }
   
   @ColumnProperty(value="IsRecordingOncePending", type="boolean")
   @JsonProperty("IsRecordingOncePending")
   public boolean isIsRecordingOncePending() {
      return IsRecordingOncePending;
   }
   
   @ColumnProperty(value="IsRecordingOncePending", type="boolean")
   @JsonProperty("IsRecordingOncePending")
   public void setIsRecordingOncePending(boolean isRecordingOncePending) {
      IsRecordingOncePending = isRecordingOncePending;
   }
   
   @ColumnProperty(value="IsRecordingSeries", type="boolean")
   @JsonProperty("IsRecordingSeries")
   public boolean isIsRecordingSeries() {
      return IsRecordingSeries;
   }
   
   @ColumnProperty(value="IsRecordingSeries", type="boolean")
   @JsonProperty("IsRecordingSeries")
   public void setIsRecordingSeries(boolean isRecordingSeries) {
      IsRecordingSeries = isRecordingSeries;
   }
   
   @ColumnProperty(value="IsRecordingSeriesPending", type="boolean")
   @JsonProperty("IsRecordingSeriesPending")
   public boolean isIsRecordingSeriesPending() {
      return IsRecordingSeriesPending;
   }
   
   @ColumnProperty(value="IsRecordingSeriesPending", type="boolean")
   @JsonProperty("IsRecordingSeriesPending")
   public void setIsRecordingSeriesPending(boolean isRecordingSeriesPending) {
      IsRecordingSeriesPending = isRecordingSeriesPending;
   }
   
   @ColumnProperty(value="Notify", type="boolean")
   @JsonProperty("Notify")
   public boolean isNotify() {
      return Notify;
   }
   
   @ColumnProperty(value="Notify", type="boolean")
   @JsonProperty("Notify")
   public void setNotify(boolean notify) {
      Notify = notify;
   }
   
   @ColumnProperty(value="OriginalAirDate", type="date")
   @JsonProperty("OriginalAirDate")
   public Date getOriginalAirDate() {
      return OriginalAirDate;
   }
   
   @ColumnProperty(value="OriginalAirDate", type="date")
   @JsonProperty("OriginalAirDate")
   public void setOriginalAirDate(Date originalAirDate) {
      OriginalAirDate = originalAirDate;
   }
   
   @ColumnProperty(value="ParentalRating", type="integer")
   @JsonProperty("ParentalRating")
   public int getParentalRating() {
      return ParentalRating;
   }
   
   @ColumnProperty(value="ParentalRating", type="integer")
   @JsonProperty("ParentalRating")
   public void setParentalRating(int parentalRating) {
      ParentalRating = parentalRating;
   }
   
   @ColumnProperty(value="StarRating", type="integer")
   @JsonProperty("StarRating")
   public int getStarRating() {
      return StarRating;
   }
   
   @ColumnProperty(value="StarRating", type="integer")
   @JsonProperty("StarRating")
   public void setStarRating(int starRating) {
      StarRating = starRating;
   }
	
	

}
