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

import java.util.Date;

public class DateTimeHelper {

   public static Date parseDate(String _dateString) {
      return new Date();
   }

   public static String getDateString(Date _date, boolean _getHours) {
      if (_date != null) {
         if (_getHours) {
            return (String) android.text.format.DateFormat.format("yyyy-MM-dd kk:mm", _date);
         } else {
            return (String) android.text.format.DateFormat.format("yyyy-MM-dd", _date);
         }
      } else {
         return "";
      }
   }

   public static String getTimeString(Date _date) {
      return (String) android.text.format.DateFormat.format("kk:mm", _date);
   }

   public static String getDayOfWeek(Date _day) {
      switch (_day.getDay()) {
      case 1:
         return "Monday";
      case 2:
         return "Tuesday";
      case 3:
         return "Wednesday";
      case 4:
         return "Thursday";
      case 5:
         return "Friday";
      case 6:
         return "Saturday";
      case 7:
         return "Sunday";
      default:
         return "Unknown";
      }
   }

   public static String getNumberWithLeadingZero(int _number) {
      if (_number < 10) {
         return "0" + String.valueOf(_number);
      } else {
         return String.valueOf(_number);
      }
   }

   public static String getTimeStringFromMs(long _ms) {
      int totalSeconds = (int) _ms / 1000;
      int seconds = totalSeconds % 60;
      int minutes = totalSeconds / 60;

      return getNumberWithLeadingZero(minutes) + ":" + getNumberWithLeadingZero(seconds);
   }

}
