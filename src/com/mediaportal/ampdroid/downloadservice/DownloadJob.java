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
package com.mediaportal.ampdroid.downloadservice;

import java.util.Date;
import java.util.Random;

import com.mediaportal.ampdroid.database.ColumnProperty;

public class DownloadJob {
   public static final String TABLE_NAME = "Downloads";
   private int mJobId;
   private String mFileName;
   private String mDisplayName;
   private String mLocalFilename;
   private String mUrl;
   private long mLength;
   private boolean mUseAut;
   private String mUsername;
   private String mPassword;
   private int mGroupId;
   private DownloadState mState;
   private int mProgress;
   private String mErrorMessage;
   private boolean mRequestCancelation;
   private Date mDateStarted;
   private Date mDateFinished;
   private MediaItemType mMediaType;
   private Date mDateLastUpdated;
   private Date mDateAdded;
   private String mGroupName;
   private int mGroupSize;
   private int mGroupPart;

   public DownloadJob() {
      mJobId = new Random().nextInt();
   }

   public DownloadJob(int _groupId) {
      this();
      mGroupId = _groupId;
   }

   public DownloadJob(int _groupId, String _displayName, String _fileName, String _url, long _length) {
      this(_groupId);
      mDisplayName = _displayName;
      mFileName = _fileName;
      mUrl = _url;
      setLength(_length);
   }

   public void setAuth(String _username, String _password) {
      mUseAut = true;
      mUsername = _username;
      mPassword = _password;
   }

   @ColumnProperty(value = "Id", type = "integer")
   public int getId() {
      return mJobId;
   }

   @ColumnProperty(value = "Id", type = "integer")
   public void setId(int _id) {
      this.mJobId = _id;
   }

   @ColumnProperty(value = "FileName", type = "text")
   public String getFileName() {
      return mFileName;
   }

   public String getShortenedName() {
      String name = mFileName;
      if (name.length() > 30) {
         name = name.substring(name.length() - 30);
      }
      return name;
   }

   @ColumnProperty(value = "FileName", type = "text")
   public void setFileName(String _name) {
      this.mFileName = _name;
   }

   @ColumnProperty(value = "Url", type = "text")
   public String getUrl() {
      return mUrl;
   }

   @ColumnProperty(value = "Url", type = "text")
   public void setUrl(String _url) {
      this.mUrl = _url;
   }

   @ColumnProperty(value = "Length", type = "real")
   public void setLength(long length) {
      mLength = length;
   }

   @ColumnProperty(value = "Length", type = "real")
   public long getLength() {
      return mLength;
   }

   @ColumnProperty(value = "UseAut", type = "boolean")
   public boolean isUseAut() {
      return mUseAut;
   }

   @ColumnProperty(value = "UseAut", type = "boolean")
   public void setUseAut(boolean useAut) {
      mUseAut = useAut;
   }

   @ColumnProperty(value = "Username", type = "text")
   public String getUsername() {
      return mUsername;
   }

   @ColumnProperty(value = "Username", type = "text")
   public void setUsername(String username) {
      mUsername = username;
   }

   @ColumnProperty(value = "Password", type = "text")
   public String getPassword() {
      return mPassword;
   }

   @ColumnProperty(value = "Password", type = "text")
   public void setPassword(String password) {
      mPassword = password;
   }

   @ColumnProperty(value = "GroupId", type = "integer")
   public void setGroupId(int groupId) {
      mGroupId = groupId;
   }

   @ColumnProperty(value = "GroupId", type = "integer")
   public int getGroupId() {
      return mGroupId;
   }

   @ColumnProperty(value = "DisplayName", type = "text")
   public void setDisplayName(String displayName) {
      mDisplayName = displayName;
   }

   @ColumnProperty(value = "DisplayName", type = "text")
   public String getDisplayName() {
      return mDisplayName;
   }

   @ColumnProperty(value = "StateInt", type = "integer")
   public void setStateInt(int state) {
      mState = DownloadState.fromInt(state);
   }

   @ColumnProperty(value = "StateInt", type = "integer")
   public int getStateInt() {
      return DownloadState.toInt(mState);
   }

   public void setState(DownloadState state) {
      mState = state;
   }

   public DownloadState getState() {
      return mState;
   }

