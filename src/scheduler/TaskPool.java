package scheduler;

import java.util.ArrayList;
import java.util.Collections;

public class TaskPool extends ArrayList<Task> {
 
	private static final long serialVersionUID = 1L;
	
	public int getLastId() {
		if(size()==0)
			return -1;
		else {
			Task last = get(size()-1);
			return last.getTaskId();
		}
	}
	
	public void sort() {
		Collections.sort(this);
	}
	
	public Task getLowestPeriod() {
		TaskPool tp = (TaskPool)this.clone();
		tp.sort();
		
		return tp.get(0);
	}
	
	public Task removeLowestPeriod() {
		Task task = null;
		if(size()==0)
			return task;
		
		task = get(0);
		for (Task t : this) {
			if(t.compareTo(task)<=0) {
				task = t;
			}
		}
		
		remove(task);
		return task;
	}
	
	public Task removeLowestDeadline() {
		Task task = null;
		if(size()==0)
			return task;
		
		task = get(0);
		for (Task t : this) {
			if(t.compareDeadlineTo(task)<=0) {
				task = t;
			}
		}
		
		remove(task);
		return task;
	}
	
	public Task removeLowestRelativeDeadline() {
		Task task = null;
		if(size()==0)
			return task;
		
		task = get(0);
		for (Task t : this) {
			if(t.compareRelativeDeadlineTo(task)<=0) {
				task = t;
			}
		}
		
		remove(task);
		return task;
	}
	
	public Task removeLowestComputation() {
		Task task = null;
		if(size()==0)
			return task;
		
		task = get(0);
		for (Task t : this) {
			if(t.compareComputationTo(task)<=0) {
				task = t;
			}
		}
		
		remove(task);
		return task;
	}
	
	public Task removeLowestSlackTime(int clk) {
		Task task = null;
		if(size()==0)
			return task;
		
		task = get(0);
		for (Task t : this) {
			if(t.compareSlackTimeTo(task, clk)<=0) {
				task = t;
			}
		}
		
		remove(task);
		return task;
	}
	
	public Task dequeue() {
		if(size()>0)
			return remove(0);
		return null;
	}
	
	public void enqueue(Task task) {
		add(task);
	}
	
	public void undoDequeue(Task task) {
		add(0, task);
	}
	
	@Override
	public String toString() {
		String str = "";
		
		for(Task t : this) {
			str += "[id:"+t.getTaskId()+", period:"+t.getPeriod()+", computation:"+t.getComputation()+
					", deadline:"+t.getDeadline()+", relative:"+t.getRelativeDeadline()+"]\n";
		}
		
		return str;
	}

}
 