package utils;

import java.io.File;
import java.io.FilenameFilter;

import javax.swing.filechooser.FileFilter;

public class ImageFilter extends FileFilter implements FilenameFilter {

	private String description = "some images only";

	@Override
	public boolean accept(File f) {
		String extention = getExtention(f);
		if (f.isDirectory()) {
			return true;
		} else if (extention != null) {
			if (extention.equals(ImageFormats.jpeg) || extention.equals(ImageFormats.jpg)
					|| extention.equals(ImageFormats.gif) || extention.equals(ImageFormats.png)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	@Override
	public String getDescription() {

		return description;
	}

	private String getExtention(File f) {
		String extention = null;
		String fileName = f.getName();
		int i = fileName.lastIndexOf('.');

		if (i > 0 && i < fileName.length() - 1) {
			extention = fileName.substring(i + 1).toLowerCase();
		}

		return extention;

	}

	public static String getExtention(String name) {
		String extention = null;
		String fileName = name;
		int i = fileName.lastIndexOf('.');

		if (i > 0 && i < fileName.length() - 1) {
			extention = fileName.substring(i + 1).toLowerCase();
		}

		return extention;

	}

	@Override
	public boolean accept(File dir, String name) {
		//System.out.println(name);
		String extention = getExtention(name);

		if (extention != null) {

			if (extention.equals(ImageFormats.jpeg) || extention.equals(ImageFormats.jpg)
					|| extention.equals(ImageFormats.gif) || extention.equals(ImageFormats.png)) {

				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
