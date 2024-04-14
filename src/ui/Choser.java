package ui;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import utils.ImageFilter;

public class Choser extends JFileChooser {

	public Choser() {

		setFileFilter(new ImageFilter());
		setFileFilter(new ImageFilter());
		setAcceptAllFileFilterUsed(false);
	}

	@Override
	public void approveSelection() {
		if (getSelectedFile().exists()) {
			super.approveSelection();
		} else {
			JOptionPane.showMessageDialog(this, "File doesn`t exist");
		}
	}

}
