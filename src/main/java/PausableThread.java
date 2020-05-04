public class PausableThread implements Runnable {

	private Thread thread = null;
	private Updatable updatable;

	public PausableThread(Updatable o) {
		this.updatable = o;
	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		while (true) {
			if (Project3.isRunning()) {
				this.updatable.update();
			} else {
				Project3.waitForRestart();
			}
		}

	}

}
