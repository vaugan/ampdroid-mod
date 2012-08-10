package com.mediaportal.ampdroid.lists;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;

public class FileCache {
   private File cacheDir;
   
   public FileCache(Context context){
       //Find the dir to save cached images
       if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
          cacheDir = new File(android.os.Environment.getExternalStorageDirectory(),
          "aMPdroid/.Cache");
       else
           cacheDir=context.getCacheDir();
       if(!cacheDir.exists())
           cacheDir.mkdirs();
   }
   
   public File getFile(String filename){
       //I identify images by hashcode. Not a perfect solution, good for the demo.
       //String filename=String.valueOf(url.hashCode());
       File f = new File(cacheDir, filename);
       return f;
       
   }
   
   public void clear(){
       File[] files=cacheDir.listFiles();
       for(File f:files)
           f.delete();
   }

   public void storeBitmap(Bitmap _bitmap, File _file) {
      try {
         
         if (android.os.Environment.getExternalStorageState().equals(
               android.os.Environment.MEDIA_MOUNTED)) {
            File parentDir = _file.getParentFile();
            if (!parentDir.exists()) {
               parentDir.mkdirs();
            }

            FileOutputStream f = new FileOutputStream(_file);
            _bitmap.compress(Bitmap.CompressFormat.JPEG, 100, f);
            f.flush();
            f.close();
         } else {
            // todo: write to internal memory here???
         }

      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
