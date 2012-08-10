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

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mediaportal.ampdroid.api.gmawebservice.GmaJsonWebserviceApi;
import com.mediaportal.ampdroid.api.tv4home.Tv4HomeJsonApi;
import com.mediaportal.ampdroid.api.wifiremote.WifiRemoteMpController;
import com.mediaportal.ampdroid.data.PlaybackSession;
import com.mediaportal.ampdroid.data.RemoteClient;
import com.mediaportal.ampdroid.data.RemoteClientSetting;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;

public class PlaybackDatabaseHandler {
   Context mContext;
   private PlaybackDatabaseHelper mDbHelper;
   private SQLiteDatabase mDatabase;


   public PlaybackDatabaseHandler(Context _context) {
      mContext = _context;
      mDbHelper = new PlaybackDatabaseHelper(mContext, "ampdroid_playback_sessions", null, 1);
   }

   public void open() {
      mDatabase = mDbHelper.getWritableDatabase();
   }

   public void close() {
      mDatabase.close();
   }

   public List<PlaybackSession> getAllPlaybackSessions() {
      try {
         Cursor result = mDatabase.query(PlaybackSession.TABLE_NAME, null, null, null, null, null,
               null);
         ArrayList<PlaybackSession> downloads = null;
         if (result.moveToFirst()) {
            downloads = (ArrayList<PlaybackSession>) SqliteAnnotationsHelper.getObjectsFromCursor(
                  result, PlaybackSession.class, 0);
         }

         result.close();

         return downloads;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }

   public PlaybackSession getPlaybackSession(DownloadItemType _type, String _itemId) {
      try {
         int videoType = DownloadItemType.toInt(_type);
         Cursor result = mDatabase.query(PlaybackSession.TABLE_NAME, null, "VideoType=" + videoType
               + " AND VideoId LIKE\"" + _itemId + "\"", null, null, null, null);

         PlaybackSession item = null;
         if (result.getCount() == 1) {
            result.moveToFirst();
            item = (PlaybackSession) SqliteAnnotationsHelper.getObjectFromCursor(result,
                  PlaybackSession.class);
         }
         result.close();
         return item;
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
      // something went wrong obviously
      return null;
   }
   
   public void savePlaybackSession(PlaybackSession _session) {
      try {
         int videoType = _session.getVideoType();
         String videoId = _session.getVideoId();
         ContentValues dbValues = SqliteAnnotationsHelper.getContentValuesFromObject(_session,
               PlaybackSession.class);

         int rows = mDatabase.update(PlaybackSession.TABLE_NAME, dbValues, "VideoType=" + videoType
               + " AND VideoId LIKE\"" + videoId + "\"", null);

         if (rows != 1) {
            mDatabase.insert(PlaybackSession.TABLE_NAME, null, dbValues);
         }
      } catch (Exception ex) {
         Log.e("Database", ex.getMessage());
      }
   }

}
