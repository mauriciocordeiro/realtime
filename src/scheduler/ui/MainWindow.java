package scheduler.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

import scheduler.EDF;
import scheduler.LST;
import scheduler.RM;
import scheduler.Task;
import scheduler.TaskPool;
import javax.swing.SwingConstants;

public class MainWindow extends JFrame {
	
	public static final int TOTAL_TASKS = 5;
	public static final long INTERVAL = 5000;
	
	public TaskPool taskPool = new TaskPool();

	private JPanel contentPane;
	private JTable tbTasks;
	private JRadioButton rdbRm;
	private JRadioButton rdbEdf;
	private JRadioButton rdbLst;
	
	public Chart chart;
	
	private String schedulerName;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public MainWindow() {
		setTitle("Scheduler");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTasks = new JLabel("Tasks");
		lblTasks.setBounds(10, 11, 46, 14);
		contentPane.add(lblTasks);
		
		tbTasks = new JTable();
		tbTasks.setBorder(new LineBorder(new Color(0, 0, 0)));
		tbTasks.setModel(new DefaultTableModel(new Object[][] {},
				new String[] {"Id.", "Tempo de Computa\u00E7\u00E3o", "Deadline", "Per\u00EDodo"}
			) {
				@Override
				public boolean isCellEditable(int row, int column) {
			       //all cells false
			       return false;
			    }
			});
		tbTasks.setBounds(10, 36, 518, 100);
		tbTasks.getColumnModel().getColumn(0).setPreferredWidth(20);
		contentPane.add(tbTasks);
		
		JButton btnAddTask = new JButton("+");
		btnAddTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTaskOnClick(arg0);
			}
		});
		btnAddTask.setToolTipText("Add a new task");
		btnAddTask.setBounds(538, 32, 46, 23);
		contentPane.add(btnAddTask);
		
		JButton btnDelTask = new JButton("-");
		btnDelTask.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				delTaskOnClick(arg0);
				
			}
		});
		btnDelTask.setToolTipText("Remove selected task");
		btnDelTask.setBounds(538, 66, 46, 23);
		contentPane.add(btnDelTask);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 147, 774, 4);
		contentPane.add(separator);
		
		rdbRm = new JRadioButton("RM");
		rdbRm.setBounds(20, 161, 55, 23);
		contentPane.add(rdbRm);
		
		rdbEdf = new JRadioButton("EDF");
		rdbEdf.setBounds(77, 161, 55, 23);
		contentPane.add(rdbEdf);
		
		rdbLst = new JRadioButton("LST");
		rdbLst.setBounds(134, 160, 55, 23);
		contentPane.add(rdbLst);
		
		ButtonGroup rbGroup = new ButtonGroup();
		rbGroup.add(rdbRm);
		rbGroup.add(rdbEdf);
		rbGroup.add(rdbLst);
		
		JScrollPane scrollPane = new JScrollPane(tbTasks);
		scrollPane.setBounds(10, 30, 518, 106);
		contentPane.add(scrollPane);
		
		JButton btnTest = new JButton("Run");
		btnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnTestOnClick(arg0);
			}
		});
		btnTest.setBounds(203, 161, 89, 23);
		contentPane.add(btnTest);
		
		JButton btnRandom = new JButton("}{");
		btnRandom.setToolTipText("Generate tasks");
		btnRandom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btnRandomOnClick(arg0);
			}
		});
		btnRandom.setBounds(538, 100, 46, 23);
		contentPane.add(btnRandom);
		
		createChart();
	}
	
	public void addTaskOnClick(ActionEvent e) {
		if(taskPool.size()>=TOTAL_TASKS) {
			JOptionPane.showMessageDialog(this, "Não é possível escalonar mais de "+TOTAL_TASKS+" tarefas.", "Alerta", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		TaskWindow tw = new TaskWindow(this, taskPool);
		tw.setLocationRelativeTo(this);
		tw.setVisible(true);
	}
	
	public void delTaskOnClick(ActionEvent e) {
		if(tbTasks.getSelectedRow()==-1) {
			JOptionPane.showMessageDialog(this, "Nenhuma linha selecionada.", "Alerta", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		taskPool.remove(tbTasks.getSelectedRow());
		reloadTable();
	}
	
	public void btnRandomOnClick(ActionEvent e) {
		taskPool = new TaskPool();
		
		int size = taskPool.size();
		for(int i=size; i<TOTAL_TASKS; i++) {
			int deadline = new Random().nextInt(9)+1;
			int period = new Random().nextInt(9)+1;
			int computation = new Random().nextInt(period);
			computation = computation==0 ? computation+1 : computation;
			
			Task t = new Task(taskPool.getLastId()+1, 
					Long.parseLong(computation+""), 
					Long.parseLong(deadline+""), 
					Long.parseLong(period+""));
			
			taskPool.add(t);
		}
		reloadTable();
	}
	
	public void btnTestOnClick(ActionEvent e) {
		createChart();
		
		if(taskPool.size()<1) {
			JOptionPane.showMessageDialog(this, "Número insuficiente de tarefas.", "Alerta", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(rdbRm.isSelected()) {
			schedulerName = "Rate Monotonic";
			
			JOptionPane.showMessageDialog(this, "No escalonamento RM, o deadline é igual ao período.", "Info", JOptionPane.INFORMATION_MESSAGE);
			for (Task task : taskPool)
				task.setDeadline(task.getPeriod());
			reloadTable();
			
			RM rm = new RM(taskPool, this);
			rm.start();
			
		} else if(rdbEdf.isSelected()) {
			schedulerName = "Earliest Deadline First";
			
			JOptionPane.showMessageDialog(this, "No escalonamento EDF, o deadline é igual ao período.", "Info", JOptionPane.INFORMATION_MESSAGE);
			for (Task task : taskPool)
				task.setDeadline(task.getPeriod());
			reloadTable();
			
			EDF edf = new EDF(taskPool, this);
			edf.start();
			
		} else if(rdbLst.isSelected()) {
			schedulerName = "LST";
			
			LST lst = new LST(taskPool, this);
			lst.start();
			
		} else {
			JOptionPane.showMessageDialog(this, "Nenhuma escalonador selecionado.", "Alerta", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
	}
	
	public void createChart() {
		if(chart!=null)
			contentPane.remove(chart);
		contentPane.repaint();
		
		chart = new Chart();
		chart.setBorder(new LineBorder(new Color(0, 0, 0)));
		chart.setBackground(Color.WHITE);
		chart.setBounds(10, 191, 774, 269);
		contentPane.add(chart);
	}
		
	public void reloadTable() {
		clearTable();
		for (Task task : taskPool) {
			Object[] row = {task.getTaskId(), 
					task.getComputation(), 
					task.getDeadline(), 
					task.getPeriod()};
			((DefaultTableModel)tbTasks.getModel()).addRow(row);
		}
	}
	
	private void clearTable() {
		if(tbTasks.getRowCount()>0) {
			for(int i=tbTasks.getRowCount()-1; i>=0; i--) {
				((DefaultTableModel)tbTasks.getModel()).removeRow(i);
			}			
		}
	}
}
