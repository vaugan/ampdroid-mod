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
/*
 *      Copyright (C) 2005-2009 Team XBMC
 *      http://xbmc.org
 *
 *  This Program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2, or (at your option)
 *  any later version.
 *
 *  This Program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with XBMC Remote; see the file license.  If not, write to
 *  the Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139, USA.
 *  http://www.gnu.org/copyleft/gpl.html
 *
 */

package com.mediaportal.ampdroid.utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.util.Log;

public class WakeOnLan {
   public static Boolean sendMagicPacket(String _mac, String _ip, int _port) {
      try {
         Log.i(Constants.LOG_CONST, "Sending WOL packet to: " + _mac + "|" + _ip + "|" + _port);
         byte[] macBytes = getMacBytes(_mac);
         byte[] bytes = new byte[6 + 16 * macBytes.length];
         for (int i = 0; i < 6; i++) {
            bytes[i] = (byte) 0xff;
         }
         for (int i = 6; i < bytes.length; i += macBytes.length) {
            System.arraycopy(macBytes, 0, bytes, i, macBytes.length);
         }

         InetAddress address = InetAddress.getByName(_ip);
         DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, _port);
         DatagramSocket socket = new DatagramSocket();
         socket.send(packet);
         socket.close();
         return true;
      } catch (Exception e) {
         return false;
      }
   }

   public static Boolean sendMagicPacket(String _mac, int _port) {
      return sendMagicPacket(_mac, "255.255.255.255", _port);
   }

   public static Boolean sendMagicPacket(String _mac) {
      return sendMagicPacket(_mac, "255.255.255.255", 9);
   }

   private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
      byte[] bytes = new byte[6];
      String[] hex = macStr.split("(\\:|\\-)");
      if (hex.length != 6) {
         throw new IllegalArgumentException("Invalid MAC address.");
      }
      try {
         for (int i = 0; i < 6; i++) {
            bytes[i] = (byte) Integer.parseInt(hex[i], 16);
         }
      } catch (NumberFormatException e) {
         throw new IllegalArgumentException("Invalid hex digit in MAC address.");
      }
      return bytes;
   }
}
