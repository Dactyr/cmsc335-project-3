import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

class Project3 {
	private static final ReentrantReadWriteLock runningLock = new ReentrantReadWriteLock();

	private static final Lock r = runningLock.readLock();
	private static final Lock w = runningLock.writeLock();
	private static final Condition restarted = w.newCondition();

	private static boolean running = false;

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
		JFrame window = new JFrame("Project 3");

		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

		pane.add(new ControlPanel());
		pane.add(SimulationPanel.getInstance());
		window.setContentPane(pane);

		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}
