package com.mediaportal.ampdroid.data;

import org.codehaus.jackson.annotate.JsonProperty;

import com.mediaportal.ampdroid.database.ColumnProperty;

public class ChannelState {
   public enum State {
      NotTunable, Tunable, Timeshifting, Recording
   }

   private int mChannelId;
   private State mChannelState;

   @ColumnProperty(value = "ChannelId", type = "integer")
   @JsonProperty("ChannelId")
   public int getChannelId() {
      return mChannelId;
   }

   @ColumnProperty(value = "ChannelId", type = "integer")
   @JsonProperty("ChannelId")
   public void setChannelId(int channelId) {
      mChannelId = channelId;
   }

   @ColumnProperty(value = "ChannelState", type = "text")
   @JsonProperty("ChannelState")
   public State getChannelState() {
      return mChannelState;
   }

   @ColumnProperty(value = "ChannelState", type = "text")
   @JsonProperty("ChannelState")
   public void setChannelState(State channelState) {
      mChannelState = channelState;
   }

}
