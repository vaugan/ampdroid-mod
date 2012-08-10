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
package com.mediaportal.ampdroid.api;

public class RemoteFunctions {
   private boolean tvEnabled;
   private boolean tvAvailable;
   private boolean mediaEnabled;
   private boolean mediaAvailable;
   private boolean remoteEnabled;
   private boolean remoteAvailable;
   
   
   public boolean isTvEnabled() {
      return tvEnabled;
   }
   public void setTvEnabled(boolean tvEnabled) {
      this.tvEnabled = tvEnabled;
   }
   public boolean isTvAvailable() {
      return tvAvailable;
   }
   public void setTvAvailable(boolean tvAvailable) {
      this.tvAvailable = tvAvailable;
   }
   public boolean isMediaEnabled() {
      return mediaEnabled;
   }
   public void setMediaEnabled(boolean mediaEnabled) {
      this.mediaEnabled = mediaEnabled;
   }
   public boolean isMediaAvailable() {
      return mediaAvailable;
   }
   public void setMediaAvailable(boolean mediaAvailable) {
      this.mediaAvailable = mediaAvailable;
   }
   public boolean isRemoteEnabled() {
      return remoteEnabled;
   }
   public void setRemoteEnabled(boolean remoteEnabled) {
      this.remoteEnabled = remoteEnabled;
   }
   public boolean isRemoteAvailable() {
      return remoteAvailable;
   }
   public void setRemoteAvailable(boolean remoteAvailable) {
      this.remoteAvailable = remoteAvailable;
   }
   
   
}
