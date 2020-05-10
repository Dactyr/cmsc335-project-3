import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class SimulationPanel extends JPanel implements Updatable {

	private static SimulationPanel INSTANCE = new SimulationPanel();

	public static SimulationPanel getInstance() {
		return INSTANCE;
	}

	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 480;
	private static final int TARGET_FPS = 60;
	private static final long TARGET_FRAME_TIME_IN_NS = 1_000_000_000 / TARGET_FPS;

	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private long totalRunTimeInSecs = 0L;
	private long lastLoopTimeNs = System.nanoTime();
	private Entities entities = Entities.getInstance();

	private SimulationPanel() {
	    this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		PausableThread timer = new PausableThread(() -> {
			try {
				Thread.sleep(1_000);
			} catch (InterruptedException e) {
				// ignore
			}

			this.lock.writeLock().lock();
			try {
				this.totalRunTimeInSecs++;
			} finally {
				this.lock.writeLock().unlock();
			}
		});
		timer.setName("Timer");
		timer.start();
	}

	public synchronized int getXInPixelsFromXInMeters(double xInMeters) {
		double distanceIntoTheScreen = xInMeters - this.leftBoundryInMeters();
		double percentThroughTotalSpace = distanceIntoTheScreen / getWidthInMeters();
		double thatPercentOfTotalPixels = percentThroughTotalSpace * this.getWidth();

		return (int) Math.round(thatPercentOfTotalPixels);
	}

	public synchronized double getWidthInMeters() {
		return this.rightBoundryInMeters() - this.leftBoundryInMeters();
	}

	public synchronized double leftBoundryInMeters() {
		return this.entities.minXLocation();
	}

	public synchronized double rightBoundryInMeters() {
	    return Math.max(this.entities.maxXLocation(), this.getWidth() / 100.0);
	}

	@Override
	public void addNotify() {
		super.addNotify();
		PausableThread thread = new PausableThread(this);
		thread.setName("SimulationPanel");
		thread.start();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (Drawable o : this.entities) {
			o.draw(g);
		}

		long seconds;
		this.lock.readLock().lock();
		try {
			seconds = this.totalRunTimeInSecs;
		} finally {
			this.lock.readLock().unlock();
		}

		long hours = seconds / (60 * 60);
		seconds -= hours * (60 * 60);
		long minutes = seconds / 60;
		seconds -= minutes * 60;

		FontMetrics fm = g.getFontMetrics();
		String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		int timeWidth = fm.stringWidth(time);

		String min = String.format("%.1f", this.leftBoundryInMeters());
		String max = String.format("%.1f", this.rightBoundryInMeters());
		int maxWidth = fm.stringWidth(max);

		final int MARGIN = 10;
		int fontHeight = fm.getHeight();
		int bottomY = this.getHeight() - fontHeight - MARGIN;

		g.setColor(Color.BLACK);
		g.drawString(min, MARGIN, bottomY);
		g.drawString(max, this.getWidth() - maxWidth - MARGIN, bottomY);
		g.drawString(time, this.getWidth() - timeWidth - MARGIN, MARGIN + fontHeight);
	}

	@Override
	public void update() {
		// Find time since last render
		this.lastLoopTimeNs = System.nanoTime();

		this.repaint();

		// Figure out how long to sleep this thread for

		long nsElapsed = System.nanoTime() - lastLoopTimeNs;
		long waitTimeInNs = TARGET_FRAME_TIME_IN_NS - nsElapsed;
		if (0 < waitTimeInNs) {
			long waitTimeInMs = waitTimeInNs / 1_000_000;
			try {
				Thread.sleep(waitTimeInMs);
			} catch (InterruptedException e) {
				// ignore
			}
		}
	}

}
