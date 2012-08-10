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

import java.util.Random;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.DialogPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.api.gmawebservice.GmaJsonWebserviceApi;
import com.mediaportal.ampdroid.api.tv4home.Tv4HomeJsonApi;
import com.mediaportal.ampdroid.api.wifiremote.WifiRemoteMpController;
import com.mediaportal.ampdroid.data.RemoteClient;
import com.mediaportal.ampdroid.database.SettingsDatabaseHandler;
import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.Util;

public class ClientPreference extends DialogPreference {
   private Context mContext;
   private RemoteClient mClient;
   private SettingsDatabaseHandler mDbHandler;

   private EditText mNameView;
   private EditText mHostView;
   private EditText mUserView;
   private EditText mPassView;
   private EditText mEditTextMac;

   private CheckBox mUseAuthView;
   private EditText mEditTextGmaHost;
   private EditText mEditTextGmaPort;
   private EditText mEditTextGmaMac;
   private EditText mEditTextTv4HomeHost;
   private EditText mEditTextTv4HomePort;
   private EditText mEditTextTv4HomeMac;
   private EditText mEditTextWifiRemoteHost;
   private EditText mEditTextWifiRemotePort;
   private EditText mEditTextWifiRemoteMac;
   private CheckBox mUseGma;
   private CheckBox mUseTv4Home;
   private CheckBox mUseWifiRemote;
   private boolean mUseSimple = true;
   private LinearLayout mLinearLayoutSimple;
   private LinearLayout mLinearLayoutAdvanced;
   private EditText mUserViewGma;
   private EditText mPassViewGma;
   private CheckBox mUseGmaAuth;
   private CheckBox mUseTv4HomeAuth;
   private EditText mUserViewTv4Home;
   private EditText mPassViewTv4Home;
   private CheckBox mUseWifiRemoteAuth;
   private EditText mUserViewWifiRemote;
   private EditText mPassViewWifiRemote;
   private QRClientDescription mQrDescription;
   private PreferenceScreen mRoot;
   private Button mButtonSwitchSimpleAdvanced;

   public ClientPreference(Context context, AttributeSet attrs) {
      super(context, attrs);

      mContext = context;

      setDialogLayoutResource(R.layout.preference_host);
      setDialogTitle(mContext.getString(R.string.dialog_title_addhost));
      setDialogIcon(R.drawable.bubble_add);
   }

   public ClientPreference(Context context) {
      this(context, null);
   }

   public void fillFromQRCode(QRClientDescription _desc) {
      mQrDescription = _desc;
      // set defaults:
      if (mQrDescription != null) {

         if (mQrDescription.getHardwareAddresses() != null) {
            String addr = mQrDescription.getHardwareAddresses();
            if (addr.contains(";")) {
               final String[] items = addr.split(";");
               if (items.length == 0) {

               }
               AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
               builder.setTitle(mContext.getString(R.string.settings_clients_clientmac));
               builder.setItems(items, new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int item) {
                     setHardwareAddress(items[item]);
                  }
               });
               AlertDialog alert = builder.create();

               alert.show();
            } else {
               setHardwareAddress(addr);
            }
         }

         if (mQrDescription.getAuthOptions() != 0) {
            mUseAuthView.setChecked(true);
         } else {
            mUseAuthView.setChecked(false);
         }

         if (mQrDescription.getName() != null) {
            mNameView.setText(mQrDescription.getName());
         }

         if (mQrDescription.getUser() != null) {
            mUserView.setText(mQrDescription.getUser());
         }

         if (mQrDescription.getPassword() != null) {
            mPassView.setText(mQrDescription.getPassword());
         }

