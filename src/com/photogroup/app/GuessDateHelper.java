package com.photogroup.app;

import java.io.File;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.photogroup.util.StringSimilarity;

public class GuessDateHelper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final File photoFolder = new File("D:\\axing_pic\\no_date");
		Map<String, String> map = new TreeMap<String, String>(new Comparator<String>() {
			Pattern p = Pattern.compile("\\d+");

			@Override
			public int compare(String o1, String o2) {
				double similarity = StringSimilarity.similarity(o1, o2);
				if (similarity > 0.2) {
					Matcher m1 = p.matcher(o1);
					Matcher m2 = p.matcher(o2);
					if (m1.find() && m2.find()) {
						int num1 = Integer.parseInt(m1.group(0));
						int num2 = Integer.parseInt(m2.group(0));
						return num1 - num2;
					}
				}
				return o1.compareTo(o2);

			}
		});

		float progress = 0;
		float fileCount = photoFolder.listFiles().length;
		int precent = 0;

		for (final File child : photoFolder.listFiles()) {
			map.put(child.getName(), "");
		}
		System.out.println(map);
	}

}
