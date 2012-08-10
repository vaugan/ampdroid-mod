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

public class RemoteFunction {
   public static final String TABLE_NAME = "RemoteFunctions";
   
   private int Id;
   private String Name;
   private String Description;
   private String Image;
   
   public RemoteFunction(int _id, String _name, String _description, String _image) {
      Id = _id;
      Name = _name;
      Description = _description;
      Image = _image;
   }

   public RemoteFunction() {
      // TODO Auto-generated constructor stub
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
   
   @ColumnProperty(value="Name", type="text")
   @JsonProperty("Name")
   public String getName() {
      return Name;
   }
   
   @ColumnProperty(value="Name", type="text")
   @JsonProperty("Name")
   public void setName(String name) {
      this.Name = name;
   }
   
   @ColumnProperty(value="Description", type="text")
   @JsonProperty("Description")
   public String getDescription() {
      return Description;
   }
   
   @ColumnProperty(value="Description", type="text")
   @JsonProperty("Description")
   public void setDescription(String description) {
      this.Description = description;
   }
   
   @ColumnProperty(value="Image", type="text")
   @JsonProperty("Image")
   public String getImage() {
      return Image;
   }
   
   @ColumnProperty(value="Image", type="text")
   @JsonProperty("Image")
   public void setImage(String image) {
      this.Image = image;
   }
}
