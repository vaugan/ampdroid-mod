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

public class RemoteWelcomeMessage {
   private int mServerVersion;
   private RemoteStatusMessage mStatus;
   private RemoteVolumeMessage mVolume;

   @JsonProperty("Server_Version")
   public void setServerVersion(int serverVersion) {
      mServerVersion = serverVersion;
   }

   @JsonProperty("Server_Version")
   public int getServerVersion() {
      return mServerVersion;
   }

   @JsonProperty("Status")
   public void setStatus(RemoteStatusMessage status) {
      mStatus = status;
   }

   @JsonProperty("Status")
   public RemoteStatusMessage getStatus() {
      return mStatus;
   }

   @JsonProperty("Volume")
   public void setVolume(RemoteVolumeMessage volume) {
      mVolume = volume;
   }

   @JsonProperty("Volume")
   public RemoteVolumeMessage getVolume() {
      return mVolume;
   }
}
