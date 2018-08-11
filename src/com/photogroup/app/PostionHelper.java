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
