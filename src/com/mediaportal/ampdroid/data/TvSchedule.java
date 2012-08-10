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

public class TvSchedule {
   int BitRateMode;
   Date Canceled;
   String Directory;
   boolean DoesUseEpisodeManagement;
   Date EndTime;
   int IdChannel;
   int IdParentSchedule;
   int IdSchedule;
   boolean IsChanged;
   boolean IsManual;
   Date KeepDate;
   int KeepMethod;
   int MaxAirings;
   int PostRecordInterval;
   int PreRecordInterval;
   int Priority;
   String ProgramName;
   int Quality;
   int QualityType;
   int RecommendedCard;
   int ScheduleType;
   boolean Series;
   Date StartTime;

   @Override
   public String toString() {
      return ProgramName;
   }
   
   @ColumnProperty(value="BitRateMode", type="integer")
   @JsonProperty("BitRateMode")
   public int getBitRateMode() {
      return BitRateMode;
   }
   
   @ColumnProperty(value="BitRateMode", type="integer")
   @JsonProperty("BitRateMode")
   public void setBitRateMode(int bitRateMode) {
      BitRateMode = bitRateMode;
   }
   
   @ColumnProperty(value="Canceled", type="date")
   @JsonProperty("Canceled")
   public Date getCanceled() {
      return Canceled;
   }
   
   @ColumnProperty(value="Canceled", type="date")
   @JsonProperty("Canceled")
   public void setCanceled(Date canceled) {
      Canceled = canceled;
   }
   
   @ColumnProperty(value="Directory", type="text")
   @JsonProperty("Directory")
   public String getDirectory() {
      return Directory;
   }
   
   @ColumnProperty(value="Directory", type="text")
   @JsonProperty("Directory")
   public void setDirectory(String directory) {
      Directory = directory;
   }
   
   @ColumnProperty(value="DoesUseEpisodeManagement", type="boolean")
   @JsonProperty("DoesUseEpisodeManagement")
   public boolean isDoesUseEpisodeManagement() {
      return DoesUseEpisodeManagement;
   }
   
   @ColumnProperty(value="DoesUseEpisodeManagement", type="boolean")
   @JsonProperty("DoesUseEpisodeManagement")
   public void setDoesUseEpisodeManagement(boolean doesUseEpisodeManagement) {
      DoesUseEpisodeManagement = doesUseEpisodeManagement;
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
   
   @ColumnProperty(value="IdParentSchedule", type="integer")
   @JsonProperty("IdParentSchedule")
   public int getIdParentSchedule() {
      return IdParentSchedule;
   }
   
   @ColumnProperty(value="IdParentSchedule", type="integer")
   @JsonProperty("IdParentSchedule")
   public void setIdParentSchedule(int idParentSchedule) {
      IdParentSchedule = idParentSchedule;
   }
   
   @ColumnProperty(value="IdSchedule", type="integer")
   @JsonProperty("IdSchedule")
   public int getIdSchedule() {
      return IdSchedule;
   }
   
   @ColumnProperty(value="IdSchedule", type="integer")
   @JsonProperty("IdSchedule")
   public void setIdSchedule(int idSchedule) {
      IdSchedule = idSchedule;
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
   
   @ColumnProperty(value="KeepDate", type="date")
   @JsonProperty("KeepDate")
   public Date getKeepDate() {
      return KeepDate;
   }
   
   @ColumnProperty(value="KeepDate", type="date")
   @JsonProperty("KeepDate")
   public void setKeepDate(Date keepDate) {
      KeepDate = keepDate;
   }
   
   @ColumnProperty(value="KeepMethod", type="integer")
   @JsonProperty("KeepMethod")
   public int getKeepMethod() {
      return KeepMethod;
   }
   
   @ColumnProperty(value="KeepMethod", type="integer")
   @JsonProperty("KeepMethod")
   public void setKeepMethod(int keepMethod) {
      KeepMethod = keepMethod;
   }
   
   @ColumnProperty(value="MaxAirings", type="integer")
   @JsonProperty("MaxAirings")
   public int getMaxAirings() {
      return MaxAirings;
   }
   
   @ColumnProperty(value="MaxAirings", type="integer")
   @JsonProperty("MaxAirings")
   public void setMaxAirings(int maxAirings) {
      MaxAirings = maxAirings;
   }
   
   @ColumnProperty(value="PostRecordInterval", type="integer")
   @JsonProperty("PostRecordInterval")
   public int getPostRecordInterval() {
      return PostRecordInterval;
   }
   
   @ColumnProperty(value="PostRecordInterval", type="integer")
   @JsonProperty("PostRecordInterval")
   public void setPostRecordInterval(int postRecordInterval) {
      PostRecordInterval = postRecordInterval;
   }
   
   @ColumnProperty(value="PreRecordInterval", type="integer")
   @JsonProperty("PreRecordInterval")
   public int getPreRecordInterval() {
      return PreRecordInterval;
   }
   
   @ColumnProperty(value="PreRecordInterval", type="integer")
   @JsonProperty("PreRecordInterval")
   public void setPreRecordInterval(int preRecordInterval) {
      PreRecordInterval = preRecordInterval;
   }
   
   @ColumnProperty(value="Priority", type="integer")
   @JsonProperty("Priority")
   public int getPriority() {
      return Priority;
   }
   
   @ColumnProperty(value="Priority", type="integer")
   @JsonProperty("Priority")
   public void setPriority(int priority) {
      Priority = priority;
   }
   
   @ColumnProperty(value="ProgramName", type="text")
   @JsonProperty("ProgramName")
   public String getProgramName() {
      return ProgramName;
   }
   
   @ColumnProperty(value="ProgramName", type="text")
   @JsonProperty("ProgramName")
   public void setProgramName(String programName) {
      ProgramName = programName;
   }
   
   @ColumnProperty(value="Quality", type="integer")
   @JsonProperty("Quality")
   public int getQuality() {
      return Quality;
   }
   
   @ColumnProperty(value="Quality", type="integer")
   @JsonProperty("Quality")
   public void setQuality(int quality) {
      Quality = quality;
   }
   
   @ColumnProperty(value="QualityType", type="integer")
   @JsonProperty("QualityType")
   public int getQualityType() {
      return QualityType;
   }
   
   @ColumnProperty(value="QualityType", type="integer")
   @JsonProperty("QualityType")
   public void setQualityType(int qualityType) {
      QualityType = qualityType;
   }
   
   @ColumnProperty(value="RecommendedCard", type="integer")
   @JsonProperty("RecommendedCard")
   public int getRecommendedCard() {
      return RecommendedCard;
   }
   
   @ColumnProperty(value="RecommendedCard", type="integer")
   @JsonProperty("RecommendedCard")
   public void setRecommendedCard(int recommendedCard) {
      RecommendedCard = recommendedCard;
   }
   
   @ColumnProperty(value="ScheduleType", type="integer")
   @JsonProperty("ScheduleType")
   public int getScheduleType() {
      return ScheduleType;
   }
   
   @ColumnProperty(value="ScheduleType", type="integer")
   @JsonProperty("ScheduleType")
   public void setScheduleType(int scheduleType) {
      ScheduleType = scheduleType;
   }
   
   @ColumnProperty(value="Series", type="boolean")
   @JsonProperty("Series")
   public boolean isSeries() {
      return Series;
   }
   
   @ColumnProperty(value="Series", type="boolean")
   @JsonProperty("Series")
   public void setSeries(boolean series) {
      Series = series;
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
}
