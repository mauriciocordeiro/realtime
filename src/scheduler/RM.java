package scheduler;

import javax.swing.JOptionPane;

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
		
		//TODO: teste de escalonabilidade
		
		
//		while(!getReadyQueue().isEmpty()) {
//			
//		}
		System.out.println("Conjunto escalonável");
	}
	
	/**
	 * Gera a escala das tarefas.
	 * 
	 * @return
	 */
	public TaskPool schedule() {
		
		TaskPool result = new TaskPool();
		String timeline = "";
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
		
		System.out.println("============================================================\n"+getReadyQueue());
		
		while(clk <= endTime) {
//			System.out.println(getReadyQueue());
			
			clk++;
			startNewTask(clk);
			nextTask = dequeue();
			
			result.add(currentTask);
			timeline += clk + "|";
			taskline += currentTask.getTaskId() + "|";
			
			//skip if there isn't a new task
			if(nextTask==null)
				continue;
			
//			System.out.println("current: "+currentTask);
//			System.out.println("next: "+nextTask);
//			System.out.println();
			
			currentTask.setComputation(currentTask.getComputation()-1);
			if(currentTask.getComputation()==0) {
				System.out.println("clk: "+clk+" -> "+currentTask.getTaskId()+" FINNISH");
				currentTask = nextTask;
				continue;
			}
			
			if(currentTask.compareTo(nextTask)<=0) { //current has priority over next
				System.out.println("clk: "+clk+" -> "+currentTask.getTaskId()+" HAS PRIORITY OVER "+nextTask.getTaskId());
				continue;
			}
			else { //next has priority over current
				System.out.println("clk: "+clk+" -> "+nextTask.getTaskId()+" HAS PRIORITY OVER "+currentTask.getTaskId());
				if(currentTask.getRelativeDeadline() == clk) { //current hits deadline
					System.err.println("\t"+"clk: "+clk+" -> "+currentTask.getTaskId()+" HITS DEADLINE");
					isSchedulable = false;
					break;
				}
				else if(currentTask.getRelativeDeadline() < clk) { 
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
		return result;
		
	}
	
	private void startNewTask(int time) {
		for (Task task : getTaskpool()) {
			if((time % task.getPeriod().intValue())==0) {
				Task newTask = task.clone();
				newTask.setRelativeDeadline(time+task.getDeadline());
				System.out.println("\t\tclk: "+time+"-> "+newTask.getTaskId()+" enqueued.");
				addReady(newTask);
			}
		}
	}
	
	private Task getNewTask(int time) {
		Task newTask = null;
		for (Task task : getTaskpool()) {
			if((time % task.getPeriod().intValue())==0) {
				if((newTask==null) || (newTask.compareTo(task)==1))
					newTask = task.clone();
				newTask.setRelativeDeadline(time+task.getDeadline());
			}
		}
		return newTask;
	}
	
	public boolean isValidScale() {
		Long u = new Long(0);
		for (Task tsk : getTaskpool()) {
			u += tsk.getComputation()/tsk.getPeriod();
		}
		
		Long n = new Long(getTaskpool().size());
		
		if(u>(n*(Math.pow(2, (1/n)))-1)) {
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
