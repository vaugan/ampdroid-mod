package com.mediaportal.ampdroid.data;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.mediaportal.ampdroid.database.ColumnProperty;

public class PlaybackSession {
   public static final String TABLE_NAME = "PlaybackSessions";
   private int mVideoType;
   private String mVideoId;
   private long mStartPosition;
   private String mVideoUrl;
   private FileInfo mVideoFile;
   private MediaInfo mMetaData;
   private String mDisplayName;
   private Date mLastWatched;

   @ColumnProperty(value = "VideoType", type = "integer")
   @JsonProperty("VideoType")
   public int getVideoType() {
      return mVideoType;
   }

   @ColumnProperty(value = "VideoType", type = "integer")
   @JsonProperty("VideoType")
   public void setVideoType(int videoType) {
      mVideoType = videoType;
   }

   @ColumnProperty(value = "VideoId", type = "text")
   @JsonProperty("VideoId")
   public String getVideoId() {
      return mVideoId;
   }

   @ColumnProperty(value = "VideoId", type = "text")
   @JsonProperty("VideoId")
   public void setVideoId(String videoId) {
      mVideoId = videoId;
   }

   @ColumnProperty(value = "StartPosition", type = "real")
   @JsonProperty("StartPosition")
   public long getStartPosition() {
      return mStartPosition;
   }

   @ColumnProperty(value = "StartPosition", type = "real")
   @JsonProperty("StartPosition")
   public void setStartPosition(long startPosition) {
      mStartPosition = startPosition;
   }

   @ColumnProperty(value = "VideoUrl", type = "text")
   @JsonProperty("VideoUrl")
   public String getVideoUrl() {
      return mVideoUrl;
   }

   @ColumnProperty(value = "VideoUrl", type = "text")
   @JsonProperty("VideoUrl")
   public void setVideoUrl(String videoUrl) {
      mVideoUrl = videoUrl;
   }

   public FileInfo getVideoFile() {
      return mVideoFile;
   }

   public void setVideoFile(FileInfo videoFile) {
      mVideoFile = videoFile;
   }

   public MediaInfo getMetaData() {
      return mMetaData;
   }

   public void setMetaData(MediaInfo metaData) {
      mMetaData = metaData;
   }

   @ColumnProperty(value = "DisplayName", type = "text")
   @JsonProperty("DisplayName")
   public void setDisplayName(String displayName) {
      mDisplayName = displayName;
   }

   @ColumnProperty(value = "DisplayName", type = "text")
   @JsonProperty("DisplayName")
   public String getDisplayName() {
      return mDisplayName;
   }

   @ColumnProperty(value = "LastWatched", type = "date")
   @JsonProperty("LastWatched")
   public void setLastWatched(Date lastWatched) {
      mLastWatched = lastWatched;
   }

   @ColumnProperty(value = "LastWatched", type = "date")
   @JsonProperty("LastWatched")
   public Date getLastWatched() {
      return mLastWatched;
   }

}
