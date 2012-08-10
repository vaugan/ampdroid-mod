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

public class TvRtspClient {
   Date DateTimeStarted;
   String Description;
   String IpAdress;
   boolean IsActive;
   String StreamName;

   
   @ColumnProperty(value="DateTimeStarted", type="date")
   @JsonProperty("DateTimeStarted")
   public Date getDateTimeStarted() {
      return DateTimeStarted;
   }
   
   @ColumnProperty(value="DateTimeStarted", type="date")
   @JsonProperty("DateTimeStarted")
   public void setDateTimeStarted(Date dateTimeStarted) {
      DateTimeStarted = dateTimeStarted;
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
   
   @ColumnProperty(value="IpAdress", type="text")
   @JsonProperty("IpAdress")
   public String getIpAdress() {
      return IpAdress;
   }
   
   @ColumnProperty(value="IpAdress", type="text")
   @JsonProperty("IpAdress")
   public void setIpAdress(String ipAdress) {
      IpAdress = ipAdress;
   }
   
   @ColumnProperty(value="IsActive", type="boolean")
   @JsonProperty("IsActive")
   public boolean isIsActive() {
      return IsActive;
   }
   
   @ColumnProperty(value="IsActive", type="boolean")
   @JsonProperty("IsActive")
   public void setIsActive(boolean isActive) {
      IsActive = isActive;
   }
   
   @ColumnProperty(value="StreamName", type="text")
   @JsonProperty("StreamName")
   public String getStreamName() {
      return StreamName;
   }
   
   @ColumnProperty(value="StreamName", type="text")
   @JsonProperty("StreamName")
   public void setStreamName(String streamName) {
      StreamName = streamName;
   }

}
