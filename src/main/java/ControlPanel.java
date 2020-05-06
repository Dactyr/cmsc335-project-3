import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public ControlPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(1, 2, 10, 10));
		controls.add(new CarControlPanel());
		controls.add(new TrafficLightControlPanel());
		this.add(controls);

		JButton button = new JButton("Start");
		button.setPreferredSize(new Dimension(100, 25));
		button.addActionListener(e -> {
			if (Project3.isRunning()) {
				Project3.isRunning(false);
				button.setText("Restart");
			} else {
				Project3.isRunning(true);
				button.setText("Pause");
			}
		});
		this.add(button);
	}

}
