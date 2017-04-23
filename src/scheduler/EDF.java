package scheduler;

public class EDF extends Thread {
	
	private TaskPool taskpool;
	private MainWindow window;
	
	public EDF(TaskPool taskpool, MainWindow window) {
		super();
		this.taskpool = taskpool;
		this.window = window;
	}

	public EDF() {
		super();
	}
	
	@Override
	public void run() {
		
		
		
	}

}
