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

public class RemoteVolumeMessage {
   private int mVolume;
   private boolean mIsMuted;
   
   @JsonProperty("Volume")
   public int getVolume() {
      return mVolume;
   }
   
   @JsonProperty("Volume")
   public void setVolume(int volume) {
      mVolume = volume;
   }
   
   @JsonProperty("IsMuted")
   public boolean isIsMuted() {
      return mIsMuted;
   }
   
   @JsonProperty("IsMuted")
   public void setIsMuted(boolean isMuted) {
      mIsMuted = isMuted;
   }
   
   
}
