package scheduler;

import java.awt.Color;

import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;

import br.org.mac.midgard.util.Math;
import scheduler.ui.Chart;

public class Test {

	public static void main(String[] args) {
	
		//System.out.println(Math.lcm(true, 5,10,15,20,35));
		
		Chart chart = new Chart();
		chart.setBorder(new LineBorder(new Color(0, 0, 0)));
		chart.setBackground(Color.WHITE);
		chart.setBounds(10, 191, 774, 269);
		
		JOptionPane.showMessageDialog(null, chart, "Chart", JOptionPane.PLAIN_MESSAGE);

	}

}
