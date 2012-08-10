package com.mediaportal.ampdroid.api.wifiremote;

public class WifiRemotePlaylistIndexItemMessage extends WifiRemotePlaylistMessage {
   public int Index;

   public WifiRemotePlaylistIndexItemMessage(String _playlistType, String _action, int _index) {
      super(_playlistType, _action);
      Index = _index;
   }

}
