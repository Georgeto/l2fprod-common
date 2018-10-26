package com.github.sarxos.l2fprod.sheet.editor;

import java.awt.BorderLayout;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;


/**
 * Editor containing spinner inside. It can be used for various models such as
 * date, numbers, chars, etc.
 * 
 * @author Bartosz Firyn (SarXos)
 */
public class SpinnerEditor extends AbstractPropertyEditor {
	protected JSpinner spinner = null;
	protected JPanel panel = null;

	public SpinnerEditor() {

		spinner = new JSpinner() {
			@Override
			public void requestFocus() {
				// We do not want to be focused, the text field shall be focused.
			}
		};

		spinner.setBorder(BorderFactory.createEmptyBorder());
		spinner.setOpaque(false);
		spinner.setFont(UIManager.getFont("Table.font"));
		spinner.setLocation(new Point(-1, -1));

		editor = spinner;

		formatSpinner();
	}

	protected void formatSpinner() {
		DefaultEditor ne = (DefaultEditor) spinner.getEditor();
		ne.setFont(UIManager.getFont("Table.font"));
		ne.getTextField().setHorizontalAlignment(JTextField.LEFT);
		ne.getTextField().setAlignmentX(JTextField.LEFT_ALIGNMENT);
		ne.getTextField().setFont(UIManager.getFont("Table.font"));
		ne.getTextField().addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				SwingUtilities.invokeLater(() -> ((JTextField)e.getSource()).selectAll());
			}

			@Override
			public void focusLost(FocusEvent e) {
				SwingUtilities.invokeLater(() -> ((JTextField)e.getSource()).select(0, 0));
			}
		});
	}

	@Override
	public Object getValue() {
		try {
			spinner.commitEdit();
		} catch (ParseException e) {
			// Just keep the previous value...
		}
		Object value = spinner.getValue();
		if (value instanceof ObjectWrapper) {
			return ((ObjectWrapper) value).value;
		} else {
			return value;
		}
	}

	@Override
	public void setValue(Object value) {
		if (value != spinner.getValue()) {
			spinner.setValue(value);
		}
	}

	public static final class ObjectWrapper {

		private Object value;

		public ObjectWrapper(Object value, Object visualValue) {
			this.value = value;
		}

		@Override
		public boolean equals(Object o) {
			if (o == this) {
				return true;
			}
			if (value == o || (value != null && value.equals(o))) {
				return true;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return value == null ? 0 : value.hashCode();
		}
	}
}
