package com.photogroup.update;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.photogroup.exception.ExceptionHandler;

public class UpdateManager {

    private static final String VERSION_URL = "http://wedding0326.com/lemonphoto/version.php";

    private String latestVersion;

    private String downloadURL;

    private String created;

    private String filename;

    public UpdateManager() {
        init();
    }

    private void init() {
        String res;
        try {
            URL resjson = new URL(VERSION_URL);

            BufferedReader in = new BufferedReader(new InputStreamReader(resjson.openStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            in.close();
            String str = sb.toString();
            String subStr = str.substring(str.indexOf('['), str.indexOf("]") + 1);

            JSONArray jsonArray = (JSONArray) JSONValue.parse(subStr);
            JSONObject latestObj = (JSONObject) jsonArray.get(jsonArray.size() - 1);
            latestVersion = String.valueOf(latestObj.get("version"));
            downloadURL = String.valueOf(latestObj.get("download_url"));
            created = String.valueOf(latestObj.get("created_at"));
            filename = String.valueOf(latestObj.get("filename"));
            // JSONArray jsonArray = JSONArray.fromObject(subStr);
            // JSONObject latestObj = (JSONObject) jsonArray.get(jsonArray.size() - 1);
            // latestVersion = latestObj.getString("version");
            // downloadURL = latestObj.getString("download_url");
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.logError(e.getMessage());
        }
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public String getCreated() {
        return created;
    }

    public String getFilename() {
        return filename;
    }
}
