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
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
package com.mediaportal.ampdroid.utils;

import java.util.*;

public class IsoDate
{

    public static final int DATE = 1;
    public static final int TIME = 2;
    public static final int DATE_TIME = 3;

    public IsoDate()
    {
    }

    static void dd(StringBuffer stringbuffer, int i)
    {
        stringbuffer.append((char)(48 + i / 10));
        stringbuffer.append((char)(48 + i % 10));
    }

    public static String dateToString(Date date, int i)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.setTime(date);
        StringBuffer stringbuffer = new StringBuffer();
        if((i & 1) != 0)
        {
            int j = calendar.get(1);
            dd(stringbuffer, j / 100);
            dd(stringbuffer, j % 100);
            stringbuffer.append('-');
            dd(stringbuffer, (calendar.get(2) - 0) + 1);
            stringbuffer.append('-');
            dd(stringbuffer, calendar.get(5));
            if(i == 3)
            {
                stringbuffer.append("T");
            }
        }
        if((i & 2) != 0)
        {
            dd(stringbuffer, calendar.get(11));
            stringbuffer.append(':');
            dd(stringbuffer, calendar.get(12));
            stringbuffer.append(':');
            dd(stringbuffer, calendar.get(13));
            stringbuffer.append('.');
            int k = calendar.get(14);
            stringbuffer.append((char)(48 + k / 100));
            dd(stringbuffer, k % 100);
            stringbuffer.append('Z');
        }
        return stringbuffer.toString();
    }

    public static Date stringToDate(String s, int i)
    {
        Calendar calendar = Calendar.getInstance();
        if((i & 1) != 0)
        {
            calendar.set(1, Integer.parseInt(s.substring(0, 4)));
            calendar.set(2, (Integer.parseInt(s.substring(5, 7)) - 1) + 0);
            calendar.set(5, Integer.parseInt(s.substring(8, 10)));
            if(i != 3 || s.length() < 11)
            {
                calendar.set(11, 0);
                calendar.set(12, 0);
                calendar.set(13, 0);
                calendar.set(14, 0);
                return calendar.getTime();
            }
            s = s.substring(11);
        } else
        {
            calendar.setTime(new Date(0L));
        }
        calendar.set(11, Integer.parseInt(s.substring(0, 2)));
        calendar.set(12, Integer.parseInt(s.substring(3, 5)));
        calendar.set(13, Integer.parseInt(s.substring(6, 8)));
        int j = 8;
        if(j < s.length() && s.charAt(j) == '.')
        {
            int k = 0;
            int l = 100;
            do
            {
                char c = s.charAt(++j);
                if(c < '0' || c > '9')
                {
                    break;
                }
                k += (c - 48) * l;
                l /= 10;
            } while(true);
            calendar.set(14, k);
        } else
        {
            calendar.set(14, 0);
        }
        if(j < s.length())
        {
            if(s.charAt(j) == '+' || s.charAt(j) == '-')
            {
                calendar.setTimeZone(TimeZone.getTimeZone("GMT" + s.substring(j)));
            } else
            if(s.charAt(j) == 'Z')
            {
                calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
            } else
            {
                throw new RuntimeException("illegal time format!");
            }
        }
        return calendar.getTime();
    }
}

