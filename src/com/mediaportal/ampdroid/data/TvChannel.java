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


public class TvChannel {
   private String DisplayName;
   private int IdChannel;
   private String Name;
   
   @Override
   public String toString() {
      if (DisplayName != null) {
         return DisplayName;
      } else
         return "[Unknown Channel]";
   }
   
   @ColumnProperty(value="DisplayName", type="text")
   @JsonProperty("DisplayName")
   public String getDisplayName() {
      return DisplayName;
   }
   
   @ColumnProperty(value="DisplayName", type="text")
   @JsonProperty("DisplayName")
   public void setDisplayName(String displayName) {
      DisplayName = displayName;
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
}
