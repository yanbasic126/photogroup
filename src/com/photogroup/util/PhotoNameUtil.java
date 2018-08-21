package com.photogroup.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhotoNameUtil {

	static final Pattern PATTERN_NUMBER = Pattern.compile("\\d+");

	/**
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static int compareByName(String n1, String n2) {

		Matcher m1 = PATTERN_NUMBER.matcher(n1);
		Matcher m2 = PATTERN_NUMBER.matcher(n2);
		if (m1.find() && m2.find()) {
			int m1Pos = n1.indexOf(m1.group());
			int m2Pos = n2.indexOf(m2.group());
			if (m1Pos == m2Pos && n1.substring(0, m1Pos).equals(n2.substring(0, m2Pos))) {
				int num1 = Integer.parseInt(m1.group(0));
				int num2 = Integer.parseInt(m2.group(0));
				if (num1 != num2) {
					return num1 - num2;
				}
			}
		}

		return n1.compareTo(n2);
	}

	/**
	 * @param n1
	 * @param n2
	 * @return
	 */
	public static boolean isSameNamingType(String n1, String n2) {

		Matcher m1 = PATTERN_NUMBER.matcher(n1);
		Matcher m2 = PATTERN_NUMBER.matcher(n2);
		if (m1.find() && m2.find()) {
			int m1Pos = n1.indexOf(m1.group());
			int m2Pos = n2.indexOf(m2.group());
			if (m1Pos == m2Pos && n1.substring(0, m1Pos).equals(n2.substring(0, m2Pos))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Calculates the similarity (a number within 0 and 1) between two strings.
	 */
	public static double similarity(String s1, String s2) {
		String longer = s1, shorter = s2;
		if (s1.length() < s2.length()) { // longer should always have greater
											// length
			longer = s2;
			shorter = s1;
		}
		int longerLength = longer.length();
		if (longerLength == 0) {
			return 1.0;
			/* both strings are zero length */ }
		/*
		 * // If you have StringUtils, you can use it to calculate the edit
		 * distance: return (longerLength -
		 * StringUtils.getLevenshteinDistance(longer, shorter)) / (double)
		 * longerLength;
		 */
		return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

	}

	// Example implementation of the Levenshtein Edit Distance
	// See
	// http://r...content-available-to-author-only...e.org/wiki/Levenshtein_distance#Java
	public static int editDistance(String s1, String s2) {
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();

		int[] costs = new int[s2.length() + 1];
		for (int i = 0; i <= s1.length(); i++) {
			int lastValue = i;
			for (int j = 0; j <= s2.length(); j++) {
				if (i == 0)
					costs[j] = j;
				else {
					if (j > 0) {
						int newValue = costs[j - 1];
						if (s1.charAt(i - 1) != s2.charAt(j - 1))
							newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
						costs[j - 1] = lastValue;
						lastValue = newValue;
					}
				}
			}
			if (i > 0)
				costs[s2.length()] = lastValue;
		}
		return costs[s2.length()];
	}

	public static void printSimilarity(String s, String t) {
		System.out.println(String.format("%.3f is the similarity between \"%s\" and \"%s\"", similarity(s, t), s, t));
	}

	public static void main(String[] args) {
		// printSimilarity("", "");
		// printSimilarity("1234567890", "1");
		// printSimilarity("1234567890", "123");
		// printSimilarity("1234567890", "1234567");
		// printSimilarity("1234567890", "1234567890");
		// printSimilarity("1234567890", "1234567980");
		// printSimilarity("47/2010", "472010");
		// printSimilarity("47/2010", "472011");
		// printSimilarity("47/2010", "AB.CDEF");
		// printSimilarity("47/2010", "4B.CDEFG");
		// printSimilarity("47/2010", "AB.CDEFG");
		// printSimilarity("The quick fox jumped", "The fox jumped");
		// printSimilarity("The quick fox jumped", "The fox");
		// printSimilarity("The quick fox jumped", "The quick fox jumped off the
		// balcany");
		// printSimilarity("kitten", "sitting");
		System.out.println(compareByName("a553", "a2"));
	}

}