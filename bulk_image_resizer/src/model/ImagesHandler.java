package model;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import utils.ImageFilter;
import utils.ThumbnailProgressListener;

public class ImagesHandler {
	private List<File> imageList;
	private List<File> latestAddedImages;
	private Set<ThumbnailProgressListener> progressListeners = new HashSet<>();
	private File inDir;
	private File outDir;
	private boolean isMixedDir;
	private boolean isOutManualySet;
	private boolean isAspectRatioKeep;
	private final String MIXED_DIRS = "Mxed directories";

	public ImagesHandler() {
		imageList = new ArrayList<>();
		latestAddedImages = new ArrayList<>();
	}

	public boolean addImagesList(List<File> images) {

		if (images.size() > 0) {
			latestAddedImages.clear();

			for (File file : images) {

				if (!imageList.contains(file) && !file.isDirectory()) {
					imageList.add(file);
					latestAddedImages.add(file);
					setInDir();
				} else if (file.isDirectory()) {
					getImagesFromDir(file);
				}
			}
			return true;
		}
		return false;

	}

	public void addImage(File image) {
		latestAddedImages.clear();
		if (!imageList.contains(image) && !image.isDirectory()) {
			imageList.add(image);
			latestAddedImages.add(image);
			setInDir();
		}

	}

	public List<File> getLatestAddedImages() {
		return latestAddedImages;
	}

	public List<File> getImageList() {
		return imageList;
	}

	public String getInDir() {
		if (inDir != null) {
			return !isMixedDir ? inDir.getPath() : MIXED_DIRS;

		} else {
			return "unknown";
		}
		// return !isMixedDir ? inDir.getPath() : MIXED_DIRS;

	}

	private void setInDir() {
		String prevPath = null;
		for (File file : imageList) {
			if (!isMixedDir && !file.getParent().equals(prevPath) && prevPath != null) {
				isMixedDir = true;

			} else if (isMixedDir) {
				break;
			} else {
				prevPath = file.getParent();
				this.inDir = file.getParentFile();
			}
		}
		setOutDir();

	}

	public void setInDir(File inDir) {
		if (!isMixedDir) {
			this.inDir = inDir;
		}

//		System.out.println("Handler in- " + getInDir());
	}

	public File getOutDir() {
		return outDir;
	}

	public void setOutDir(File outDir) {

		this.outDir = outDir;
		isOutManualySet = true;

	}

	private void setOutDir() {
		if (!isMixedDir && !isOutManualySet) {
			this.outDir = new File(inDir.getPath() + "\\out");
			// outDir.mkdir();
		} else if (!isOutManualySet) {
			this.outDir = null;
		}

	}

	public int setThumbnailProgress() {
		int progress = 0;
		for (ThumbnailProgressListener listener : progressListeners) {
			progress += listener.getProgress();
		}
		return progress / progressListeners.size();
	}

	public void addProgressListener(ThumbnailProgressListener progressListener) {
		progressListeners.add(progressListener);
	}

	private void getImagesFromDir(File images) {
		List<File> imgDir = new ArrayList<>(Arrays.asList(images.listFiles(new ImageFilter())));

		for (File file : imgDir) {
			imageList.add(file);
			latestAddedImages.add(file);
			setInDir();
		}
	}

	public int getSize() {
		return imageList.size();
	}

	public boolean isAspectRatioKeep() {
		return isAspectRatioKeep;
	}

	public void setAspectRatioKeep(boolean isAspectRatioKeep) {
		this.isAspectRatioKeep = isAspectRatioKeep;
	}

	public File getImage(int index) {
		return imageList.get(index);
	}
}
