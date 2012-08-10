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
package com.mediaportal.ampdroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.mediaportal.ampdroid.data.PlaybackSession;
import com.mediaportal.ampdroid.data.RemoteClientSetting;
import com.mediaportal.ampdroid.data.RemoteFunction;
import com.mediaportal.ampdroid.data.StreamProfile;

public class SettingsDatabaseHelper extends SQLiteOpenHelper {
   private String mRemoteClientTableString;
   private String mFunctionsTableString;
   private String mPlaybackSessionsTableString;
   private String mProfilesTableString;

   public SettingsDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
      super(context, name, factory, version);

      mRemoteClientTableString = SqliteAnnotationsHelper.getCreateTableStringFromClass(
            RemoteClientSetting.TABLE_NAME, RemoteClientSetting.class, false);
      mFunctionsTableString = SqliteAnnotationsHelper.getCreateTableStringFromClass(
            RemoteFunction.TABLE_NAME, RemoteFunction.class, false);
      mProfilesTableString = SqliteAnnotationsHelper.getCreateTableStringFromClass(
            StreamProfile.TABLE_NAME, StreamProfile.class, false);
   }

   @Override
   public void onCreate(SQLiteDatabase _db) {
      _db.execSQL(mRemoteClientTableString);
      _db.execSQL(mFunctionsTableString);
      _db.execSQL(mProfilesTableString);
   }

   @Override
   public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
      _db.execSQL("DROP TABLE IF EXISTS " + "RemoteClients");
      _db.execSQL("DROP TABLE IF EXISTS " + "RemoteFunctions");
      _db.execSQL("DROP TABLE IF EXISTS " + "StreamingProfiles");
      onCreate(_db);

   }

}
