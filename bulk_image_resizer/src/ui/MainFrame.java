package ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;

import model.ImagesHandler;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	MainPanel panel;
	Dimension minnimumSize = new Dimension(600, 500);

	public MainFrame(ImagesHandler images) {
		panel = new MainPanel(images);

		setTitle("Bulk image resizer");
		setMinimumSize(minnimumSize);
		setBounds(new Rectangle(new Point(50, 50), minnimumSize));

		add(panel);

		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setResizable(false);

		setVisible(true);

		panel.switchFocus();
	}

}
