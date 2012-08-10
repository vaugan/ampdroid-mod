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
package com.mediaportal.ampdroid.data.commands;

public class RemoteKey {
   private int id;
   private String name;
   private String icon;
   private String action;
   
   public RemoteKey(int _id, String _name, String _action, String _icon) {
      super();
      this.id = _id;
      this.name = _name;
      this.action = _action;
      this.icon = _icon;
   }
   public int getId() {
      return id;
   }
   public void setId(int id) {
      this.id = id;
   }
   public String getName() {
      return name;
   }
   public void setName(String name) {
      this.name = name;
   }
   public String getIcon() {
      return icon;
   }
   public void setIcon(String icon) {
      this.icon = icon;
   }
   public String getAction() {
      return action;
   }
   public void setAction(String action) {
      this.action = action;
   }
   
}
