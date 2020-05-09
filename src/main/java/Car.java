import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Car implements Drawable, Updatable {
	private static int TICKS_PER_SECOND = 30;
	private static final long TICK_TIME_IN_NS = 1_000_000_000 / TICKS_PER_SECOND;

	private final ReentrantReadWriteLock xLock = new ReentrantReadWriteLock();
	private double x;
	private final double y;
	private final double speedPerTick;
	private long lastLoopTimeNs = System.nanoTime();
	private final CarRow row;
	private final PausableThread thread;

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
		System.out.println("Updating");

		this.incrementXLocation(this.speedPerTick);

		System.out.println("Location incremented");

		// this.row.setXInput(this.getXLocation());
		// System.out.println("set x");

		// this.row.setYInput(this.getYLocation());
		// System.out.println("set y");

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

		int location = SimulationPanel.getInstance().getXInPixelsFromXInMeters(this.getXLocation());
		g.fillOval(location - radius, (int)y, radius * 2, radius * 2);
	}

	@Override
	public double getXLocation() {
		this.xLock.readLock().lock();
		try {
			return this.x;
		} finally {
			this.xLock.readLock().unlock();
		}
	}

	public void incrementXLocation(double val) {
		this.xLock.writeLock().lock();
		try {
			this.x += val;
		} finally {
			this.xLock.writeLock().unlock();
		}
	}

	public double getYLocation() {
		return this.y;
	}

}
