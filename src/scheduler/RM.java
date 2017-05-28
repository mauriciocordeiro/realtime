package scheduler;

import javax.swing.JOptionPane;

import scheduler.ui.MainWindow;

public class RM extends Scheduler {
	
	public RM(TaskPool taskpool, MainWindow window) {
		super(taskpool, window, false);
		
		for (Task task : taskpool) {
			Task newTask = task.clone();
			newTask.setRelativeDeadline(newTask.getDeadline());
			addReady(newTask);
		}
	}

	public RM() {
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

			System.out.println("============================================================\n"+getReadyQueue());
			
			int[] periods = new int[getTaskpool().size()];
			for (int i=0; i<periods.length; i++) {
				periods[i] = getTaskpool().get(i).getPeriod().intValue();
			}
			int clk = 0;
			int endTime = br.org.mac.midgard.util.Math.lcm(periods);
			currentTask = dequeue();
			result.add(currentTask);
//			System.out.println("clk: "+clk+" -> "+(currentTask==null?"_":currentTask.getTaskId())+" RUNNING");
//			getWindow().chart.addTask(currentTask);
//			taskline += (currentTask==null?"_":currentTask.getTaskId()) + "|";
			
			while(clk <= endTime) {
				Thread.sleep(100);
				clk++;
				startNewTask(clk);
				
//				System.err.println("\tReady ["+clk+"]: "+getReadyQueue());
				
				if(!validateDeadlines(clk)) {
					isSchedulable = false;
					break;
				}
				
				if(currentTask==null)
					currentTask = dequeue();
				
//				System.err.println("\tcurrentTask: "+currentTask);
				
				result.add(currentTask);
				System.out.println("clk: "+clk+" -> "+(currentTask==null?"_":currentTask.getTaskId())+" RUNNING");
				getWindow().chart.addTask(currentTask);
				taskline += (currentTask==null?"_":currentTask.getTaskId()) + "|";

				nextTask = dequeue();
				
				//skip if there isn't a new task
				if(currentTask==null)
					continue;
				
				currentTask.setComputation(currentTask.getComputation()-1);
				if(currentTask.getComputation().intValue()==0) {
					System.out.println("clk: "+clk+" -> "+currentTask.getTaskId()+" FINNISH");
					currentTask = nextTask;
					continue;
				}
				
				
				if(nextTask!=null && currentTask.compareTo(nextTask)<=0) { //current has priority over next
					System.out.println("clk: "+clk+" -> "+currentTask.getTaskId()+" HAS PRIORITY OVER "+nextTask.getTaskId());
					enqueue(nextTask);
					if(clk > currentTask.getRelativeDeadline().intValue()) { //current hits deadline
						System.err.println("\t"+"clk: "+clk+" -> "+currentTask.getTaskId()+" HITS DEADLINE ("+currentTask.getRelativeDeadline()+")");
						isSchedulable = false;
						getWindow().chart.addDeadline(currentTask);
						break;
					}
					continue;
				}
				else if(nextTask!=null) { //next has priority over current
					System.out.println("clk: "+clk+" -> "+nextTask.getTaskId()+" HAS PRIORITY OVER "+currentTask.getTaskId());
					if(clk > currentTask.getRelativeDeadline().intValue()) { //current hits deadline
						System.err.println("\t"+"clk: "+clk+" -> "+currentTask.getTaskId()+" HITS DEADLINE ["+currentTask.getComputation()+"]");
						isSchedulable = false;
						getWindow().chart.addDeadline(currentTask);
						break;
					}
					else if(clk < currentTask.getRelativeDeadline().intValue()) { 
						System.out.println("\t"+"clk: "+clk+" -> "+currentTask.getTaskId()+" HAS BEEN PREEMPTED");
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
			
		} catch (Exception e) {
			System.out.println("\t\t\t\t\t"+e.getMessage());
		}
	}
	
	private void startNewTask(int time) {
		for (Task task : getTaskpool()) {
			if((time % task.getPeriod().intValue())==0) {
				Task newTask = task.clone();
				newTask.setRelativeDeadline(time+task.getDeadline());
				System.out.println("\t\tclk: "+time+"-> "+newTask.getTaskId()+" enqueued.");
				addReady(newTask);
				getWindow().chart.enqueueTask(newTask);
			}
		}
	}
	
	public boolean isSchedulable() {
		Long u = new Long(0);
		for (Task tsk : getTaskpool()) {
			u += tsk.getComputation()/tsk.getPeriod();
		}
		
		Long n = new Long(getTaskpool().size());
		
		if(u>(n*((Math.pow(2, (1/n)))-1))) {
			return false;
		}
		return true;
	}
	
	private Task dequeue() {
		return getReadyQueue().removeLowestPeriod();
	}
	
	private void enqueue(Task task) {
		getReadyQueue().add(task);
	}

}
