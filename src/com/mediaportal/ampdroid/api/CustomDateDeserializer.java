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
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;

@SuppressWarnings("rawtypes")
public class CustomDateDeserializer extends JsonDeserializer {

   @Override
   public Object deserialize(JsonParser _parser, DeserializationContext _context)
         throws IOException, JsonProcessingException {
      String dateStr = _parser.getText();
      
      //the f*** format .NET uses here is \/Date(xxxxxxxxx+xxxx)\/ were xxx is a timestamp
      dateStr = dateStr.substring(6, dateStr.length() - 2);
      
      //TODO: this filters out the "+0100" timezone representation at the end of the string
      // this might lead to problems in the future :(
      int posPlus = dateStr.indexOf('+');
      int posMinus = dateStr.indexOf('-');
      
      if(posPlus > 0){
         dateStr = dateStr.substring(0, posPlus);
      }
      
      if(posMinus > 0){
         dateStr = dateStr.substring(0, posMinus);
      }

      long timestamp = Long.parseLong(dateStr);
      Date date = new Date(timestamp);
      return date;

   }

}
