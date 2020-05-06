import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TrafficLightControlPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public TrafficLightControlPanel() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel trafficLightRows = new JPanel();
		trafficLightRows.setLayout(new BoxLayout(trafficLightRows, BoxLayout.PAGE_AXIS));
		this.add(trafficLightRows);

		JButton button = new JButton("Add Traffic Light");
		button.addActionListener(e -> {
			button.setEnabled(false);
			TrafficLightRow row = new TrafficLightRow();
			row.addActionListener(e2 -> {
				row.setEditable(false);
				button.setEnabled(true);
			});
			trafficLightRows.add(row);
			this.revalidate();
		});
		this.add(button);
	}

}
