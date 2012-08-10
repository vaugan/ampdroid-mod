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
package com.mediaportal.ampdroid.data;

import org.codehaus.jackson.annotate.JsonProperty;

import com.mediaportal.ampdroid.database.ColumnProperty;
import com.mediaportal.ampdroid.utils.StringUtils;

public class MusicTrack {
   // MPDatabase Values
   private int TrackId;
   private String[] Artists;
   private String[] AlbumArtists;
   private String Url;
   private int Duration;

   // FileSystem Values
   private int TrackNum;
   private int Year;
   private String Album;
   private String Genre;
   private String AlbumArtist;
   private String Title;
   private String TrackNumber;
   private String FilePath;
   
   @Override
   public String toString() {
      return Title;
   }

   @ColumnProperty(value="TrackId", type="integer")
   @JsonProperty("TrackId")
   public int getTrackId() {
      return TrackId;
   }

   @ColumnProperty(value="TrackId", type="integer")
   @JsonProperty("TrackId")
   public void setTrackId(int trackId) {
      TrackId = trackId;
   }

   public String getArtistsString() {
      return StringUtils.createStringArray(Artists);
   }


   @ColumnProperty(value="Artists", type="textarray")
   @JsonProperty("Artists")
   public String[] getArtists() {
      return Artists;
   }

   @ColumnProperty(value="Artists", type="textarray")
   @JsonProperty("Artists")
   public void setArtists(String[] artists) {
      Artists = artists;
   }

   public String getAlbumArtistsString() {
      return StringUtils.createStringArray(AlbumArtists);
   }


   @ColumnProperty(value="AlbumArtists", type="textarray")
   @JsonProperty("AlbumArtists")
   public String[] getAlbumArtists() {
      return AlbumArtists;
   }

   @ColumnProperty(value="AlbumArtists", type="textarray")
   @JsonProperty("AlbumArtists")
   public void setAlbumArtists(String[] albumArtists) {
      AlbumArtists = albumArtists;
   }

   @ColumnProperty(value="Url", type="text")
   @JsonProperty("Url")
   public String getUrl() {
      return Url;
   }

   @ColumnProperty(value="Url", type="text")
   @JsonProperty("Url")
   public void setUrl(String url) {
      Url = url;
   }

   @ColumnProperty(value="Duration", type="integer")
   @JsonProperty("Duration")
   public int getDuration() {
      return Duration;
   }

   @ColumnProperty(value="Duration", type="integer")
   @JsonProperty("Duration")
   public void setDuration(int duration) {
      Duration = duration;
   }

   @ColumnProperty(value="TrackNum", type="integer")
   @JsonProperty("TrackNum")
   public int getTrackNum() {
      return TrackNum;
   }

   @ColumnProperty(value="TrackNum", type="integer")
   @JsonProperty("TrackNum")
   public void setTrackNum(int trackNum) {
      TrackNum = trackNum;
   }

   @ColumnProperty(value="Year", type="integer")
   @JsonProperty("Year")
   public int getYear() {
      return Year;
   }

   @ColumnProperty(value="Year", type="integer")
   @JsonProperty("Year")
   public void setYear(int year) {
      Year = year;
   }

   @ColumnProperty(value="Album", type="text")
   @JsonProperty("Album")
   public String getAlbum() {
      return Album;
   }

   @ColumnProperty(value="Album", type="text")
   @JsonProperty("Album")
   public void setAlbum(String album) {
      Album = album;
   }

   @ColumnProperty(value="Genre", type="text")
   @JsonProperty("Genre")
   public String getGenre() {
      return Genre;
   }

   @ColumnProperty(value="Genre", type="text")
   @JsonProperty("Genre")
   public void setGenre(String genre) {
      Genre = genre;
   }

   @ColumnProperty(value="AlbumArtist", type="text")
   @JsonProperty("AlbumArtist")
   public String getAlbumArtist() {
      return AlbumArtist;
   }

   @ColumnProperty(value="AlbumArtist", type="text")
   @JsonProperty("AlbumArtist")
   public void setAlbumArtist(String albumArtist) {
      AlbumArtist = albumArtist;
   }

   @ColumnProperty(value="Title", type="text")
   @JsonProperty("Title")
   public String getTitle() {
      return Title;
   }

   @ColumnProperty(value="Title", type="text")
   @JsonProperty("Title")
   public void setTitle(String title) {
      Title = title;
   }

   @ColumnProperty(value="TrackNumber", type="text")
   @JsonProperty("TrackNumber")
   public String getTrackNumber() {
      return TrackNumber;
   }

   @ColumnProperty(value="TrackNumber", type="text")
   @JsonProperty("TrackNumber")
   public void setTrackNumber(String trackNumber) {
      TrackNumber = trackNumber;
   }

   @ColumnProperty(value="FilePath", type="text")
   @JsonProperty("FilePath")
   public String getFilePath() {
      return FilePath;
   }

   @ColumnProperty(value="FilePath", type="text")
   @JsonProperty("FilePath")
   public void setFilePath(String filePath) {
      FilePath = filePath;
   }
}
