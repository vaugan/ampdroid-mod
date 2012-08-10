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

public enum DownloadItemType {
   VideoShareItem, VideoDatabaseItem, TvSeriesItem, MovieItem, MusicTrackItem, MusicShareItem, LiveTv, TvRecording;

   public static DownloadItemType fromInt(int _state) {
      switch (_state) {
      case 0:
         return VideoShareItem;
      case 1:
         return VideoDatabaseItem;
      case 2:
         return TvSeriesItem;
      case 3:
         return MovieItem;
      case 4:
         return MusicTrackItem;
      case 5:
         return MusicShareItem;
      case 6:
         return LiveTv;
      case 7:
         return TvRecording;
      default:
         return VideoShareItem;
      }
   }

   public static int toInt(DownloadItemType _state) {
      switch (_state) {
      case VideoShareItem:
         return 0;
      case VideoDatabaseItem:
         return 1;
      case TvSeriesItem:
         return 2;
      case MovieItem:
         return 3;
      case MusicTrackItem:
         return 4;
      case MusicShareItem:
         return 5;
      case LiveTv:
         return 6;
      case TvRecording:
         return 7;
      default:
         return 0;
      }
   }

}
