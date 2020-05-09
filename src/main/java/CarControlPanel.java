import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class CarControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public CarControlPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel carRows = new JPanel();
		carRows.setLayout(new BoxLayout(carRows, BoxLayout.PAGE_AXIS));
		this.add(carRows);

		JButton button = new JButton("Add Car");
		button.addActionListener(e -> {
			button.setEnabled(false);
			CarRow row = new CarRow();
			row.addActionListener(e2 -> {
				new Car(row.getXInput().get(),
					row.getYInput().get(),
					row.getSpeedInput().get(),
					row);
				row.setEditable(false);
				button.setEnabled(true);
				SimulationPanel.getInstance().update();
			});
			carRows.add(row);
			this.revalidate();
		});
		this.add(button);
	}

}
