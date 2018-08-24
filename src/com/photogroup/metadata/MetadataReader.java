package com.photogroup.metadata;

import java.io.File;
import java.io.IOException;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class MetadataReader {
	public static String dateTaken(File file) throws ImageProcessingException, IOException {
		Metadata metadata = ImageMetadataReader.readMetadata(file);
		for (Directory directory : metadata.getDirectories()) {
			for (Tag tag : directory.getTags()) {
				if (("Date/Time").equals(tag.getTagName())) {
					return tag.getDescription().trim();
				}
				if (("Date/Time Original").equals(tag.getTagName())) {
					return tag.getDescription().trim();
				}
				if (("Date/Time Digitized").equals(tag.getTagName())) {
					return tag.getDescription().trim();
				}
			}
		}
		return null;
	}

	/**
	 * @param file
	 * @return lat, lon
	 * @throws ImageProcessingException
	 * @throws IOException
	 */
	public static Double[] gps(File file) throws ImageProcessingException, IOException {
		String lat = "";
		String lon = "";
		String latRef = "";
		String lonRef = "";

		Metadata metadata = ImageMetadataReader.readMetadata(file);
		for (Directory directory : metadata.getDirectories()) {
			for (Tag tag : directory.getTags()) {
				if (("GPS Latitude").equals(tag.getTagName())) {
					lat = tag.getDescription().trim();
				}
				if (("GPS Longitude").equals(tag.getTagName())) {
					lon = tag.getDescription().trim();
				}
				if (("GPS Latitude Ref").equals(tag.getTagName())) {
					latRef = tag.getDescription().trim();
				}
				if (("GPS Longitude Ref").equals(tag.getTagName())) {
					lonRef = tag.getDescription().trim();
				}
				if (!lon.isEmpty() && !lat.isEmpty() && !lonRef.isEmpty() && !latRef.isEmpty()) {
					return new Double[] { convertGpsToDegree(lat, latRef), convertGpsToDegree(lon, lonRef) };
				}
			}
		}
		return null;
	}

	private static Double convertGpsToDegree(String stringDMS, String stringRef) {
		double result;
		String[] split = stringDMS.split("°", 3);
		String degrees = split[0].trim();
		String[] ms = split[1].split("'");
		String minutes = ms[0].trim();
		String seconds = ms[1].split("\"")[0].trim();

		double decimal = ((Double.valueOf(minutes) * 60) + Double.valueOf(seconds)) / (60 * 60);
		if (stringRef.equals("N") || stringRef.equals("E")) {
			result = Double.valueOf(degrees) + decimal;
		} else {
			result = 0 - (Double.valueOf(degrees) + decimal);
		}
		return result;

	}

	public static void main(String[] args) throws Exception {
		Metadata metadata = ImageMetadataReader.readMetadata(new File("D:\\axing_pic\\IMG_1605.JPG"));

		for (Directory directory : metadata.getDirectories()) {
			for (Tag tag : directory.getTags()) {
				System.out.println(tag.getTagName() + ":  " + tag.getDescription());
			}
			if (directory.hasErrors()) {
				for (String error : directory.getErrors()) {
					System.err.format("ERROR: %s", error);
				}
			}
		}
	}

}
