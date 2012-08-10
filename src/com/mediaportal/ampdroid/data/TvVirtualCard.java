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

public class TvVirtualCard {
   int BitRateMode;
   String ChannelName;
   String Device;
   boolean Enabled;
   int GetTimeshiftStoppedReason;
   boolean GrabTeletext;
   boolean HasTeletext;
   int Id;
   int IdChannel;
   boolean IsGrabbingEpg;
   boolean IsRecording;
   boolean IsScanning;
   boolean IsScrambled;
   boolean IsTimeShifting;
   boolean IsTunerLocked;
   int MaxChannel;
   int MinChannel;
   String Name;
   int QualityType;
   String RecordingFileName;
   String RecordingFolder;
   int RecordingFormat;
   int RecordingScheduleId;
   Date RecordingStarted;
   String RemoteServer;
   String RTSPUrl;
   int SignalLevel;
   int SignalQuality;
   String TimeShiftFileName;
   String TimeshiftFolder;
   Date TimeShiftStarted;
   int Type;

   TvUser User;

   @Override
   public String toString() {
      if (ChannelName != null) {
         if (User != null) {
            return ChannelName + " - " + User.Name;
         } else {
            return ChannelName;
         }
      } else if (Name != null) {
         return Name;
      } else
         return "[Unknown Card]";
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
   
   @ColumnProperty(value="ChannelName", type="text")
   @JsonProperty("ChannelName")
   public String getChannelName() {
      return ChannelName;
   }
   
   @ColumnProperty(value="ChannelName", type="text")
   @JsonProperty("ChannelName")
   public void setChannelName(String channelName) {
      ChannelName = channelName;
   }
   
   @ColumnProperty(value="Device", type="text")
   @JsonProperty("Device")
   public String getDevice() {
      return Device;
   }
   
   @ColumnProperty(value="Device", type="text")
   @JsonProperty("Device")
   public void setDevice(String device) {
      Device = device;
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
   
   @ColumnProperty(value="GetTimeshiftStoppedReason", type="integer")
   @JsonProperty("GetTimeshiftStoppedReason")
   public int getGetTimeshiftStoppedReason() {
      return GetTimeshiftStoppedReason;
   }
   
   @ColumnProperty(value="GetTimeshiftStoppedReason", type="integer")
   @JsonProperty("GetTimeshiftStoppedReason")
   public void setGetTimeshiftStoppedReason(int getTimeshiftStoppedReason) {
      GetTimeshiftStoppedReason = getTimeshiftStoppedReason;
   }
   
   @ColumnProperty(value="GrabTeletext", type="boolean")
   @JsonProperty("GrabTeletext")
   public boolean isGrabTeletext() {
      return GrabTeletext;
   }
   
   @ColumnProperty(value="GrabTeletext", type="boolean")
   @JsonProperty("GrabTeletext")
   public void setGrabTeletext(boolean grabTeletext) {
      GrabTeletext = grabTeletext;
   }
   
   @ColumnProperty(value="HasTeletext", type="boolean")
   @JsonProperty("HasTeletext")
   public boolean isHasTeletext() {
      return HasTeletext;
   }
   
   @ColumnProperty(value="HasTeletext", type="boolean")
   @JsonProperty("HasTeletext")
   public void setHasTeletext(boolean hasTeletext) {
      HasTeletext = hasTeletext;
   }
   
   @ColumnProperty(value="Id", type="integer")
   @JsonProperty("Id")
   public int getId() {
      return Id;
   }
   
   @ColumnProperty(value="Id", type="integer")
   @JsonProperty("Id")
   public void setId(int id) {
      Id = id;
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
   
   @ColumnProperty(value="IsGrabbingEpg", type="boolean")
   @JsonProperty("IsGrabbingEpg")
   public boolean isIsGrabbingEpg() {
      return IsGrabbingEpg;
   }
   
   @ColumnProperty(value="IsGrabbingEpg", type="boolean")
   @JsonProperty("IsGrabbingEpg")
   public void setIsGrabbingEpg(boolean isGrabbingEpg) {
      IsGrabbingEpg = isGrabbingEpg;
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
   
   @ColumnProperty(value="IsScanning", type="boolean")
   @JsonProperty("IsScanning")
   public boolean isIsScanning() {
      return IsScanning;
   }
   
   @ColumnProperty(value="IsScanning", type="boolean")
   @JsonProperty("IsScanning")
   public void setIsScanning(boolean isScanning) {
      IsScanning = isScanning;
   }
   
   @ColumnProperty(value="IsScrambled", type="boolean")
   @JsonProperty("IsScrambled")
   public boolean isIsScrambled() {
      return IsScrambled;
   }
   
   @ColumnProperty(value="IsScrambled", type="boolean")
   @JsonProperty("IsScrambled")
   public void setIsScrambled(boolean isScrambled) {
      IsScrambled = isScrambled;
   }
   
   @ColumnProperty(value="IsTimeShifting", type="boolean")
   @JsonProperty("IsTimeShifting")
   public boolean isIsTimeShifting() {
      return IsTimeShifting;
   }
   
   @ColumnProperty(value="IsTimeShifting", type="boolean")
   @JsonProperty("IsTimeShifting")
   public void setIsTimeShifting(boolean isTimeShifting) {
      IsTimeShifting = isTimeShifting;
   }
   
   @ColumnProperty(value="IsTunerLocked", type="boolean")
   @JsonProperty("IsTunerLocked")
   public boolean isIsTunerLocked() {
      return IsTunerLocked;
   }
   
   @ColumnProperty(value="IsTunerLocked", type="boolean")
   @JsonProperty("IsTunerLocked")
   public void setIsTunerLocked(boolean isTunerLocked) {
      IsTunerLocked = isTunerLocked;
   }
   
   @ColumnProperty(value="MaxChannel", type="integer")
   @JsonProperty("MaxChannel")
   public int getMaxChannel() {
      return MaxChannel;
   }
   
   @ColumnProperty(value="MaxChannel", type="integer")
   @JsonProperty("MaxChannel")
   public void setMaxChannel(int maxChannel) {
      MaxChannel = maxChannel;
   }
   
   @ColumnProperty(value="MinChannel", type="integer")
   @JsonProperty("MinChannel")
   public int getMinChannel() {
      return MinChannel;
   }
   
   @ColumnProperty(value="MinChannel", type="integer")
   @JsonProperty("MinChannel")
   public void setMinChannel(int minChannel) {
      MinChannel = minChannel;
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
   
   @ColumnProperty(value="RecordingFileName", type="text")
   @JsonProperty("RecordingFileName")
   public String getRecordingFileName() {
      return RecordingFileName;
   }
   
   @ColumnProperty(value="RecordingFileName", type="text")
   @JsonProperty("RecordingFileName")
   public void setRecordingFileName(String recordingFileName) {
      RecordingFileName = recordingFileName;
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
   
   @ColumnProperty(value="RecordingScheduleId", type="integer")
   @JsonProperty("RecordingScheduleId")
   public int getRecordingScheduleId() {
      return RecordingScheduleId;
   }
   
   @ColumnProperty(value="RecordingScheduleId", type="integer")
   @JsonProperty("RecordingScheduleId")
   public void setRecordingScheduleId(int recordingScheduleId) {
      RecordingScheduleId = recordingScheduleId;
   }
   
   @ColumnProperty(value="RecordingStarted", type="date")
   @JsonProperty("RecordingStarted")
   public Date getRecordingStarted() {
      return RecordingStarted;
   }
   
   @ColumnProperty(value="RecordingStarted", type="date")
   @JsonProperty("RecordingStarted")
   public void setRecordingStarted(Date recordingStarted) {
      RecordingStarted = recordingStarted;
   }
   
   @ColumnProperty(value="RemoteServer", type="text")
   @JsonProperty("RemoteServer")
   public String getRemoteServer() {
      return RemoteServer;
   }
   
   @ColumnProperty(value="RemoteServer", type="text")
   @JsonProperty("RemoteServer")
   public void setRemoteServer(String remoteServer) {
      RemoteServer = remoteServer;
   }
   
   @ColumnProperty(value="RTSPUrl", type="text")
   @JsonProperty("RTSPUrl")
   public String getRTSPUrl() {
      return RTSPUrl;
   }
   
   @ColumnProperty(value="RTSPUrl", type="text")
   @JsonProperty("RTSPUrl")
   public void setRTSPUrl(String rTSPUrl) {
      RTSPUrl = rTSPUrl;
   }
   
   @ColumnProperty(value="SignalLevel", type="integer")
   @JsonProperty("SignalLevel")
   public int getSignalLevel() {
      return SignalLevel;
   }
   
   @ColumnProperty(value="SignalLevel", type="integer")
   @JsonProperty("SignalLevel")
   public void setSignalLevel(int signalLevel) {
      SignalLevel = signalLevel;
   }
   
   @ColumnProperty(value="SignalQuality", type="integer")
   @JsonProperty("SignalQuality")
   public int getSignalQuality() {
      return SignalQuality;
   }
   
   @ColumnProperty(value="SignalQuality", type="integer")
   @JsonProperty("SignalQuality")
   public void setSignalQuality(int signalQuality) {
      SignalQuality = signalQuality;
   }
   
   @ColumnProperty(value="TimeShiftFileName", type="text")
   @JsonProperty("TimeShiftFileName")
   public String getTimeShiftFileName() {
      return TimeShiftFileName;
   }
   
   @ColumnProperty(value="TimeShiftFileName", type="text")
   @JsonProperty("TimeShiftFileName")
   public void setTimeShiftFileName(String timeShiftFileName) {
      TimeShiftFileName = timeShiftFileName;
   }
   
   @ColumnProperty(value="TimeshiftFolder", type="text")
   @JsonProperty("TimeshiftFolder")
   public String getTimeshiftFolder() {
      return TimeshiftFolder;
   }
   
   @ColumnProperty(value="TimeshiftFolder", type="text")
   @JsonProperty("TimeshiftFolder")
   public void setTimeshiftFolder(String timeshiftFolder) {
      TimeshiftFolder = timeshiftFolder;
   }
   
   @ColumnProperty(value="TimeShiftStarted", type="date")
   @JsonProperty("TimeShiftStarted")
   public Date getTimeShiftStarted() {
      return TimeShiftStarted;
   }
   
   @ColumnProperty(value="TimeShiftStarted", type="date")
   @JsonProperty("TimeShiftStarted")
   public void setTimeShiftStarted(Date timeShiftStarted) {
      TimeShiftStarted = timeShiftStarted;
   }
   
   @ColumnProperty(value="Type", type="integer")
   @JsonProperty("Type")
   public int getType() {
      return Type;
   }
   
   @ColumnProperty(value="Type", type="integer")
   @JsonProperty("Type")
   public void setType(int type) {
      Type = type;
   }

   @JsonProperty("User")
   public TvUser getUser() {
      return User;
   }
   
   @JsonProperty("User")
   public void setUser(TvUser user) {
      User = user;
   }
}
