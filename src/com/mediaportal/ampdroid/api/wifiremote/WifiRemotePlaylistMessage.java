package com.mediaportal.ampdroid.api.wifiremote;


public class WifiRemotePlaylistMessage extends WifiRemoteMessage {
   public String PlaylistAction;
   public String PlaylistType;

   public WifiRemotePlaylistMessage(String _playlistType, String _action) {
      this.Type = "playlist";
      this.PlaylistType = _playlistType;
      this.PlaylistAction = _action;
   }
}
