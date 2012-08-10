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
package com.mediaportal.ampdroid.lists;

public class TvServerFeature {
   private String mName;

   private String mDescription;
   private int mIcon;
   
   public String getName() {
      return mName;
   }

   public void setName(String mName) {
      this.mName = mName;
   }

   public String getDescription() {
      return mDescription;
   }

   public void setDescription(String _description) {
      this.mDescription = _description;
   }

   public int getIcon() {
      return mIcon;
   }

   public void setIcon(int _icon) {
      this.mIcon = _icon;
   }


   public TvServerFeature(String _name, String _description, int _icon) {
      mName = _name;
      mDescription = _description;
      mIcon = _icon;
   }
}
