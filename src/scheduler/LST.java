package scheduler;

public class LST extends Thread {
	
	private TaskPool taskpool;
	private MainWindow window;
	
	public LST(TaskPool taskpool, MainWindow window) {
		super();
		this.taskpool = taskpool;
		this.window = window;
	}

	public LST() {
		super();
	}
	
	@Override
	public void run() {
		
		
		
	}

}
