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
package com.mediaportal.ampdroid.data;

import java.util.Date;

import com.mediaportal.ampdroid.database.ColumnProperty;

public class CacheItemsSetting {
   public static final String TABLE_NAME = "CacheSettings";
   
   public static final int CACHE_ID_VIDEOS = 5;
   
   private int mCacheType;
   private int mCacheCount;
   private Date mCacheDate;

   public CacheItemsSetting(){
      
   }
   
   public CacheItemsSetting(int _cacheId, int _count, Date _date) {
      this();
      mCacheType = _cacheId;
      mCacheCount = _count;
      mCacheDate = _date;
   }

   @ColumnProperty(value="CacheType", type="integer")
   public int getCacheType() {
      return mCacheType;
   }

   @ColumnProperty(value="CacheType", type="integer")
   public void setCacheType(int mCacheType) {
      this.mCacheType = mCacheType;
   }

   @ColumnProperty(value="CacheCount", type="integer")
   public int getCacheCount() {
      return mCacheCount;
   }

   @ColumnProperty(value="CacheCount", type="integer")
   public void setCacheCount(int mCacheCount) {
      this.mCacheCount = mCacheCount;
   }

   @ColumnProperty(value="CacheDate", type="date")
   public Date getCacheDate() {
      return mCacheDate;
   }

   @ColumnProperty(value="CacheDate", type="date")
   public void setCacheDate(Date mCacheDate) {
      this.mCacheDate = mCacheDate;
   }

}
