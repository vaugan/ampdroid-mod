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

import java.util.List;

import com.mediaportal.ampdroid.data.MusicTrack;
import com.mediaportal.ampdroid.data.SeriesEpisode;
import com.mediaportal.ampdroid.data.commands.RemoteKey;

public interface IClientControlApi extends IApiInterface {
   boolean connect();

   void disconnect();

   boolean isConnected();

   void setTimeOut(int _timeout);

   int getTimeOut();

   void addApiListener(IClientControlListener _listener);

   void clearApiListener();

   void removeApiListener(IClientControlListener _listener);

   void sendKeyCommand(RemoteKey _key);

   void setVolume(int level);

   int getVolume();

   void startVideo(String _path, int _pos);

   void startAudio(String _path, int _pos);

   void sendKeyDownCommand(RemoteKey _key, int _timeout);

   void sendKeyUpCommand();

   void playTvChannelOnClient(int _channel, boolean _fullscreen);

   void requestPlugins();

   void openWindow(int _windowId, String _parameter);

   void sendPowerMode(PowerModes _mode);

   void sendPosition(int _position);

   void sendRemoteKey(int keyCode, int i);

   void getClientImage(String filePath);

   void createPlaylistWithSongs(List<MusicTrack> _tracks, boolean _autoPlay, int _startPos);

   void requestPlaylist(String _type);

   void movePlaylistItem(String _type, int _oldIndex, int _newIndex);

   void playPlaylistItem(String _type, int _index);

   void clearPlaylistItems(String _type);

   void removePlaylistItem(String _type, int _index);

   void createPlaylistWithEpisodes(List<SeriesEpisode> _episodes, boolean _autoPlay, int _startPos);

}
