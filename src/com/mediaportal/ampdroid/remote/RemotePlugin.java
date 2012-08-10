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
package com.mediaportal.ampdroid.remote;

import org.codehaus.jackson.annotate.JsonProperty;

public class RemotePlugin {
   private String mName;
   private int mWindowId;
   private byte[] mIcon;
   
   @JsonProperty("Name")
   public String getName() {
      return mName;
   }
   
   @JsonProperty("Name")
   public void setName(String name) {
      mName = name;
   }
   
   @JsonProperty("WindowId")
   public int getWindowId() {
      return mWindowId;
   }
   
   @JsonProperty("WindowId")
   public void setWindowId(int windowId) {
      mWindowId = windowId;
   }
   
   @JsonProperty("Icon")
   public byte[] getIcon() {
      return mIcon;
   }
   
   @JsonProperty("Icon")
   public void setIcon(byte[] icon) {
      mIcon = icon;
   }
}
