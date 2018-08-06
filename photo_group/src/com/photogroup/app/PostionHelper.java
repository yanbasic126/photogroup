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
package com.photogroup.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * DOC yyi class global comment. Detailled comment <br/>
 *
 * $Id$
 *
 */
public class PostionHelper {

    public static String getAddress() {
        String res;
        String address = null;
        try {
            URL resjson = new URL("http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=" + "N 39d 56m 42.88s"
                    + "," + "E 116d 26m 58.01s" + "&output=json&pois=1&ak=" + "1607e140964c4974ddfd87286ae9d6b7");

            BufferedReader in = new BufferedReader(new InputStreamReader(resjson.openStream()));
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            in.close();
            String str = sb.toString();
            String subStr = str.substring(str.indexOf('(') + 1, str.indexOf("})") + 1);
            JSONObject jsonObj = JSONObject.fromObject(subStr);
            // 获取匹配到的中文地址
            address = (String) jsonObj.getJSONObject("result").get("formatted_address");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return address;
    }

    public static void main(String[] args) {

        String address = getAddress();
        System.out.println(address);
    }
}
