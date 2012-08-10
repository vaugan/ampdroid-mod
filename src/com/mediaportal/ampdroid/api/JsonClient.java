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
package com.mediaportal.ampdroid.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import android.util.Log;

import com.mediaportal.ampdroid.utils.Constants;
import com.mediaportal.ampdroid.utils.Util;

public class JsonClient {
   public static int DEFAULT_TIMEOUT = 5000;
   
   public enum RequestMethod {
      GET, POST
   }

   private ArrayList<NameValuePair> headers;

   private String url;

   private String mUser;
   private String mPass;
   private boolean mUseAuth;

   private int responseCode;
   private String message;

   private String response;

   public String getResponse() {
      return response;
   }

   public String getErrorMessage() {
      return message;
   }

   public int getResponseCode() {
      return responseCode;
   }

   public JsonClient(String url, String _user, String _pass, boolean _useAuth) {
      this.url = url;
      mUser = _user;
      mPass = _pass;
      mUseAuth = _useAuth;
      headers = new ArrayList<NameValuePair>();
   }

//   public JsonClient(String url) {
//      this(url, null, null, false);
//   }

   public void AddHeader(String name, String value) {
      headers.add(new BasicNameValuePair(name, value));
   }

   public String Execute(String methodName) {
      return Execute(methodName, DEFAULT_TIMEOUT, RequestMethod.GET);
   }
   
   public String Execute(String methodName, int _timeout) {
      return Execute(methodName, _timeout, RequestMethod.GET);
   }
   
   public String Execute(String methodName, int _timeout, NameValuePair... _params) {
      return Execute(methodName, _timeout, RequestMethod.GET, _params);
   }

   public String Execute(String methodName, NameValuePair... _params) {
      return Execute(methodName, RequestMethod.GET, _params);
   }
   
   public String Execute(String methodName, RequestMethod methodType, NameValuePair... _params) {
      try {
         return DoExecute(methodName, DEFAULT_TIMEOUT, methodType, _params);
      } catch (Exception e) {
         Log.e(Constants.LOG_CONST, e.toString());
         return null;
      }
   }
   
   public String Execute(String methodName, int _timeout, RequestMethod methodType, NameValuePair... _params) {
      try {
         return DoExecute(methodName, _timeout, methodType, _params);
      } catch (Exception e) {
         Log.e(Constants.LOG_CONST, e.toString());
         return null;
      }
   }

   private String DoExecute(String methodName, int _timeout, RequestMethod methodType, NameValuePair... _params)
         throws Exception {
      switch (methodType) {
      case GET: {
         // add parameters
         String combinedParams = "";
         if (_params.length > 0) {
            combinedParams += "?";
            for (NameValuePair p : _params) {
               String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
               if (combinedParams.length() > 1) {
                  combinedParams += "&" + paramString;
               } else {
                  combinedParams += paramString;
               }
            }
         }

         HttpGet request = new HttpGet(url + "/" + methodName + combinedParams);

         // add headers
         for (NameValuePair h : headers) {
            request.addHeader(h.getName(), h.getValue());
         }

         return executeRequest(request, _timeout, url);
      }
      case POST: {
         HttpPost request = new HttpPost(url + "/" + methodName);

         // add headers
         for (NameValuePair h : headers) {
            request.addHeader(h.getName(), h.getValue());
         }

         if (_params.length > 0) {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            for (NameValuePair p : _params) {
               paramList.add(p);
            }
            request.setEntity(new UrlEncodedFormEntity(paramList, HTTP.UTF_8));
         }

         return executeRequest(request, _timeout, url);
      }
      default:
         return null;
      }
   }

   private String executeRequest(HttpUriRequest request, int _timeout, String url) {
      HttpParams httpParameters = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout(httpParameters, _timeout);
      HttpConnectionParams.setSoTimeout(httpParameters, _timeout);
      HttpConnectionParams.setTcpNoDelay(httpParameters, true);

      DefaultHttpClient client = new DefaultHttpClient(httpParameters);
      request.setHeader("User-Agent", Constants.USER_AGENT);

      if (mUseAuth) {
         CredentialsProvider credProvider = new BasicCredentialsProvider();
         credProvider.setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
               new UsernamePasswordCredentials(mUser, mPass));
         client.setCredentialsProvider(credProvider);
      }

      HttpResponse httpResponse;

      try {
         httpResponse = client.execute(request);
         responseCode = httpResponse.getStatusLine().getStatusCode();
         message = httpResponse.getStatusLine().getReasonPhrase();

         HttpEntity entity = httpResponse.getEntity();

         if (entity != null) {

            InputStream instream = entity.getContent();
            response = convertStreamToString(instream);

            // Closing the input stream will trigger connection release
            instream.close();

            return response;
         }

      } catch (ClientProtocolException e) {
         client.getConnectionManager().shutdown();
         e.printStackTrace();
      } catch (IOException e) {
         client.getConnectionManager().shutdown();
         e.printStackTrace();
      }
      return null;
   }

   private static String convertStreamToString(InputStream is) {

      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      StringBuilder sb = new StringBuilder();

      String line = null;
      try {
         while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
         }
      } catch (IOException e) {
         e.printStackTrace();
      } finally {
         try {
            is.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
      return sb.toString();
   }
}
