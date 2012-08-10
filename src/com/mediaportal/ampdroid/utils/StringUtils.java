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

import java.text.DecimalFormat;

public class StringUtils {

   public static String createStringArray(String[] _array) {
      if(_array != null){
         String retString = "|";
         for(String s : _array){
            retString += " " + s + " |";
         }
         return retString;
      }
      return "";
   }

   public static boolean containedInArray(String _string, String[] _array) {
      if(_array == null)return false;
      for(String s : _array){
         if(s != null && s.equals(_string)){
            return true;
         }
      }
      return false;
   }
   
   public static String formatFileSize(long size){
      if(size <= 0) return "0";
      final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
      int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
      return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
  }


}
