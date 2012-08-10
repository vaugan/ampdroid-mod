package com.mediaportal.ampdroid.api.wifiremote;

public class WifiRemoteMovePlaylistItemMessage extends WifiRemotePlaylistMessage {

   public WifiRemoteMovePlaylistItemMessage(String _playlistType, String _action, int _oldIndex,
         int _newIndex) {
      super(_playlistType, _action);
      NewIndex = _newIndex;
      OldIndex = _oldIndex;
   }

   public int NewIndex;
   public int OldIndex;

}
