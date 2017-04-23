package scheduler;

import java.util.ArrayList;

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

}
 