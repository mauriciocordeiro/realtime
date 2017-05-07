package scheduler;

import javax.swing.JOptionPane;

import scheduler.ui.MainWindow;

public class EDF extends Scheduler {
	
	public EDF(TaskPool taskpool, MainWindow window) {
		super(taskpool, window, false);
		
		for (Task task : taskpool) {
			Task newTask = task.clone();
			newTask.setRelativeDeadline(newTask.getDeadline());
			addReady(newTask);
		}
	}

	public EDF() {
		super();
	}
	
	@Override
	public void run() {
		
		try {
			
			TaskPool result = new TaskPool();
			String taskline = "";
			
			Task currentTask = null;
			Task nextTask = null;
			
			boolean isSchedulable = true;
			
			int[] periods = new int[getTaskpool().size()];
			for (int i=0; i<periods.length; i++) {
				periods[i] = getTaskpool().get(i).getPeriod().intValue();
			}
			int clk = 0;
			int endTime = br.org.mac.midgard.util.Math.lcm(periods);
			currentTask = dequeue();
			
			System.out.println("============================================================");
			System.out.println(getReadyQueue());
			
			while(clk <= endTime) {
				Thread.sleep(1000);
				
				if(!validateDeadlines(clk)) {
					isSchedulable = false;
					break;
				}
				
				clk++;
				startNewTask(clk);
				nextTask = dequeue();
				
				result.add(currentTask);
				taskline += currentTask.getTaskId() + "|";
				getWindow().chart.addTask(currentTask);
				
				//skip if there isn't a new task
				if(nextTask==null)
					continue;
				
				//currentTask.setComputation(currentTask.getComputation()-1);
				if(!currentTask.isAlive() || currentTask.isInterrupted()) {
					System.out.println("clk: "+clk+" -> "+currentTask.getTaskId()+" FINNISH");
					currentTask = nextTask;
					continue;
				}
				
				if(currentTask.compareRelativeDeadlineTo(nextTask)<=0) { //current has priority over next
					System.out.println("clk: "+clk+" -> "+currentTask.getTaskId()+" HAS PRIORITY OVER "+nextTask.getTaskId());
					if(currentTask.getRelativeDeadline() == clk) { //current hits deadline
						System.err.println("\t"+"clk: "+clk+" -> "+currentTask.getTaskId()+" HITS DEADLINE");
						isSchedulable = false;
						break;
					}
					continue;
				}
				else { //next has priority over current
					System.out.println("clk: "+clk+" -> "+nextTask.getTaskId()+" HAS PRIORITY OVER "+currentTask.getTaskId());
					if(currentTask.getRelativeDeadline() < clk) { //current hits deadline
						System.err.println("\t"+"clk: "+clk+" -> "+currentTask.getTaskId()+" HITS DEADLINE");
						isSchedulable = false;
						break;
					}
					else if(currentTask.getRelativeDeadline() >= clk) { 
						System.err.println("\t"+"clk: "+clk+" -> "+currentTask.getTaskId()+" HAS BEEN PREEMPTED");
						enqueue(currentTask);
					}
					
					currentTask = nextTask; // new is the new current
				}
			}
			
			if(!isSchedulable) 
				JOptionPane.showMessageDialog(getWindow(), "Conjunto de tarefas não escalonável", "Alerta", JOptionPane.WARNING_MESSAGE);
			else
				JOptionPane.showMessageDialog(getWindow(), "Conjunto de tarefas escalonável", "Informação", JOptionPane.INFORMATION_MESSAGE);
			
			System.out.println("result:\n"+taskline);
			
			
		} catch (Exception e) { }
	}
	
	public boolean isSchedulable() {
		Long u = new Long(0);
		for (Task tsk : getTaskpool()) {
			u += tsk.getComputation()/tsk.getPeriod();
		}
		
		return u<=1;
	}
	
	private void startNewTask(int clk) {
		for (Task task : getTaskpool()) {
			if((clk % task.getPeriod().intValue())==0) {
				Task newTask = task.clone();
				newTask.setRelativeDeadline(clk+task.getDeadline());
				System.out.println("\t\tclk: "+clk+"-> "+newTask.getTaskId()+" enqueued.");
				addReady(newTask);
			}
		}
	}
	
	private Task dequeue() {
		return getReadyQueue().removeLowestRelativeDeadline();
	}
	
	private void enqueue(Task task) {
		getReadyQueue().add(task);
	}

}
