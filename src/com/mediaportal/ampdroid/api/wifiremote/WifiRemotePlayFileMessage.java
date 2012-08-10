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

public class WifiRemotePlayFileMessage extends WifiRemoteMessage {
   public enum FileType {
      video, audio
   };

   public WifiRemotePlayFileMessage(String _file, FileType _type, int _pos) {
      this.Type = "playfile";
      this.FileType = _type.toString();
      this.Filepath = _file;
      this.StartPosition = _pos;
   }

   public String FileType;
   public String Filepath;
   public int StartPosition;
}
