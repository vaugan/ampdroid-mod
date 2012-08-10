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

public class Movie {
   public static final int CACHE_ID = 0;
   public static final String TABLE_NAME = "Movies";
   public static final String TABLE_NAME_VIDEOS = "Videos";
   
	private int Id;
	private String Title;
	private String TagLine;
	private String Filename;
	private String Genre;
	private int Year;
	private String ParentalRating;
	private String CoverThumbPath;
	private String BackdropPath;
	
	public Movie()
	{

	}
	
	@Override 
	public String toString() {
		if(Title != null)
		{
			return Title;
		}
		else return super.toString();
    }
	
   @ColumnProperty(value="Id", type="integer")
   @JsonProperty("Id")
   public int getId() {
      return Id;
   }

   @ColumnProperty(value="Id", type="integer")
   @JsonProperty("Id")
   public void setId(int id) {
      this.Id = id;
   }

	@ColumnProperty(value="TagLine", type="text")
   @JsonProperty("TagLine")
	public String getTagline() {
		return TagLine;
	}

	@ColumnProperty(value="TagLine", type="text")
   @JsonProperty("TagLine")
	public void setTagline(String tagline) {
		this.TagLine = tagline;
	}

   @ColumnProperty(value="Filename", type="text")
   @JsonProperty("Filename")
	public String getFilename() {
		return Filename;
	}

   @ColumnProperty(value="Filename", type="text")
   @JsonProperty("Filename")
	public void setFilename(String filename) {
		this.Filename = filename;
	}

   @ColumnProperty(value="Genre", type="text")
   @JsonProperty("Genre")
	public String getGenreString() {
		return Genre;
	}

   @ColumnProperty(value="Genre", type="text")
   @JsonProperty("Genre")
	public void setGenreString(String genreString) {
		this.Genre = genreString;
	}

   @ColumnProperty(value="Year", type="integer")
   @JsonProperty("Year")
	public int getYear() {
		return Year;
	}

   @ColumnProperty(value="Year", type="integer")
   @JsonProperty("Year")
	public void setYear(int year) {
		this.Year = year;
	}

   @ColumnProperty(value="ParentalRating", type="text")
   @JsonProperty("ParentalRating")
	public String getParentalRating() {
		return ParentalRating;
	}

   @ColumnProperty(value="ParentalRating", type="text")
   @JsonProperty("ParentalRating")
	public void setParentalRating(String parentalRating) {
		this.ParentalRating = parentalRating;
	}

   @ColumnProperty(value="CoverThumbPath", type="text")
   @JsonProperty("CoverThumbPath")
	public String getCoverThumbPath() {
		return CoverThumbPath;
	}

   @ColumnProperty(value="CoverThumbPath", type="text")
   @JsonProperty("CoverThumbPath")
	public void setCoverThumbPath(String coverThumbPath) {
		this.CoverThumbPath = coverThumbPath;
	}




   @ColumnProperty(value="Title", type="text")
   @JsonProperty("Title")
	public String getName() {
		return Title;
	}

   @ColumnProperty(value="Title", type="text")
   @JsonProperty("Title")
	public void setName(String name) {
		this.Title = name;
	}
   

   @ColumnProperty(value="BackdropPath", type="text")
   @JsonProperty("BackdropPath")
   public String getBackdropPath() {
      return BackdropPath;
   }

   @ColumnProperty(value="BackdropPath", type="text")
   @JsonProperty("BackdropPath")
   public void setBackdropPath(String backdropPath) {
      BackdropPath = backdropPath;
   }
}
