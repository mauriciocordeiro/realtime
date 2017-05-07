package scheduler;

public class Activator extends Thread {
	
	private Scheduler scheduler;
	private TaskPool taskPool;
	
	public Activator(Scheduler scheduler, TaskPool taskPool) {
		this.scheduler = scheduler;
		this.taskPool = taskPool;
	}
	
	@Override
	public void run() {		
		try {
			while(true) {
				Thread.sleep(1000);
				scheduler.setTime(scheduler.getTime()+1);
				for(Task task : taskPool) {
					if(scheduler.getTime() % task.getPeriod() == 0) {
						scheduler.addReady(task.clone());
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
}
