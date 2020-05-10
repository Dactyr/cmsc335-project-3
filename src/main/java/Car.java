import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Car implements Drawable, Updatable {
	private static int TICKS_PER_SECOND = 60;
	private static final long TICK_TIME_IN_NS = 1_000_000_000 / TICKS_PER_SECOND;
	public static final int HEIGHT = 40;

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

		this.thread = new PausableThread(this);
		this.thread.start();
	}

	@Override
	public void update() {
		this.lastLoopTimeNs = System.nanoTime();

		double cur = this.getXLocation();
		double destination = cur + this.speedPerTick;

		for (TrafficLight tl : Entities.getInstance().trafficLights()) {
			if (tl.isRed()) {
				double tlLocation = tl.getXLocation();
				System.out.println("found red traffic light at " + tlLocation);

				if (tlLocation >= cur && tlLocation <= destination) {
					destination = Math.min(tlLocation, destination);
				}
			}
		}

		this.setXLocation(destination);
		this.row.setXInput(destination);

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
		g.setColor(Color.BLUE);

		int location = SimulationPanel.getInstance().getXInPixelsFromXInMeters(this.getXLocation());
		g.fillOval(location - (HEIGHT / 2), (int) y, HEIGHT, HEIGHT);
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

	public void setXLocation(double val) {
		this.xLock.writeLock().lock();
		try {
			this.x = val;
		} finally {
			this.xLock.writeLock().unlock();
		}
	}

	public double getYLocation() {
		return this.y;
	}

}
