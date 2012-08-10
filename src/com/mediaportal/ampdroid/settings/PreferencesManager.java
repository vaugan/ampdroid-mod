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
package com.mediaportal.ampdroid.settings;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

import com.mediaportal.ampdroid.lists.views.MediaListType;
import com.mediaportal.ampdroid.lists.views.ViewTypes;
import com.mediaportal.ampdroid.utils.Constants;

public class PreferencesManager {
   public enum StatusbarAutohide {
      AlwaysShow, AlwaysHide, ShowWhenConnected, ShowManually
   }

   static SharedPreferences Preferences;

   public static void intitialisePreferencesManager(Context _context) {
      Preferences = PreferenceManager.getDefaultSharedPreferences(_context);
   }

   public static boolean isFullscreen() {
      if (Preferences != null) {
         return Preferences.getBoolean("general_fullscreen", true);
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return false;
      }
   }

   public static boolean isVibrating() {
      if (Preferences != null) {
         return Preferences.getBoolean("general_vibrate", false);
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return false;
      }
   }

   public static String getClientName() {
      if (Preferences != null) {
         return Preferences.getString("general_clientname", "unknown");
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return "unknown";
      }
   }

   public static StatusbarAutohide getStatusbarAutohide() {
      if (Preferences != null) {
         String value = Preferences.getString("statusbar_autohide", "0");
         if (value.equals("0")) {
            return StatusbarAutohide.AlwaysShow;
         } else if (value.equals("1")) {
            return StatusbarAutohide.AlwaysHide;
         } else if (value.equals("2")) {
            return StatusbarAutohide.ShowWhenConnected;
         } else if (value.equals("3")) {
            return StatusbarAutohide.ShowManually;
         }
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
      }
      return StatusbarAutohide.AlwaysShow;
   }

   public static boolean connectOnStartup() {
      if (Preferences != null) {
         return Preferences.getBoolean("auto_connect", false);
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return false;
      }
   }

   public static ViewTypes getDefaultView(MediaListType _listType) {
      if (Preferences != null) {
         String prefId = getSettingsId(_listType);

         if (prefId != null) {
            String type = Preferences.getString(prefId, "0");
            return ViewTypes.fromInt(Integer.valueOf(type));
         } else {
            return ViewTypes.TextView;
         }
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return ViewTypes.TextView;
      }
   }

   public static void setDefaultView(MediaListType _listType, ViewTypes _view) {
      if (Preferences != null) {
         String prefId = getSettingsId(_listType);

         if (prefId != null) {
            Editor editor = Preferences.edit();
            editor.putString(prefId, String.valueOf(ViewTypes.toInt(_view)));
            editor.commit();
         } else {
            Log.w(Constants.LOG_CONST, "No preference id for enum: " + _listType);
         }
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
      }
   }

   private static String getSettingsId(MediaListType _listType) {
      String pref_id = null;
      switch (_listType) {
      case VideoShares:
         pref_id = "media_default_view_videoshares";
         break;
      case Videos:
         pref_id = "media_default_view_videos";
         break;
      case Series:
         pref_id = "media_default_view_series";
         break;
      case Movies:
         pref_id = "media_default_view_movies";
         break;
      case MusicShares:
         pref_id = "media_default_view_musicshares";
         break;
      case Artists:
         pref_id = "media_default_view_musicartists";
         break;
      case Albums:
         pref_id = "media_default_view_musicalbums";
         break;
      case Songs:
         pref_id = "media_default_view_musictracks";
         break;
      default:
         pref_id = null;
      }
      return pref_id;
   }

   public static int getNumItemsToLoad() {
      if (Preferences != null) {
         return Integer.valueOf(Preferences.getString("media_preload_items", "0"));
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return 0;
      }
   }

   public static boolean isWolAutoConnect() {
      if (Preferences != null) {
         return Preferences.getBoolean("auto_wol", false);
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return false;
      }
   }

   public static boolean useDirectStream() {
      if (Preferences != null) {
         return Preferences.getBoolean("settings_tvserver_use_rtsp", false);
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return false;
      }
   }

   public static String getDefaultTvProfile() {
      if (Preferences != null) {
         return Preferences.getString("settings_tvserver_tvprofile_default", null);
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return null;
      }
   }

   public static List<String> getTvProfiles() {
      if (Preferences != null) {
         // return Preferences.getString("settings_tvserver_tvprofiles", null);
         return null;
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return null;
      }
   }

   public static boolean isQuickStartEnabled(boolean _tv) {
      if (Preferences != null) {
         if (_tv) {
            return Preferences.getBoolean("tvserver_streaming_quickconf", false);
         } else {
            return Preferences.getBoolean("media_streaming_quickconf", false);

         }
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return false;
      }
   }

   public static void setDefaultProfile(String _profile, boolean _tv) {
      if (Preferences != null) {
         Editor editor = Preferences.edit();
         if (_tv) {
            editor.putString("default_profile_tv", _profile);
         } else {
            editor.putString("default_profile_media", _profile);
         }
         editor.commit();
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
      }
   }

   public static String getDefaultProfile(boolean _tv) {
      if (Preferences != null) {
         if (_tv) {
            return Preferences.getString("default_profile_tv", null);
         } else {
            return Preferences.getString("default_profile_media", null);
         }
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return null;
      }
   }

   public static boolean getUseExternalPlayer() {
      if (Preferences != null) {
         return Preferences.getBoolean("streaming_use_external", false);
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return false;
      }
   }

   public static boolean getShowAllChannelsGroup() {
      if (Preferences != null) {
         return Preferences.getBoolean("tvserver_showall", false);
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return false;
      }
   }

   public static boolean getAutoReconnect() {
      if (Preferences != null) {
         return Preferences.getBoolean("auto_reconnect", false);
      } else {
         Log.w(Constants.LOG_CONST, "PreferencesManager not initialised");
         return false;
      }
   }

}
