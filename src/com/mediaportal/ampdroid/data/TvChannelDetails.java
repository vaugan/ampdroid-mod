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

public class TvChannelDetails extends TvChannel{
   private TvProgram CurrentProgram;
   private boolean EpgHasGaps;
   private String ExternalId;
   private boolean FreeToAir;
   private boolean GrabEpg;
   private boolean IsChanged;
   private boolean IsRadio;
   private boolean IsTv;
   private Date LastGrabTime;
   private TvProgram NextProgram;
   private int SortOrder;
   private int TimesWatched;
   private Date TotalTimeWatched;
   private boolean VisibleInGuide;
   private String[] GroupNames;
   
   @ColumnProperty(value="GroupNames", type="textarray")
   @JsonProperty("GroupNames")
   public String[] getGroupNames() {
      return GroupNames;
   }
   
   @ColumnProperty(value="GroupNames", type="textarray")
   @JsonProperty("GroupNames")
   public void setGroupNames(String[] groupNames) {
      GroupNames = groupNames;
   }
   
   @JsonProperty("CurrentProgram")
   public TvProgram getCurrentProgram() {
      return CurrentProgram;
   }
   
   @JsonProperty("CurrentProgram")
   public void setCurrentProgram(TvProgram currentProgram) {
      CurrentProgram = currentProgram;
   }
   
   @ColumnProperty(value="EpgHasGaps", type="boolean")
   @JsonProperty("EpgHasGaps")
   public boolean isEpgHasGaps() {
      return EpgHasGaps;
   }
   
   @ColumnProperty(value="EpgHasGaps", type="boolean")
   @JsonProperty("EpgHasGaps")
   public void setEpgHasGaps(boolean epgHasGaps) {
      EpgHasGaps = epgHasGaps;
   }
   
   @ColumnProperty(value="ExternalId", type="text")
   @JsonProperty("ExternalId")
   public String getExternalId() {
      return ExternalId;
   }
   
   @ColumnProperty(value="ExternalId", type="text")
   @JsonProperty("ExternalId")
   public void setExternalId(String externalId) {
      ExternalId = externalId;
   }
   
   @ColumnProperty(value="FreeToAir", type="boolean")
   @JsonProperty("FreeToAir")
   public boolean isFreeToAir() {
      return FreeToAir;
   }
   
   @ColumnProperty(value="FreeToAir", type="boolean")
   @JsonProperty("FreeToAir")
   public void setFreeToAir(boolean freeToAir) {
      FreeToAir = freeToAir;
   }
   
   @ColumnProperty(value="GrabEpg", type="boolean")
   @JsonProperty("GrabEpg")
   public boolean isGrabEpg() {
      return GrabEpg;
   }
   
   @ColumnProperty(value="GrabEpg", type="boolean")
   @JsonProperty("GrabEpg")
   public void setGrabEpg(boolean grabEpg) {
      GrabEpg = grabEpg;
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
   
   @ColumnProperty(value="IsRadio", type="boolean")
   @JsonProperty("IsRadio")
   public boolean isIsRadio() {
      return IsRadio;
   }
   
   @ColumnProperty(value="IsRadio", type="boolean")
   @JsonProperty("IsRadio")
   public void setIsRadio(boolean isRadio) {
      IsRadio = isRadio;
   }
   
   @ColumnProperty(value="IsTv", type="boolean")
   @JsonProperty("IsTv")
   public boolean isIsTv() {
      return IsTv;
   }
   
   @ColumnProperty(value="IsTv", type="boolean")
   @JsonProperty("IsTv")
   public void setIsTv(boolean isTv) {
      IsTv = isTv;
   }
   
   @ColumnProperty(value="LastGrabTime", type="date")
   @JsonProperty("LastGrabTime")
   public Date getLastGrabTime() {
      return LastGrabTime;
   }
   
   @ColumnProperty(value="LastGrabTime", type="date")
   @JsonProperty("LastGrabTime")
   public void setLastGrabTime(Date lastGrabTime) {
      LastGrabTime = lastGrabTime;
   }
   
   @JsonProperty("NextProgram")
   public TvProgram getNextProgram() {
      return NextProgram;
   }
   
   @JsonProperty("NextProgram")
   public void setNextProgram(TvProgram nextProgram) {
      NextProgram = nextProgram;
   }
   
   @ColumnProperty(value="SortOrder", type="integer")
   @JsonProperty("SortOrder")
   public int getSortOrder() {
      return SortOrder;
   }
   
   @ColumnProperty(value="SortOrder", type="integer")
   @JsonProperty("SortOrder")
   public void setSortOrder(int sortOrder) {
      SortOrder = sortOrder;
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
   
   @ColumnProperty(value="TotalTimeWatched", type="date")
   @JsonProperty("TotalTimeWatched")
   public Date getTotalTimeWatched() {
      return TotalTimeWatched;
   }
   
   @ColumnProperty(value="TotalTimeWatched", type="date")
   @JsonProperty("TotalTimeWatched")
   public void setTotalTimeWatched(Date totalTimeWatched) {
      TotalTimeWatched = totalTimeWatched;
   }
   
   @ColumnProperty(value="VisibleInGuide", type="boolean")
   @JsonProperty("VisibleInGuide")
   public boolean isVisibleInGuide() {
      return VisibleInGuide;
   }
   
   @ColumnProperty(value="VisibleInGuide", type="boolean")
   @JsonProperty("VisibleInGuide")
   public void setVisibleInGuide(boolean visibleInGuide) {
      VisibleInGuide = visibleInGuide;
   }
   
   
}
