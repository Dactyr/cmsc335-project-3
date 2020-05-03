import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class SimulationPanel extends JPanel implements Drawable, Runnable {

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	private static final int TARGET_FPS = 60;
	private static final long TARGET_FRAME_TIME_IN_NS = 1_000_000_000 / TARGET_FPS;

	private int x = 30;
	private int y = 30;
	private int deltaX = 1;
	private int deltaY = 1;
	private int radius = 20;

	private Thread thread = null;
	private boolean running = true;

	public SimulationPanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}

	@Override
	public void addNotify() {
		super.addNotify();
		if (null == this.thread) {
			System.out.println("Starting thread");

			this.thread = new Thread(this);
			this.thread.start();
		}
		this.running = true;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.draw(g);
	}

	public void update() {
		x += deltaX;
		y += deltaY;

		if (x < 0) {
			x = 0;
			deltaX = -deltaX;
		}

		if (x > WIDTH - 2 * radius - 1) {
			x = WIDTH - 2 * radius - 1;
			deltaX = -deltaX;
		}

		if (y < 0) {
			y = 0;
			deltaY = -deltaY;
		}

		if (y > HEIGHT - 2 * radius - 1) {
			y = HEIGHT - 2 * radius - 1;
			deltaY = -deltaY;
		}

	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillOval(x, y, radius * 2, radius * 2);
		g.setColor(Color.BLACK);
		g.drawString("Ball Position: (" + x + ", " + y + ")", 450, 460);
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

			this.update();
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
				long nsElapsed = System.nanoTime() - nsSinceLastLoop;
				System.out.println("That took " + nsElapsed + "ns");

				long waitTimeInNs = nsSinceLastLoop + TARGET_FRAME_TIME_IN_NS - System.nanoTime();
				if (0 < waitTimeInNs) {
					long waitTimeInMs = waitTimeInNs / 1_000_000;

					System.out.println("Waiting for " + waitTimeInMs);

					Thread.sleep(waitTimeInMs);
				}

			} catch (InterruptedException e) {
				// ignore
			}
		}
	}

}
