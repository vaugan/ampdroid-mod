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

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.util.Log;

import com.mediaportal.ampdroid.api.gmawebservice.GmaSortOptions;
import com.mediaportal.ampdroid.utils.IsoDate;
import com.mediaportal.ampdroid.utils.Constants;

public class JsonUtils {
   @SuppressWarnings({ "rawtypes", "unchecked" })
   public static Object getObjectsFromJson(String _jsonString, Class _class, ObjectMapper _objectMapper) {
      try {
         Object returnObjects = _objectMapper.readValue(_jsonString, _class);

         return returnObjects;
      } catch (JsonParseException e) {
         Log.e(Constants.LOG_CONST, e.toString());
      } catch (JsonMappingException e) {
         Log.e(Constants.LOG_CONST, e.toString());
      } catch (IOException e) {
         Log.e(Constants.LOG_CONST, e.toString());
      }
      return null;
   }

   public static BasicNameValuePair newPair(String _name, String _value) {
      return new BasicNameValuePair(_name, _value);
   }

   public static BasicNameValuePair newPair(String _name, int _value) {
      return new BasicNameValuePair(_name, String.valueOf(_value));
   }
   
   public static NameValuePair newPair(String _name, GmaSortOptions _sort) {
      return new BasicNameValuePair(_name, String.valueOf(_sort.ordinal()));
   }

   public static BasicNameValuePair newPair(String _name, Date _value) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(_value);
      int offset = (int) ((cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET)) / 60000);

      cal.add(Calendar.MINUTE, offset);
      String dateString = IsoDate.dateToString(cal.getTime(), IsoDate.DATE_TIME);

      return new BasicNameValuePair(_name, dateString);
   }


}
