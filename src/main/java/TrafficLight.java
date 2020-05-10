import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TrafficLight implements Drawable {
	public static final int HEIGHT = 60;

	private final double timeGreen;
	private final double timeYellow;
	private final double timeRed;
	private final double x;

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private long timeRunningInSec = 0L;

	public TrafficLight(double greenInSec, double yellowInSec, double redInSec, double x) {
		this.timeGreen = greenInSec;
		this.timeYellow = yellowInSec;
		this.timeRed = redInSec;
		this.x = x;

		PausableThread timer = new PausableThread(() -> {
			try {
				Thread.sleep(1_000);
			} catch (InterruptedException e) {
				// ignore
			}

			this.lock.writeLock().lock();
			try {
				this.timeRunningInSec++;
			} finally {
				this.lock.writeLock().unlock();
			}
		});
		timer.start();

		Entities.getInstance().add(this);
	}

	@Override
	public void draw(Graphics g) {
		final int length = 30;

		int location = SimulationPanel.getInstance().getXInPixelsFromXInMeters(this.x);

		g.setColor(this.getColor());
		g.fillRect(location - (length / 2), 0, length, HEIGHT);
	}

	private double cycleTime() {
		return this.timeGreen + this.timeYellow + this.timeRed;
	}

	private double timeIntoCurrentCycle() {
	    this.lock.readLock().lock();
	    try {
		return this.timeRunningInSec % this.cycleTime();
	    } finally {
		this.lock.readLock().unlock();
	    }
	}

	private Color getColor() {
		double currentTime = this.timeIntoCurrentCycle();
		if (currentTime <= this.timeGreen) {
			return Color.GREEN;
		} else if (currentTime <= (this.timeGreen + this.timeYellow)) {
			return Color.YELLOW;
		} else {
			return Color.RED;
		}
	}

	public boolean isRed() {
		return this.getColor() == Color.RED;
	}

	@Override
	public double getXLocation() {
		return this.x;
	}

}
