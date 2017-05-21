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

			System.out.println("============================================================");
			System.out.println(getReadyQueue());
			
			int[] periods = new int[getTaskpool().size()];
			for (int i=0; i<periods.length; i++) {
				periods[i] = getTaskpool().get(i).getPeriod().intValue();
			}
			int clk = 0;
			int endTime = br.org.mac.midgard.util.Math.lcm(periods);
			currentTask = dequeue();
			
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
				getWindow().chart.enqueueTask(newTask);
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
