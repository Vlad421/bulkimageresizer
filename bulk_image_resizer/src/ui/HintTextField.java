package ui;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class HintTextField extends JTextField implements FocusListener {
	private String hint;
	private boolean showingHint;

	public HintTextField(final String hint) {
		super(hint);
		this.hint = hint;
		this.showingHint = true;
		setForeground(Color.LIGHT_GRAY);
		super.addFocusListener(this);
	}

	public HintTextField() {
		this.hint = "";
		this.showingHint = false;
		super.addFocusListener(this);
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (this.getText().isEmpty() && showingHint) {
			super.setText("");
			setForeground(Color.BLACK);
			showingHint = false;
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (this.getText().isEmpty() && !showingHint) {
			super.setText(hint);
			setForeground(Color.LIGHT_GRAY);
			showingHint = true;
		}
	}

	@Override
	public String getText() {
		return showingHint ? "" : super.getText();
	}

	public void setHint(String hint) {
		this.hint = hint;
		setForeground(Color.LIGHT_GRAY);
		showingHint = true;
	}

	@Override
	public void setText(String t) {

		super.setText(t);
		showingHint = false;
	}
}
