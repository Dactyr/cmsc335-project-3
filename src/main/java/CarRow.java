import java.util.Optional;
import java.awt.Color;
import java.awt.event.ActionListener;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CarRow extends JPanel {

	private class InputField extends JPanel implements DocumentListener {
		private static final long serialVersionUID = 1L;

		private final JLabel label;
		private final JTextField textField;

		private InputField(String label) {
			this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

			this.label = new JLabel(label);
			this.textField = new JTextField();
			this.textField.getDocument().addDocumentListener(this);
			this.textField.setSize(10, 20);

			this.add(this.label);
			this.add(this.textField);
		}

		@Override
		public boolean requestFocusInWindow() {
			return this.textField.requestFocusInWindow();
		}

		public String getText() {
			return this.textField.getText();
		}

		public void setText(String val) {
			this.textField.setText(val);
		}

		public boolean isEditable() {
			return this.textField.isEditable();
		}

		public void setEditable(boolean val) {
			this.textField.setEditable(val);
		}

		private void handleChange() {
			boolean valid = true;
			if (CarRow.this.getXInput().isPresent()) {
				CarRow.this.xInput.textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			} else {
				CarRow.this.xInput.textField.setBorder(BorderFactory.createLineBorder(Color.RED));
				valid = false;
			}

			if (CarRow.this.getYInput().isPresent()) {
				CarRow.this.yInput.textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			} else {
				CarRow.this.yInput.textField.setBorder(BorderFactory.createLineBorder(Color.RED));
				valid = false;
			}

			if (CarRow.this.getSpeedInput().isPresent()) {
				CarRow.this.speedInput.textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			} else {
				CarRow.this.speedInput.textField.setBorder(BorderFactory.createLineBorder(Color.RED));
				valid = false;
			}

			CarRow.this.button.setEnabled(valid);
		}

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			this.handleChange();
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			this.handleChange();
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			this.handleChange();
		}
	}

	private static final long serialVersionUID = 1L;

	private InputField xInput = new InputField("X:");
	private InputField yInput = new InputField("Y:");
	private InputField speedInput = new InputField("Speed:");
	private JButton button = new JButton("save");

	public CarRow() {
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		this.add(this.xInput);
		this.add(this.yInput);
		this.add(this.speedInput);
		this.add(this.button);

		this.setXInput(0);
		this.setYInput(0);
		this.setSpeedInput(10);

		this.button.addActionListener(e -> {
			this.remove(this.button);
			this.revalidate();
		});
	}

	@Override
	public boolean requestFocusInWindow() {
		return this.xInput.requestFocusInWindow();
	}

	public Optional<Double> getXInput() {
		try {
			return Optional.of(Double.parseDouble(this.xInput.getText()));
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	public void setXInput(double val) {
		this.xInput.setText(Double.toString(val));
	}

	public Optional<Double> getYInput() {
		try {
			return Optional.of(Double.parseDouble(this.yInput.getText()));
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	public void setYInput(double val) {
		this.yInput.setText(Double.toString(val));
	}

	public Optional<Double> getSpeedInput() {
		try {
			return Optional.of(Double.parseDouble(this.speedInput.getText()));
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	public void setSpeedInput(double val) {
		this.speedInput.setText(Double.toString(val));
	}

	public boolean isEditable() {
		return this.xInput.isEditable();
	}

	public void setEditable(boolean val) {
		this.xInput.setEditable(val);
		this.yInput.setEditable(val);
		this.speedInput.setEditable(val);
	}

	public void addActionListener(ActionListener l) {
		this.button.addActionListener(l);
	}

}
