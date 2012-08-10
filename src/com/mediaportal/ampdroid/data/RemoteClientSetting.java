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
package com.mediaportal.ampdroid.data;

import com.mediaportal.ampdroid.database.ColumnProperty;

public class RemoteClientSetting {
   public static final String TABLE_NAME = "RemoteClients";
   
   private int mClientId;
   
   private String mClientName;
   private String mClientDescription;
   
   private String mRemoteAccessServer;
   private int mRemoteAccessPort;
   private String mRemoteAccessMac;
   private String mRemoteAccessUser;
   private String mRemoteAccessPass;
   private boolean mRemoteAccessUseAuth;
   
   private String mTvServer;
   private int mTvPort;
   private String mTvMac;
   private String mTvUser;
   private String mTvPass;
   private boolean mTvUseAuth;
   
   private String mRemoteControlServer;
   private int mRemoteControlPort;
   private String mRemoteControlMac;
   private String mRemoteControlUser;
   private String mRemoteControlPass;
   private boolean mRemoteControlUseAuth;
   
   @ColumnProperty(value="ClientId", type="integer")
   public int getClientId() {
      return mClientId;
   }

   @ColumnProperty(value="ClientId", type="integer")
   public void setClientId(int clientId) {
      this.mClientId = clientId;
   }

   @ColumnProperty(value="ClientName", type="text")
   public void setClientName(String clientName) {
      this.mClientName = clientName;
   }

   @ColumnProperty(value="ClientName", type="text")
   public String getClientName() {
      return mClientName;
   }

   @ColumnProperty(value="ClientDescription", type="text")
   public String getClientDescription() {
      return mClientDescription;
   }

   @ColumnProperty(value="ClientDescription", type="text")
   public void setClientDescription(String clientDescription) {
      this.mClientDescription = clientDescription;
   }

   @ColumnProperty(value="RemoteAccessServer", type="text")
   public String getRemoteAccessServer() {
      return mRemoteAccessServer;
   }

   @ColumnProperty(value="RemoteAccessServer", type="text")
   public void setRemoteAccessServer(String mRemoteAccessServer) {
      this.mRemoteAccessServer = mRemoteAccessServer;
   }

   @ColumnProperty(value="RemoteAccessPort", type="integer")
   public int getRemoteAccessPort() {
      return mRemoteAccessPort;
   }

   @ColumnProperty(value="RemoteAccessPort", type="integer")
   public void setRemoteAccessPort(int mRemoteAccessPort) {
      this.mRemoteAccessPort = mRemoteAccessPort;
   }

   @ColumnProperty(value="TvServer", type="text")
   public String getTvServer() {
      return mTvServer;
   }

   @ColumnProperty(value="TvServer", type="text")
   public void setTvServer(String mTvServer) {
      this.mTvServer = mTvServer;
   }

   @ColumnProperty(value="TvPort", type="integer")
   public int getTvPort() {
      return mTvPort;
   }

   @ColumnProperty(value="TvPort", type="integer")
   public void setTvPort(int mTvPort) {
      this.mTvPort = mTvPort;
   }

   @ColumnProperty(value="RemoteControlServer", type="text")
   public String getRemoteControlServer() {
      return mRemoteControlServer;
   }

   @ColumnProperty(value="RemoteControlServer", type="text")
   public void setRemoteControlServer(String mRemoteControlServer) {
      this.mRemoteControlServer = mRemoteControlServer;
   }

   @ColumnProperty(value="RemoteControlPort", type="integer")
   public int getRemoteControlPort() {
      return mRemoteControlPort;
   }

   @ColumnProperty(value = "RemoteControlPort", type = "integer")
   public void setRemoteControlPort(int mRemoteControlPort) {
      this.mRemoteControlPort = mRemoteControlPort;
   }

   @ColumnProperty(value="RemoteAccessUser", type="text")
   public String getRemoteAccessUser() {
      return mRemoteAccessUser;
   }

   @ColumnProperty(value="RemoteAccessUser", type="text")
   public void setRemoteAccessUser(String remoteAccessUser) {
      mRemoteAccessUser = remoteAccessUser;
   }

   @ColumnProperty(value="RemoteAccessPass", type="text")
   public String getRemoteAccessPass() {
      return mRemoteAccessPass;
   }

   @ColumnProperty(value="RemoteAccessPass", type="text")
   public void setRemoteAccessPass(String remoteAccessPass) {
      mRemoteAccessPass = remoteAccessPass;
   }

   @ColumnProperty(value="RemoteAccessUseAuth", type="boolean")
   public boolean isRemoteAccessUseAuth() {
      return mRemoteAccessUseAuth;
   }

   @ColumnProperty(value="RemoteAccessUseAuth", type="boolean")
   public void setRemoteAccessUseAuth(boolean remoteAccessUseAuth) {
      mRemoteAccessUseAuth = remoteAccessUseAuth;
   }

   @ColumnProperty(value="TvUser", type="text")
   public String getTvUser() {
      return mTvUser;
   }

   @ColumnProperty(value="TvUser", type="text")
   public void setTvUser(String tvUser) {
      mTvUser = tvUser;
   }

   @ColumnProperty(value="TvPass", type="text")
   public String getTvPass() {
      return mTvPass;
   }

   @ColumnProperty(value="TvPass", type="text")
   public void setTvPass(String tvPass) {
      mTvPass = tvPass;
   }

   @ColumnProperty(value="TvUseAuth", type="boolean")
   public boolean isTvUseAuth() {
      return mTvUseAuth;
   }

   @ColumnProperty(value="TvUseAuth", type="boolean")
   public void setTvUseAuth(boolean tvUseAuth) {
      mTvUseAuth = tvUseAuth;
   }

   @ColumnProperty(value="RemoteControlUser", type="text")
   public String getRemoteControlUser() {
      return mRemoteControlUser;
   }

   @ColumnProperty(value="RemoteControlUser", type="text")
   public void setRemoteControlUser(String remoteControlUser) {
      mRemoteControlUser = remoteControlUser;
   }

   @ColumnProperty(value="RemoteControlPass", type="text")
   public String getRemoteControlPass() {
      return mRemoteControlPass;
   }

   @ColumnProperty(value="RemoteControlPass", type="text")
   public void setRemoteControlPass(String remoteControlPass) {
      mRemoteControlPass = remoteControlPass;
   }

   @ColumnProperty(value="RemoteControlUseAuth", type="boolean")
   public boolean isRemoteControlUseAuth() {
      return mRemoteControlUseAuth;
   }

   @ColumnProperty(value="RemoteControlUseAuth", type="boolean")
   public void setRemoteControlUseAuth(boolean remoteControlUseAuth) {
      mRemoteControlUseAuth = remoteControlUseAuth;
   }

   @ColumnProperty(value="RemoteAccessMac", type="text")
   public void setRemoteAccessMac(String remoteAccessMac) {
      mRemoteAccessMac = remoteAccessMac;
   }

   @ColumnProperty(value="RemoteAccessMac", type="text")
   public String getRemoteAccessMac() {
      return mRemoteAccessMac;
   }

   @ColumnProperty(value="TvMac", type="text")
   public void setTvMac(String tvMac) {
      mTvMac = tvMac;
   }

   @ColumnProperty(value="TvMac", type="text")
   public String getTvMac() {
      return mTvMac;
   }

   @ColumnProperty(value="RemoteControlMac", type="text")
   public void setRemoteControlMac(String remoteControlMac) {
      mRemoteControlMac = remoteControlMac;
   }

   @ColumnProperty(value="RemoteControlMac", type="text")
   public String getRemoteControlMac() {
      return mRemoteControlMac;
   }
   
   
}
