import java.awt.Color;
import java.awt.Graphics;

public class Car implements Drawable, Updatable {
	private static int TICKS_PER_SECOND = 60;
	private static final long TICK_TIME_IN_NS = 1_000_000_000 / TICKS_PER_SECOND;

	private double x;
	private double y;
	private double speedPerTick;
	private long lastLoopTimeNs = System.nanoTime();
	private CarRow row;
	private PausableThread thread;

	public Car(double x, double y, double speedPerSec, CarRow row) {
		this.x = x;
		this.y = y;
		this.speedPerTick = speedPerSec / TICKS_PER_SECOND;
		this.row = row;

		Entities.getInstance().add(this);

		thread = new PausableThread(this);
		thread.start();
	}

	@Override
	public void update() {
		System.out.println("updating");

		this.x += this.speedPerTick;
		System.out.println("x = " + this.x);

		this.row.setXInput(this.x);

		if (this.x > SimulationPanel.WIDTH) {
			this.thread.stop();
		}

		// Figure out how long to sleep this thread for
		try {
			long nsElapsed = System.nanoTime() - lastLoopTimeNs;
			long waitTimeInNs = TICK_TIME_IN_NS - nsElapsed;
			if (0 < waitTimeInNs) {
				long waitTimeInMs = waitTimeInNs / 1_000_000;
				Thread.sleep(waitTimeInMs);
			}
		} catch (InterruptedException e) {
			// ignore
		}
	}

	@Override
	public void draw(Graphics g) {
		final int radius = 20;
		g.setColor(Color.BLUE);
		g.fillOval((int) x, (int) y, radius * 2, radius * 2);
	}

}
