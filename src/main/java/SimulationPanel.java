import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class SimulationPanel extends JPanel implements Updatable {

	private static SimulationPanel INSTANCE = new SimulationPanel();

	public static SimulationPanel getInstance() {
		return INSTANCE;
	}

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	private static final int TARGET_FPS = 60;
	private static final long TARGET_FRAME_TIME_IN_NS = 1_000_000_000 / TARGET_FPS;

	private long lastLoopTimeNs = System.nanoTime();
	private Entities entities = Entities.getInstance();

	private SimulationPanel() {
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}

	public int getXInPixelsFromXInMeters(double xInMeters) {
		double totalSpaceInMeters = this.entities.maxXLocation() - this.entities.minXLocation();
		double percentThroughTotalSpace = xInMeters / totalSpaceInMeters;
		double thatPercentOfTotalPixels = percentThroughTotalSpace * this.getWidth();
		System.out.println("this width = " + this.getWidth());

		return (int) Math.round(thatPercentOfTotalPixels);
	}

	@Override
	public void addNotify() {
		super.addNotify();
		PausableThread thread = new PausableThread(this);
		thread.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Drawable o : this.entities) {
			o.draw(g);
		}
	}

	@Override
	public void update() {
		// Find time since last render
		this.lastLoopTimeNs = System.nanoTime();

		this.repaint();

		// Figure out how long to sleep this thread for
		try {
			long nsElapsed = System.nanoTime() - lastLoopTimeNs;
			long waitTimeInNs = TARGET_FRAME_TIME_IN_NS - nsElapsed;
			if (0 < waitTimeInNs) {
				long waitTimeInMs = waitTimeInNs / 1_000_000;
				Thread.sleep(waitTimeInMs);
			}

		} catch (InterruptedException e) {
			// ignore
		}
	}

}