   @ColumnProperty(value = "MediaItemTypeInt", type = "integer")
   public void setMediaTypeInt(int type) {
      mMediaType = MediaItemType.fromInt(type);
   }

   @ColumnProperty(value = "MediaItemTypeInt", type = "integer")
   public int getMediaTypeInt() {
      return MediaItemType.toInt(mMediaType);
   }

   public void setMediaType(MediaItemType mediaType) {
      mMediaType = mediaType;
   }

   public MediaItemType getMediaType() {
      return mMediaType;
   }

   @ColumnProperty(value = "Progress", type = "integer")
   public void setProgress(int progress) {
      mProgress = progress;
   }

   @ColumnProperty(value = "Progress", type = "integer")
   public int getProgress() {
      return mProgress;
   }

   @ColumnProperty(value = "ErrorMessage", type = "text")
   public void setErrorMessage(String errorMessage) {
      mErrorMessage = errorMessage;
   }

   @ColumnProperty(value = "ErrorMessage", type = "text")
   public String getErrorMessage() {
      return mErrorMessage;
   }

   @ColumnProperty(value = "RequestCancelation", type = "boolean")
   public void setRequestCancelation(boolean requestCancelation) {
      mRequestCancelation = requestCancelation;
   }

   @ColumnProperty(value = "RequestCancelation", type = "boolean")
   public boolean isRequestCancelation() {
      return mRequestCancelation;
   }

   @ColumnProperty(value = "DateStarted", type = "date")
   public void setDateStarted(Date dateStarted) {
      mDateStarted = dateStarted;
   }

   @ColumnProperty(value = "DateStarted", type = "date")
   public Date getDateStarted() {
      return mDateStarted;
   }

   @ColumnProperty(value = "DateFinished", type = "date")
   public void setDateFinished(Date dateFinished) {
      mDateFinished = dateFinished;
   }

   @ColumnProperty(value = "DateFinished", type = "date")
   public Date getDateFinished() {
      return mDateFinished;
   }

   @ColumnProperty(value = "DateLastUpdated", type = "date")
   public void setDateLastUpdated(Date lastUpdated) {
      mDateLastUpdated = lastUpdated;
   }

   @ColumnProperty(value = "DateLastUpdated", type = "date")
   public Date getDateLastUpdated() {
      return mDateLastUpdated;
   }

   @ColumnProperty(value = "DateAdded", type = "date")
   public void setDateAdded(Date dateAdded) {
      mDateAdded = dateAdded;
   }

   @ColumnProperty(value = "DateAdded", type = "date")
   public Date getDateAdded() {
      return mDateAdded;
   }

   @ColumnProperty(value = "GroupName", type = "text")
   public void setGroupName(String groupName) {
      mGroupName = groupName;
   }

   @ColumnProperty(value = "GroupName", type = "text")
   public String getGroupName() {
      return mGroupName;
   }
   
   @ColumnProperty(value = "GroupSize", type = "integer")
   public void setGroupSize(int groupSize) {
      mGroupSize = groupSize;
   }

   @ColumnProperty(value = "GroupSize", type = "integer")
   public int getGroupSize() {
      return mGroupSize;
   }

   @ColumnProperty(value = "GroupPart", type = "integer")
   public void setGroupPart(int groupPart) {
      mGroupPart = groupPart;
   }

   @ColumnProperty(value = "GroupPart", type = "integer")
   public int getGroupPart() {
      return mGroupPart;
   }


   public void updateValues(DownloadJob _newValue) {
      if (_newValue != null) {
         setDateLastUpdated(_newValue.getDateLastUpdated());
         setDateStarted(_newValue.getDateStarted());
         setDateFinished(_newValue.getDateFinished());
         setDateAdded(_newValue.getDateAdded());

         setDisplayName(_newValue.getDisplayName());
         setErrorMessage(_newValue.getErrorMessage());
         setFileName(_newValue.getFileName());
         setGroupId(_newValue.getGroupId());
         setId(_newValue.getId());
         setLength(_newValue.getLength());
         setMediaType(_newValue.getMediaType());
         setPassword(_newValue.getPassword());
         setProgress(_newValue.getProgress());
         setRequestCancelation(_newValue.isRequestCancelation());
         setState(_newValue.getState());
         setUrl(_newValue.getUrl());
         setUseAut(_newValue.isUseAut());
         setUsername(_newValue.getUsername());
      }
   }



}
