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
package com.mediaportal.ampdroid.utils;

import android.os.Environment;

import com.mediaportal.ampdroid.data.FileInfo;
import com.mediaportal.ampdroid.data.Movie;
import com.mediaportal.ampdroid.data.SeriesEpisode;
import com.mediaportal.ampdroid.data.VideoShare;

public class DownloaderUtils {
   public static String getTvEpisodePath(String _seriesName, SeriesEpisode _episode) {
      return getTvEpisodePath(_seriesName, _episode.getSeasonNumber());
   }
   
   public static String getTvEpisodePath(String _seriesName, int _seasonNumber) {
      String dirName = "Series/" + _seriesName + "/Season."
            + getNumberWithTrailingZero(_seasonNumber) + "/";
      
      return dirName;
   }

   private static String getNumberWithTrailingZero(int _number) {
      if (_number < 10)
         return "0" + _number;
      return String.valueOf(_number);
   }

   public static String getBaseDirectory() {
      return Environment.getExternalStorageDirectory() + "/aMPdroid/";
   }

   public static String getVideoPath(VideoShare _share, FileInfo _file) {
      String relativeDir = _file.getFullPath().replace(_share.getPath(), "");
      String dirName = "Shares/" + _share.Name + relativeDir.replace('\\', '/');
      
      return dirName;
   }

   public static String getMoviePath(Movie _movie) {
      return getMoviePath(_movie.getName());
   }
   
   public static String getMoviePath(String _movieName) {
      String dirName = "Movies/" + _movieName + "/";
      return dirName;
   }
   
   public static String getMusicSharesPath(){
      String dirName = "Music/Shares/";
      return dirName;
   }
   
   public static String getMusicTrackPath(){
      return getMusicTrackPath(null, null);
   }
   
   public static String getMusicTrackPath(String _albumTitle){
      return getMusicTrackPath(null, _albumTitle);
   }

   public static String getMusicTrackPath(String _artistTitle, String _albumTitle) {
      String dirName = "Music/";
      if(_artistTitle != null && !_artistTitle.equals("")){
         dirName += _artistTitle + "/";
      }
      if(_albumTitle != null && !_albumTitle.equals("")){
         dirName += _albumTitle + "/";
      }
      
      if(_albumTitle == null && _artistTitle == null){
         dirName += "Songs/";
      }

      return dirName;
   }
}
