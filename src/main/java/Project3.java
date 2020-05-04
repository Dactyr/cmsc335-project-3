import javax.swing.JFrame;

class Project3 {
	public static void main(String[] args) {

	    for (int i = 0; i < 40; i ++) {
		new Ball();
	    }

		JFrame window = new JFrame("Project 3");
		window.setContentPane(new SimulationPanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}
