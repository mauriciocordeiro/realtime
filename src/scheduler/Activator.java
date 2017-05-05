package scheduler;

import java.util.Timer;
import java.util.TimerTask;

public class Activator extends Thread {
	
	private Scheduler scheduler;
	private TaskPool taskPool;
	
	public Activator(Scheduler scheduler, TaskPool taskPool) {
		this.scheduler = scheduler;
		this.taskPool = taskPool;
	}
	
	@Override
	public void run() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				scheduler.setTime(scheduler.getTime()+1);
			}
		}, 0, 1000);
		
		while(true) {
			for(Task task : taskPool) {
				if(scheduler.getTime() % task.getPeriod() == 0) {
					scheduler.addReady(task.clone());
				}
			}
		}
	}
}
