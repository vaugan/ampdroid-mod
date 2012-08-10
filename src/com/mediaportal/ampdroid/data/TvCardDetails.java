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

public class TvCardDetails {
   boolean CAM;
   int CamType;
   int DecryptLimit;
   String DevicePath;
   boolean Enabled;
   boolean GrabEPG;
   int IdCard;
   int IdServer;
   boolean IsChanged;
   Date LastEpgGrab;
   String Name;
   int netProvider;
   boolean PreloadCard;
   int Priority;
   String RecordingFolder;
   int RecordingFormat;
   boolean SupportSubChannels;
   String TimeShiftFolder;

   @Override
   public String toString() {
      if (Name != null) {
         return Name;
      } else
         return "[Unknown Card]";
   }
   
   @ColumnProperty(value="CAM", type="boolean")
   @JsonProperty("CAM")
   public boolean isCAM() {
      return CAM;
   }
   
   @ColumnProperty(value="CAM", type="boolean")
   @JsonProperty("CAM")
   public void setCAM(boolean cAM) {
      CAM = cAM;
   }
   
   @ColumnProperty(value="CamType", type="integer")
   @JsonProperty("CamType")
   public int getCamType() {
      return CamType;
   }
   
   @ColumnProperty(value="CamType", type="integer")
   @JsonProperty("CamType")
   public void setCamType(int camType) {
      CamType = camType;
   }
   
   @ColumnProperty(value="DecryptLimit", type="integer")
   @JsonProperty("DecryptLimit")
   public int getDecryptLimit() {
      return DecryptLimit;
   }
   
   @ColumnProperty(value="DecryptLimit", type="integer")
   @JsonProperty("DecryptLimit")
   public void setDecryptLimit(int decryptLimit) {
      DecryptLimit = decryptLimit;
   }
   
   @ColumnProperty(value="DevicePath", type="text")
   @JsonProperty("DevicePath")
   public String getDevicePath() {
      return DevicePath;
   }
   
   @ColumnProperty(value="DevicePath", type="text")
   @JsonProperty("DevicePath")
   public void setDevicePath(String devicePath) {
      DevicePath = devicePath;
   }
   
   @ColumnProperty(value="Enabled", type="boolean")
   @JsonProperty("Enabled")
   public boolean isEnabled() {
      return Enabled;
   }
   
   @ColumnProperty(value="Enabled", type="boolean")
   @JsonProperty("Enabled")
   public void setEnabled(boolean enabled) {
      Enabled = enabled;
   }
   
   @ColumnProperty(value="GrabEPG", type="boolean")
   @JsonProperty("GrabEPG")
   public boolean isGrabEPG() {
      return GrabEPG;
   }
   
   @ColumnProperty(value="GrabEPG", type="boolean")
   @JsonProperty("GrabEPG")
   public void setGrabEPG(boolean grabEPG) {
      GrabEPG = grabEPG;
   }
   
   @ColumnProperty(value="IdCard", type="integer")
   @JsonProperty("IdCard")
   public int getIdCard() {
      return IdCard;
   }
   
   @ColumnProperty(value="IdCard", type="integer")
   @JsonProperty("IdCard")
   public void setIdCard(int idCard) {
      IdCard = idCard;
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
   
   @ColumnProperty(value="LastEpgGrab", type="date")
   @JsonProperty("LastEpgGrab")
   public Date getLastEpgGrab() {
      return LastEpgGrab;
   }
   
   @ColumnProperty(value="LastEpgGrab", type="date")
   @JsonProperty("LastEpgGrab")
   public void setLastEpgGrab(Date lastEpgGrab) {
      LastEpgGrab = lastEpgGrab;
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
   
   @ColumnProperty(value="CardName", type="integer")
   @JsonProperty("CardName")
   public int getNetProvider() {
      return netProvider;
   }
   
   @ColumnProperty(value="CardName", type="integer")
   @JsonProperty("CardName")
   public void setNetProvider(int netProvider) {
      this.netProvider = netProvider;
   }
   
   @ColumnProperty(value="PreloadCard", type="boolean")
   @JsonProperty("PreloadCard")
   public boolean isPreloadCard() {
      return PreloadCard;
   }
   
   @ColumnProperty(value="PreloadCard", type="boolean")
   @JsonProperty("PreloadCard")
   public void setPreloadCard(boolean preloadCard) {
      PreloadCard = preloadCard;
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
   
   @ColumnProperty(value="RecordingFolder", type="text")
   @JsonProperty("RecordingFolder")
   public String getRecordingFolder() {
      return RecordingFolder;
   }
   
   @ColumnProperty(value="RecordingFolder", type="text")
   @JsonProperty("RecordingFolder")
   public void setRecordingFolder(String recordingFolder) {
      RecordingFolder = recordingFolder;
   }
   
   @ColumnProperty(value="RecordingFormat", type="integer")
   @JsonProperty("RecordingFormat")
   public int getRecordingFormat() {
      return RecordingFormat;
   }
   
   @ColumnProperty(value="RecordingFormat", type="integer")
   @JsonProperty("RecordingFormat")
   public void setRecordingFormat(int recordingFormat) {
      RecordingFormat = recordingFormat;
   }
   
   @ColumnProperty(value="SupportSubChannels", type="boolean")
   @JsonProperty("SupportSubChannels")
   public boolean isSupportSubChannels() {
      return SupportSubChannels;
   }
   
   @ColumnProperty(value="SupportSubChannels", type="boolean")
   @JsonProperty("SupportSubChannels")
   public void setSupportSubChannels(boolean supportSubChannels) {
      this.SupportSubChannels = supportSubChannels;
   }
   
   @ColumnProperty(value="TimeShiftFolder", type="text")
   @JsonProperty("TimeShiftFolder")
   public String getTimeShiftFolder() {
      return TimeShiftFolder;
   }
   
   @ColumnProperty(value="TimeShiftFolder", type="text")
   @JsonProperty("TimeShiftFolder")
   public void setTimeShiftFolder(String timeShiftFolder) {
      TimeShiftFolder = timeShiftFolder;
   }

}
