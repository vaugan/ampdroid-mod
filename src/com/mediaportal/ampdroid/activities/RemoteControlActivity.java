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
package com.mediaportal.ampdroid.activities;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.api.PowerModes;
import com.mediaportal.ampdroid.api.RemoteCommands;
import com.mediaportal.ampdroid.data.commands.RemoteKey;
import com.mediaportal.ampdroid.remote.RemotePlugin;
import com.mediaportal.ampdroid.remote.RemotePluginMessage;
import com.mediaportal.ampdroid.remote.RemoteStatusMessage;
import com.mediaportal.ampdroid.utils.Util;

public class RemoteControlActivity extends BaseActivity {
   protected class SendKeyTask extends AsyncTask<RemoteKey, String, String> {
      private DataHandler mController;
      private boolean mRepeat;
      private Context mContext;

      protected SendKeyTask(Context _parent) {
         mContext = _parent;
      }

      private SendKeyTask(Context _parent, DataHandler _controller) {
         this(_parent);
         mController = _controller;
      }

      private SendKeyTask(Context _parent, DataHandler _controller, boolean _repeat) {
         this(_parent, _controller);
         mController = _controller;
         mRepeat = _repeat;
      }

      @Override
      protected String doInBackground(RemoteKey... _keys) {
         if (mController.isClientControlConnected()) {
            mController.sendRemoteButton((RemoteKey) _keys[0]);

            waitForMilliseconds(500);

            while (mRepeat) {
               try {
                  mController.sendRemoteButtonDown((RemoteKey) _keys[0], 60);
                  Thread.sleep(1000);
               } catch (InterruptedException e) {
                  e.printStackTrace();
               }
            }
            return null;
         } else {
            return getString(R.string.info_remote_notconnected);
         }
      }

      private void waitForMilliseconds(int _ms) {
         int msDone = 0;
         while (mRepeat && msDone < _ms) {
            try {
               Thread.sleep(10);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            msDone += 10;
         }
      }

      @Override
      protected void onPostExecute(String _result) {
         if (_result != null) {
            Util.showToast(mContext, _result);
         }
      }

      public void setRepeat(boolean _repeat) {
         this.mRepeat = _repeat;
      }

      public boolean getRepeat() {
         return mRepeat;
      }
   }

   protected class SendKeyUpTask extends AsyncTask<RemoteKey, String, String> {
      private DataHandler mController;

      private SendKeyUpTask(Context _parent, DataHandler _controller) {
         mController = _controller;
      }

      @Override
      protected String doInBackground(RemoteKey... _keys) {
         mController.sendRemoteButtonUp();
         return null;
      }
   }

   protected class RequestPluginsTask extends AsyncTask<Void, String, String> {
      private DataHandler mController;

      private RequestPluginsTask(Context _parent, DataHandler _controller) {
         mController = _controller;
      }

      @Override
      protected String doInBackground(Void... _keys) {
         if (mController != null) {
            mController.requestPlugins();
         }
         return null;
      }
   }

   private TextView mStatusLabel;
   private RequestPluginsTask mRequestPluginsTask;
   private RemotePlugin[] mRemotePlugins;
   protected InputMethodManager m;
   protected EditText mEditTextKeyboardInput;
   protected boolean mKeyboardShown;
   private ImageButton mMoreButton;

   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_remotecontrol);

      mRequestPluginsTask = new RequestPluginsTask(this, mService);
      mRequestPluginsTask.execute();

      mStatusLabel = (TextView) findViewById(R.id.TextViewRemoteState);

      final ImageButton backButton = (ImageButton) findViewById(R.id.ImageButtonBack);
      backButton.setOnTouchListener(new OnTouchListener() {
         @Override
         public boolean onTouch(View _view, MotionEvent _event) {
            if (_event.getAction() == MotionEvent.ACTION_DOWN) {
               Util.Vibrate(_view.getContext(), 30);
               new SendKeyTask(_view.getContext(), mService).execute(RemoteCommands.backButton);
               backButton.setImageResource(R.drawable.remote_back_sel);
               return true;
            }
            if (_event.getAction() == MotionEvent.ACTION_UP) {
               backButton.setImageResource(R.drawable.remote_back);
               return true;
            }
            return false;
         }
      });

