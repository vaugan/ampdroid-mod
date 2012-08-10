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

public class WifiRemoteLoginMessage extends WifiRemoteMessage {
   public class AuthMessage {
      public String User;
      public String Password;
      public String AuthMethod;
   }

   public String Name;// client name
   public String Description;
   public String AppName;
   public String Version;
   public AuthMessage Authenticate;

   WifiRemoteLoginMessage() {
      this.Type = "identify";
   }

   public WifiRemoteLoginMessage(String _user, String _pwd) {
      this();
      SetAuth(_user, _pwd);
   }

   public void SetAuth(String _user, String _pwd) {
      this.Authenticate = new AuthMessage();
      this.Authenticate.User = _user;
      this.Authenticate.Password = _pwd;
      this.Authenticate.AuthMethod = "userpass";
   }
}
