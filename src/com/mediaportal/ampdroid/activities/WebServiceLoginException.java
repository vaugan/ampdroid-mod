package com.mediaportal.ampdroid.activities;

import com.mediaportal.ampdroid.api.exceptions.WebServiceException;

public class WebServiceLoginException extends WebServiceException {
   private static final long serialVersionUID = 3976634791346274824L;
   private boolean mUseAuth;
   private String mUser;
   private String mPass;

   public WebServiceLoginException(boolean useAuth, String user, String pass) {
      mUseAuth = useAuth;
      mUser = user;
      mPass = pass;
   }

   @Override
   public String toString() {
      return "Failed to authenticate with parameters: use-auth=" + mUseAuth + ", user=" + mUser + ", pass=" + mPass;
   }
}
