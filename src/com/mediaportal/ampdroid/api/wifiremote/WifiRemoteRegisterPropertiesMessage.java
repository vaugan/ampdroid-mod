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
package com.mediaportal.ampdroid.api.wifiremote;

import java.util.ArrayList;
import java.util.List;

public class WifiRemoteRegisterPropertiesMessage  extends WifiRemoteMessage {
   public WifiRemoteRegisterPropertiesMessage(){
      this.Type = "properties";
      this.Properties = new ArrayList<String>();
   }
   
   public List<String> Properties;
   
}
