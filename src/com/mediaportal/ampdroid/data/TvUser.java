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

public class TvUser {
   int CardId;
   Date HeartBeat;
   int IdChannel;
   boolean IsAdmin;
   String Name;
   int SubChannel;
   int TvStoppedReason;

   @Override
   public String toString() {
      return Name;
   }
   
   @ColumnProperty(value="CardId", type="integer")
   @JsonProperty("CardId")
   public int getCardId() {
      return CardId;
   }
   
   @ColumnProperty(value="CardId", type="integer")
   @JsonProperty("CardId")
   public void setCardId(int cardId) {
      CardId = cardId;
   }
   
   @ColumnProperty(value="HeartBeat", type="date")
   @JsonProperty("HeartBeat")
   public Date getHeartBeat() {
      return HeartBeat;
   }
   
   @ColumnProperty(value="HeartBeat", type="date")
   @JsonProperty("HeartBeat")
   public void setHeartBeat(Date heartBeat) {
      HeartBeat = heartBeat;
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
   
   @ColumnProperty(value="IsAdmin", type="boolean")
   @JsonProperty("IsAdmin")
   public boolean isIsAdmin() {
      return IsAdmin;
   }
   
   @ColumnProperty(value="IsAdmin", type="boolean")
   @JsonProperty("IsAdmin")
   public void setIsAdmin(boolean isAdmin) {
      IsAdmin = isAdmin;
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
   
   @ColumnProperty(value="SubChannel", type="integer")
   @JsonProperty("SubChannel")
   public int getSubChannel() {
      return SubChannel;
   }
   
   @ColumnProperty(value="SubChannel", type="integer")
   @JsonProperty("SubChannel")
   public void setSubChannel(int subChannel) {
      SubChannel = subChannel;
   }
   
   @ColumnProperty(value="TvStoppedReason", type="integer")
   @JsonProperty("TvStoppedReason")
   public int getTvStoppedReason() {
      return TvStoppedReason;
   }
   
   @ColumnProperty(value="TvStoppedReason", type="integer")
   @JsonProperty("TvStoppedReason")
   public void setTvStoppedReason(int tvStoppedReason) {
      TvStoppedReason = tvStoppedReason;
   }

}
