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
package com.mediaportal.ampdroid.utils;

import android.view.KeyEvent;

public class SoftkeyboardUtils {

   public static String getChar(int keyCode) {
      switch (keyCode) {
      case KeyEvent.KEYCODE_0:
         return "0";
      case KeyEvent.KEYCODE_1:
         return "1";
      case KeyEvent.KEYCODE_2:
         return "2";
      case KeyEvent.KEYCODE_3:
         return "3";
      case KeyEvent.KEYCODE_4:
         return "4";
      case KeyEvent.KEYCODE_5:
         return "5";
      case KeyEvent.KEYCODE_6:
         return "6";
      case KeyEvent.KEYCODE_7:
         return "7";
      case KeyEvent.KEYCODE_8:
         return "8";
      case KeyEvent.KEYCODE_9:
         return "9";
      case KeyEvent.KEYCODE_A:
         return "a";
      case KeyEvent.KEYCODE_B:
         return "b";
      case KeyEvent.KEYCODE_C:
         return "c";
      case KeyEvent.KEYCODE_D:
         return "d";
      case KeyEvent.KEYCODE_E:
         return "e";
      case KeyEvent.KEYCODE_F:
         return "f";
      case KeyEvent.KEYCODE_G:
         return "g";
      case KeyEvent.KEYCODE_H:
         return "h";
      case KeyEvent.KEYCODE_I:
         return "i";
      case KeyEvent.KEYCODE_J:
         return "j";
      case KeyEvent.KEYCODE_K:
         return "k";
      case KeyEvent.KEYCODE_L:
         return "l";
      case KeyEvent.KEYCODE_M:
         return "m";
      case KeyEvent.KEYCODE_N:
         return "n";
      case KeyEvent.KEYCODE_O:
         return "o";
      case KeyEvent.KEYCODE_P:
         return "p";
      case KeyEvent.KEYCODE_Q:
         return "q";
      case KeyEvent.KEYCODE_R:
         return "r";
      case KeyEvent.KEYCODE_S:
         return "s";
      case KeyEvent.KEYCODE_T:
         return "t";
      case KeyEvent.KEYCODE_U:
         return "u";
      case KeyEvent.KEYCODE_V:
         return "v";
      case KeyEvent.KEYCODE_W:
         return "w";
      case KeyEvent.KEYCODE_X:
         return "x";
      case KeyEvent.KEYCODE_Y:
         return "y";
      case KeyEvent.KEYCODE_Z:
         return "z";
      case KeyEvent.KEYCODE_DEL:
         return "{BKSP}";
      case KeyEvent.KEYCODE_ENTER:
      case KeyEvent.KEYCODE_DPAD_CENTER:
         return "{ENTER}";
      case KeyEvent.KEYCODE_SPACE:
         return " ";
      case KeyEvent.KEYCODE_DPAD_DOWN:
         return "{DOWN}";
      case KeyEvent.KEYCODE_DPAD_UP:
         return "{UP}";
      case KeyEvent.KEYCODE_DPAD_LEFT:
         return "{LEFT}";
      case KeyEvent.KEYCODE_DPAD_RIGHT:
         return "{RIGHT}";
      }
      return null;
   }

}
