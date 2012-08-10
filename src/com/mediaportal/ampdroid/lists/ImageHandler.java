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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.lists.LazyLoadingImage.ImageType;
import com.mediaportal.ampdroid.utils.Constants;

public class ImageHandler {
   public static int ImagePrefferedWidth = 400;// width of loaded image ->
   // default = 400
   public static int ImagePrefferedHeight = 300;// height of loaded image ->
   // default = 400

   public static int MEMORY_CACHE_SIZE = 20;

   MemoryCache memoryCache = new MemoryCache();

   private FileCache fileCache;
   private Context mContext;

   public ImageHandler(Context context) {
      // Make the background thead low priority. This way it will not affect
      // the UI performance
      mContext = context;
      photoLoaderThread.setPriority(Thread.MIN_PRIORITY);

      fileCache = new FileCache(context);
   }

   public void DisplayImage(LazyLoadingImage _image, int _loadingImage, Activity activity,
         ImageView imageView) {
      String url = _image.getImageUrl();
      Bitmap bitmap = memoryCache.get(url);

      if (bitmap != null) {
         imageView.setImageBitmap(bitmap);
      } else {
         queuePhoto(_image, activity, imageView);

         // loading image
         if (_loadingImage == 0) {
            // show nothing as loading image
            imageView.setImageBitmap(null);
         } else {
            imageView.setImageResource(_loadingImage);
         }
      }
   }

   private void queuePhoto(LazyLoadingImage _image, Activity activity, ImageView imageView) {
      // This ImageView may be used for other images before. So there may be
      // some old tasks in the queue. We need to discard them.
      photosQueue.Clean(imageView);
      PhotoToLoad p = new PhotoToLoad(_image, imageView);
      synchronized (photosQueue.photosToLoad) {
         photosQueue.photosToLoad.push(p);
         photosQueue.photosToLoad.notifyAll();
      }

      // start thread if it's not started yet
      if (photoLoaderThread.getState() == Thread.State.NEW)
         photoLoaderThread.start();
   }

   public String getHashOfFileName(String url) {
      return String.valueOf(url.hashCode());
   }

   public Bitmap getBitmap(LazyLoadingImage _image, boolean thumb) {
      if (_image != null && _image.getImageUrl() != null && !_image.getImageUrl().equals("")) {

         File file = fileCache.getFile(_image.getImageCacheName());

         // from SD cache
         Bitmap b = decodeFile(file, thumb);

         if (b != null)
            return b;

         // from web
         try {
            DataHandler service = DataHandler.getCurrentRemoteInstance();
            if (_image.getImageType() == ImageType.TvLogo) {
               b = service.getTvLogoImage(_image.getImageUrl());
            } else {
               b = service.getImage(_image.getImageUrl(), _image.getMaxWidth(),
                     _image.getMaxHeight());
            }
         } catch (Exception ex) {
            Log.w(Constants.LOG_CONST, ex);
            return null;
         }

         if (b != null) {
            // save bitmap to sd card for caching
            fileCache.storeBitmap(b, file);
         }

         return b;
      }
      return null;
   }

   // decodes image and scales it to reduce memory consumption
   private Bitmap decodeFile(File file, boolean thumb) {
      try {
         if (!file.exists())
            return null;

         InputStream is = new FileInputStream(file);
         if (thumb) {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            BitmapFactory.decodeStream(is, null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
               if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                  break;
               width_tmp /= 2;
               height_tmp /= 2;
               scale++;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            is = mContext.openFileInput(file.toString());
            return BitmapFactory.decodeStream(is, null, o2);
         } else {
            // full size
            BitmapFactory.Options o = new BitmapFactory.Options();
            // o.
            return BitmapFactory.decodeStream(is, null, o);
         }

      } catch (Exception e) {
      }
      return null;
   }

   // Task for the queue
   private class PhotoToLoad {
      public LazyLoadingImage image;
      public ImageView imageView;

      public PhotoToLoad(LazyLoadingImage _image, ImageView _view) {
         image = _image;
         imageView = _view;
      }
   }

   PhotosQueue photosQueue = new PhotosQueue();

   public void stopThread() {
      photoLoaderThread.interrupt();
   }

   // stores list of photos to download
   class PhotosQueue {
      private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

      // removes all instances of this ImageView
      public void Clean(ImageView image) {
         for (int j = 0; j < photosToLoad.size();) {
            if (photosToLoad.get(j).imageView == image)
               photosToLoad.remove(j);
            else
               ++j;
         }
      }
   }

   class PhotosLoader extends Thread {
      public void run() {
         try {
            while (true) {
               // thread waits until there are any images to load in the
               // queue
               if (photosQueue.photosToLoad.size() == 0)
                  synchronized (photosQueue.photosToLoad) {
                     photosQueue.photosToLoad.wait();
                  }
               if (photosQueue.photosToLoad.size() != 0) {
                  PhotoToLoad photoToLoad;
                  synchronized (photosQueue.photosToLoad) {
                     photoToLoad = photosQueue.photosToLoad.pop();
                  }
                  Bitmap bmp = getBitmap(photoToLoad.image, false);

                  memoryCache.put(photoToLoad.image.getImageUrl(), bmp);
                  // if (memoryCache.size() > MEMORY_CACHE_SIZE) {
                  // // cache.
                  // }

                  if (bmp != null) {
                     try {
                        if (((String) photoToLoad.imageView.getTag()).equals(photoToLoad.image
                              .getImageUrl())) {
                           BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad.imageView);
                           Activity a = (Activity) photoToLoad.imageView.getContext();
                           a.runOnUiThread(bd);
                        }
                     } catch (Exception ex) {
                        Log.e(Constants.LOG_CONST, "Error on displaying image:" + ex.toString());
                     }
                  }
               }
               if (Thread.interrupted())
                  break;
            }
         } catch (InterruptedException e) {
            // allow thread to exit
         }
      }
   }

   PhotosLoader photoLoaderThread = new PhotosLoader();

   // Used to display bitmap in the UI thread
   class BitmapDisplayer implements Runnable {
      Bitmap bitmap;
      ImageView imageView;

      public BitmapDisplayer(Bitmap b, ImageView i) {
         bitmap = b;
         imageView = i;
      }

      public void run() {
         if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
         }
      }
   }

   /**
    * Clear memory cache
    */
   public void clearCache() {
      memoryCache.clear();
   }

   /**
    * Delete an image from the image cache
    * 
    * @param original
    *           URL of the image
    */
   public void deleteImage(String url) {
      String filename = getHashOfFileName(url);
      File fileToDelete = new File(filename);
      fileToDelete.delete();
   }

}
