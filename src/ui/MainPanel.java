package ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.ImagesHandler;
import utils.ImageFilter;
import utils.ResizeMode;
import utils.TxfFilter;
import utils.WorkingState;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {

	private ThumbnailsPanel thumbnailsPanel;
	private JScrollPane scrollPane;

	private JProgressBar progressBar;
	private JTextField txtFieldInputPath;
	private JTextField txtFieldOutputPath;
	private JPanel toolBar;

	private ButtonListener action;
	private JButton buttonInputLocation;
	private JButton buttonOutputLocation;
	private JButton btnProcess;
	private ImagesHandler images;
	private ImageFilter filter;

	private JTextField txfWidth;
	private JTextField txfHeigth;
	private JPanel panelOptions;
	private JCheckBox cbAspectRatio;
	private JCheckBox cbFast;

	private JCheckBox percentChBox;

	public MainPanel(ImagesHandler images) {
		System.out.println(ResizeMode.MODE);
		filter = new ImageFilter();
		this.images = images;
		setAlignmentY(TOP_ALIGNMENT);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[] { 0.1, 5.0, 0.001 };
		gridBagLayout.columnWeights = new double[] { 1.0 };

		setLayout(gridBagLayout);

		toolBar = new JPanel();

		action = new ButtonListener();
		GridBagConstraints gbcToolBar = new GridBagConstraints();

		gbcToolBar.anchor = GridBagConstraints.NORTH;
		gbcToolBar.fill = GridBagConstraints.HORIZONTAL;

		gbcToolBar.gridx = 0;
		gbcToolBar.gridy = 0;
		add(toolBar, gbcToolBar);
		toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));

		JPanel panelPathNameFields = new JPanel();
		toolBar.add(panelPathNameFields);
		panelPathNameFields.setLayout(new BoxLayout(panelPathNameFields, BoxLayout.Y_AXIS));

		txtFieldInputPath = new HintTextField("Input directory");

		txtFieldInputPath.setColumns(30);
		panelPathNameFields.add(txtFieldInputPath);

		txtFieldOutputPath = new HintTextField("Output directory");

		txtFieldOutputPath.setColumns(30);
		panelPathNameFields.add(txtFieldOutputPath);

		JPanel panelButton = new JPanel();
		toolBar.add(panelButton);
		panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.Y_AXIS));

		buttonInputLocation = new JButton(UIManager.getIcon("FileView.directoryIcon"));

		buttonInputLocation.addActionListener(action);
		panelButton.add(buttonInputLocation);

		buttonOutputLocation = new JButton(UIManager.getIcon("FileView.floppyDriveIcon"));

		buttonOutputLocation.addActionListener(action);
		panelButton.add(buttonOutputLocation);

		btnProcess = new JButton("Resize");
		btnProcess.setPreferredSize(new Dimension(90, 23));
		btnProcess.setMinimumSize(new Dimension(90, 23));
