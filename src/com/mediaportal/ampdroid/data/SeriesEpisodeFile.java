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

import org.codehaus.jackson.annotate.JsonProperty;

import com.mediaportal.ampdroid.database.ColumnProperty;

public class SeriesEpisodeFile {
   private String FileName;// EpisodeFileName
   private int EpisodeIndex;// EpisodeIndex
   private int SeasonIndex;// SeasonIndex
   private boolean IsAvailable;// IsAvailable
   private boolean IsRemovable;// Removable
   private int Duration;// localPlaytime
   private int VideoWidth;// videoWidth
   private int VideoHeight;// videoHeight

   private String VideoCodec;// VideoCodec
   private int VideoBitrate;// VideoBitrate
   private float VideoFrameRate;// VideoFrameRate
   private String AudioCodec;// AudioCodec
   private int AudioBitrate;// AudioBitrate
   private int AudioChannels;// AudioChannels
   private int AudioTracks;// AudioTracks
   private boolean HasSubtitles; // AvailableSubtitles

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

   @ColumnProperty(value="EpisodeIndex", type="integer")
   @JsonProperty("EpisodeIndex")
   public int getEpisodeIndex() {
      return EpisodeIndex;
   }

   @ColumnProperty(value="EpisodeIndex", type="integer")
   @JsonProperty("EpisodeIndex")
   public void setEpisodeIndex(int episodeIndex) {
      EpisodeIndex = episodeIndex;
   }

   @ColumnProperty(value="SeasonIndex", type="integer")
   @JsonProperty("SeasonIndex")
   public int getSeasonIndex() {
      return SeasonIndex;
   }

   @ColumnProperty(value="SeasonIndex", type="integer")
   @JsonProperty("SeasonIndex")
   public void setSeasonIndex(int seasonIndex) {
      SeasonIndex = seasonIndex;
   }

   @ColumnProperty(value="IsAvailable", type="boolean")
   @JsonProperty("IsAvailable")
   public boolean isIsAvailable() {
      return IsAvailable;
   }

   @ColumnProperty(value="IsAvailable", type="boolean")
   @JsonProperty("IsAvailable")
   public void setIsAvailable(boolean isAvailable) {
      IsAvailable = isAvailable;
   }

   @ColumnProperty(value="IsRemovable", type="boolean")
   @JsonProperty("IsRemovable")
   public boolean isIsRemovable() {
      return IsRemovable;
   }

   @ColumnProperty(value="IsRemovable", type="boolean")
   @JsonProperty("IsRemovable")
   public void setIsRemovable(boolean isRemovable) {
      IsRemovable = isRemovable;
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

   @ColumnProperty(value="VideoBitrate", type="integer")
   @JsonProperty("VideoBitrate")
   public int getVideoBitrate() {
      return VideoBitrate;
   }

   @ColumnProperty(value="VideoBitrate", type="integer")
   @JsonProperty("VideoBitrate")
   public void setVideoBitrate(int videoBitrate) {
      VideoBitrate = videoBitrate;
   }

   @ColumnProperty(value="VideoFrameRate", type="float")
   @JsonProperty("VideoFrameRate")
   public float getVideoFrameRate() {
      return VideoFrameRate;
   }

   @ColumnProperty(value="VideoFrameRate", type="float")
   @JsonProperty("VideoFrameRate")
   public void setVideoFrameRate(float videoFrameRate) {
      VideoFrameRate = videoFrameRate;
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

   @ColumnProperty(value="AudioBitrate", type="integer")
   @JsonProperty("AudioBitrate")
   public int getAudioBitrate() {
      return AudioBitrate;
   }

   @ColumnProperty(value="AudioBitrate", type="integer")
   @JsonProperty("AudioBitrate")
   public void setAudioBitrate(int audioBitrate) {
      AudioBitrate = audioBitrate;
   }

   @ColumnProperty(value="AudioChannels", type="integer")
   @JsonProperty("AudioChannels")
   public int getAudioChannels() {
      return AudioChannels;
   }

   @ColumnProperty(value="AudioChannels", type="integer")
   @JsonProperty("AudioChannels")
   public void setAudioChannels(int audioChannels) {
      AudioChannels = audioChannels;
   }

   @ColumnProperty(value="AudioTracks", type="integer")
   @JsonProperty("AudioTracks")
   public int getAudioTracks() {
      return AudioTracks;
   }

   @ColumnProperty(value="AudioTracks", type="integer")
   @JsonProperty("AudioTracks")
   public void setAudioTracks(int audioTracks) {
      AudioTracks = audioTracks;
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
}
