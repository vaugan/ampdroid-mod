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

import java.util.Date;

import org.codehaus.jackson.annotate.JsonProperty;

import com.mediaportal.ampdroid.database.ColumnProperty;
import com.mediaportal.ampdroid.lists.Utils;

public class FileInfo {
   private String FullPath;
   private boolean IsFolder;
   
   private long Length;
   private String Name;
   private Date LastAccessTime;
   private Date LastWriteTime;
   private String DirectoryName;
   private boolean Exists;
   private String Extension;
   private boolean IsReadOnly;

   public FileInfo(String _fullPath, boolean _isFolder) {
      FullPath = _fullPath;
      IsFolder = _isFolder;
      
      if(_isFolder){
         Name = Utils.getFolderNameWithoutExtension(FullPath, "\\");
      }
   }
   
   public FileInfo(){
      IsFolder = false;
   }

   @ColumnProperty(value = "FullPath", type = "text")
   @JsonProperty("FullName")
   public void setFullPath(String fileInfo) {
      FullPath = fileInfo;
   }

   @ColumnProperty(value = "FullPath", type = "text")
   @JsonProperty("FullName")
   public String getFullPath() {
      return FullPath;
   }
   
   @Override
   public String toString() {
      if (FullPath != null) {
         if(IsFolder){
            return Utils.getFolderNameWithoutExtension(FullPath, "\\");
         }
         else{
            return Utils.getFileNameWithExtension(FullPath, "\\");
         }
      } else {
         return "Unknown";
      }
   }
   
   @ColumnProperty(value = "IsFolder", type = "boolean")
   public void setFolder(boolean isFolder) {
      IsFolder = isFolder;
   }
   
   @ColumnProperty(value = "IsFolder", type = "boolean")
   public boolean isFolder() {
      return IsFolder;
   }

   @ColumnProperty(value = "Length", type = "long")
   @JsonProperty("Length")
   public long getLength() {
      return Length;
   }

   @ColumnProperty(value = "Length", type = "text")
   @JsonProperty("Length")
   public void setLength(long length) {
      Length = length;
   }

   @ColumnProperty(value = "Name", type = "text")
   @JsonProperty("Name")
   public String getName() {
      return Name;
   }

   @ColumnProperty(value = "Name", type = "text")
   @JsonProperty("Name")
   public void setName(String name) {
      Name = name;
   }

   @ColumnProperty(value = "LastAccessTime", type = "date")
   @JsonProperty("LastAccessTime")
   public Date getLastAccessTime() {
      return LastAccessTime;
   }

   @ColumnProperty(value = "LastAccessTime", type = "date")
   @JsonProperty("LastAccessTime")
   public void setLastAccessTime(Date lastAccessTime) {
      LastAccessTime = lastAccessTime;
   }

   @ColumnProperty(value = "LastWriteTime", type = "date")
   @JsonProperty("LastWriteTime")
   public Date getLastWriteTime() {
      return LastWriteTime;
   }

   @ColumnProperty(value = "LastWriteTime", type = "date")
   @JsonProperty("LastWriteTime")
   public void setLastWriteTime(Date lastWriteTime) {
      LastWriteTime = lastWriteTime;
   }

   @ColumnProperty(value = "DirectoryName", type = "text")
   @JsonProperty("DirectoryName")
   public String getDirectoryName() {
      return DirectoryName;
   }

   @ColumnProperty(value = "DirectoryName", type = "text")
   @JsonProperty("DirectoryName")
   public void setDirectoryName(String directoryName) {
      DirectoryName = directoryName;
   }

   @ColumnProperty(value = "Exists", type = "boolean")
   @JsonProperty("Exists")
   public boolean Exists() {
      return Exists;
   }

   @ColumnProperty(value = "Exists", type = "boolean")
   @JsonProperty("Exists")
   public void setExists(boolean exists) {
      Exists = exists;
   }

   @ColumnProperty(value = "Extension", type = "text")
   @JsonProperty("Extension")
   public String getExtension() {
      return Extension;
   }

   @ColumnProperty(value = "Extension", type = "text")
   @JsonProperty("Extension")
   public void setExtension(String extension) {
      Extension = extension;
   }

   @ColumnProperty(value = "IsReadOnly", type = "boolean")
   @JsonProperty("IsReadOnly")
   public boolean isIsReadOnly() {
      return IsReadOnly;
   }

   @ColumnProperty(value = "IsReadOnly", type = "boolean")
   @JsonProperty("IsReadOnly")
   public void setIsReadOnly(boolean isReadOnly) {
      IsReadOnly = isReadOnly;
   }
   
   
}
