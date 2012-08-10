package com.mediaportal.ampdroid.utils;

import android.app.Activity;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class AdUtils {
   public final static String APP_ID = "a14df2720e7131d";

   public static LinearLayout createAdForView(Activity _activity, int _layout) {
      try {
         if (Constants.SHOW_ADS) {
            // Create the adView
            AdView adView = new AdView(_activity, AdSize.BANNER, APP_ID);
            adView.setAdListener(new AdListener() {

               @Override
               public void onReceiveAd(Ad arg0) {
                  Log.d(Constants.LOG_CONST, "Received Ad");
               }

               @Override
               public void onPresentScreen(Ad arg0) {
                  Log.d(Constants.LOG_CONST, "onPresentScreen");
               }

               @Override
               public void onLeaveApplication(Ad arg0) {
                  Log.d(Constants.LOG_CONST, "onLeaveApplication");
               }

               @Override
               public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
                 
                  Log.d(Constants.LOG_CONST, "onFailedToReceiveAd");
               }

               @Override
               public void onDismissScreen(Ad arg0) {
                  Log.d(Constants.LOG_CONST, "onDismissScreen");
               }
            });
            // Lookup your LinearLayout assuming it’s been given
            // the attribute android:id="@+id/mainLayout"
            LinearLayout layout = (LinearLayout) _activity.findViewById(_layout);
            // Add the adView to it
            layout.addView(adView);
            // Initiate a generic request to load it with an ad

            AdRequest req = new AdRequest();
           // req.addTestDevice(AdRequest.TEST_EMULATOR);

            adView.loadAd(req);
            
            return layout;
         }
      } catch (Exception ex) {
         Log.w(Constants.LOG_CONST, "Error creating ad: " + ex.toString());
      }
      return null;
   }

}