      final ImageButton infoButton = (ImageButton) findViewById(R.id.ImageButtonInfo);
      infoButton.setOnTouchListener(new OnTouchListener() {
         @Override
         public boolean onTouch(View _view, MotionEvent _event) {
            if (_event.getAction() == MotionEvent.ACTION_DOWN) {
               Util.Vibrate(_view.getContext(), 30);
               new SendKeyTask(_view.getContext(), mService).execute(RemoteCommands.infoButton);
               infoButton.setImageResource(R.drawable.remote_info_sel);
               return true;
            }
            if (_event.getAction() == MotionEvent.ACTION_UP) {
               infoButton.setImageResource(R.drawable.remote_info);
               return true;
            }
            return false;
         }
      });

      final ImageButton homeButton = (ImageButton) findViewById(R.id.ImageButtonHome);
      homeButton.setOnTouchListener(new OnTouchListener() {
         @Override
         public boolean onTouch(View _view, MotionEvent _event) {
            if (_event.getAction() == MotionEvent.ACTION_DOWN) {
               Util.Vibrate(_view.getContext(), 30);

               new SendKeyTask(_view.getContext(), mService).execute(RemoteCommands.homeButton);
               homeButton.setImageResource(R.drawable.remote_home_sel);
               return true;
            }
            if (_event.getAction() == MotionEvent.ACTION_UP) {
               homeButton.setImageResource(R.drawable.remote_home);
               return true;
            }
            return false;
         }
      });

      final ImageButton switchFullscreenButton = (ImageButton) findViewById(R.id.ImageButtonSwitchFullscreen);
      switchFullscreenButton.setOnTouchListener(new OnTouchListener() {
         @Override
         public boolean onTouch(View _view, MotionEvent _event) {
            if (_event.getAction() == MotionEvent.ACTION_DOWN) {
               Util.Vibrate(_view.getContext(), 30);

               new SendKeyTask(_view.getContext(), mService)
                     .execute(RemoteCommands.switchFullscreenButton);
               switchFullscreenButton.setImageResource(R.drawable.remote_switch_fullscreen_sel);
               return true;
            }
            if (_event.getAction() == MotionEvent.ACTION_UP) {
               switchFullscreenButton.setImageResource(R.drawable.remote_switch_fullscreen);
               return true;
            }
            return false;
         }
      });

      mMoreButton = (ImageButton) findViewById(R.id.ImageButtonMore);
      mMoreButton.setOnTouchListener(new OnTouchListener() {
         @Override
         public boolean onTouch(View _view, MotionEvent _event) {
            if (_event.getAction() == MotionEvent.ACTION_DOWN) {
               Util.Vibrate(_view.getContext(), 30);

               mMoreButton.setImageResource(R.drawable.remote_more_sel);
               return true;
            }
            if (_event.getAction() == MotionEvent.ACTION_UP) {
               mMoreButton.setImageResource(R.drawable.remote_more);
               registerForContextMenu(mMoreButton);
               mMoreButton.showContextMenu();
               return true;
            }
            return false;
         }
      });

      final ImageButton remote = (ImageButton) findViewById(R.id.ImageButtonArrows);

      remote.setOnTouchListener(new OnTouchListener() {
         SendKeyTask task = null;

         @Override
         public boolean onTouch(View _view, MotionEvent _event) {
            if (_event.getAction() == MotionEvent.ACTION_DOWN) {
               Util.Vibrate(_view.getContext(), 30);
               float x = _event.getX();
               float y = _event.getY();
               int index = getTouchPart(x, y);

               switch (index) {
               case 0:
                  remote.setBackgroundResource(R.drawable.remote_left);
                  if (task != null) {
                     task.setRepeat(false);
                  }
                  task = (SendKeyTask) new SendKeyTask(_view.getContext(), mService, true)
                        .execute(RemoteCommands.leftButton);
                  break;
               case 1:
                  remote.setBackgroundResource(R.drawable.remote_right);
                  if (task != null) {
                     task.setRepeat(false);
                  }
                  task = (SendKeyTask) new SendKeyTask(_view.getContext(), mService, true)
                        .execute(RemoteCommands.rightButton);
                  break;
               case 2:
                  remote.setBackgroundResource(R.drawable.remote_up);
                  if (task != null) {
                     task.setRepeat(false);
                  }
                  task = (SendKeyTask) new SendKeyTask(_view.getContext(), mService, true)
                        .execute(RemoteCommands.upButton);
                  break;
               case 3:
                  remote.setBackgroundResource(R.drawable.remote_down);
                  if (task != null) {
                     task.setRepeat(false);
                  }
                  task = (SendKeyTask) new SendKeyTask(_view.getContext(), mService, true)
                        .execute(RemoteCommands.downButton);
                  break;
               case 4:
                  remote.setBackgroundResource(R.drawable.remote_enter);
                  if (task != null) {
                     task.setRepeat(false);
                  }
                  task = (SendKeyTask) new SendKeyTask(_view.getContext(), mService, true)
                        .execute(RemoteCommands.okButton);
                  break;
               }

            }
            if (_event.getAction() == MotionEvent.ACTION_UP) {
               if (task != null) {
                  task.setRepeat(false);
               }
               new SendKeyUpTask(_view.getContext(), mService).execute();
               // mService.sendRemoteButtonUp();
               remote.setBackgroundResource(R.drawable.remote_default);
            }
            return false;
         }

         private int getTouchPart(float _x, float _y) {
            int width = remote.getWidth();
            int height = remote.getHeight();

            if (_x < width * 0.25 && _y > height * 0.10 && _y < height * 0.9) {
               // left
               return 0;
            }

            if (_x > width * 0.75 && _y > height * 0.10 && _y < height * 0.9) {
               // right
               return 1;
            }

            if (_y < height * 0.25 && _x > width * 0.10 && _x < width * 0.9) {
               return 2;
            }

            if (_y > height * 0.75 && _x > width * 0.10 && _x < width * 0.9) {
               return 3;
            }

            if (_x > width * 0.25 && _x < width * 0.75 && _y > height * 0.25 && _y < height * 0.75) {
               return 4;// middle
            }
            return -99;
         }

      });

