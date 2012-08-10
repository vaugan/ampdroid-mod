package com.mediaportal.ampdroid.api.wifiremote;

import com.mediaportal.ampdroid.data.MusicTrack;
import com.mediaportal.ampdroid.data.SeriesEpisode;

public class WifiRemotePlaylistEntry {
   public String Name;
   public String FileName;
   public int Duration;

   public WifiRemotePlaylistEntry(MusicTrack _track) {
      this.Name = _track.getTitle();
      this.FileName = _track.getFilePath();
      this.Duration = _track.getDuration();
   }
   
   public WifiRemotePlaylistEntry(SeriesEpisode _episode) {
      this.Name = _episode.getName();
      this.FileName = _episode.getFileName();
      this.Duration = 0;
   }
}
