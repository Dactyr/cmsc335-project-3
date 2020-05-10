public class PausableThread implements Runnable {

	private Thread thread = null;
	private Updatable updatable;
	private boolean stopped = false;

	public PausableThread(Updatable o) {
		this.updatable = o;
		this.thread = new Thread(this);
	}

	public void start() {
		thread.start();
	}

	public void setName(String name) {
		this.thread.setName(name);
	}

	@Override
	public void run() {
		while (!stopped) {
			if (Project3.isRunning()) {
				this.updatable.update();
			} else {
				Project3.waitForRestart();
			}
		}
	}

	public void stop() {
		this.stopped = true;
	}

}
