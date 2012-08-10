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

import com.mediaportal.ampdroid.downloadservice.DownloadJob;

public class DownloadsDatabaseHelper extends SQLiteOpenHelper  {
   private String mDownloadsTableString;
   public DownloadsDatabaseHelper(Context context, String name, CursorFactory factory, int version) {
      super(context, name, factory, version);

      mDownloadsTableString = SqliteAnnotationsHelper.getCreateTableStringFromClass(DownloadJob.TABLE_NAME, 
            DownloadJob.class, false);

   }

   @Override
   public void onCreate(SQLiteDatabase _db) {
      _db.execSQL(mDownloadsTableString);

   }

   @Override
   public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
      _db.execSQL("DROP TABLE IF EXISTS " + "Downloads");
      onCreate(_db);

   }


}
