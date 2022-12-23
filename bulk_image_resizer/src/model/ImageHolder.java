package model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Random;

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

import utils.ThumbnailProgressListener;

@SuppressWarnings("serial")
public class ImageHolder extends JLabel implements Runnable, ThumbnailProgressListener {
	private File file;
	private JProgressBar progressBar;
	private ImageIcon iconLabel;
	private String name;
	private Integer progress;

	Random r = new Random();

	private Dimension size = new Dimension(180, 180);

	public ImageHolder(ImageIcon iconLabel, File file) {
		super(iconLabel);

		JPanel panel = new JPanel();
		panel.setPreferredSize(size);

		this.file = file;
		this.iconLabel = iconLabel;
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
		progressBar.setForeground(new Color(col.getRed(), col.getGreen(), col.getBlue()));
		progressBar.setOrientation(JProgressBar.VERTICAL);
		progressBar.setOpaque(false);
		progressBar.setValue(70);
		progressBar.setFocusable(false);

		progressBar.setUI(new BasicProgressBarUI());

		add(progressBar);
		progressBar.addMouseListener(new ClickListener());

	}

	public void doIt() {
		Random r = new Random();
		int sleep = r.nextInt(500);
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		progressBar.setForeground(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), r.nextInt(90, 190)));
		progressBar.setValue(r.nextInt(50));
		SwingUtilities.invokeLater(() -> revalidate());
	}

	@Override
	public void run() {
		Random r = new Random();
		int sleep = r.nextInt(500);
		while (true) {
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			progressBar.setForeground(new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), r.nextInt(90, 190)));
			progressBar.setValue(r.nextInt(50));
			SwingUtilities.invokeLater(() -> revalidate());
		}

	}

	@Override
	public int getProgress() {

		return progressBar.getValue();
	}

	class ClickListener extends MouseAdapter {
//		@Override
//		public void mouseClicked(MouseEvent e) {
//			Random r = new Random();
//			if (!progress.isOpaque()) {
//				Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255), 80);
//
//				progress.setBackground(c);
//				progress.setBorder(new BevelBorder(BevelBorder.LOWERED, Color.BLUE, null, null, null));
//				System.out.println(progress.getBackground().toString());
//				progress.setOpaque(true);
//
//			} else {
//				progress.setBorder(new EmptyBorder(0, 0, 0, 0));
//				progress.setOpaque(false);
//			}
//			SwingUtilities.invokeLater(() -> {
//				revalidate();
//				repaint();
//			});
//			System.out.println(file.getPath() + " - clicked, opaque = " + progress.isOpaque());
//		}
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

}
