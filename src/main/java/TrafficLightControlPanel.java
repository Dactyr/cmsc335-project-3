import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TrafficLightControlPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	JButton button = new JButton("Add Traffic Light");

	public TrafficLightControlPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel trafficLightRows = new JPanel();
		trafficLightRows.setLayout(new BoxLayout(trafficLightRows, BoxLayout.PAGE_AXIS));
		this.add(trafficLightRows);

		// add initial lights
		for (int i = 0; i < 3; i++) {
			TrafficLightRow row = new TrafficLightRow();
			row.setXInput((i + 1) * 1000);
			row.setGreen((double) ThreadLocalRandom.current().nextInt(48, 97));
			row.setYellow((double) ThreadLocalRandom.current().nextInt(12, 25));
			row.setRed((double) ThreadLocalRandom.current().nextInt(48, 97));
			new TrafficLight(row.getGreen().get(), row.getYellow().get(), row.getRed().get(), row.getXInput().get());
			row.setEditable(false);
			row.save();
			trafficLightRows.add(row);
		}


		button.addActionListener(e -> {
			button.setEnabled(false);
			TrafficLightRow row = new TrafficLightRow();
			row.addActionListener(e2 -> {
				new TrafficLight(row.getGreen().get(), row.getYellow().get(), row.getRed().get(),
						row.getXInput().get());
				row.setEditable(false);
				button.setEnabled(true);
				SimulationPanel.getInstance().update();
			});
			trafficLightRows.add(row);
			this.revalidate();
		});
		this.add(button);
	}

	public void isPaused(boolean b) {
		this.button.setEnabled(b);
	}

}
