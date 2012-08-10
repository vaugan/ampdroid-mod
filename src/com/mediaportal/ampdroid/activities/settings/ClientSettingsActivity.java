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
package com.mediaportal.ampdroid.activities.settings;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.barcodes.IntentIntegrator;
import com.mediaportal.ampdroid.barcodes.IntentResult;
import com.mediaportal.ampdroid.data.RemoteClient;
import com.mediaportal.ampdroid.database.SettingsDatabaseHandler;
import com.mediaportal.ampdroid.utils.Util;

public class ClientSettingsActivity extends PreferenceActivity {
   public static final int MENU_ADD_HOST = 1;
   public static final int MENU_EXIT = 2;
   public static final int MENU_ADD_HOST_SCAN = 3;
   private static final int MENU_ZEROCONF_SCAN = 0;
   private SettingsDatabaseHandler mDbHandler;
   private PreferenceManager mPrefsManager;
   private PreferenceScreen mRoot;
   private QRClientDescription mQrDescription = null;

   android.net.wifi.WifiManager.MulticastLock lock;
   android.os.Handler handler = new android.os.Handler();

   private String type = "_mepo-remote._tcp.local.";

   private JmDNS jmdns = null;
   private ServiceListener listener = null;
   private ServiceInfo serviceInfo;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setTitle("MediaPortal Clients");
   }

   private void setUp() {
      try {
         android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) getSystemService(android.content.Context.WIFI_SERVICE);
         lock = wifi.createMulticastLock("mylockthereturn");

         WifiInfo wifiinfo = wifi.getConnectionInfo();
         int intaddr = wifiinfo.getIpAddress();

         byte[] byteaddr = new byte[] { (byte) (intaddr & 0xff), (byte) (intaddr >> 8 & 0xff),
               (byte) (intaddr >> 16 & 0xff), (byte) (intaddr >> 24 & 0xff) };
         InetAddress addr = InetAddress.getByAddress(byteaddr);

         lock.setReferenceCounted(true);
         lock.acquire();

         jmdns = JmDNS.create(addr);
         jmdns.addServiceListener(type, listener = new ServiceListener() {
            @Override
            public void serviceResolved(ServiceEvent ev) {
               showToast("Service resolved: " + ev.getInfo().getQualifiedName() + " port:"
                     + ev.getInfo().getPort());
            }

            @Override
            public void serviceRemoved(ServiceEvent ev) {
               showToast("Service removed: " + ev.getName());
            }

            @Override
            public void serviceAdded(ServiceEvent event) {
               showToast("Service Added");
               // Required to force serviceResolved to be called again (after
               // the first search)
               jmdns.requestServiceInfo(event.getType(), event.getName(), 1);
            }
         });
         // serviceInfo = ServiceInfo.create("_test._tcp.local.", "AndroidTest",
         // 0,
         // "plain test service from android");
         // jmdns.registerService(serviceInfo);
      } catch (IOException e) {
         e.printStackTrace();
         return;
      }
   }

   protected void showToast(String _text) {
      Util.showToast(this, _text);
   }

   private void notifyUser(final String msg) {
      /*
       * handler.postDelayed(new Runnable() { public void run() {
       * 
       * TextView t = (TextView)findViewById(R.id.text);
       * t.setText(msg+"\n=== "+t.getText()); } }, 1);
       */

   }

   @Override
   protected void onResume() {
      mPrefsManager = getPreferenceManager();
      mRoot = mPrefsManager.createPreferenceScreen(this);

      mDbHandler = new SettingsDatabaseHandler(this);
      mDbHandler.open();
      List<RemoteClient> clients = mDbHandler.getClients();

      if (clients != null) {
         for (RemoteClient c : clients) {
            ClientPreference pref = new ClientPreference(this);
            pref.setTitle(c.getClientName());
            pref.setSummary("Id: " + c.getClientId());
            pref.setClient(c);
            pref.setDbHandler(mDbHandler);

            mRoot.addPreference(pref);
         }
      }
      if (clients == null || clients.size() == 0) {
         Util.showToast(this, getString(R.string.settings_clients_noclients));
      }

      setPreferenceScreen(mRoot);

      if (mQrDescription != null) {
         ClientPreference pref = new ClientPreference(this);
         pref.setTitle("New Client");
         pref.create(mPrefsManager, mRoot);
         mRoot.addPreference(pref);
         pref.fillFromQRCode(mQrDescription);
         pref.setDbHandler(mDbHandler);
         mQrDescription = null;
      }
      super.onResume();
   }

   @Override
   protected void onPause() {
      mDbHandler.close();
      super.onPause();
   }

   @Override
   protected void onStop() {
      if (jmdns != null) {
         if (listener != null) {
            jmdns.removeServiceListener(type, listener);
            listener = null;
         }
         jmdns.unregisterAllServices();
         try {
            jmdns.close();
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         jmdns = null;
      }
      // repo.stop();
      // s.stop();
      lock.release();
      super.onStop();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      menu.addSubMenu(0, MENU_ADD_HOST, 0, getString(R.string.menu_addhost)).setIcon(
            R.drawable.ic_menu_addclients);
      menu.addSubMenu(0, MENU_ADD_HOST_SCAN, 0, getString(R.string.menu_addhost_scan)).setIcon(
            R.drawable.ic_menu_barcode);
      menu.addSubMenu(0, MENU_ZEROCONF_SCAN, 0, "Zeroconf");

      // menu.addSubMenu(0, MENU_EXIT, 0, "Exit");//
      // .setIcon(R.drawable.menu_exit);

      return true;
   }

   @Override
   public boolean onMenuItemSelected(int featureId, MenuItem item) {
      switch (item.getItemId()) {
      case MENU_ADD_HOST:
         ClientPreference pref = new ClientPreference(this);
         pref.setTitle("New Client");
         pref.create(mPrefsManager, mRoot);
         mRoot.addPreference(pref);
         pref.setDbHandler(mDbHandler);
         break;
      case MENU_ADD_HOST_SCAN:
         scan();
         break;
      case MENU_ZEROCONF_SCAN:
         handler.postDelayed(new Runnable() {
            public void run() {
               setUp();
            }
         }, 1000);
         break;
      case MENU_EXIT:
         System.exit(0);
         break;
      }
      return true;
   }

   protected void scan() {
      IntentIntegrator.initiateScan(this);
   }

   public void onActivityResult(int requestCode, int resultCode, Intent intent) {
      IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode,
            intent);
      if (scanResult != null) {
         if (scanResult.getContents() != null) {

            try {
               String content = scanResult.getContents();
               ObjectMapper mapper = new ObjectMapper();
               mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

               // add a client from QR code information
               mQrDescription = mapper.readValue(content, QRClientDescription.class);
            } catch (JsonParseException e) {
               e.printStackTrace();
            } catch (JsonMappingException e) {
               e.printStackTrace();
            } catch (IOException e) {
               e.printStackTrace();
            }
         }
         // handle scan result
      }
   }
}
