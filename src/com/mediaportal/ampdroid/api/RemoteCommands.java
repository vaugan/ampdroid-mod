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

import com.mediaportal.ampdroid.data.commands.RemoteKey;

public class RemoteCommands {
   public static RemoteKey leftButton = new RemoteKey(0, "Left", "left", "");
   public static RemoteKey rightButton = new RemoteKey(1, "Right", "right", "");
   public static RemoteKey upButton = new RemoteKey(2, "Up", "up", "");
   public static RemoteKey downButton = new RemoteKey(3, "Down", "down", "");
   public static RemoteKey backButton = new RemoteKey(4, "Back", "back", "");
   public static RemoteKey okButton = new RemoteKey(4, "OK", "ok", "");
   public static RemoteKey infoButton = new RemoteKey(5, "Info", "info", "");
   public static RemoteKey pauseButton = new RemoteKey(6, "Pause", "pause", "");
   public static RemoteKey stopButton = new RemoteKey(6, "Stop", "stop", "");
   public static RemoteKey prevButton = new RemoteKey(7, "Prev", "replay", "");
   public static RemoteKey nextButton = new RemoteKey(8, "Next", "skip", "");
   public static RemoteKey homeButton = new RemoteKey(9, "Home", "home", "");
   public static RemoteKey switchFullscreenButton = new RemoteKey(10, "Switch Fullscreen", "red", "");
   public static RemoteKey subtitlesButton = new RemoteKey(8, "Subtitles", "subtitles", "");
   public static RemoteKey audioTrackButton = new RemoteKey(9, "Audio Tracks", "audiotracks", "");
   public static RemoteKey menuButton = new RemoteKey(10, "Menu", "menu", "");
   public static RemoteKey channelUpButton = new RemoteKey(11, "Channel Up", "chup", "");
   public static RemoteKey channelDownButton = new RemoteKey(12, "Channel Down", "chdown", "");
   public static RemoteKey recordingButton = new RemoteKey(13, "Recording", "record", "");
   public static RemoteKey muteButton = new RemoteKey(13, "Mute", "volmute", "");
}
