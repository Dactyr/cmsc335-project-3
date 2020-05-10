import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CarControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private int recommendedY = TrafficLight.HEIGHT + 10;
	private final JButton button = new JButton("Add Car");

	public CarControlPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel carRows = new JPanel();
		carRows.setLayout(new BoxLayout(carRows, BoxLayout.PAGE_AXIS));
		this.add(carRows);

		// Create the three initial cars
		for (int i = 0; i < 3; i++) {
			CarRow row = new CarRow();
			row.setSpeedInput((double) (ThreadLocalRandom.current().nextInt(1, 11)));
			row.setEditable(false);
			new Car(0, recommendedY, row.getSpeedInput().get(), row);
			recommendedY += Car.HEIGHT + 10;
			row.save();

			carRows.add(row);
		}

		button.addActionListener(e -> {
			button.setEnabled(false);
			CarRow row = new CarRow();

			row.addActionListener(e2 -> {
				new Car(row.getXInput().get(), (double) recommendedY, row.getSpeedInput().get(), row);
				row.setEditable(false);
				button.setEnabled(true);
				SimulationPanel.getInstance().update();

				recommendedY += Car.HEIGHT + 10;
				SimulationPanel.getInstance().update();
			});
			carRows.add(row);
			this.revalidate();
		});
		this.add(button);
	}

	public void isPaused(boolean b) {
		this.button.setEnabled(b);
	}

}
