import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public class SimulationPanel extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	private static final int TARGET_FPS = 60;
	private static final long TARGET_FRAME_TIME_IN_NS = 1_000_000_000 / TARGET_FPS;

	private Thread thread = null;
	private boolean running = true;

	public SimulationPanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if (null == this.thread) {
			this.thread = new Thread(this);
			this.thread.start();
		}
		this.running = true;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Drawable o : Entities.getInstance()) {
			o.draw(g);
		}
	}

	@Override
	public void run() {
		// Store the last time a frame was printed, initialize
		// to now
		long lastLoopTimeNs = System.nanoTime();

		long lastFpsTime = System.nanoTime();
		long fps = 0;

		while (running) {
			// Find time since last render
			long now = System.nanoTime();
			long nsSinceLastLoop = now - lastLoopTimeNs;
			lastLoopTimeNs = now;

			this.repaint();
			lastFpsTime += nsSinceLastLoop;
			fps++;

			if (lastFpsTime >= 1_000_000_000) {
				System.out.println("FPS: " + fps);
				lastFpsTime = 0;
				fps = 0;
			}

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

}
