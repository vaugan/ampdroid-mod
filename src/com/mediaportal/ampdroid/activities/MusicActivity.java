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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.mediaportal.ampdroid.R;
import com.mediaportal.ampdroid.activities.actionbar.ActionBar;
import com.mediaportal.ampdroid.activities.music.TabAlbumsActivityGroup;
import com.mediaportal.ampdroid.activities.music.TabArtistsActivityGroup;
import com.mediaportal.ampdroid.activities.music.TabMusicSharesActivityGroup;
import com.mediaportal.ampdroid.activities.music.TabTracksActivityGroup;

public class MusicActivity extends BaseTabActivity {
   TabHost mTabHost;

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle _savedInstanceState) {
      super.onCreate(_savedInstanceState);
      setContentView(R.layout.activity_media);

      ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
      actionBar.setTitle(getString(R.string.title_media));

      mTabHost = getTabHost();
      mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
      /** tid1 is firstTabSpec Id. Its used to access outside. */
      TabSpec musicSharesTabSpec = mTabHost.newTabSpec("tidmusic0");
      View tab = createTabView(this, getString(R.string.music_tabs_shares));
      musicSharesTabSpec.setIndicator(tab);
      Intent sharesIntent = new Intent(this, TabMusicSharesActivityGroup.class);
      sharesIntent.putExtra("activity_group", "shares");
      musicSharesTabSpec.setContent(sharesIntent);
      mTabHost.addTab(musicSharesTabSpec);

      TabSpec artistsTabSpec = mTabHost.newTabSpec("tidmusic1");
      artistsTabSpec.setIndicator(createTabView(this, getString(R.string.music_tabs_artists)));
      Intent artistsIntent = new Intent(this, TabArtistsActivityGroup.class);
      artistsIntent.putExtra("activity_group", "artists");
      artistsTabSpec.setContent(artistsIntent);
      mTabHost.addTab(artistsTabSpec);

      TabSpec albumsTabSpec = mTabHost.newTabSpec("tidmusic2");
      albumsTabSpec.setIndicator(createTabView(this, getString(R.string.music_tabs_albums)));
      Intent albumIntent = new Intent(this, TabAlbumsActivityGroup.class);
      albumsTabSpec.setContent(albumIntent);
      mTabHost.addTab(albumsTabSpec);

      TabSpec tracksTabSpec = mTabHost.newTabSpec("tidmusic3");
      tracksTabSpec.setIndicator(createTabView(this, getString(R.string.music_tabs_songs)));
      Intent tracksIntent = new Intent(this, TabTracksActivityGroup.class);
      tracksTabSpec.setContent(tracksIntent);
      mTabHost.addTab(tracksTabSpec);
   }

   private static View createTabView(final Context _context, final String _text) {
      try {
         View view = LayoutInflater.from(_context).inflate(R.layout.tabs_bg, null);
         TextView tv = (TextView) view.findViewById(R.id.tabsText);
         tv.setText(_text);
         return view;
      } catch (Exception ex) {
         Log.d("Tab:", ex.toString());
         return null;
      }
   }

   /*
    * 
    * 
    * RemoteHandler service = RemoteHandler.getCurrentRemoteInstance();
    * 
    * 
    * 
    * URL url = service.getDownloadUri(
    * "C:\\Users\\DieBagger\\Torrents\\the.big.bang.theory.s04e11.hdtv.xvid-fever.avi"
    * ); Intent i = new Intent(Intent.ACTION_VIEW);
    * i.setData(Uri.parse(url.toString())); startActivity(i);
    * 
    * 
    * URL url =
    * service.getDownloadUri("C:\\Users\\DieBagger\\Music\\sacred_cows_shout.mp3"
    * );
    * 
    * Intent intent = new Intent();
    * intent.setDataAndType(Uri.parse(url.toString()), "audio/*");
    * intent.setAction(Intent.ACTION_VIEW); startActivity(intent);
    * 
    * 
    * MediaPlayer mp = new MediaPlayer(); try {
    * mp.setDataSource(url.toString()); mp.prepare(); mp.start(); } catch
    * (IllegalArgumentException e1) { e1.printStackTrace(); } catch
    * (IllegalStateException e1) { e1.printStackTrace(); } catch (IOException
    * e1) { e1.printStackTrace(); }
    * 
    * 
    * 
    * InputStream inputStream = null; URLConnection conn; try { conn =
    * url.openConnection();
    * 
    * HttpURLConnection httpConn = (HttpURLConnection) conn;
    * httpConn.setRequestMethod("GET"); httpConn.connect();
    * 
    * if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) { inputStream
    * = httpConn.getInputStream(); }
    * 
    * 
    * FileOutputStream f;
    * 
    * f = new FileOutputStream(new File(
    * Environment.getExternalStorageDirectory() + "/downloads", "shout.mp3"));
    * byte[] buffer = new byte[1024]; int len1 = 0; while ((len1 =
    * inputStream.read(buffer)) > 0) { f.write(buffer, 0, len1); } f.close();
    * 
    * inputStream.close(); } catch (FileNotFoundException e) {
    * e.printStackTrace(); } catch (IOException e) { e.printStackTrace(); }
    */

   /*
    * RemoteHandler service = RemoteHandler.getCurrentRemoteInstance();
    * 
    * try { List<MusicAlbum> albums = service.getAlbums(0, 10); Object i =
    * albums; } catch (Exception e) { e.printStackTrace(); }
    */

   // }
}
