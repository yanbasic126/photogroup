package com.photogroup.groupby.position;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import com.drew.imaging.ImageProcessingException;
import com.photogroup.exception.ExceptionHandler;
import com.photogroup.groupby.metadata.MetadataReader;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Help http://lbsyun.baidu.com/index.php?title=webapi, https://www.bingmapsportal.com
 */
public class PostionHelper {

    private static final String BAIDU_API_KEY = "1607e140964c4974ddfd87286ae9d6b7";

    private static final String BAIDU_ADDRESS_URL = "http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=";

    private static final String BAIDU_ADDRESS_PARAM = "&output=json&pois=1&ak=";

    private static final String BAIDU_COORD_URL = "http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x=";

    private static final String BING_API_KEY = "As7u1lYGlv0-xxLPr2ZrAFlBPwCinH7O3F2EsebbIv6wRwD0ru8K7zvu3vg4kKwP";

    private static final String BING_ADDRESS_URL = "http://dev.virtualearth.net/REST/v1/Locations/";

    private static final String BING_ADDRESS_PARAM = "?o=json&key=";

    private static String getBaiduAddress(double lat, double lon) {
        String res;
        String address = null;
        String location = lat + "," + lon;
        try {
            URL resjson = new URL(BAIDU_ADDRESS_URL + location + BAIDU_ADDRESS_PARAM + BAIDU_API_KEY);

            BufferedReader in = new BufferedReader(new InputStreamReader(resjson.openStream(), "utf-8"));
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            in.close();
            String str = sb.toString();
            String subStr = str.substring(str.indexOf('(') + 1, str.indexOf("})") + 1);
            // System.out.println(subStr);
            JSONObject jsonObj = JSONObject.fromObject(subStr);
            JSONArray poiRegions = (JSONArray) jsonObj.getJSONObject("result").getJSONArray("poiRegions");
            if (poiRegions.size() > 0) {
                JSONObject region = (JSONObject) poiRegions.get(0);
                address = region.getString("name");
            }
            if (address == null) {
                JSONArray positions = (JSONArray) jsonObj.getJSONObject("result").getJSONArray("pois");
                if (positions.size() > 0) {
                    JSONObject posi = (JSONObject) positions.get(0);
                    address = posi.getString("name");
                }
            }
            if (address == null) {
                address = (String) jsonObj.getJSONObject("result").get("formatted_address");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.logError(e.getMessage());
        }
        return address;
    }

    private static String getBingAddress(double lat, double lon) {

        return null;
    }

    private static double[] convertBaiduCoordinate(double lat, double lng) {
        double[] latlng = null;

        URL url = null;
        URLConnection connection = null;
        try {
            url = new URL(BAIDU_COORD_URL + String.valueOf(lat) + "&y=" + String.valueOf(lng));
            connection = url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            connection.setDoOutput(true);
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
            out.flush();
            out.close();

            String sCurrentLine = "";
            String sTotalString = "";
            InputStream l_urlStream;
            l_urlStream = connection.getInputStream();
            BufferedReader l_reader = new BufferedReader(new InputStreamReader(l_urlStream));
            while ((sCurrentLine = l_reader.readLine()) != null) {
                if (!sCurrentLine.equals(""))
                    sTotalString += sCurrentLine;
            }
            // System.out.println(sTotalString);
            sTotalString = sTotalString.substring(1, sTotalString.length() - 1);
            // System.out.println(sTotalString);
            String[] results = sTotalString.split("\\,");
            if (results.length == 3) {
                if (results[0].split("\\:")[1].equals("0")) {
                    String mapX = results[1].split("\\:")[1];
                    String mapY = results[2].split("\\:")[1];
                    mapX = mapX.substring(1, mapX.length() - 1);
                    mapY = mapY.substring(1, mapY.length() - 1);
                    mapX = new String(Base64.decode(mapX));
                    mapY = new String(Base64.decode(mapY));
                    latlng = new double[] { Double.parseDouble(mapX), Double.parseDouble(mapY) };
                } else {
                    ExceptionHandler.logError("error != 0, " + url);
                }
            } else {
                ExceptionHandler.logError("String invalid!" + url);
            }
        } catch (Exception e) {
            ExceptionHandler.logError("error: x=" + String.valueOf(lat) + "&y=" + String.valueOf(lng) + "|" + e.getMessage());
            // http://dev.virtualearth.net/REST/v1/Locations/39.8749,116.44358055555556?o=json&key=As7u1lYGlv0-xxLPr2ZrAFlBPwCinH7O3F2EsebbIv6wRwD0ru8K7zvu3vg4kKwP
        }
        return latlng;
    }

    private static String postProcessAddressName(String name) {
        String result = name;
        // if (result.endsWith(")") && result.lastIndexOf('(') > 0) {
        // result = result.substring(0, result.lastIndexOf("("));
        // }
        // if (result.lastIndexOf('-') > 0) {
        // result = result.substring(0, result.lastIndexOf('-'));
        // }

        return UserDefineDirectory.INSTANCE.replace(result);
    }

    public static String queryPostion(File photo) throws ImageProcessingException, IOException {
        Double[] gps = MetadataReader.gps(photo);
        if (gps != null && gps.length == 2) {

            double[] postBaidu = convertBaiduCoordinate(gps[1], gps[0]);
            if (postBaidu != null && postBaidu.length == 2) {
                String baiduAddress = getBaiduAddress(postBaidu[1], postBaidu[0]);
                if (baiduAddress != null) {
                    return postProcessAddressName(baiduAddress);
                }
            }
            // else {
            // return getBaiduAddress(gps[1].toString() + "," +
            // gps[0].toString());
            // }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        File photoFolder = new File("D:\\test\\pic\\2018.7.22春秀路小区,北京海晟世纪酒店(英福瑞国际公寓),东外大街社区-来京人员和出租房屋服务站,西中街小学");
        File[] listFiles = photoFolder.listFiles();
        for (final File child : listFiles) {

            if (child.isFile()) {

                String postion = queryPostion(child);
                if (postion == null) {
                    System.out.println(child);
                }
                System.out.println(postion);

            }
        }

    }
}
