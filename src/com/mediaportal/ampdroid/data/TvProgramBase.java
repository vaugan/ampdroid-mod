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

public class TvProgramBase {
   int IdChannel;
   int IdProgram;
   String Title;
   String Description;
   Date EndTime;
   Date StartTime;
   
   int DurationInMinutes;
   boolean IsScheduled;

   
   @ColumnProperty(value="IdProgram", type="integer")
   @JsonProperty("IdChannel")
   public int getIdProgram() {
      return IdProgram;
   }
   
   @ColumnProperty(value="IdProgram", type="integer")
   @JsonProperty("IdProgram")
   public void setIdProgram(int idProgram) {
      IdProgram = idProgram;
   }

   @ColumnProperty(value="DurationInMinutes", type="integer")
   @JsonProperty("DurationInMinutes")
   public int getDurationInMinutes() {
      return DurationInMinutes;
   }

   @ColumnProperty(value="DurationInMinutes", type="integer")
   @JsonProperty("DurationInMinutes")
   public void setDurationInMinutes(int durationInMinutes) {
      DurationInMinutes = durationInMinutes;
   }

   @ColumnProperty(value="IsScheduled", type="boolean")
   @JsonProperty("IsScheduled")
   public boolean isIsScheduled() {
      return IsScheduled;
   }

   @ColumnProperty(value="IsScheduled", type="boolean")
   @JsonProperty("IsScheduled")
   public void setIsScheduled(boolean isScheduled) {
      IsScheduled = isScheduled;
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



   private Object mTag;
   public void setTag(Object tag) {
      mTag = tag;
   }

   public Object getTag() {
      return mTag;
   }
   

   
   
}
