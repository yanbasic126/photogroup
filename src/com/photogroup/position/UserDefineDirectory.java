package com.photogroup.position;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

enum UserDefineDirectory {

	INSTANCE;
	private Properties directoryMap = null;

	private UserDefineDirectory() {
		try {
			initMap();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initMap() throws IOException {

		File directory = new File("custom_name_mapping.txt");
		if (directory.exists() && directory.canRead()) {
			directoryMap = new Properties();
			FileInputStream is = new FileInputStream(directory);
			BufferedInputStream in = new BufferedInputStream(is);
			directoryMap.load(new InputStreamReader(in, "utf-8"));
		}
	}

	public String replace(String source) {
		return directoryMap != null && directoryMap.containsKey(source) ? directoryMap.getProperty(source) : source;
	}

	public static void main(String[] args) {
		System.out.println(INSTANCE.replace("双龙南里-西区"));
		System.out.println(INSTANCE.replace("新鸿基地产大楼"));
		System.out.println(INSTANCE.replace("新鸿基地产大"));
	}

}
