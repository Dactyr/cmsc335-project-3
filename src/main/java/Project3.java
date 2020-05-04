import java.awt.Dimension;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Project3 {
	private static final ReentrantReadWriteLock runningLock = new ReentrantReadWriteLock();

	private static final Lock r = runningLock.readLock();
	private static final Lock w = runningLock.writeLock();
	private static final Condition restarted = w.newCondition();

	private static boolean running = true;

	public static boolean isRunning() {
		r.lock();
		try {
			return running;
		} finally {
			r.unlock();
		}
	}

	public static void isRunning(boolean val) {
		w.lock();
		try {
			running = val;
			if (running)
				restarted.signalAll();
		} finally {
			w.unlock();
		}
	}

	public static void waitForRestart() {
		w.lock();
		try {
		    while (!running) {
			restarted.await();
		    }
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			w.unlock();
		}
	}

	public static void main(String[] args) {

		for (int i = 0; i < 40; i++) {
			new Ball();
		}

		JFrame window = new JFrame("Project 3");
		JButton button = new JButton("Pause");
		JPanel pane = new JPanel();
		button.setPreferredSize(new Dimension(100, 25));
		button.addActionListener(e -> {
			if (isRunning()) {
				isRunning(false);
				button.setText("Restart");
			} else {
				isRunning(true);
				button.setText("Pause");
			}
		});

		pane.add(new SimulationPanel());
		pane.add(button);
		window.setContentPane(pane);

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}
