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
package com.mediaportal.ampdroid.activities.settings;

import org.codehaus.jackson.annotate.JsonProperty;

public class QRClientDescription {
   private String mAddress;
   private int mPort;
   private String mUser;
   private String mPassword;
   private String mPasscode;
   private int mAuthOptions;
   private String mName;
   private String mHardwareAddresses;

   @JsonProperty("Address")
   public String getAddress() {
      return mAddress;
   }

   @JsonProperty("Address")
   public void setAddress(String address) {
      mAddress = address;
   }

   @JsonProperty("Port")
   public int getPort() {
      return mPort;
   }

   @JsonProperty("Port")
   public void setPort(int port) {
      mPort = port;
   }

   @JsonProperty("User")
   public String getUser() {
      return mUser;
   }

   @JsonProperty("User")
   public void setUser(String user) {
      mUser = user;
   }

   @JsonProperty("Password")
   public String getPassword() {
      return mPassword;
   }

   @JsonProperty("Password")
   public void setPassword(String password) {
      mPassword = password;
   }

   @JsonProperty("Passcode")
   public String getPasscode() {
      return mPasscode;
   }

   @JsonProperty("Passcode")
   public void setPasscode(String passcode) {
      mPasscode = passcode;
   }

   @JsonProperty("AuthOptions")
   public int getAuthOptions() {
      return mAuthOptions;
   }

   @JsonProperty("AuthOptions")
   public void setAuthOptions(int authOptions) {
      mAuthOptions = authOptions;
   }

   @JsonProperty("Name")
   public String getName() {
      return mName;
   }

   @JsonProperty("Name")
   public void setName(String name) {
      mName = name;
   }

   @JsonProperty("HardwareAddresses")
   public void setHardwareAddresses(String hardwareAddresses) {
      mHardwareAddresses = hardwareAddresses;
   }

   @JsonProperty("HardwareAddresses")
   public String getHardwareAddresses() {
      return mHardwareAddresses;
   }

}
