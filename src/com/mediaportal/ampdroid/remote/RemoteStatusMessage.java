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

public class RemoteStatusMessage {

   private boolean mIsPlaying;
   private boolean mIsPaused;
   private String mTitle;
   private String mCurrentModule;
   private String mSelectedItem;
   
   @JsonProperty("IsPlaying")
   public boolean isIsPlaying() {
      return mIsPlaying;
   }
   
   @JsonProperty("IsPlaying")
   public void setIsPlaying(boolean isPlaying) {
      mIsPlaying = isPlaying;
   }
   
   @JsonProperty("IsPaused")
   public boolean isIsPaused() {
      return mIsPaused;
   }
   
   @JsonProperty("IsPaused")
   public void setIsPaused(boolean isPaused) {
      mIsPaused = isPaused;
   }
   
   @JsonProperty("Title")
   public String getTitle() {
      return mTitle;
   }
   
   @JsonProperty("Title")
   public void setTitle(String title) {
      mTitle = title;
   }
   
   @JsonProperty("CurrentModule")
   public String getCurrentModule() {
      return mCurrentModule;
   }
   
   @JsonProperty("CurrentModule")
   public void setCurrentModule(String currentModule) {
      mCurrentModule = currentModule;
   }
   
   @JsonProperty("SelectedItem")
   public String getSelectedItem() {
      return mSelectedItem;
   }
   
   @JsonProperty("SelectedItem")
   public void setSelectedItem(String selectedItem) {
      mSelectedItem = selectedItem;
   }
}
