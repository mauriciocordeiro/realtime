package scheduler;

import java.util.GregorianCalendar;

public class Task extends Thread implements Comparable<Task> {
	
	private Integer id;
	
	private Long computation;
	private Long deadline;
	private Long period;
	
	private Long relativeDeadline;
	
	public Task(Integer id, Long computation, Long deadline, Long period) {
		super();
		this.id = id;
		this.computation = computation;
		this.deadline = deadline;
		this.period = period;
	}

	public Task() {
		super();
	}

	public Integer getTaskId() {
		return id;
	}

	public void setTaskId(Integer id) {
		this.id = id;
	}

	public Long getComputation() {
		return computation;
	}

	public void setComputation(Long computation) {
		this.computation = computation;
	}

	public Long getDeadline() {
		return deadline;
	}

	public void setDeadline(Long deadline) {
		this.deadline = deadline;
	}

	public Long getPeriod() {
		return period;
	}

	public void setPeriod(Long period) {
		this.period = period;
	}
	
	public Long getRelativeDeadline() {
		return relativeDeadline;
	}

	public void setRelativeDeadline(Long relativeDeadline) {
		this.relativeDeadline = relativeDeadline;
	}
	
	public int getSlackTime(int clk) {
//		return (relativeDeadline.intValue()-clk)-computation.intValue();
		return deadline.intValue() - computation.intValue();
	}

	@Override
	public void run() {
		long startTime = new GregorianCalendar().getTimeInMillis();
		long now = 0;
		do {
			System.out.print(id);
			now = new GregorianCalendar().getTimeInMillis();
		} while((now-startTime)<(computation*1000));
	}
	
	public Task clone() {
		return new Task(id, computation, deadline, period);
	}

	@Override
	public int compareTo(Task o) {
		if(this.period < o.period)
			return -1;
		if(this.period > o.period)
			return 1;
		return 0;
	}
	
	public int compareDeadlineTo(Task o) {
		if(this.deadline < o.deadline)
			return -1;
		if(this.deadline > o.deadline)
			return 1;
		return 0;
	}
	
	public int compareComputationTo(Task o) {
		if(this.computation < o.computation)
			return -1;
		if(this.computation > o.computation)
			return 1;
		return 0;
	}
	
	public int compareRelativeDeadlineTo(Task o) {
		if(this.relativeDeadline < o.relativeDeadline)
			return -1;
		if(this.relativeDeadline > o.relativeDeadline)
			return 1;
		return 0;
	}
	
	public int compareSlackTimeTo(Task o, int clk) {
		if(this.getSlackTime(clk) < o.getSlackTime(clk))
			return -1;
		if(this.getSlackTime(clk) > o.getSlackTime(clk))
			return 1;
		return 0;
	}
	
	@Override
	public String toString() {
		return "[id:"+getTaskId()+", period:"+getPeriod()+", computation:"+getComputation()+
				", deadline:"+getDeadline()+", relative:"+getRelativeDeadline()+"]";
	}

}
