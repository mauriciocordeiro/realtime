package scheduler;

import javax.swing.JOptionPane;

import scheduler.ui.MainWindow;

public class LST extends Scheduler {
	
	public LST(TaskPool taskpool, MainWindow window) {
		super(taskpool, window, false);
		
		for (Task task : taskpool) {
			Task newTask = task.clone();
			newTask.setRelativeDeadline(newTask.getDeadline());
			addReady(newTask);
		}
	}

	public LST() {
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
			currentTask = dequeue(clk);
			
			while(clk <= endTime) {
				Thread.sleep(1000);
				
				if(!validateDeadlines(clk)) {
					isSchedulable = false;
					break;
				}

				result.add(currentTask);
				System.out.println("clk: "+clk+" -> "+currentTask.getTaskId()+" RUNNING");
				getWindow().chart.addTask(currentTask);
				taskline += currentTask.getTaskId() + "|";
				
				clk++;
				startNewTask(clk);

				nextTask = dequeue(clk);
				
				//skip if there isn't a new task
				if(nextTask==null)
					continue;
				
				currentTask.setComputation(currentTask.getComputation()-1);
				if(currentTask.getComputation().intValue()==0) {
					System.out.println("clk: "+clk+" -> "+currentTask.getTaskId()+" FINNISH");
					currentTask = nextTask;
					continue;
				}
				
				if(currentTask.compareTo(nextTask)<=0) { //current has priority over next
					System.out.println("clk: "+clk+" -> "+currentTask.getTaskId()+" HAS PRIORITY OVER "+nextTask.getTaskId());
					enqueue(nextTask);
					if(currentTask.getRelativeDeadline().intValue() < clk) { //current hits deadline
						System.err.println("\t"+"clk: "+clk+" -> "+currentTask.getTaskId()+" HITS DEADLINE ("+currentTask.getRelativeDeadline()+")");
						isSchedulable = false;
						getWindow().chart.addDeadline(currentTask);
						break;
					}
					continue;
				}
				else { //next has priority over current
					System.out.println("clk: "+clk+" -> "+nextTask.getTaskId()+" HAS PRIORITY OVER "+currentTask.getTaskId());
					if(currentTask.getRelativeDeadline().intValue() < clk) { //current hits deadline
						System.err.println("\t"+"clk: "+clk+" -> "+currentTask.getTaskId()+" HITS DEADLINE");
						isSchedulable = false;
						getWindow().chart.addDeadline(currentTask);
						break;
					}
					else if(currentTask.getRelativeDeadline().intValue() >= clk) { 
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
	
	private Task dequeue(int clk) {
		return getReadyQueue().removeLowestSlackTime(clk);
	}
	
	private void enqueue(Task task) {
		getReadyQueue().add(task);
	}

}
