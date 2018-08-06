//============================================================================
//
// Talend Community Edition
//
// Copyright (C) 2006-2013 Talend – www.talend.com
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
//============================================================================
package com.photogroup.util;


/**
 * DOC yyi  class global comment. Detailled comment
 * <br/>
 *
 * $Id$
 *
 */
public class PrintProgress {
    public static void main(String[] args) throws Exception {
        System.out.print("Progress:");
        for (int i = 1; i <= 100; i++) {
            System.out.print(i + "%");
            Thread.sleep(100);

            for (int j = 0; j <= String.valueOf(i).length(); j++) {
                System.out.print("\b");
            }
        }
        System.out.println();
    }
}
