package com.mediaportal.ampdroid.api.wifiremote;

import org.codehaus.jackson.annotate.JsonProperty;

public class WifiRemoteNowPlayingMediaInfo {
   private String mMediaType;
   private String mTitle;

   private String mSeriesName;
   private String mSeason;
   private String mEpisodeIndex;

   private String mItemId;

   @JsonProperty("MediaType")
   public String getMediaType() {
      return mMediaType;
   }

   @JsonProperty("MediaType")
   public void setMediaType(String mediaType) {
      mMediaType = mediaType;
   }

   @JsonProperty("Title")
   public String getTitle() {
      return mTitle;
   }

   @JsonProperty("Title")
   public void setTitle(String title) {
      mTitle = title;
   }

   @JsonProperty("Series")
   public String getSeriesName() {
      return mSeriesName;
   }

   @JsonProperty("Series")
   public void setSeriesName(String seriesName) {
      mSeriesName = seriesName;
   }

   @JsonProperty("Season")
   public String getSeason() {
      return mSeason;
   }

   @JsonProperty("Season")
   public void setSeason(String season) {
      mSeason = season;
   }

   @JsonProperty("Episode")
   public String getEpisodeIndex() {
      return mEpisodeIndex;
   }

   @JsonProperty("Episode")
   public void setEpisodeIndex(String episodeId) {
      mEpisodeIndex = episodeId;
   }

   @JsonProperty("ItemId")
   public void setItemId(String itemId) {
      mItemId = itemId;
   }

   @JsonProperty("ItemId")
   public String getItemId() {
      return mItemId;
   }
}
