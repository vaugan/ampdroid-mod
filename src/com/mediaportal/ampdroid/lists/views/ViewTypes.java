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
package com.mediaportal.ampdroid.lists.views;

public enum ViewTypes {
   TextView, PosterView, BannerView, ThumbView, WallView, CoverFlowView;

   public static ViewTypes fromInt(int _view) {
      switch (_view) {
      case 0:
         return TextView;
      case 1:
         return PosterView;
      case 2:
         return BannerView;
      case 3:
         return ThumbView;
      case 4:
         return WallView;
      case 5:
         return CoverFlowView;
      default:
         return TextView;
      }
   }

   public static int toInt(ViewTypes _view) {
      switch (_view) {
      case TextView:
         return 0;
      case PosterView:
         return 1;
      case BannerView:
         return 2;
      case ThumbView:
         return 3;
      case WallView:
         return 4;
      case CoverFlowView:
         return 5;
      default:
         return 0;
      }
   }
}
