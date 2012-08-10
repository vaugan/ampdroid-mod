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

public class WifiRemoteMessageSetVolume extends WifiRemoteMessage {
   public WifiRemoteMessageSetVolume(int _volume) {
      this.Type = "volume";
      this.Volume = _volume;
   }

   public int Volume;
}
