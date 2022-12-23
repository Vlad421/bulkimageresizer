package utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class TxfFilter extends PlainDocument {
	// Store maximum characters permitted
	private int maxChars = 3;
	Pattern regEx = Pattern.compile("\\d*");

	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		Matcher matcher = regEx.matcher(str);
		if (!matcher.matches()) {
			return;
		}
		if (str != null && (getLength() + str.length() <= maxChars)) {
			super.insertString(offs, str, a);
		}
	}

}
