package com.mediaportal.ampdroid.utils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.videoplayback.StreamingDetailsActivity;
import com.mediaportal.ampdroid.activities.videoplayback.VideoStreamingPlayerActivity;
import com.mediaportal.ampdroid.api.DataHandler;
import com.mediaportal.ampdroid.data.StreamProfile;
import com.mediaportal.ampdroid.data.TvChannel;
import com.mediaportal.ampdroid.downloadservice.DownloadItemType;
import com.mediaportal.ampdroid.settings.PreferencesManager;

public class IntentUtils {
   public static Intent startTvStreaming(Activity _parent, DataHandler _service, TvChannel _channel) {
      if (PreferencesManager.useDirectStream()) {
         String url = _service.startTimeshift(_channel.getIdChannel(),
               PreferencesManager.getClientName());

         if (url != null) {
            try {
               Intent i = new Intent(Intent.ACTION_VIEW);
               i.setDataAndType(Uri.parse(url), "video/*");
               _parent.startActivityForResult(i, 1);
            } catch (Exception ex) {
               Log.e(Constants.LOG_CONST, ex.toString());
            }
         } else {
            Util.showToast(_parent, _parent.getString(R.string.tvserver_errorplaying));
         }
      } else {
         if (PreferencesManager.isQuickStartEnabled(true)) {
            String defaultProfile = PreferencesManager.getDefaultTvProfile();
            List<String> profiles = PreferencesManager.getTvProfiles();
            startStreamingMediaPlayer(_parent, String.valueOf(_channel.getIdChannel()),
                  DownloadItemType.toInt(DownloadItemType.LiveTv), _channel.getDisplayName(),
                  defaultProfile, profiles, 0);

         } else {
            Intent streamIntent = new Intent(_parent, StreamingDetailsActivity.class);
            streamIntent.putExtra("video_id", String.valueOf(_channel.getIdChannel()));
            streamIntent.putExtra("video_type", DownloadItemType.toInt(DownloadItemType.LiveTv));
            streamIntent.putExtra("video_name", _channel.getDisplayName());
            _parent.startActivity(streamIntent);
         }
      }
      return null;
   }

   public static void startStreamingMediaPlayer(Context _context, String _videoId, int _videoType,
         String _videoName, String _startProfile, List<String> _profiles, int _startFrom) {
      Intent streamIntent = new Intent(_context, VideoStreamingPlayerActivity.class);
      streamIntent.putExtra("video_id", _videoId);
      streamIntent.putExtra("video_type", _videoType);
      streamIntent.putExtra("video_name", _videoName);
      streamIntent.putExtra("profile_name", _startProfile);
      streamIntent.putExtra("video_startfrom", _startFrom);

      if (_profiles != null) {
         String[] strResult = new String[_profiles.size()];
         for (int i = 0; i < _profiles.size(); i++) {
            strResult[i] = _profiles.get(i);
         }
         streamIntent.putExtra("streaming_profiles", strResult);
      }
      _context.startActivity(streamIntent);

   }
}
