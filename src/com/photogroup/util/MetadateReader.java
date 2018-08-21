package com.photogroup.util;

import java.io.File;
import java.io.IOException;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

public class MetadateReader {
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
