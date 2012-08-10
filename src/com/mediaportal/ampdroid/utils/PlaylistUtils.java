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

import java.io.File;

import com.mediaportal.ampdroid.lists.Utils;

public class PlaylistUtils {

   public static String createM3UPlaylistFromFolder(File _folder, String[] _extension) {
      String retString = null;
      if(_folder.isDirectory()){
         File[] files = _folder.listFiles();
         StringBuilder builder = new StringBuilder();
         
         for(File f : files){
            if(f.isFile()){
               if(StringUtils.containedInArray(Utils.getExtension(f.getName()), _extension)){
                  builder.append(f.getName());
                  builder.append("\n");
               }
            }
         }
         retString = builder.toString();
      }
      return retString;
   }

}