         if (mQrDescription.getAddress() != null) {
            mHostView.setText(mQrDescription.getAddress());
         }
      }
   }

   private void setHardwareAddress(String addr) {
      if (mEditTextMac != null) {
         if (addr.contains(":") || addr.contains("-")) {
            mEditTextMac.setText(addr);
         } else {
            StringBuilder newMac = new StringBuilder();
            for (int i = 0; i < addr.length(); i++) {
               newMac.append(addr.charAt(i));
               if (i % 2 == 1 && i < addr.length() - 1) {
                  newMac.append(":");
               }
            }
            mEditTextMac.setText(newMac.toString());
         }
      }

   }

   public void create(PreferenceManager preferenceManager, PreferenceScreen _root) {
      onAttachedToHierarchy(preferenceManager);
      mRoot = _root;
      showDialog(null);
   }

   public void setClient(RemoteClient _client) {
      mClient = _client;
      setTitle(_client.getClientName());
      setSummary(_client.getClientDescription());
      setDialogTitle(_client.getClientName());
      setDialogIcon(null);
   }

   public RemoteClient getHost() {
      return mClient;
   }

   public void setDbHandler(SettingsDatabaseHandler _dbHandler) {
      mDbHandler = _dbHandler;
   }

   @Override
   protected View onCreateView(final ViewGroup parent) {
      final ViewGroup view = (ViewGroup) super.onCreateView(parent);
      ImageView btn = new ImageView(getContext());
      btn.setImageResource(R.drawable.bubble_del_up);
      btn.setClickable(true);
      btn.setOnClickListener(new OnClickListener() {
         public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(mContext.getString(R.string.settings_delete_client_title));
            builder.setMessage(mContext.getString(R.string.settings_delete_client_start)
                  + mClient.getClientName()
                  + mContext.getString(R.string.settings_delete_client_end));
            builder.setPositiveButton(mContext.getString(R.string.dialog_yes),
                  new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {
                        mDbHandler.removeRemoteClient(mClient);
                        ((PreferenceActivity) view.getContext()).getPreferenceScreen()
                              .removePreference(ClientPreference.this);
                     }
                  });
            builder.setNegativeButton(mContext.getString(R.string.dialog_no),
                  new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                     }
                  });
            builder.create().show();
         }
      });
      view.addView(btn);
      return view;
   }

   @Override
   protected View onCreateDialogView() {
      final ViewGroup parent = (ViewGroup) super.onCreateDialogView();

      // simple layout
      mNameView = (EditText) parent.findViewById(R.id.pref_name);
      mHostView = (EditText) parent.findViewById(R.id.pref_host);
      mUserView = (EditText) parent.findViewById(R.id.pref_user);
      mEditTextMac = (EditText) parent.findViewById(R.id.pref_mac);
      // mUserView.setHint(mContext.getString(R.string.settings_clients_default)
      // + Constants.DEFAULT_USERNAME);
      mPassView = (EditText) parent.findViewById(R.id.pref_pass);
      // mPassView.setHint(mContext.getString(R.string.settings_clients_default)
      // + Constants.DEFAULT_PASSWORD);
      mUseAuthView = (CheckBox) parent.findViewById(R.id.pref_use_auth);
      mUseAuthView.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setUseAuth(isChecked, mUseAuthView, mUserView, mPassView);
         }
      });

      // advanced layout
      mEditTextGmaHost = (EditText) parent.findViewById(R.id.pref_host_gma_addr);
      mEditTextGmaPort = (EditText) parent.findViewById(R.id.pref_host_gma_port);
      mEditTextGmaPort.setHint(mContext.getString(R.string.settings_clients_default)
            + String.valueOf(Constants.DEFAULT_REMOTEACCESS_PORT));
      mEditTextGmaMac = (EditText) parent.findViewById(R.id.pref_host_gma_mac);
      mUseGma = (CheckBox) parent.findViewById(R.id.pref_gma_use_service);
      mUseGmaAuth = (CheckBox) parent.findViewById(R.id.pref_gma_use_auth);
      mUserViewGma = (EditText) parent.findViewById(R.id.pref_gma_user);
      // mUserViewGma.setHint(mContext.getString(R.string.settings_clients_default)
      // + Constants.DEFAULT_USERNAME);
      mPassViewGma = (EditText) parent.findViewById(R.id.pref_gma_pass);
      // mPassViewGma.setHint(mContext.getString(R.string.settings_clients_default)
      // + Constants.DEFAULT_PASSWORD);
      mUseGmaAuth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setUseAuth(isChecked, mUseGmaAuth, mUserViewGma, mPassViewGma);
         }
      });

      mEditTextTv4HomeHost = (EditText) parent.findViewById(R.id.pref_host_tv4home_addr);
      mEditTextTv4HomePort = (EditText) parent.findViewById(R.id.pref_host_tv4home_port);
      mEditTextTv4HomePort.setHint(mContext.getString(R.string.settings_clients_default)
            + String.valueOf(Constants.DEFAULT_TV_PORT));
      mEditTextTv4HomeMac = (EditText) parent.findViewById(R.id.pref_host_tv4home_mac);
      mUseTv4Home = (CheckBox) parent.findViewById(R.id.pref_tv4home_use_service);
      mUseTv4HomeAuth = (CheckBox) parent.findViewById(R.id.pref_tv4home_use_auth);
      mUserViewTv4Home = (EditText) parent.findViewById(R.id.pref_tv4home_user);
      // mUserViewTv4Home.setHint(mContext.getString(R.string.settings_clients_default)
      // + Constants.DEFAULT_USERNAME);
      mPassViewTv4Home = (EditText) parent.findViewById(R.id.pref_tv4home_pass);
      // mPassViewTv4Home.setHint(mContext.getString(R.string.settings_clients_default)
      // + Constants.DEFAULT_PASSWORD);
      mUseTv4HomeAuth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setUseAuth(isChecked, mUseTv4HomeAuth, mUserViewTv4Home, mPassViewTv4Home);
         }
      });

      mEditTextWifiRemoteHost = (EditText) parent.findViewById(R.id.pref_host_wifiremote_addr);
      mEditTextWifiRemotePort = (EditText) parent.findViewById(R.id.pref_host_wifiremote_port);
      mEditTextWifiRemotePort.setHint(mContext.getString(R.string.settings_clients_default)
            + String.valueOf(Constants.DEFAULT_WIFI_PORT));
      mEditTextWifiRemoteMac = (EditText) parent.findViewById(R.id.pref_host_wifiremote_mac);
      mUseWifiRemote = (CheckBox) parent.findViewById(R.id.pref_wifiremote_use_service);
      mUseWifiRemoteAuth = (CheckBox) parent.findViewById(R.id.pref_wifiremote_use_auth);
      mUserViewWifiRemote = (EditText) parent.findViewById(R.id.pref_wifiremote_user);
      // mUserViewWifiRemote.setHint(mContext.getString(R.string.settings_clients_default)
      // + Constants.DEFAULT_USERNAME);
      mPassViewWifiRemote = (EditText) parent.findViewById(R.id.pref_wifiremote_pass);
      // mPassViewWifiRemote.setHint(mContext.getString(R.string.settings_clients_default)
      // + Constants.DEFAULT_PASSWORD);
      mUseWifiRemoteAuth.setOnCheckedChangeListener(new OnCheckedChangeListener() {
         @Override
         public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setUseAuth(isChecked, mUseWifiRemoteAuth, mUserViewWifiRemote, mPassViewWifiRemote);
         }
      });

      mLinearLayoutSimple = (LinearLayout) parent.findViewById(R.id.LinearLayoutSimpleHost);
      mLinearLayoutAdvanced = (LinearLayout) parent.findViewById(R.id.LinearLayoutAdvancedHost);
      mButtonSwitchSimpleAdvanced = (Button) parent.findViewById(R.id.ButtonSwitchSimpleAdvanced);
      mButtonSwitchSimpleAdvanced.setOnClickListener(new OnClickListener() {
         @Override
         public void onClick(View arg0) {
            if (!mUseSimple && hasDifferentSettings()) {
               // user wants to switch to simple mode AND has different settings
               // for clients
               // -> warn that those different values will be overwritten
               AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
               builder.setTitle(mContext
                     .getString(R.string.settings_clients_switchsimple_warning_title));
               builder.setMessage(mContext
                     .getString(R.string.settings_clients_switchsimple_warning_text));
               builder.setPositiveButton(mContext.getString(R.string.dialog_yes),
                     new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           handleSwitchSimpleAdvancedMode(!mUseSimple);
                        }
                     });
               builder.setNegativeButton(mContext.getString(R.string.dialog_no),
                     new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                           dialog.cancel();
                        }
                     });
               builder.create().show();
            } else {
               handleSwitchSimpleAdvancedMode(!mUseSimple);
            }
         }
      });

      return parent;
   }

   protected boolean hasDifferentSettings() {
      if (!Util.compare(mEditTextTv4HomeHost.getText().toString(), mEditTextGmaHost.getText()
            .toString(), mEditTextWifiRemoteHost.getText().toString())) {
         return true;
      }

      if (!Util.compare(mEditTextTv4HomeMac.getText().toString(), mEditTextGmaMac.getText()
            .toString(), mEditTextWifiRemoteMac.getText().toString())) {
         return true;
      }

      if (!Util.compare(mUserViewTv4Home.getText().toString(), mUserViewGma.getText().toString(),
            mUserViewWifiRemote.getText().toString())) {
         return true;
      }
      if (!Util.compare(mPassViewTv4Home.getText().toString(), mPassViewGma.getText().toString(),
            mPassViewWifiRemote.getText().toString())) {
         return true;
      }
      if (!Util.compare(mUseTv4HomeAuth.isChecked(), mUseGmaAuth.isChecked(),
            mUseWifiRemoteAuth.isChecked())) {
         return true;
      }
      return false;
   }

   private void handleSimpleAdvancedModeVisibility(boolean _state) {
      if (_state) {
         mLinearLayoutSimple.setVisibility(View.VISIBLE);
         mLinearLayoutAdvanced.setVisibility(View.GONE);
         mButtonSwitchSimpleAdvanced.setText(mContext
               .getString(R.string.settings_clients_switchadvanced));
      } else {
         mLinearLayoutSimple.setVisibility(View.GONE);
         mLinearLayoutAdvanced.setVisibility(View.VISIBLE);
         mButtonSwitchSimpleAdvanced.setText(mContext
               .getString(R.string.settings_clients_switchsimple));
      }
   }

   private void handleSwitchSimpleAdvancedMode(boolean _state) {
      handleSimpleAdvancedModeVisibility(_state);
      if (_state) {
         if (Util.compare(mEditTextTv4HomeHost.getText().toString(), mEditTextGmaHost.getText()
               .toString(), mEditTextWifiRemoteHost.getText().toString())) {
            mHostView.setText(mEditTextTv4HomeHost.getText().toString());
         } else {
            mHostView.setText("");
         }

         if (Util.compare(mEditTextTv4HomeMac.getText().toString(), mEditTextGmaMac.getText()
               .toString(), mEditTextWifiRemoteMac.getText().toString())) {
            mEditTextMac.setText(mEditTextTv4HomeMac.getText().toString());
         } else {
            mEditTextMac.setText("");
         }

         if (Util.compare(mUserViewTv4Home.getText().toString(), mUserViewGma.getText().toString(),
               mUserViewWifiRemote.getText().toString())) {
            mUserView.setText(mUserViewTv4Home.getText().toString());
         } else {
            mUserView.setText("");
         }

         if (Util.compare(mPassViewTv4Home.getText().toString(), mPassViewGma.getText().toString(),
               mPassViewWifiRemote.getText().toString())) {
            mPassView.setText(mPassViewTv4Home.getText().toString());
         } else {
            mPassView.setText("");
         }

         if (Util.compare(mUseTv4HomeAuth.isChecked(), mUseGmaAuth.isChecked(),
               mUseWifiRemoteAuth.isChecked())) {
            mUseAuthView.setChecked(mUseTv4HomeAuth.isChecked());
         } else {
            mUseAuthView.setChecked(false);
         }

      } else {
         String host = mHostView.getText().toString();
         String user = mUserView.getText().toString();
         String pass = mPassView.getText().toString();
         String mac = mEditTextMac.getText().toString();
         boolean useAuth = mUseAuthView.isChecked();

         mEditTextGmaHost.setText(host);
         mEditTextGmaPort.setText(String.valueOf(Constants.DEFAULT_REMOTEACCESS_PORT));
         mUserViewGma.setText(user);
         mPassViewGma.setText(pass);
         mEditTextGmaMac.setText(mac);
         setUseAuth(useAuth, mUseGmaAuth, mUserViewGma, mPassViewGma);

         mEditTextTv4HomeHost.setText(host);
         mEditTextTv4HomePort.setText(String.valueOf(Constants.DEFAULT_TV_PORT));
         mEditTextTv4HomeMac.setText(mac);
         mUserViewTv4Home.setText(user);
         mPassViewTv4Home.setText(pass);
         setUseAuth(useAuth, mUseTv4HomeAuth, mUserViewTv4Home, mPassViewTv4Home);

         mEditTextWifiRemoteHost.setText(host);
         mEditTextWifiRemotePort.setText(String.valueOf(Constants.DEFAULT_WIFI_PORT));
         mEditTextWifiRemoteMac.setText(mac);
         mUserViewWifiRemote.setText(user);
         mPassViewWifiRemote.setText(pass);
         setUseAuth(useAuth, mUseWifiRemoteAuth, mUserViewWifiRemote, mPassViewWifiRemote);
      }
      mUseSimple = _state;
   }

   @Override
   protected void onBindDialogView(View view) {
      super.onBindDialogView(view);
      if (mClient != null) {

         // general
         mNameView.setText(mClient.getClientName());

         // simple
         String addr = mClient.getClientAddress();
         String mac = mClient.getMac();
         String user = mClient.getUserName();
         String pwd = mClient.getUserPassword();

         mHostView.setText(addr);
         mEditTextMac.setText(mac);
         mUserView.setText(user);
         mPassView.setText(pwd);
         setUseAuth(mClient.useAuth(), mUseAuthView, mUserView, mPassView);

         // advanced
         mEditTextGmaHost.setText(mClient.getRemoteAccessApi().getServer());
         mEditTextGmaPort.setText(String.valueOf(mClient.getRemoteAccessApi().getPort()));
         mEditTextGmaMac.setText(String.valueOf(mClient.getRemoteAccessApi().getMac()));
         mUserViewGma.setText(mClient.getRemoteAccessApi().getUserName());
         mPassViewGma.setText(mClient.getRemoteAccessApi().getUserPass());
         setUseAuth(mClient.getRemoteAccessApi().getUseAuth(), mUseGmaAuth, mUserViewGma,
               mPassViewGma);

         mEditTextTv4HomeHost.setText(mClient.getTvControlApi().getServer());
         mEditTextTv4HomePort.setText(String.valueOf(mClient.getTvControlApi().getPort()));
         mEditTextTv4HomeMac.setText(String.valueOf(mClient.getTvControlApi().getMac()));
         mUserViewTv4Home.setText(mClient.getTvControlApi().getUserName());
         mPassViewTv4Home.setText(mClient.getTvControlApi().getUserPass());
         setUseAuth(mClient.getTvControlApi().getUseAuth(), mUseTv4HomeAuth, mUserViewTv4Home,
               mPassViewTv4Home);

         mEditTextWifiRemoteHost.setText(mClient.getClientControlApi().getServer());
         mEditTextWifiRemotePort.setText(String.valueOf(mClient.getClientControlApi().getPort()));
         mEditTextWifiRemoteMac.setText(String.valueOf(mClient.getClientControlApi().getMac()));
         mUserViewWifiRemote.setText(mClient.getClientControlApi().getUserName());
         mPassViewWifiRemote.setText(mClient.getClientControlApi().getUserPass());
         setUseAuth(mClient.getClientControlApi().getUseAuth(), mUseWifiRemoteAuth,
               mUserViewWifiRemote, mPassViewWifiRemote);

         boolean hasDiffSettings = mClient.hasDifferentSettings();
         handleSimpleAdvancedModeVisibility(!hasDiffSettings);
         mUseSimple = !hasDiffSettings;
      } else {
         handleSimpleAdvancedModeVisibility(true);
      }
   }

   private void setUseAuth(boolean useAuth, CheckBox _checkBox, EditText _userEdit,
         EditText _passEdit) {
      _checkBox.setChecked(useAuth);
      _userEdit.setEnabled(useAuth);
      _passEdit.setEnabled(useAuth);
   }

   @Override
   protected void onDialogClosed(boolean positiveResult) {
      super.onDialogClosed(positiveResult);
      if (positiveResult) {
         boolean create = false;
         if (mClient == null) {
            Random rnd = new Random();
            mClient = new RemoteClient(rnd.nextInt());
            create = true;
         }
         mClient.setClientName(mNameView.getText().toString());

         if (mUseSimple) {
            String addr = mHostView.getText().toString();
            String mac = mEditTextMac.getText().toString();
            String user = mUserView.getText().toString();
            String pass = mPassView.getText().toString();
            boolean auth = mUseAuthView.isChecked();
            GmaJsonWebserviceApi api = new GmaJsonWebserviceApi(addr,
                  Constants.DEFAULT_REMOTEACCESS_PORT, mac, user, pass, auth);
            mClient.setRemoteAccessApi(api);

            Tv4HomeJsonApi tvApi = new Tv4HomeJsonApi(addr, Constants.DEFAULT_TV_PORT, mac, user,
                  pass, auth);
            mClient.setTvControlApi(tvApi);

            WifiRemoteMpController clientApi = new WifiRemoteMpController(mContext, addr,
                  Constants.DEFAULT_WIFI_PORT, mac, user, pass, auth);
            mClient.setClientControlApi(clientApi);
         } else {
            String gmaAddr = mEditTextGmaHost.getText().toString();
            String gmaPortString = mEditTextGmaPort.getText().toString();
            String gmaMac = mEditTextGmaMac.getText().toString();
            String gmaUser = mUserViewGma.getText().toString();
            String gmaPass = mPassViewGma.getText().toString();
            boolean gmaUseAuth = mUseGmaAuth.isChecked();
            
            int gmaPort = Constants.DEFAULT_REMOTEACCESS_PORT;
            try {
               gmaPort = Integer.valueOf(gmaPortString);
            } catch (Exception ex) {
               Log.w(Constants.LOG_CONST, gmaPortString + " is not a valid port");
            }
            
            GmaJsonWebserviceApi api = new GmaJsonWebserviceApi(gmaAddr, gmaPort,
                  gmaMac, gmaUser, gmaPass, gmaUseAuth);
            mClient.setRemoteAccessApi(api);

            String tv4homeAddr = mEditTextTv4HomeHost.getText().toString();
            String tv4homePort = mEditTextTv4HomePort.getText().toString();
            String tv4homeMac = mEditTextTv4HomeMac.getText().toString();
            String tvUser = mUserViewTv4Home.getText().toString();
            String tvPass = mPassViewTv4Home.getText().toString();
            boolean tvUseAuth = mUseTv4HomeAuth.isChecked();
            int tvPort = Constants.DEFAULT_TV_PORT;
            try {
               tvPort = Integer.valueOf(tv4homePort);
            } catch (Exception ex) {
               Log.w(Constants.LOG_CONST, tv4homePort + " is not a valid port");
            }

            Tv4HomeJsonApi tvApi = new Tv4HomeJsonApi(tv4homeAddr, tvPort, tv4homeMac, tvUser,
                  tvPass, tvUseAuth);
            mClient.setTvControlApi(tvApi);

            String wifiAddr = mEditTextWifiRemoteHost.getText().toString();
            String wifiPortString = mEditTextWifiRemotePort.getText().toString();
            String wifiMac = mEditTextWifiRemoteMac.getText().toString();
            String wifiUser = mUserViewWifiRemote.getText().toString();
            String wifiPass = mPassViewWifiRemote.getText().toString();
            boolean wifiUseAuth = mUseWifiRemoteAuth.isChecked();

            int wifiPort = Constants.DEFAULT_WIFI_PORT;
            try {
               wifiPort = Integer.valueOf(wifiPortString);
            } catch (Exception ex) {
               Log.w(Constants.LOG_CONST, wifiPortString + " is not a valid port");
            }

            WifiRemoteMpController clientApi = new WifiRemoteMpController(mContext, wifiAddr,
                  wifiPort, wifiMac, wifiUser, wifiPass, wifiUseAuth);
            mClient.setClientControlApi(clientApi);
         }

         if (create) {
            mDbHandler.addRemoteClient(mClient);
         } else {
            mDbHandler.updateRemoteClient(mClient);
         }

         DataHandler.updateRemoteClient(mClient);
         // host.user = mUserView.getText().toString();
         // host.pass = mPassView.getText().toString();

         // host.access_point = mAccPointView.getText().toString();
         // host.wifi_only = mWifiOnlyView.isChecked();

         setClient(mClient);

      } else {
         if (mClient == null) {
            mRoot.removePreference(ClientPreference.this);
         }
      }
   }

}
