package com.mediaportal.ampdroid.data;

import org.codehaus.jackson.annotate.JsonProperty;

import com.mediaportal.ampdroid.database.ColumnProperty;

public class MediaInfo {
   private int mDuration;
   private int mDisplayAspectRatio;
   private String mDisplayAspectRatioString;
   private String mSourceCodec;
   private boolean mStreamingNeedsPreconversion;

   @ColumnProperty(value = "Duration", type = "integer")
   @JsonProperty("Duration")
   public int getDuration() {
      return mDuration;
   }

   @ColumnProperty(value = "Duration", type = "integer")
   @JsonProperty("Duration")
   public void setDuration(int duration) {
      mDuration = duration;
   }

   @ColumnProperty(value = "DisplayAspectRatio", type = "integer")
   @JsonProperty("DisplayAspectRatio")
   public int getAspectRatio() {
      return mDisplayAspectRatio;
   }

   @ColumnProperty(value = "DisplayAspectRatio", type = "integer")
   @JsonProperty("DisplayAspectRatio")
   public void setAspectRatio(int aspectRatio) {
      mDisplayAspectRatio = aspectRatio;
   }

   @ColumnProperty(value = "SourceCodec", type = "text")
   @JsonProperty("SourceCodec")
   public void setSourceCodec(String sourceCodec) {
      mSourceCodec = sourceCodec;
   }
   @ColumnProperty(value = "SourceCodec", type = "text")
   @JsonProperty("SourceCodec")
   public String getSourceCodec() {
      return mSourceCodec;
   }
   @ColumnProperty(value = "DisplayAspectRatioString", type = "text")
   @JsonProperty("DisplayAspectRatioString")
   public void setDisplayAspectRatioString(String displayAspectRatioString) {
      mDisplayAspectRatioString = displayAspectRatioString;
   }
   @ColumnProperty(value = "DisplayAspectRatioString", type = "text")
   @JsonProperty("DisplayAspectRatioString")
   public String getDisplayAspectRatioString() {
      return mDisplayAspectRatioString;
   }

   @ColumnProperty(value = "StreamingNeedsPreconversion", type = "boolean")
   @JsonProperty("StreamingNeedsPreconversion")
   public void setStreamingNeedsPreconversion(boolean streamingNeedsPreconversion) {
      mStreamingNeedsPreconversion = streamingNeedsPreconversion;
   }

   @ColumnProperty(value = "StreamingNeedsPreconversion", type = "boolean")
   @JsonProperty("StreamingNeedsPreconversion")
   public boolean isStreamingNeedsPreconversion() {
      return mStreamingNeedsPreconversion;
   }

}
