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

public class TvRecording {
   int IdChannel;
   String Title;
   boolean IsChanged;
   String Description;
   Date EndTime;
   String EpisodeName;
   String EpisodeNum;
   String EpisodeNumber;
   String EpisodePart;
   String Genre;
   Date StartTime;
   String SeriesNum;
   String FileName;
   int IdRecording;
   int Idschedule;
   int IdServer;
   boolean IsManual;
   boolean IsRecording;
   int KeepUntil;
   Date KeepUntilDate;
   boolean ShouldBeDeleted;
   int StopTime;
   int TimesWatched;
   
   @Override
   public String toString(){
      return this.Title;
   }
   
   @ColumnProperty(value="IdChannel", type="integer")
   @JsonProperty("IdChannel")
   public int getIdChannel() {
      return IdChannel;
   }   
   
   @ColumnProperty(value="IdChannel", type="integer")
   @JsonProperty("IdChannel")
   public void setIdChannel(int idChannel) {
      IdChannel = idChannel;
   }   
   
   @ColumnProperty(value="Title", type="text")
   @JsonProperty("Title")
   public String getTitle() {
      return Title;
   }   
   
   @ColumnProperty(value="Title", type="text")
   @JsonProperty("Title")
   public void setTitle(String title) {
      Title = title;
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
   
   @ColumnProperty(value="Description", type="text")
   @JsonProperty("Description")
   public String getDescription() {
      return Description;
   }   
   
   @ColumnProperty(value="Description", type="text")
   @JsonProperty("Description")
   public void setDescription(String description) {
      Description = description;
   }   
   
   @ColumnProperty(value="EndTime", type="date")
   @JsonProperty("EndTime")
   public Date getEndTime() {
      return EndTime;
   }   
   
   @ColumnProperty(value="EndTime", type="date")
   @JsonProperty("EndTime")
   public void setEndTime(Date endTime) {
      EndTime = endTime;
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
   
   @ColumnProperty(value="StartTime", type="date")
   @JsonProperty("StartTime")
   public Date getStartTime() {
      return StartTime;
   }   
   
   @ColumnProperty(value="StartTime", type="date")
   @JsonProperty("StartTime")
   public void setStartTime(Date startTime) {
      StartTime = startTime;
   }   
   
   @ColumnProperty(value="IdChannel", type="text")
   @JsonProperty("SeriesNum")
   public String getSeriesNum() {
      return SeriesNum;
   }   
   
   @ColumnProperty(value="IdChannel", type="text")
   @JsonProperty("SeriesNum")
   public void setSeriesNum(String seriesNum) {
      SeriesNum = seriesNum;
   }
   
   @ColumnProperty(value="FileName", type="text")
   @JsonProperty("FileName")
   public String getFileName() {
      return FileName;
   }
   
   @ColumnProperty(value="FileName", type="text")
   @JsonProperty("FileName")
   public void setFileName(String fileName) {
      FileName = fileName;
   }
   
   @ColumnProperty(value="IdRecording", type="integer")
   @JsonProperty("IdRecording")
   public int getIdRecording() {
      return IdRecording;
   }
   
   @ColumnProperty(value="IdRecording", type="integer")
   @JsonProperty("IdRecording")
   public void setIdRecording(int idRecording) {
      IdRecording = idRecording;
   }
   
   @ColumnProperty(value="Idschedule", type="integer")
   @JsonProperty("Idschedule")
   public int getIdschedule() {
      return Idschedule;
   }
   
   @ColumnProperty(value="Idschedule", type="integer")
   @JsonProperty("Idschedule")
   public void setIdschedule(int idschedule) {
      Idschedule = idschedule;
   }
   
   @ColumnProperty(value="IdServer", type="integer")
   @JsonProperty("IdServer")
   public int getIdServer() {
      return IdServer;
   }
   
   @ColumnProperty(value="IdServer", type="integer")
   @JsonProperty("IdServer")
   public void setIdServer(int idServer) {
      IdServer = idServer;
   }
   
   @ColumnProperty(value="IsManual", type="boolean")
   @JsonProperty("IsManual")
   public boolean isIsManual() {
      return IsManual;
   }
   
   @ColumnProperty(value="IsManual", type="boolean")
   @JsonProperty("IsManual")
   public void setIsManual(boolean isManual) {
      IsManual = isManual;
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
   
   @ColumnProperty(value="KeepUntil", type="integer")
   @JsonProperty("KeepUntil")
   public int getKeepUntil() {
      return KeepUntil;
   }
   
   @ColumnProperty(value="KeepUntil", type="integer")
   @JsonProperty("KeepUntil")
   public void setKeepUntil(int keepUntil) {
      KeepUntil = keepUntil;
   }
   
   @ColumnProperty(value="KeepUntilDate", type="date")
   @JsonProperty("KeepUntilDate")
   public Date getKeepUntilDate() {
      return KeepUntilDate;
   }
   
   @ColumnProperty(value="KeepUntilDate", type="date")
   @JsonProperty("KeepUntilDate")
   public void setKeepUntilDate(Date keepUntilDate) {
      KeepUntilDate = keepUntilDate;
   }
   
   @ColumnProperty(value="ShouldBeDeleted", type="boolean")
   @JsonProperty("ShouldBeDeleted")
   public boolean isShouldBeDeleted() {
      return ShouldBeDeleted;
   }
   
   @ColumnProperty(value="ShouldBeDeleted", type="boolean")
   @JsonProperty("ShouldBeDeleted")
   public void setShouldBeDeleted(boolean shouldBeDeleted) {
      ShouldBeDeleted = shouldBeDeleted;
   }
   
   @ColumnProperty(value="StopTime", type="integer")
   @JsonProperty("StopTime")
   public int getStopTime() {
      return StopTime;
   }
   
   @ColumnProperty(value="StopTime", type="integer")
   @JsonProperty("StopTime")
   public void setStopTime(int stopTime) {
      StopTime = stopTime;
   }
   
   @ColumnProperty(value="TimesWatched", type="integer")
   @JsonProperty("TimesWatched")
   public int getTimesWatched() {
      return TimesWatched;
   }
   
   @ColumnProperty(value="TimesWatched", type="integer")
   @JsonProperty("TimesWatched")
   public void setTimesWatched(int timesWatched) {
      TimesWatched = timesWatched;
   }
   
   

}
