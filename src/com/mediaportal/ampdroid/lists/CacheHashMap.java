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
package com.mediaportal.ampdroid.lists;

import java.util.LinkedHashMap;

import android.graphics.Bitmap;

public class CacheHashMap extends LinkedHashMap<String, Bitmap> {

   private static final long serialVersionUID = 1399232902907446561L;
   private final int capacity;

   public CacheHashMap(int capacity)
   {
     super(capacity + 1, 1.1f, true);
     this.capacity = capacity;
   }

   @Override
   protected boolean removeEldestEntry(java.util.Map.Entry<String, Bitmap> eldest) {
      return size() > capacity;
   }


}
