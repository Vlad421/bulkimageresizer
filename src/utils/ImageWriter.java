package utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageWriter {

	private String format;
	private ProgressCallback progress;
	private File output;
	private File image;
	private int p;
	Random r = new Random();

	private ImageWriter() {

	}

	public static ImageWriter build() {
		return new ImageWriter();
	}

	public ImageWriter setImage(File image) {
		this.image = image;
		return this;
	}

	public ImageWriter setFormat(String format) {
		this.format = format;
		return this;
	}

	public ImageWriter setProgressCallback(ProgressCallback progress) {
		this.progress = progress;
		return this;
	}

	public ImageWriter setOutput(File output) {

		this.output = output;
		return this;
	}

//	public void write(Image image, File output, String format, int width, int heigth) {
//
//		Image scaled = image.getScaledInstance(width, heigth, BufferedImage.SCALE_SMOOTH);
//		BufferedImage bi;
//		if (scaled instanceof BufferedImage img) {
//			bi = img;
//		} else {
//			bi = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null), BufferedImage.TYPE_INT_ARGB);
//
//			Graphics2D bGr = bi.createGraphics();
//			bGr.drawImage(scaled, 0, 0, null);
//			bGr.dispose();
//		}
//
//		File outputfile = output;
//		try {
//			ImageIO.write(bi, format, outputfile);
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
//	}

	public void write(int width, int heigth) {
		int hints = ResizeMode.MODE.isFast() ? BufferedImage.SCALE_FAST : BufferedImage.SCALE_SMOOTH;

		try {
			setProgress(5, 10);
			Image scaled = ImageIO.read(image).getScaledInstance(width, heigth, hints);
			setProgress(10, 35);
			BufferedImage bi;
			if (scaled instanceof BufferedImage img) {
				bi = img;
				setProgress(35, 55);
			} else {
				bi = new BufferedImage(scaled.getWidth(null), scaled.getHeight(null), BufferedImage.TYPE_INT_RGB);

				Graphics2D bGr = bi.createGraphics();
				setProgress(55, 65);
				bGr.drawImage(scaled, 0, 0, null);
				bGr.dispose();
			}
			output.mkdir();
			setProgress(65, 75);
			File outputfile = new File(output, image.getName());

			ImageIO.write(bi, format, outputfile);
			setProgress(75, 95);
		} catch (IOException e) {

			e.printStackTrace();
		}
		setProgress(100);
	}

	@Override
	public String toString() {
		// System.out.println(image != null ? image.toString() : "null");
		return "path = " + output.getPath() + ", format = " + format + ", image = "
				+ (image != null ? image.toString() : "null") + ", progress = " + progress;
	}

	private void setProgress(int min, int max) {
		progress.setProgress(p = r.nextInt(Math.min(p, min), max));
	}

	private void setProgress(int progress) {
		this.progress.setProgress(p = progress);
	}
}
