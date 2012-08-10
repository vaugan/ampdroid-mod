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

import com.mediaportal.ampdroid.activities.WebServiceLoginException;
import com.mediaportal.ampdroid.api.IApiInterface;
import com.mediaportal.ampdroid.api.IClientControlApi;
import com.mediaportal.ampdroid.api.IMediaAccessApi;
import com.mediaportal.ampdroid.api.ITvServiceApi;
import com.mediaportal.ampdroid.utils.Util;

public class RemoteClient {
   private int mClientId;
   private String mClientName;
   private String mClientDescription;
   private int mRemoteAccessApiId;
   private int mTvControlApiId;
   private int mClientControlApiId;

   private IMediaAccessApi mRemoteAccessApi;
   private ITvServiceApi mTvControlApi;
   private IClientControlApi mClientControlApi;

   public RemoteClient(int _clientId) {
      mClientId = _clientId;
   }

   public RemoteClient(int _clientId, String _clientName) {
      this(_clientId);
      mClientName = _clientName;
   }

   public RemoteClient(int _clientId, String _clientName, IMediaAccessApi _remoteAccessApi,
         ITvServiceApi _tvControlApi, IClientControlApi _clientControlApi) {
      this(_clientId, _clientName);
      mRemoteAccessApi = _remoteAccessApi;
      mTvControlApi = _tvControlApi;
      mClientControlApi = _clientControlApi;
   }

   public int getClientId() {
      return mClientId;
   }

   public void setClientId(int clientId) {
      this.mClientId = clientId;
   }

   public void setClientName(String clientName) {
      this.mClientName = clientName;
   }

   public String getClientName() {
      return mClientName;
   }

   public String getClientDescription() {
      return mClientDescription;
   }

   public void setClientDescription(String clientDescription) {
      this.mClientDescription = clientDescription;
   }

   public int getRemoteAccessApiId() {
      return mRemoteAccessApiId;
   }

   public void setRemoteAccessApiId(int mRemoteAccessApiId) {
      this.mRemoteAccessApiId = mRemoteAccessApiId;
   }

   public int getTvControlApiId() {
      return mTvControlApiId;
   }

   public void setTvControlApiId(int mTvControlApiId) {
      this.mTvControlApiId = mTvControlApiId;
   }

   public int getClientControlApiId() {
      return mClientControlApiId;
   }

   public void setClientControlApiId(int mClientControlApiId) {
      this.mClientControlApiId = mClientControlApiId;
   }

   public String getClientAddress() {
      if (!compareApiClients(0)) {// all clients have same address
         return "";
      } else {
         if (mRemoteAccessApi != null) {
            return mRemoteAccessApi.getAddress();
         }
         if (mClientControlApi != null) {
            return mClientControlApi.getAddress();
         }
         if (mTvControlApi != null) {
            return mTvControlApi.getAddress();
         }
      }
      return "No api defined";// shouldn't be possible
   }

   /**
    * Compare the api clients
    * 
    * @param _field
    *           which field to compare (0: address, 1:user, 2:pass, 3: auth)
    * @return true if similar, false otherwise
    */
   private boolean compareApiClients(int _field) {
      if (mRemoteAccessApi != null) {
         if (!compareApi(mRemoteAccessApi, mTvControlApi, _field)
               || !compareApi(mRemoteAccessApi, mClientControlApi, _field)) {
            return false;
         }
      }

      if (mTvControlApi != null) {
         if (!compareApi(mTvControlApi, mRemoteAccessApi, _field)
               || !compareApi(mTvControlApi, mClientControlApi, _field)) {
            return false;
         }
      }

      if (mClientControlApi != null) {
         if (!compareApi(mClientControlApi, mRemoteAccessApi, _field)
               || !compareApi(mClientControlApi, mTvControlApi, _field)) {
            return false;
         }
      }

      return true;
   }