//		btnProcess.setHorizontalTextPosition(SwingConstants.LEFT);
//		btnProcess.setAlignmentX(JButton.LEFT_ALIGNMENT);
//		btnProcess.setHorizontalAlignment(SwingConstants.LEFT);
		textWrap(btnProcess);

		// toolBar.add(btnProcess);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		toolBar.add(panel);

		percentChBox = new JCheckBox("Percent resize");

		panel.add(percentChBox);

		panel.add(percentChBox);
		panel.add(btnProcess);

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
		// labelPanel.setAlignmentY(TOP_ALIGNMENT);

		JLabel labelX = new JLabel("X(px):");
		JLabel labelY = new JLabel("Y(px):");

		labelPanel.add(Box.createVerticalGlue());
		labelPanel.add(labelX);
		labelPanel.add(Box.createVerticalGlue());
		labelPanel.add(labelY);
		labelPanel.add(Box.createVerticalGlue());
		toolBar.add(labelPanel);
		percentChBox.addActionListener((e) -> {
			if (percentChBox.isSelected()) {
				ResizeMode.MODE.setMode(percentChBox.isSelected());
				labelX.setText("X(%):");
				labelY.setText("Y(%):");
				System.out.println(ResizeMode.MODE);
			} else {
				ResizeMode.MODE.setMode(percentChBox.isSelected());
				labelX.setText("X(px):");
				labelY.setText("Y(px):");
				System.out.println(ResizeMode.MODE);
			}

		});
		JPanel sizePanel = new JPanel();
		sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.Y_AXIS));

		txfWidth = new JTextField();
		txfWidth.setHorizontalAlignment(JTextField.RIGHT);
		txfWidth.setColumns(2);
		txfWidth.setDocument(new TxfFilter());

		txfHeigth = new JTextField();
		txfHeigth.setHorizontalAlignment(JTextField.RIGHT);
		txfHeigth.setColumns(2);
		txfHeigth.setDocument(new TxfFilter());

		txfHeigth.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (txfHeigth.getText().length() > 0) {
					txfWidth.setEditable(false);
				} else {
					txfWidth.setEditable(true);
				}

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (txfHeigth.getText().length() > 0) {
					txfWidth.setEditable(false);
				} else {
					txfWidth.setEditable(true);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (txfHeigth.getText().length() > 0) {
					txfWidth.setEditable(false);
				} else {
					txfWidth.setEditable(true);
				}
			}
		});

		txfWidth.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (txfWidth.getText().length() > 0) {
					txfHeigth.setEditable(false);
				} else {
					txfHeigth.setEditable(true);
				}

			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				if (txfWidth.getText().length() > 0) {
					txfHeigth.setEditable(false);
				} else {
					txfHeigth.setEditable(true);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (txfWidth.getText().length() > 0) {
					txfHeigth.setEditable(false);
				} else {
					txfHeigth.setEditable(true);
				}
			}
		});

		sizePanel.add(txfWidth);
		sizePanel.add(txfHeigth);
		toolBar.add(sizePanel);

		panelOptions = new JPanel();
		toolBar.add(panelOptions);
		panelOptions.setLayout(new BoxLayout(panelOptions, BoxLayout.Y_AXIS));

		cbAspectRatio = new JCheckBox("Keep aspect ratio");
		cbAspectRatio.setSelected(true);
		cbAspectRatio.setEnabled(false);
		cbAspectRatio.addActionListener((e) -> images.setAspectRatioKeep(cbAspectRatio.isSelected()));
		panelOptions.add(cbAspectRatio);

		cbFast = new JCheckBox("Fast resize");
		panelOptions.add(cbFast);

		progressBar = new JProgressBar();
		progressBar.setVisible(false);

		thumbnailsPanel = new ThumbnailsPanel(images, progressBar, this);
	//	thumbnailsPanel.addParent(this);

		GridBagConstraints gbcScrollPane = new GridBagConstraints();
		// gbcScrollPane.insets = new Insets(0, 0, 5, 0);
		gbcScrollPane.anchor = GridBagConstraints.NORTH;
		gbcScrollPane.fill = GridBagConstraints.BOTH;
		// gbcScrollPane.weightx = 1;
		// gbcScrollPane.weighty = 0.5;
		gbcScrollPane.gridx = 0;
		gbcScrollPane.gridy = 1;

		scrollPane = new JScrollPane(thumbnailsPanel);
		scrollPane.setDoubleBuffered(true);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		add(scrollPane, gbcScrollPane);

		scrollPane.setDropTarget(new Drop());
		GridBagConstraints gbcProgressBar = new GridBagConstraints();
		// gbcProgressBar.insets = new Insets(0, 0, 5, 0);
		// gbcProgressBar.weightx = 1.0;
		gbcProgressBar.anchor = GridBagConstraints.SOUTH;
		gbcProgressBar.fill = GridBagConstraints.HORIZONTAL;
		gbcProgressBar.gridx = 0;
		gbcProgressBar.gridy = 2;

		// gbcProgressBar.weightx = 1;
		// gbcProgressBar.weighty = 0.001;
		add(progressBar, gbcProgressBar);

		btnProcess.addActionListener(action);

		ResizeMode.MODE.setMode(percentChBox.isSelected());
		System.out.println(ResizeMode.MODE);
	}

	public void switchFocus() {
		scrollPane.requestFocusInWindow();
	}

	private void setOutputDirText() {
		if (images.getOutDir() != null) {
			txtFieldOutputPath.setText(images.getOutDir().getPath());
		} else if (images.getOutDir() == null) {
			txtFieldOutputPath.setText("specify output dir");
		}

	}

	private void textWrap(JComponent component) {
		String text;
		if (component instanceof JButton button) {
			text = button.getText();
			button.setText("<html>" + text.replaceAll("\\n", "<br>") + "</html>");
		} else if (component instanceof JLabel label) {
			text = label.getText();
			label.setText("<html>" + text.replaceAll("\\n", "<br>") + "</html>");

		}

	}

	class ButtonListener implements ActionListener {
		JFileChooser chooser;

		public ButtonListener() {
			chooser = new Choser();

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == buttonInputLocation) {
				chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int retVal = chooser.showOpenDialog(MainPanel.this);
				if (retVal == JFileChooser.APPROVE_OPTION) {
					if (chooser.getSelectedFile().exists()) {
						System.out.println("Open");
						System.out.println(chooser.getSelectedFile().getAbsolutePath());
						if (chooser.getSelectedFile().isDirectory()) {
							images.setInDir(chooser.getSelectedFile());
							images.addImagesList(Arrays.asList(chooser.getSelectedFile().listFiles(new ImageFilter())));

						} else {
							images.setInDir(chooser.getCurrentDirectory());
							images.addImagesList(
									Arrays.asList(chooser.getCurrentDirectory().listFiles(new ImageFilter())));
						}
						txtFieldInputPath.setText(images.getInDir());
						txtFieldInputPath.setEditable(false);
						thumbnailsPanel.addImages();
					}

				}
			} else if (e.getSource() == buttonOutputLocation) {
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int retVal = chooser.showOpenDialog(MainPanel.this);
				if (retVal == JFileChooser.APPROVE_OPTION) {

					if (chooser.getSelectedFile().exists()) {
						System.out.println("Save");
						System.out.println(chooser.getSelectedFile().getAbsolutePath());
						images.setOutDir(chooser.getSelectedFile());
						txtFieldOutputPath.setText(chooser.getSelectedFile().getPath());
						txtFieldOutputPath.setEditable(false);

					}

				}
			} else if (e.getSource() == btnProcess) {
//				if (!(txfWidth.getText().length() > 0) && !(txfHeigth.getText().length() > 0)) {
//					JOptionPane.showMessageDialog(getParent(), "Size have to be specified", "No size specified",
//							JOptionPane.ERROR_MESSAGE);
//				} else
//					

				if (images.getSize() <= 0) {
					JOptionPane.showMessageDialog(getParent(), "Add some images", "No images",
							JOptionPane.INFORMATION_MESSAGE);
				} else if (images.getSize() > 0 && images.getOutDir() != null) {

					if (!(txfWidth.getText().length() > 0) && !(txfHeigth.getText().length() > 0)) {
						JOptionPane.showMessageDialog(getParent(), "Size have to be specified", "No size specified",
								JOptionPane.ERROR_MESSAGE);
					} else {
						String width = txfWidth.getText().length() > 0 ? txfWidth.getText() : "0";
						String heigth = txfHeigth.getText().length() > 0 ? txfHeigth.getText() : "0";
						System.out.println(width + "    " + heigth);
						ResizeMode.MODE.setSizeX(Integer.parseInt(width));
						ResizeMode.MODE.setSizeY(Integer.parseInt(heigth));
						thumbnailsPanel.doIt();
					}

				} else if (images.getOutDir() == null) {
					JOptionPane.showMessageDialog(getParent(), "Output dir have to be specified", "No output dir",
							JOptionPane.ERROR_MESSAGE);
				}
			}

		}

	}

	class Drop extends DropTarget {

		List<File> filesList = new ArrayList<>();

		public synchronized void drop(DropTargetDropEvent evt) {
			System.out.println(WorkingState.STATE);
			if (WorkingState.STATE == WorkingState.NOT_WORKING || WorkingState.STATE == WorkingState.RESIZE_DONE) {

				filesList.clear();
				evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				Transferable t = evt.getTransferable();
				if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
					try {
						Object td = t.getTransferData(DataFlavor.javaFileListFlavor);
						if (td instanceof List) {

							for (Object value : (List<?>) td) {
								if (value instanceof File file) {
									if (filter.accept(file)) {
										filesList.add(file);
									}

								}

							}

							boolean success = images.addImagesList(filesList);

							if (success) {
								txtFieldInputPath.setText(images.getInDir());
								txtFieldInputPath.setEditable(false);
								buttonInputLocation.setEnabled(false);
								setOutputDirText();
							}

						}
					} catch (UnsupportedFlavorException | IOException ex) {
						ex.printStackTrace();
					}
				}

				thumbnailsPanel.addImages();
			}

		}

	}

}
