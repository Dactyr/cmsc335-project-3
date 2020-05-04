import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball implements Drawable, Updatable {
	private int x;
	private int y;
	private int deltaX;
	private int deltaY;
	private int radius;

	public Ball() {
		Random r = new Random();
		x = r.nextInt() % (SimulationPanel.WIDTH + 1);
		y = r.nextInt() % (SimulationPanel.HEIGHT + 1);
		deltaX = (r.nextInt() % 11) + 1;
		deltaY = (r.nextInt() % 11) + 1;
		radius = (r.nextInt() % 51) + 20;

		Entities.getInstance().add(this);

		PausableThread thread = new PausableThread(this);
		thread.start();
	}

	@Override
	public void update() {
		x += deltaX;
		y += deltaY;

		if (x < 0) {
			x = 0;
			deltaX = -deltaX;
		}

		if (x > SimulationPanel.WIDTH - 2 * radius - 1) {
			x = SimulationPanel.WIDTH - 2 * radius - 1;
			deltaX = -deltaX;
		}

		if (y < 0) {
			y = 0;
			deltaY = -deltaY;
		}

		if (y > SimulationPanel.HEIGHT - 2 * radius - 1) {
			y = SimulationPanel.HEIGHT - 2 * radius - 1;
			deltaY = -deltaY;
		}

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.fillOval(x, y, radius * 2, radius * 2);
	}

}
