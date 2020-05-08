import java.awt.Color;
import java.awt.Graphics;

public class TrafficLight implements Drawable {
	private static final long NANOSECONDS_PER_SECOND = 1_000_000_000L;

	private final double timeGreenInNs;
	private final double timeYellowInNs;
	private final double timeRedInNs;
	private final double x;
	private final double y;
	private final long startTimeInNs = System.nanoTime();

	public TrafficLight(double greenInSec, double yellowInSec, double redInSec, double x) {
		this.timeGreenInNs = greenInSec * NANOSECONDS_PER_SECOND;
		this.timeYellowInNs = yellowInSec * NANOSECONDS_PER_SECOND;
		this.timeRedInNs = redInSec * NANOSECONDS_PER_SECOND;
		this.x = x;
		this.y = 0;

		Entities.getInstance().add(this);
	}

	@Override
	public void draw(Graphics g) {
		final int length = 10;
		final int height = 20;

		int location = SimulationPanel.getInstance().getXInPixelsFromXInMeters(this.x);

		g.setColor(this.getColor());
		g.fillRect(location,
			   0,
			   length,
			   height);
	}

	private double cycleTimeInNs() {
		return this.timeGreenInNs + this.timeYellowInNs + this.timeRedInNs;
	}

	private double nsIntoCurrentCycle() {
		return (System.nanoTime() - this.startTimeInNs) % this.cycleTimeInNs();
	}

	private Color getColor() {
		double currentTime = this.nsIntoCurrentCycle();
		if (currentTime <= this.timeGreenInNs) {
			return Color.GREEN;
		} else if (currentTime <= (this.timeGreenInNs + this.timeYellowInNs)) {
			return Color.YELLOW;
		} else {
			return Color.RED;
		}
	}

	@Override
	public double getXLocation() {
		return this.x;
	}


}
