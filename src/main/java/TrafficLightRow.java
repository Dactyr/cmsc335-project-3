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

public class TrafficLightRow extends JPanel {

	private class InputField extends JPanel implements DocumentListener {
		private static final long serialVersionUID = 1L;

		private final JLabel label;
		private final JTextField textField;

		private InputField(String label) {
			this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

			this.label = new JLabel(label);
			this.textField = new JTextField();
			this.textField.getDocument().addDocumentListener(this);

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
			if (TrafficLightRow.this.getGreen().isPresent()) {
				TrafficLightRow.this.green.textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			} else {
				TrafficLightRow.this.green.textField.setBorder(BorderFactory.createLineBorder(Color.RED));
				valid = false;
			}

			if (TrafficLightRow.this.getYellow().isPresent()) {
				TrafficLightRow.this.yellow.textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			} else {
				TrafficLightRow.this.yellow.textField.setBorder(BorderFactory.createLineBorder(Color.RED));
				valid = false;
			}

			if (TrafficLightRow.this.getRed().isPresent()) {
				TrafficLightRow.this.red.textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			} else {
				TrafficLightRow.this.red.textField.setBorder(BorderFactory.createLineBorder(Color.RED));
				valid = false;
			}

			TrafficLightRow.this.button.setEnabled(valid);
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

	private InputField green = new InputField("Sec Green:");
	private InputField yellow = new InputField("Sec Yellow:");
	private InputField red = new InputField("Sec Red:");
	private JButton button = new JButton("save");

	public TrafficLightRow() {
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

		this.add(this.green);
		this.add(this.yellow);
		this.add(this.red);
		this.add(this.button);

		this.setGreen(0);
		this.setYellow(0);
		this.setRed(10);

		this.button.addActionListener(e -> {
			this.remove(this.button);
			this.revalidate();
		});
	}

	@Override
	public boolean requestFocusInWindow() {
		return this.green.requestFocusInWindow();
	}

	public Optional<Double> getGreen() {
		try {
			return Optional.of(Double.parseDouble(this.green.getText()));
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	public void setGreen(double val) {
		this.green.setText(Double.toString(val));
	}

	public Optional<Double> getYellow() {
		try {
			return Optional.of(Double.parseDouble(this.yellow.getText()));
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	public void setYellow(double val) {
		this.yellow.setText(Double.toString(val));
	}

	public Optional<Double> getRed() {
		try {
			return Optional.of(Double.parseDouble(this.red.getText()));
		} catch (Exception ex) {
			return Optional.empty();
		}
	}

	public void setRed(double val) {
		this.red.setText(Double.toString(val));
	}

	public boolean isEditable() {
		return this.green.isEditable();
	}

	public void setEditable(boolean val) {
		this.green.setEditable(val);
		this.yellow.setEditable(val);
		this.red.setEditable(val);
	}

	public void addActionListener(ActionListener l) {
		this.button.addActionListener(l);
	}

}
