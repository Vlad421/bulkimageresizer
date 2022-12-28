package ui;

import java.awt.Cursor;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import model.ImageHolder;
import model.ImagesHandler;

@SuppressWarnings("serial")
public class ThumbnailsPanel extends JPanel {

	private GridLayout layout;
	private JProgressBar progressBar;
	private List<ImageHolder> icons = new ArrayList<>();
	private ImagesHandler images;
	private MainPanel mainPanel;

	public ThumbnailsPanel(ImagesHandler images, JProgressBar progressBar, MainPanel mainPanel) {
		this.images = images;
		this.progressBar = progressBar;
		this.mainPanel = mainPanel;
		layout = new GridLayout(0, 3, 5, 5);
		progressBar.setStringPainted(true);

		setLayout(layout);
		// ImageHolder imageHolder = new ImageHolder(null, null);
		// imageHolder.setVerticalAlignment(SwingConstants.TOP);
		// add(imageHolder);
		addComponentListener(new ColumnAdapter());
		System.out.println(Arrays.toString(ImageIO.getWriterFormatNames()));
	}

	public void addImages() {
		progressBar.setValue(0);
		ThumbnailWorker worker = new ThumbnailWorker(images.getLatestAddedImages());

		worker.addPropertyChangeListener(worker);
		worker.execute();
	}

	int threadAmount = 1;
	int currentThread;

	public void doIt() {

//		for (ImageHolder imageHolder : icons) {
//			new Thread(imageHolder).start();
//		}
		for (int i = 1; i <= 10; i++) {
			if (icons.size() % i == 0) {
				threadAmount = i;
			}

		}

		for (currentThread = 0; currentThread < threadAmount; currentThread++) {
			new Thread(new Runnable() {
				int threadNum = currentThread;
				int picsCount = icons.size() / threadAmount * (currentThread + 1);
				int start = icons.size() / threadAmount * threadNum;

				@Override
				public void run() {

					// while (true) {
					for (int j = start; j < picsCount; j++) {
						icons.get(j).doIt(images.getOutDir());
						// System.out.println(images.getImageList());
//						ImageWriter writer = ImageWriter.build().setImage(images.getImage(j))
//								.setFormat(ImageFilter.getExtention(icons.get(j).getName()))
//								.setOutput(images.getOutDir());
//						// System.out.println(writer);
//						writer.write(2000, 2000);
					}
					System.out.println("current thread - " + threadNum + ", pics count = " + picsCount);
					// }

				}
			}).start();
		}

		new Thread(new Runnable() {

			@Override
			public void run() {

				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SwingUtilities.invokeLater(() -> progressBar.setVisible(true));
					progressBar.setValue(images.setThumbnailProgress());
					SwingUtilities.invokeLater(() -> revalidate());
				}

			}
		}).start();
	}

	class ThumbnailWorker extends SwingWorker<Void, ImageHolder> implements PropertyChangeListener {
		private int imageCount;
		private int currentImage;
		private ImageIcon icon;
		private ImageHolder label;
		private JLabel iconlLabel;

		List<File> toWork;

		public ThumbnailWorker(List<File> toWork) {
			this.toWork = new ArrayList<>(toWork);
		}

		@Override
		protected Void doInBackground() throws Exception {
			setProgress(0);

			if (toWork.size() != 0) {
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				progressBar.setVisible(true);
			}

			imageCount = toWork.size();
			for (int i = 0; i < imageCount; i++) {

				currentImage = i;

				BufferedImage image;
				try {
					image = ImageIO.read(toWork.get(currentImage));
					if (image != null) {
						icon = new ImageIcon(image.getScaledInstance(160, 160, Image.SCALE_FAST));
						iconlLabel = new JLabel(icon);
						label = new ImageHolder(icon, toWork.get(currentImage));

						setProgress(getCurrentProgress());

						icons.add(label);
						publish(label);

					} else {
						System.out.println("not an image - " + toWork.get(i).getPath());
					}

				} catch (IOException | NullPointerException e) {

					e.printStackTrace();
				}
			}

			return null;
		}

		@Override
		protected void done() {
			setCursor(null);
			progressBar.setValue(100);
			SwingUtilities.invokeLater(() -> revalidate());

			TimerTask task = new TimerTask() {

				@Override
				public void run() {
					SwingUtilities.invokeLater(() -> progressBar.setVisible(false));

				}
			};
			new Timer().schedule(task, 3000);

			Executors.newSingleThreadScheduledExecutor().schedule(new TimerTask() {

				@Override
				public void run() {
					SwingUtilities.invokeLater(() -> progressBar.setVisible(false));

				}
			}, 3, TimeUnit.SECONDS);
			mainPanel.setAddingProcessing(false);
		}

		@Override
		protected void process(List<ImageHolder> chunks) {

			for (ImageHolder thumbnail : chunks) {
				images.addProgressListener(thumbnail);
				add(thumbnail);
			}

			if (currentImage % 10 == 0) {
				revalidate();
			}

		}

		private int getCurrentProgress() {
			return 100 * currentImage / imageCount;
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if ("progress" == evt.getPropertyName()) {
				int progress = (Integer) evt.getNewValue();

				progressBar.setValue(progress);

			}

		}

	}

	class ColumnAdapter extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			int width = getSize().width;

			if (width < 800) {
				layout.setColumns(3);
			} else if (width > 800 && width < 1000) {
				layout.setColumns(4);
			} else if (getSize().width > 1000 && width < 1200) {
				layout.setColumns(5);
			} else if (width > 1200) {
				layout.setColumns(6);
			}
			SwingUtilities.invokeLater(() -> revalidate());

		}

	}

}
