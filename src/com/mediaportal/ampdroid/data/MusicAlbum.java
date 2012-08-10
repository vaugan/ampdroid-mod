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

public class MusicAlbum {
   private String[] AlbumArtists;
   private String Title;
   private int Year;
   private String Genre;
   private String Composer;
   private String Publisher;
   private String CoverPathLarge;

   @Override
   public String toString() {
      return Title;
   }

   public String getAlbumArtistString() {
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

   @ColumnProperty(value="Composer", type="text")
   @JsonProperty("Composer")
   public String getComposer() {
      return Composer;
   }

   @ColumnProperty(value="Composer", type="text")
   @JsonProperty("Composer")
   public void setComposer(String composer) {
      Composer = composer;
   }

   @ColumnProperty(value="Publisher", type="text")
   @JsonProperty("Publisher")
   public String getPublisher() {
      return Publisher;
   }

   @ColumnProperty(value="Publisher", type="text")
   @JsonProperty("Publisher")
   public void setPublisher(String publisher) {
      Publisher = publisher;
   }

   @ColumnProperty(value="CoverPathL", type="text")
   @JsonProperty("CoverPathL")
   public void setCoverPathLarge(String coverPathLarge) {
      CoverPathLarge = coverPathLarge;
   }

   @ColumnProperty(value="CoverPathL", type="text")
   @JsonProperty("CoverPathL")
   public String getCoverPathLarge() {
      return CoverPathLarge;
   }

}
