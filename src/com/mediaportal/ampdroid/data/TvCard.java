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

public class TvCard {
   String CardName;
   TvChannel Channel;
   boolean IsTimeShifting;
   boolean IsRecording;

   @Override
   public String toString() {
      if (CardName != null) {
         return CardName;
      } else
         return "[Unknown Card]";
   }
   
   @ColumnProperty(value="CardName", type="text")
   public String getCardName() {
      return CardName;
   }
   
   @ColumnProperty(value="CardName", type="text")
   @JsonProperty("CardName")
   public void setCardName(String cardName) {
      CardName = cardName;
   }
   
   @JsonProperty("Channel")
   public TvChannel getChannel() {
      return Channel;
   }

   @JsonProperty("Channel")
   public void setChannel(TvChannel channel) {
      Channel = channel;
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
}
