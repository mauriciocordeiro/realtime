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
		Long u = new Long(0);
		for (Task tsk : getTaskpool()) {
			u += tsk.getComputation()/tsk.getPeriod();
		}
		
		Long n = new Long(getTaskpool().size());
		
		if(u>(n*(Math.pow(2, (1/n)))-1)) {
			JOptionPane.showMessageDialog(null, "Conjunto de tarefas não escalonável.", "Alerta", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
//		while(!getReadyQueue().isEmpty()) {
//			
//		}
		System.out.println("Conjunto escalonável");
	}
	
	public TaskPool schedule() {
		
		TaskPool result = new TaskPool();
		
		Task currentTask = null;
		Task nextTask = null;
		
		boolean isSchedule = true;
		boolean currentContinue = true;
		
		//TODO: calcular mmc dos períodos e escalonar até o mmc
		int[] periods = new int[getTaskpool().size()];
		for (int i=0; i<periods.length; i++) {
			periods[i] = getReadyQueue().get(i).getPeriod().intValue();
		}
		int endTime = 60;//mmc(periods);
		
		while(getTime().intValue() <= endTime) {
			
			if(getTime().intValue()==0) {
				currentTask = getReadyQueue().remove(0);
				
			}
			else {
				
				startNewTask(getTime().intValue());
				
				if(!currentContinue && getReadyQueue().size()>0) {
					currentTask = getReadyQueue().remove(0);
				}
				if(getReadyQueue().size()>0) {
					nextTask = getReadyQueue().remove(0);
				}
				
				if(currentTask == null)
					continue;
				if(nextTask == null)
					continue;
					
				if(currentTask.compareTo(nextTask)==1) {
					if(currentTask.getRelativeDeadline() == getTime().intValue()) {
						currentTask = nextTask;	
						currentContinue = true;
					}
					else if(currentTask.getRelativeDeadline() < getTime().intValue()) {
						addReady(currentTask, true);
						currentTask = nextTask;	
						currentContinue = true;
					}
					else if(currentTask.getRelativeDeadline() > getTime().intValue()) {
						isSchedule = false;
						currentContinue = false;
						break;
					}
					
				}
				else {
					currentContinue = true;
					addReady(nextTask, true);
				}
			}
			
			result.add(currentTask);
			setTime(getTime()+1);
		}
		
		
		return result;
		
	}
	
	private void startNewTask(int time) {
		for (Task task : getTaskpool()) {
			if((time % task.getPeriod().intValue())==0) {
				Task newTask = task.clone();
				newTask.setRelativeDeadline(time+task.getDeadline());
				addReady(newTask, true);
			}
		}
	}
	
	public int mmc(int... numbers) {
		int mmc = 1;
		
		int factor = 1;
		
		while(hasMore(numbers)) {
			for (int i=0; i<numbers.length; i++) {
				if(isPrime(factor)) {
					if(numbers[i] % factor == 0) {
						numbers[i] = numbers[i]/factor;
						System.out.println("i /= factor = "+numbers[i]);
						mmc = mmc*factor;
					}
				}
				else {
					continue;
				}
			}
			factor++;
		}	
		
		return mmc;
		
	}
	
	private boolean hasMore(int[] numbers) {
		boolean hasMore = false;
		
		for (int i=0; i<numbers.length; i++) {
			if(numbers[i] > 1) {
				hasMore = true;
				break;
			}
		}
		return hasMore;
	}
	
	private boolean isPrime(int number) {
		int count = 0; 
		
		for(int i=1; i<=number; i++) {
			if(number%i==0) {
				count++;
			}
		}
		
		return count<=2;
	}
	
	

}