   private boolean compareApi(IApiInterface _api1, IApiInterface _api2, int _field) {
      if (_api1 == null || _api2 == null)
         return true;
      switch (_field) {
      case 0:
         if (_api1.getAddress() == null || _api2.getAddress() == null) {
            return false;
         }
         return _api1.getAddress().equals(_api2.getAddress());
      case 1:
         if (_api1.getUserName() == null || _api2.getUserName() == null) {
            return false;
         }
         return _api1.getUserName().equals(_api2.getUserName());
      case 2:
         if (_api1.getUserPass() == null || _api2.getUserPass() == null) {
            return false;
         }
         return _api1.getUserPass().equals(_api2.getUserPass());
      case 3:
         return _api1.getUseAuth() == _api2.getUseAuth();
      case 4:
         if (_api1.getMac() == null || _api2.getMac() == null) {
            return false;
         }
         return _api1.getMac().equals(_api2.getMac());
      default:
         return true;
      }

   }

   @Override
   public String toString() {
      if (mClientName != null) {
         return mClientName;
      } else {
         return "Client" + mClientId;
      }
   }

   public IMediaAccessApi getRemoteAccessApi() {
      return mRemoteAccessApi;
   }

   public void setRemoteAccessApi(IMediaAccessApi remoteAccessApi) {
      this.mRemoteAccessApi = remoteAccessApi;
   }

   public ITvServiceApi getTvControlApi() {
      return mTvControlApi;
   }

   public void setTvControlApi(ITvServiceApi tvControlApi) {
      this.mTvControlApi = tvControlApi;
   }

   public IClientControlApi getClientControlApi() {
      return mClientControlApi;
   }

   public void setClientControlApi(IClientControlApi clientControlApi) {
      this.mClientControlApi = clientControlApi;
   }

   public boolean useAuth() {
      if (Util.compare(mClientControlApi.getUseAuth(), mRemoteAccessApi.getUseAuth(),
            mTvControlApi.getUseAuth())) {
         return mClientControlApi.getUseAuth();
      }
      return false;
   }

   public String getUserPassword() {
      if (!compareApiClients(2)) {// all clients have same password
         return "Different";
      } else {
         if (mRemoteAccessApi != null) {
            return mRemoteAccessApi.getUserPass();
         }
         if (mClientControlApi != null) {
            return mClientControlApi.getUserPass();
         }
         if (mTvControlApi != null) {
            return mTvControlApi.getUserPass();
         }
         return "No api defined";// shouldn't be possible
      }
   }

   public String getUserName() {
      if (!compareApiClients(1)) {// all clients have same address
         return "Different";
      } else {
         if (mRemoteAccessApi != null) {
            return mRemoteAccessApi.getUserName();
         }
         if (mClientControlApi != null) {
            return mClientControlApi.getUserName();
         }
         if (mTvControlApi != null) {
            return mTvControlApi.getUserName();
         }
         return "No api defined";// shouldn't be possible
      }
   }
   
   public String getMac() {
      if (!compareApiClients(4)) {// all clients have same address
         return "Different";
      } else {
         if (mRemoteAccessApi != null) {
            return mRemoteAccessApi.getMac();
         }
         if (mClientControlApi != null) {
            return mClientControlApi.getMac();
         }
         if (mTvControlApi != null) {
            return mTvControlApi.getMac();
         }
         return "No api defined";// shouldn't be possible
      }
   }

   public boolean hasDifferentSettings() {
      if (!Util.compare(mClientControlApi.getAddress(), mRemoteAccessApi.getAddress(),
            mTvControlApi.getAddress())) {
         return true;
      }
      
      if (!Util.compare(mClientControlApi.getMac(), mRemoteAccessApi.getMac(),
            mTvControlApi.getMac())) {
         return true;
      }
      
      if (!Util.compare(mClientControlApi.getUserName(), mRemoteAccessApi.getUserName(),
            mTvControlApi.getUserName())) {
         return true;
      }
      if (!Util.compare(mClientControlApi.getUserPass(), mRemoteAccessApi.getUserPass(),
            mTvControlApi.getUserPass())) {
         return true;
      }
      if (!Util.compare(mClientControlApi.getUseAuth(), mRemoteAccessApi.getUseAuth(),
            mTvControlApi.getUseAuth())) {
         return true;
      }
      return false;
   }


}
