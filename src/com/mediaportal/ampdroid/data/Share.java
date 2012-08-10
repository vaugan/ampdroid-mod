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

import org.codehaus.jackson.annotate.JsonProperty;

import com.mediaportal.ampdroid.database.ColumnProperty;

public class Share {
   public int ShareId;
   public String Path;
   public String Name;
   public String[] Extensions;
   public String PinCode;
   public boolean IsFtp;
   public String FtpServer;
   public int FtpPort;
   public String FtpPath;
   public String FtpLogin;
   public String FtpPassword;
   
   public Share(){
      super();
   }
   
   @Override
   public String toString(){
      return Name;
   }
   
   @ColumnProperty(value="ShareId", type="integer")
   @JsonProperty("ShareId")
   public int getShareId() {
      return ShareId;
   }
   
   @ColumnProperty(value="ShareId", type="integer")
   @JsonProperty("ShareId")
   public void setShareId(int shareId) {
      ShareId = shareId;
   }
   
   @ColumnProperty(value="Path", type="text")
   @JsonProperty("Path")
   public String getPath() {
      return Path;
   }
   
   @ColumnProperty(value="Path", type="text")
   @JsonProperty("Path")
   public void setPath(String path) {
      Path = path;
   }
   
   @ColumnProperty(value="Name", type="text")
   @JsonProperty("Name")
   public String getName() {
      return Name;
   }
   
   @ColumnProperty(value="Name", type="text")
   @JsonProperty("Name")
   public void setName(String name) {
      Name = name;
   }
   
   @ColumnProperty(value="Extensions", type="textarray")
   @JsonProperty("Extensions")
   public String[] getExtensions() {
      return Extensions;
   }
   
   @ColumnProperty(value="Extensions", type="textarray")
   @JsonProperty("Extensions")
   public void setExtensions(String[] extensions) {
      Extensions = extensions;
   }
   
   @ColumnProperty(value="PinCode", type="text")
   @JsonProperty("PinCode")
   public String getPinCode() {
      return PinCode;
   }
   
   @ColumnProperty(value="PinCode", type="text")
   @JsonProperty("PinCode")
   public void setPinCode(String pinCode) {
      PinCode = pinCode;
   }
   
   @ColumnProperty(value="IsFtp", type="boolean")
   @JsonProperty("IsFtp")
   public boolean isIsFtp() {
      return IsFtp;
   }
   
   @ColumnProperty(value="IsFtp", type="boolean")
   @JsonProperty("IsFtp")
   public void setIsFtp(boolean isFtp) {
      IsFtp = isFtp;
   }
   
   @ColumnProperty(value="FtpServer", type="text")
   @JsonProperty("FtpServer")
   public String getFtpServer() {
      return FtpServer;
   }
   
   @ColumnProperty(value="FtpServer", type="text")
   @JsonProperty("FtpServer")
   public void setFtpServer(String ftpServer) {
      FtpServer = ftpServer;
   }
   
   @ColumnProperty(value="FtpPort", type="integer")
   @JsonProperty("FtpPort")
   public int getFtpPort() {
      return FtpPort;
   }
   
   @ColumnProperty(value="FtpPort", type="integer")
   @JsonProperty("FtpPort")
   public void setFtpPort(int ftpPort) {
      FtpPort = ftpPort;
   }
   
   @ColumnProperty(value="FtpPath", type="text")
   @JsonProperty("FtpPath")
   public String getFtpPath() {
      return FtpPath;
   }
   
   @ColumnProperty(value="FtpPath", type="text")
   @JsonProperty("FtpPath")
   public void setFtpPath(String ftpPath) {
      FtpPath = ftpPath;
   }
   
   @ColumnProperty(value="FtpLogin", type="text")
   @JsonProperty("FtpLogin")
   public String getFtpLogin() {
      return FtpLogin;
   }
   
   @ColumnProperty(value="FtpLogin", type="text")
   @JsonProperty("FtpLogin")
   public void setFtpLogin(String ftpLogin) {
      FtpLogin = ftpLogin;
   }
   
   @ColumnProperty(value="FtpPassword", type="text")
   @JsonProperty("FtpPassword")
   public String getFtpPassword() {
      return FtpPassword;
   }
   
   @ColumnProperty(value="FtpPassword", type="text")
   @JsonProperty("FtpPassword")
   public void setFtpPassword(String ftpPassword) {
      FtpPassword = ftpPassword;
   }
   
   
}
