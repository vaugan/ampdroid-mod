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

import com.mediaportal.ampdroid.data.commands.RemoteKey;

public class WifiRemoteMessageKey extends WifiRemoteMessage {
   public WifiRemoteMessageKey(RemoteKey _key){
      this.Type = "command";
      this.Command = _key.getAction();
   }
   
   public String Command;
}
