package scheduler;

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

public class MainWindow extends JFrame {
	
	public static final int TOTAL_TASKS = 5;
	
	public TaskPool taskPool = new TaskPool();

	private JPanel contentPane;
	private JTable tbTasks;
	private JRadioButton rdbRm;
	private JRadioButton rdbEdf;
	private JRadioButton rdbLst;

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
		setBounds(100, 100, 600, 500);
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
		separator.setBounds(10, 147, 574, 4);
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
		int size = taskPool.size();
		for(int i=size; i<TOTAL_TASKS; i++) {
			Task t = new Task(taskPool.getLastId()+1, 
					Long.parseLong((new Random().nextInt(59)+1)+""), 
					Long.parseLong((new Random().nextInt(59)+1)+""), 
					Long.parseLong((new Random().nextInt(59)+1)+""));
			
			taskPool.add(t);
		}
		reloadTable();
	}
	
	public void btnTestOnClick(ActionEvent e) {
		
		if(taskPool.size()<TOTAL_TASKS) {
			JOptionPane.showMessageDialog(this, "Número insuficiente de tarefas.", "Alerta", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		if(rdbRm.isSelected()) {
			
			
			
		} else if(rdbEdf.isSelected()) {
			
			
			
		} else if(rdbLst.isSelected()) {
			
			
			
		} else {
			JOptionPane.showMessageDialog(this, "Nenhuma escalonador selecionado.", "Alerta", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
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
