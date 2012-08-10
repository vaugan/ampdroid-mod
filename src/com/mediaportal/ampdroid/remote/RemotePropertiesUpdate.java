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

public class RemotePropertiesUpdate {
   private String mTag;
   private String mValue;

   @JsonProperty("Tag")
   public String getTag() {
      return mTag;
   }

   @JsonProperty("Tag")
   public void setTag(String _tag) {
      mTag = _tag;
   }

   @JsonProperty("Value")
   public String getValue() {
      return mValue;
   }

   @JsonProperty("Value")
   public void setValue(String _value) {
      mValue = _value;
   }

}
