package scheduler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;

public class TaskWindow extends JDialog {
	
	private TaskPool pool;
	private MainWindow parent;

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public TaskWindow(MainWindow parent, TaskPool pool) {
		
		this.parent = parent;
		this.pool = pool;
		
		this.setModal(true);
		
		setResizable(false);
		setAlwaysOnTop(true);
		
		setBounds(100, 100, 190, 220);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTempoDeComputao = new JLabel("Tempo de computa\u00E7\u00E3o");
		lblTempoDeComputao.setBounds(10, 60, 120, 14);
		contentPane.add(lblTempoDeComputao);
		
		JSpinner spnComputation = new JSpinner();
		spnComputation.setModel(new SpinnerNumberModel(new Long(1), new Long(1), new Long(60), new Long(1)));
		spnComputation.setBounds(128, 57, 42, 20);
		contentPane.add(spnComputation);
		
		JLabel lblPerodo = new JLabel("Per\u00EDodo");
		lblPerodo.setBounds(82, 88, 48, 14);
		contentPane.add(lblPerodo);
		
		JSpinner spnPeriod = new JSpinner();
		spnPeriod.setModel(new SpinnerNumberModel(new Long(1), new Long(1), new Long(60), new Long(1)));
		spnPeriod.setBounds(128, 85, 42, 20);
		contentPane.add(spnPeriod);
		
		JLabel lblDeadline = new JLabel("Deadline");
		lblDeadline.setBounds(82, 116, 48, 14);
		contentPane.add(lblDeadline);
		
		JSpinner spnDeadline = new JSpinner();
		spnDeadline.setModel(new SpinnerNumberModel(new Long(1), new Long(1), new Long(60), new Long(1)));
		spnDeadline.setBounds(128, 113, 42, 20);
		contentPane.add(spnDeadline);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addTask((Long)spnComputation.getValue(), (Long)spnDeadline.getValue(), (Long)spnPeriod.getValue());		
			}
		});
		btnOk.setBounds(81, 155, 89, 23);
		contentPane.add(btnOk);
		
		JLabel lblTask = new JLabel("Task "+Integer.toString(pool.size()));
		lblTask.setHorizontalAlignment(SwingConstants.CENTER);
		lblTask.setBounds(10, 11, 160, 14);
		contentPane.add(lblTask);
	}
	
	private void addTask(Long computation, Long deadline, Long period) {
		pool.add(new Task(pool.size(), computation, deadline, period));
		parent.reloadTable();
		this.setVisible(false);
	}
}
