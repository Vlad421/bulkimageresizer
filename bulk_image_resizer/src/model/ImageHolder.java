package model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicProgressBarUI;

import utils.ImageFilter;
import utils.ImageWriter;
import utils.ProgressCallback;
import utils.ThumbnailProgressListener;

@SuppressWarnings("serial")
public class ImageHolder extends JLabel implements ThumbnailProgressListener, ProgressCallback {
	private File file;
	private JProgressBar progressBar;

	private String name;
	private Integer progress;
	private boolean isProcessing;
	private int sizeXpixels, sizeYpixels, sizePercent;

	Random r = new Random();

	private Dimension size = new Dimension(180, 180);

	public ImageHolder(ImageIcon iconLabel, File file) {
		super(iconLabel);
		progress = 0;
		JPanel panel = new JPanel();
		panel.setPreferredSize(size);

		this.file = file;
		// this.iconLabel = iconLabel;
		name = file.getName();
		setVerticalAlignment(TOP);
		setHorizontalTextPosition(SwingConstants.CENTER);
		setVerticalTextPosition(SwingConstants.BOTTOM);
		// setText("<html>" + this.fileName + "</html>");
		setText(name);
		setOpaque(true);
		setPreferredSize(size);
		setMaximumSize(size);
		setLayout(new OverlayLayout(this));

		progressBar = new JProgressBar();
		progressBar.setAlignmentY(Component.TOP_ALIGNMENT);
		progressBar.setBorder(new EmptyBorder(0, 0, 0, 0));
		progressBar.setPreferredSize(size);
		progressBar.setMaximumSize(size);
		Color col = progressBar.getForeground();
		progressBar.setForeground(new Color(col.getRed(), col.getGreen(), col.getBlue(), 120));
		progressBar.setOrientation(JProgressBar.VERTICAL);
		progressBar.setOpaque(false);
		//progressBar.setValue(70);
		progressBar.setFocusable(false);
		progressBar.setVisible(false);

		progressBar.setUI(new BasicProgressBarUI());

		add(progressBar);
		progressBar.addMouseListener(new ClickListener());

	}

	public void doIt(File outputDir) {
		Random r = new Random();
		isProcessing = true;
//		new Thread(new Runnable() {
//
//			@Override
//			public void run() {
//				progressBar.setVisible(true);
//				while (isProcessing) {
//					int sleep = r.nextInt(50);
//					try {
//						Thread.sleep(sleep);
//					} catch (InterruptedException e) {
//
//						e.printStackTrace();
//					}
//
//					progressBar.setValue(progress);
//					SwingUtilities.invokeLater(() -> revalidate());
//
//				}
//
//			}
//		}).start();
		progressBar.setVisible(true);
		ImageWriter writer = ImageWriter.build().setImage(file).setFormat(ImageFilter.getExtention(getName()))
				.setOutput(outputDir).setProgressCallback(this);
		System.out.println(writer);
		writer.write(2000, 2000);
		isProcessing = false;
		Executors.newSingleThreadScheduledExecutor().schedule(new TimerTask() {

			@Override
			public void run() {
				SwingUtilities.invokeLater(() -> progressBar.setVisible(false));

			}
		}, 2, TimeUnit.SECONDS);

	}

	@Override
	public int getProgress() {

		return progressBar.getValue();
	}

	class ClickListener extends MouseAdapter {

		@Override
		public void mousePressed(MouseEvent e) {
			Random r = new Random();
			if (!progressBar.isOpaque()) {
				Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), 80);

				progressBar.setBackground(c);
				progressBar.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				System.out.println(progressBar.getBackground().toString());
				progressBar.setOpaque(true);

			} else {
				progressBar.setBorder(new EmptyBorder(0, 0, 0, 0));
				progressBar.setOpaque(false);
			}
			SwingUtilities.invokeLater(() -> {
				revalidate();
				repaint();
			});
			System.out.println(file.getPath() + " - clicked, opaque = " + progressBar.isOpaque());
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	public File getFile() {

		return file;
	}

	@Override
	public void setDone() {
		// TODO Auto-generated method stub

	}
	@Override
	public void setProgress(int progress) {
		progressBar.setValue(progress);
		SwingUtilities.invokeLater(() -> revalidate());
	}

}
