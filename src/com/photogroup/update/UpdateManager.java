package com.photogroup.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import com.photogroup.exception.ExceptionHandler;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class UpdateManager {

    private String latestVersion;

    private String downloadURL;

    public UpdateManager() {
        init();
    }

    private void init() {
        String res;
        try {
            URL resjson = new URL("http://wedding0326.com/lemonphoto/version.php");

            BufferedReader in = new BufferedReader(new InputStreamReader(resjson.openStream(), "utf-8"));
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            in.close();
            String str = sb.toString();
            String subStr = str.substring(str.indexOf('['), str.indexOf("]") + 1);
            JSONArray jsonArray = JSONArray.fromObject(subStr);
            JSONObject latestObj = (JSONObject) jsonArray.get(jsonArray.size() - 1);
            latestVersion = latestObj.getString("version");
            downloadURL = latestObj.getString("download_url");
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.logError(e.getMessage());
        }
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public static void main(String[] args) throws Exception {
        new UpdateManager().init();
    }

    public String getDownloadURL() {
        return downloadURL;
    }
}
