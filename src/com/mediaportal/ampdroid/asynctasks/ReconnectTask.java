package com.mediaportal.ampdroid.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.utils.Constants;

public class ReconnectTask extends AsyncTask<DataHandler, Void, Void> {
   private boolean mTryReconnecting;
   private static final int RECONNECT_TIME = 5000;

   @Override
   protected Void doInBackground(DataHandler... _service) {
      if (_service != null && _service[0] != null) {
         mTryReconnecting = true;
         int attempts = 0;
         while (mTryReconnecting) {
            try {
               attempts++;
               Log.i(Constants.LOG_CONST, "Reconnecting (attempt nr." + attempts + ")");
               if (_service[0].connectClientControl()) {
                  Log.i(Constants.LOG_CONST, "Reconnect successful");
                  return null;
               } else {
                  Log.d(Constants.LOG_CONST, "Reconnect failed, waiting " + RECONNECT_TIME + " ms");
               }
            } catch (Exception ex) {
               Log.e(Constants.LOG_CONST, "Error Reconnecting: " + ex.toString());
            }

            try {
               Thread.sleep(RECONNECT_TIME);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
         }
      }
      return null;
   }

   public void cancelReConnect() {
      mTryReconnecting = false;
   }

}
