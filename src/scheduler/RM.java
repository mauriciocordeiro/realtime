package scheduler;

public class RM extends Thread {
	
	private TaskPool taskpool;
	private MainWindow window;
	
	public RM(TaskPool taskpool, MainWindow window) {
		super();
		this.taskpool = taskpool;
		this.window = window;
	}

	public RM() {
		super();
	}
	
	@Override
	public void run() {
		
		
		
	}

}