      mEditTextKeyboardInput = (EditText) findViewById(R.id.EditTextKeyboardInput);

   }

   @Override
   public void messageReceived(Object _message) {
      super.messageReceived(_message);
      if (_message != null) {
         if (_message.getClass().equals(RemoteStatusMessage.class)) {
            String module = ((RemoteStatusMessage) _message).getCurrentModule();
            mStatusLabel.setText(module);
         } else if (_message.getClass().equals(RemotePluginMessage.class)) {
            mRemotePlugins = ((RemotePluginMessage) _message).getPlugins();
         }
      }

   }

   @Override
   public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);
      if (v.equals(mMoreButton)) {
         createMoreCommandsMenu(menu);
      }
   }

   @Override
   public boolean onCreateOptionsMenu(Menu _menu) {
      super.onCreateOptionsMenu(_menu);
      SubMenu moreItem = _menu.addSubMenu(0, Menu.FIRST + 1, Menu.NONE,
            getString(R.string.remote_menu_morecommands));
      moreItem.setIcon(R.drawable.ic_menu_more2);
      createMoreCommandsMenu(moreItem);

      SubMenu powerItem = _menu.addSubMenu(0, Menu.FIRST + 2, Menu.NONE,
            getString(R.string.remote_menu_powermodes));
      powerItem.setIcon(R.drawable.ic_menu_powermodes);
      createPowerModeMenu(powerItem);

      SubMenu pluginsItem = _menu.addSubMenu(0, Menu.FIRST + 3, Menu.NONE,
            getString(R.string.remote_menu_plugins));
      pluginsItem.setIcon(R.drawable.ic_menu_more);
      createPluginsMenu(pluginsItem);

      MenuItem keyboardItem = _menu.add(0, Menu.FIRST + 4, Menu.NONE,
            getString(R.string.remote_menu_keyboard));
      keyboardItem.setIcon(R.drawable.ic_menu_keyboard);
      keyboardItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            mKeyboardShown = true;
            // FrameLayout keyboardLayout = (FrameLayout)
            // findViewById(R.id.FrameLayoutKeyboard);
            // keyboardLayout.setVisibility(View.VISIBLE);

            // mEditTextKeyboardInput.requestFocus();
            return false;
         }
      });

      return true;
   }

   @Override
   public boolean onKeyUp(int keyCode, KeyEvent event) {
      if (mKeyboardShown && keyCode != KeyEvent.KEYCODE_BACK) {
         mService.sendRemoteKey(keyCode, 0);
         return true;
      } else {
         mKeyboardShown = false;
         return super.onKeyUp(keyCode, event);
      }
   }

   @Override
   public boolean onKeyDown(int keyCode, KeyEvent event) {
      if (mKeyboardShown) {
         return true;
      } else {
         return super.onKeyDown(keyCode, event);
      }
   }

   private void createPluginsMenu(SubMenu _menu) {
      if (mRemotePlugins != null) {
         for (final RemotePlugin p : mRemotePlugins) {
            MenuItem pluginItem = _menu.add(0, Menu.FIRST, Menu.NONE, p.getName());
            pluginItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
               @Override
               public boolean onMenuItemClick(MenuItem item) {
                  mService.sendOpenWindowMessage(p.getWindowId(), null);
                  return false;
               }
            });
         }
      }

   }

   private void createPowerModeMenu(Menu _powerItem) {
      MenuItem logoffSettingsItem = _powerItem.add(0, Menu.FIRST, Menu.NONE,
            getString(R.string.remote_logoff));
      logoffSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendSetPowerModeCommand(PowerModes.Logoff);
            return true;
         }
      });

      MenuItem suspendSettingsItem = _powerItem.add(0, Menu.FIRST, Menu.NONE,
            getString(R.string.remote_suspend));
      suspendSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendSetPowerModeCommand(PowerModes.Suspend);
            return true;
         }
      });

      MenuItem hibernateSettingsItem = _powerItem.add(0, Menu.FIRST, Menu.NONE,
            getString(R.string.remote_hibernate));
      hibernateSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendSetPowerModeCommand(PowerModes.Hibernate);
            return true;
         }
      });

      MenuItem rebootSettingsItem = _powerItem.add(0, Menu.FIRST, Menu.NONE,
            getString(R.string.remote_reboot));
      rebootSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendSetPowerModeCommand(PowerModes.Reboot);
            return true;
         }
      });

      MenuItem shutdownSettingsItem = _powerItem.add(0, Menu.FIRST, Menu.NONE,
            getString(R.string.remote_shutdown));
      shutdownSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendSetPowerModeCommand(PowerModes.Shutdown);
            return true;
         }
      });

      MenuItem exitSettingsItem = _powerItem.add(0, Menu.FIRST, Menu.NONE,
            getString(R.string.remote_exit));
      exitSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendSetPowerModeCommand(PowerModes.Exit);
            return true;
         }
      });
   }

   private void createMoreCommandsMenu(Menu _menu) {
      MenuItem stopSettingsItem = _menu.add(0, Menu.FIRST, Menu.NONE,
            RemoteCommands.stopButton.getName());
      stopSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendRemoteButton(RemoteCommands.stopButton);
            return true;
         }
      });

      MenuItem switchFullscreenSettingsItem = _menu.add(0, Menu.FIRST + 1, Menu.NONE,
            RemoteCommands.switchFullscreenButton.getName());
      switchFullscreenSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendRemoteButton(RemoteCommands.switchFullscreenButton);
            return true;
         }
      });

      MenuItem subtitlesSettingsItem = _menu.add(0, Menu.FIRST + 2, Menu.NONE,
            RemoteCommands.subtitlesButton.getName());
      subtitlesSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendRemoteButton(RemoteCommands.subtitlesButton);
            return true;
         }
      });

      MenuItem switchAudioTrackSettingsItem = _menu.add(0, Menu.FIRST + 3, Menu.NONE,
            RemoteCommands.audioTrackButton.getName());
      switchAudioTrackSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendRemoteButton(RemoteCommands.audioTrackButton);
            return true;
         }
      });

      MenuItem menuSettingsItem = _menu.add(0, Menu.FIRST + 4, Menu.NONE,
            RemoteCommands.menuButton.getName());
      menuSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendRemoteButton(RemoteCommands.menuButton);
            return true;
         }
      });

      MenuItem channelUpSettingsItem = _menu.add(0, Menu.FIRST + 5, Menu.NONE,
            RemoteCommands.channelUpButton.getName());
      channelUpSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendRemoteButton(RemoteCommands.channelUpButton);
            return true;
         }
      });

      MenuItem channelDownSettingsItem = _menu.add(0, Menu.FIRST + 6, Menu.NONE,
            RemoteCommands.channelDownButton.getName());
      channelDownSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendRemoteButton(RemoteCommands.channelDownButton);
            return true;
         }
      });

      MenuItem recordingSettingsItem = _menu.add(0, Menu.FIRST + 7, Menu.NONE,
            RemoteCommands.recordingButton.getName());
      recordingSettingsItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem item) {
            mService.sendRemoteButton(RemoteCommands.recordingButton);
            return true;
         }
      });
   }

   @Override
   public void onAttachedToWindow() {
      super.onAttachedToWindow();
      Window window = getWindow();
      window.setFormat(PixelFormat.RGBA_8888);
   }
}
