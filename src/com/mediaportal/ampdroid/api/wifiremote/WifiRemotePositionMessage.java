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


public class WifiRemotePositionMessage extends WifiRemoteMessage {
   public WifiRemotePositionMessage(int _position){
      this.Type = "position";
      this.Position = _position;
      this.SeekType = 0;
   }
   
   public int Position;
   public int SeekType;
}
