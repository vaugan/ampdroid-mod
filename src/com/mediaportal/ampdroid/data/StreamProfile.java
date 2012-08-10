package com.mediaportal.ampdroid.data;

import org.codehaus.jackson.annotate.JsonProperty;

import com.mediaportal.ampdroid.database.ColumnProperty;

public class StreamProfile {
   public static final String TABLE_NAME = "StreamingProfiles";
   private String mName;
   private String mDescription;
   private boolean mUseTranscoding;
   private String mMIME;
   private int mMaxOutputWidth;
   private int mMaxOutputHeight;
   private String mTarget;
   private String mBandwidth;
   private boolean mIsTv;

   @ColumnProperty(value = "Name", type = "text")
   @JsonProperty("Name")
   public String getName() {
      return mName;
   }

   @ColumnProperty(value = "Name", type = "text")
   @JsonProperty("Name")
   public void setName(String name) {
      mName = name;
   }

   @ColumnProperty(value = "Description", type = "text")
   @JsonProperty("Description")
   public String getDescription() {
      return mDescription;
   }

   @ColumnProperty(value = "Description", type = "text")
   @JsonProperty("Description")
   public void setDescription(String description) {
      mDescription = description;
   }

   @ColumnProperty(value = "UseTranscoding", type = "boolean")
   @JsonProperty("UseTranscoding")
   public boolean isUseTranscoding() {
      return mUseTranscoding;
   }

   @ColumnProperty(value = "UseTranscoding", type = "boolean")
   @JsonProperty("UseTranscoding")
   public void setUseTranscoding(boolean useTranscoding) {
      mUseTranscoding = useTranscoding;
   }

   @ColumnProperty(value = "MIME", type = "text")
   @JsonProperty("MIME")
   public String getMIME() {
      return mMIME;
   }

   @ColumnProperty(value = "MIME", type = "text")
   @JsonProperty("MIME")
   public void setMIME(String mime) {
      mMIME = mime;
   }

   @ColumnProperty(value = "MaxOutputWidth", type = "integer")
   @JsonProperty("MaxOutputWidth")
   public int getMaxOutputWidth() {
      return mMaxOutputWidth;
   }

   @ColumnProperty(value = "MaxOutputWidth", type = "integer")
   @JsonProperty("MaxOutputWidth")
   public void setMaxOutputWidth(int maxOutputWidth) {
      mMaxOutputWidth = maxOutputWidth;
   }

   @ColumnProperty(value = "MaxOutputHeight", type = "integer")
   @JsonProperty("MaxOutputHeight")
   public int getMaxOutputHeight() {
      return mMaxOutputHeight;
   }

   @ColumnProperty(value = "MaxOutputHeight", type = "integer")
   @JsonProperty("MaxOutputHeight")
   public void setMaxOutputHeight(int maxOutputHeight) {
      mMaxOutputHeight = maxOutputHeight;
   }

   @ColumnProperty(value = "Target", type = "text")
   @JsonProperty("Target")
   public String getTarget() {
      return mTarget;
   }

   @ColumnProperty(value = "Target", type = "text")
   @JsonProperty("Target")
   public void setTarget(String target) {
      mTarget = target;
   }

   @ColumnProperty(value = "Bandwidth", type = "text")
   @JsonProperty("Bandwidth")
   public String getBandwidth() {
      return mBandwidth;
   }

   @ColumnProperty(value = "Bandwidth", type = "text")
   @JsonProperty("Bandwidth")
   public void setBandwidth(String bandwidth) {
      mBandwidth = bandwidth;
   }

   @ColumnProperty(value = "IsTv", type = "text")
   @JsonProperty("IsTv")
   public void setIsTv(boolean isTv) {
      mIsTv = isTv;
   }

   @ColumnProperty(value = "IsTv", type = "text")
   @JsonProperty("IsTv")
   public boolean isIsTv() {
      return mIsTv;
   }
}
