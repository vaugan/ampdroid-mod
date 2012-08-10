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


public class LazyLoadingImage {
   private String mImageUrl;
   private String mImageCacheName;
   private int mMaxWidth;
   private int mMaxHeight;
   private ImageType mImageType;
   public enum ImageType { Media, TvLogo };
   
   public LazyLoadingImage(String _url, String _cache, int _maxWidth, int _maxHeight) {
      mImageUrl = _url;
      mImageCacheName = _cache;
      mMaxWidth = _maxWidth;
      mMaxHeight = _maxHeight;
   }
   
   public void setImageType(ImageType _type){
      mImageType = _type;
   }
   
   public ImageType getImageType(){
      return mImageType;
   }
   
   /**
    * The url of the image that should be loaded on demand
    * @return Url to the image
    */
   public String getImageUrl() {
      return mImageUrl;
   }
   
   /**
    * The url of the image that should be loaded on demand
    * @param _imageUrl Url to the image
    */
   public void setImageUrl(String _imageUrl) {
      this.mImageUrl = _imageUrl;
   }
   
   /**
    * Name of image in cache
    * @return The path of the image (once cached) relative to the cache root
    */
   public String getImageCacheName() {
      return mImageCacheName;
   }
   
   /**
    * Name of image in cache
    * @param _cacheName The path of the image (once cached) relative to the cache root
    */
   public void setImageCacheName(String _cacheName) {
      this.mImageCacheName = _cacheName;
   }
   public int getMaxWidth() {
      return mMaxWidth;
   }
   public void setMaxWidth(int _maxWidth) {
      this.mMaxWidth = _maxWidth;
   }
   public int getMaxHeight() {
      return mMaxHeight;
   }
   public void setMaxHeight(int _maxHeight) {
      this.mMaxHeight = _maxHeight;
   }
   
   
}
