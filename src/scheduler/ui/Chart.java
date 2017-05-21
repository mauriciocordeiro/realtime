package scheduler.ui;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import scheduler.Task;
import scheduler.TaskPool;

public class Chart extends JPanel {
	
	private final int MARGIN = 10;
	private static final int SIZE = 10;
	public static final Color[] colors = {Color.YELLOW, Color.CYAN, Color.RED, Color.GREEN, Color.MAGENTA};
	
	private int pointer;
	
	public Chart() {
		pointer = MARGIN;
		
		//chart label
		int x = 10;
		for (int i=0; i<colors.length; i++) {
			JPanel p = new JPanel();
			p.setBorder(new LineBorder(new Color(0, 0, 0)));
			p.setBackground(colors[i]);
			p.setBounds(x, 10, 10, 10);
			
			JLabel label = new JLabel(i+"");
			label.setBounds(x+12, 10, 25, 14);
			
			add(p);
			add(label);
			
			x+=30;
		}
	}
	
	public void addTasks(TaskPool tasks) {
		for (Task task : tasks) {
			JPanel t = new JPanel();
			t.setBorder(new LineBorder(new Color(0, 0, 0)));
			t.setBackground(colors[task.getTaskId()]);
			t.setBounds(pointer, getY(), SIZE, SIZE);
			
			pointer += SIZE;
			add(t);
		}
		
		repaint();
	}
	
	public void addTask(Task task) {
		JPanel t = new JPanel();
		if(task==null)
			t.setBorder(new LineBorder(new Color(220,220,220)));
		else
			t.setBorder(new LineBorder(new Color(0, 0, 0)));
		t.setBackground(task==null ? Color.WHITE : colors[task.getTaskId()]);
		t.setBounds(pointer, getY(), SIZE, SIZE);
		pointer += SIZE;
		add(t);
		
		repaint();
	}
	
	public void addDeadline(Task task) {
		JPanel t = new JPanel();
		t.setBorder(new LineBorder(new Color(255, 0, 0)));
		t.setBounds(pointer-1, getY()-(SIZE*2), 1, SIZE*2);
		
		JLabel l = new JLabel(task.getTaskId()+"");
		l.setBounds(pointer, getY()-(SIZE*2)-7, 14, 14);
		
		add(t);
		add(l);
		repaint();
	}
	
	public void enqueueTask(Task task) {
		
		JPanel t = new JPanel();
		t.setBorder(new LineBorder(new Color(0, 0, 255)));
		t.setBounds(pointer-1, getY(), 1, SIZE*2);
		
		JLabel l = new JLabel(task.getTaskId()+"");
		l.setBounds(pointer-3, getY()+(SIZE*2), 14, 14);
		
		add(t);
		add(l);
		repaint();
		
	}

}
