package scheduler;

import java.util.GregorianCalendar;

public class Task extends Thread {
	
	private Integer id;
	
	private Long computation;
	private Long deadline;
	private Long period;
	
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

	@Override
	public void run() {
				
		long startTime = new GregorianCalendar().getTimeInMillis();
		long now = 0;
		do {
			System.out.println("\t\t"+id+". running!");
			now = new GregorianCalendar().getTimeInMillis();
		} while((now-startTime)<(computation*1000));
		
	}

}
