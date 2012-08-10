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
package com.mediaportal.ampdroid.database;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public class SqliteAnnotationsHelper {
   private static class AccessHolder {
      private Method method;
      private String columnName;
      private String columnType;
      private int columnIndex;
   }

   public static HashMap<Integer, List<AccessHolder>> cachedSetterValues = new HashMap<Integer, List<AccessHolder>>();
   public static HashMap<Integer, List<AccessHolder>> contentValues = new HashMap<Integer, List<AccessHolder>>();

   public static <T> String getCreateTableStringFromClass(String _tableName, Class<T> _class,
         boolean _hasClientId) {
      Method[] methods = _class.getMethods();

      String createString = "create table " + _tableName
            + " ( RowId integer primary key autoincrement";
      if (_hasClientId) {
         createString += ", ClientId integer";
      }
      for (Method m : methods) {
         if (!m.getReturnType().equals(Void.TYPE)) {
            ColumnProperty column = m.getAnnotation(ColumnProperty.class);

            if (column != null) {
               String columnName = column.value();
               String columnType = column.type();
               createString += ", " + columnName + " " + columnType;
            }
         }
      }
      createString += ");";

      return createString;
   }

   public static <T> List<T> getObjectsFromCursor(Cursor _cursor, Class<T> _class, int _limit) {
      try {
         int hashCode = _class.hashCode();
         List<AccessHolder> setters = null;
         if (!cachedSetterValues.containsKey(hashCode)) {
            setters = getAccessHolders(_cursor, _class, true);
            cachedSetterValues.put(hashCode, setters);
         } else {
            setters = cachedSetterValues.get(hashCode);
         }

         List<T> returnList = new ArrayList<T>();

         if (_cursor.getCount() > 0) {
            int count = 0;
            do {
               T returnObject = _class.newInstance();
               fillObjectFromAccessHolders(returnObject, _cursor, setters);
               count++;
               returnList.add(returnObject);
            } while (_cursor.moveToNext() && (count < _limit || _limit == 0));
         }
         return returnList;
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      } catch (InstantiationException e) {
         e.printStackTrace();
      } catch (IllegalArgumentException e) {
         e.printStackTrace();
      }
      return null;
   }

   public static <T> Object getObjectFromCursor(Cursor _cursor, Class<T> _class) {
      try {
         int hashCode = _class.hashCode();
         List<AccessHolder> setters = null;
         if (!cachedSetterValues.containsKey(hashCode)) {
            setters = getAccessHolders(_cursor, _class, true);

            cachedSetterValues.put(hashCode, setters);
         } else {
            setters = cachedSetterValues.get(hashCode);
         }

         if (_cursor.getCount() > 0) {
            Object returnObject = _class.newInstance();
            fillObjectFromAccessHolders(returnObject, _cursor, setters);

            return returnObject;
         } else {
            return null;
         }
      } catch (IllegalAccessException e) {
         e.printStackTrace();
      } catch (InstantiationException e) {
         e.printStackTrace();
      } catch (IllegalArgumentException e) {
         e.printStackTrace();
      }
      return null;
   }

   public static <T> ContentValues getContentValuesFromObject(Object _object, Class<T> _class) {

      int hashCode = _class.hashCode();
      List<AccessHolder> getters = null;
      if (!contentValues.containsKey(hashCode)) {
         getters = getAccessHolders(null, _class, false);

         contentValues.put(hashCode, getters);
      } else {
         getters = contentValues.get(hashCode);
      }

      ContentValues contentValues = new ContentValues();
      for (AccessHolder a : getters) {
         try {
            // column exists
            if (a.columnType.equals("text")) {
               contentValues.put(a.columnName, (String) a.method.invoke(_object));
            } else if (a.columnType.equals("integer")) {
               contentValues.put(a.columnName, (Integer) a.method.invoke(_object));
            } else if (a.columnType.equals("real")) {
               contentValues.put(a.columnName, (Long) a.method.invoke(_object));
            } else if (a.columnType.equals("boolean")) {
               contentValues.put(a.columnName, (Boolean) a.method.invoke(_object));
            } else if (a.columnType.equals("float")) {
               contentValues.put(a.columnName, (Float) a.method.invoke(_object));
            } else if (a.columnType.equals("double")) {
               contentValues.put(a.columnName, (Double) a.method.invoke(_object));
            } else if (a.columnType.equals("textarray")) {
               String[] array = (String[]) a.method.invoke(_object);
               if (array != null) {
                  StringBuilder arrayBuilder = new StringBuilder();
                  for (String t : array) {
                     arrayBuilder.append(t);
                     arrayBuilder.append('|');
                  }
                  contentValues.put(a.columnName, arrayBuilder.toString());
               }
            } else if (a.columnType.equals("date")) {
               Object o = a.method.invoke(_object);
               if (o != null) {
                  Date d = (Date) o;
                  contentValues.put(a.columnName, d.getTime());
               }
            }
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         } catch (IllegalArgumentException e) {
            e.printStackTrace();
         } catch (InvocationTargetException e) {
            e.printStackTrace();
         } catch (Exception e) {
            e.printStackTrace();
         }

      }

      return contentValues;
   }

   private static <T> List<AccessHolder> getAccessHolders(Cursor _cursor, Class<T> _class,
         boolean _setters) {
      List<AccessHolder> accessHolders = new ArrayList<AccessHolder>();
      Method[] allMethods = _class.getMethods();
      for (Method m : allMethods) {
         if (m.getReturnType().equals(Void.TYPE) == _setters) {// get either
                                                               // getters or
                                                               // setters
            ColumnProperty column = m.getAnnotation(ColumnProperty.class);

            if (column != null) {
               String columnName = column.value();
               AccessHolder holder = new AccessHolder();
               holder.columnName = columnName;
               holder.columnType = column.type();

               holder.method = m;

               if (_cursor != null) {
                  int columnIndex = _cursor.getColumnIndex(columnName);
                  if (columnIndex != -1) {// column exists
                     holder.columnIndex = columnIndex;
                  }
               }

               accessHolders.add(holder);
            }
         }
      }
      return accessHolders;
   }

   private static void fillObjectFromAccessHolders(Object _object, Cursor _cursor,
         List<AccessHolder> _holders) {
      for (AccessHolder h : _holders) {
         try {
            if (h.columnType.equals("text")) {
               h.method.invoke(_object, _cursor.getString(h.columnIndex));
            } else if (h.columnType.equals("integer")) {
               h.method.invoke(_object, _cursor.getInt(h.columnIndex));
            } else if (h.columnType.equals("real")) {
               h.method.invoke(_object, _cursor.getLong(h.columnIndex));
            } else if (h.columnType.equals("boolean")) {
               h.method.invoke(_object, (_cursor.getInt(h.columnIndex) == 1));
            } else if (h.columnType.equals("float")) {
               h.method.invoke(_object, _cursor.getFloat(h.columnIndex));
            } else if (h.columnType.equals("double")) {
               h.method.invoke(_object, _cursor.getDouble(h.columnIndex));
            } else if (h.columnType.equals("date")) {
               long dateTimestamp = _cursor.getLong(h.columnIndex);
               if (dateTimestamp != 0) {
                  Date date = new Date(dateTimestamp);
                  h.method.invoke(_object, date);
               }
            } else if (h.columnType.equals("textarray")) {
               String arrayString = _cursor.getString(h.columnIndex);
               if (arrayString != null) {
                  String[] array = arrayString.split("\\|");
                  h.method.invoke(_object, (Object) array);
               }
            }
         } catch (IllegalArgumentException e) {
            e.printStackTrace();
         } catch (IllegalAccessException e) {
            e.printStackTrace();
         } catch (InvocationTargetException e) {
            e.printStackTrace();
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
   }

}
