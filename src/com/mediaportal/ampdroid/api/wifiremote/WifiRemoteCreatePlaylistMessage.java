package com.mediaportal.ampdroid.api.wifiremote;

import java.util.ArrayList;
import java.util.List;

import com.mediaportal.ampdroid.data.MusicTrack;
import com.mediaportal.ampdroid.data.SeriesEpisode;

public class WifiRemoteCreatePlaylistMessage extends WifiRemoteMessage {


   public boolean AutoPlay;
   public String PlaylistAction;
   public String PlaylistType;
   public List<WifiRemotePlaylistEntry> PlaylistItems;
   public int StartPosition;
   
   public WifiRemoteCreatePlaylistMessage(){
      
   }

   public static WifiRemoteCreatePlaylistMessage createFromMusicTracks(List<MusicTrack> _tracks, boolean _autoPlay, int _startPos) {
      WifiRemoteCreatePlaylistMessage msg = new WifiRemoteCreatePlaylistMessage();
      
      msg.Type = "playlist";
      msg.PlaylistAction = "new";
      msg.PlaylistType = "music";
      msg.AutoPlay = _autoPlay;
      msg.StartPosition = _startPos;

      msg.PlaylistItems = new ArrayList<WifiRemotePlaylistEntry>();
      for (MusicTrack t : _tracks) {
         msg.PlaylistItems.add(new WifiRemotePlaylistEntry(t));
      }
      
      return msg;
   }

   public static WifiRemoteCreatePlaylistMessage createFromEpisodes(List<SeriesEpisode> _episodes, boolean _autoPlay,
         int _startPos) {
      WifiRemoteCreatePlaylistMessage msg = new WifiRemoteCreatePlaylistMessage();
      
      msg.Type = "playlist";
      msg.PlaylistAction = "new";
      msg.PlaylistType = "video";
      msg.AutoPlay = _autoPlay;
      msg.StartPosition = _startPos;

      msg.PlaylistItems = new ArrayList<WifiRemotePlaylistEntry>();
      for (SeriesEpisode e : _episodes) {
         msg.PlaylistItems.add(new WifiRemotePlaylistEntry(e));
      }
      
      return msg;
   }

}
