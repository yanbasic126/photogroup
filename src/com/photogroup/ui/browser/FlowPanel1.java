// ============================================================================
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
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
//
// ============================================================================
package com.photogroup.ui.browser;

import javax.swing.AbstractListModel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * DOC yyan class global comment. Detailled comment <br/>
 *
 * $Id$
 *
 */
public class FlowPanel1 extends JPanel {

    /**
     * Create the panel.
     */
    public FlowPanel1() {

        JList list = new JList();
        list.setModel(new AbstractListModel() {

            String[] values = new String[] { "1", "2", "3" };

            public int getSize() {
                return values.length;
            }

            public Object getElementAt(int index) {
                return values[index];
            }
        });
        add(list);

    }

}
