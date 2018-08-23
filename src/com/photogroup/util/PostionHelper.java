package com.photogroup.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;

import com.drew.imaging.ImageProcessingException;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * DOC yyi class global comment. Detailled comment <br/>
 * Help http://lbsyun.baidu.com/index.php?title=webapi
 *
 */
public class PostionHelper {

	private static String getBaiduAddress(double lat, double lon) {
		String res;
		String address = null;
		String location = lat + "," + lon;
		try {
			URL resjson = new URL("http://api.map.baidu.com/geocoder/v2/?callback=renderReverse&location=" + location
					+ "&output=json&pois=1&ak=" + "1607e140964c4974ddfd87286ae9d6b7");

			BufferedReader in = new BufferedReader(new InputStreamReader(resjson.openStream()));
			StringBuilder sb = new StringBuilder("");
			while ((res = in.readLine()) != null) {
				sb.append(res.trim());
			}
			in.close();
			String str = sb.toString();
			String subStr = str.substring(str.indexOf('(') + 1, str.indexOf("})") + 1);
			System.out.println(subStr);
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
		}
		return address;
	}

	private static double[] convertBaiduCoordinate(double lat, double lng) {
		double[] latlng = null;

		URL url = null;
		URLConnection connection = null;
		try {
			url = new URL("http://api.map.baidu.com/ag/coord/convert?from=0&to=4&x=" + String.valueOf(lat) + "&y="
					+ String.valueOf(lng));
			connection = url.openConnection();
			connection.setConnectTimeout(1000);
			connection.setReadTimeout(1000);
			connection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
			out.flush();
			out.close();

			String sCurrentLine;
			String sTotalString;
			sCurrentLine = "";
			sTotalString = "";
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
					System.out.println("error != 0");
				}
			} else {
				System.out.println("String invalid!");
			}
		} catch (Exception e) {
			System.out.println("error: x=" + String.valueOf(lat) + "&y=" + String.valueOf(lng));
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

		return result;
	}

	public static String queryPostion(File photo) throws ImageProcessingException, IOException {
		Double[] gps = MetadateReader.gps(photo);
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
