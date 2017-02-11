/**
 * Launch screen for the JGradeBook application.
 * JGradeBook is a simple spreadsheet program that 
 * allows academic course data to be viewed and modified. 
 * 
 * The launch screen prompts the user to select course
 * to view from a dropdown list. All course files must 
 * be located in the /courses directory. 
 * 
 * CSCIE-10B
 * Final Project
 * Spring 2016
 * 
 * @author Jon Janelle
 * @version Last modified 4/30/16
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class JGradeBook extends JFrame{

	private JComboBox courseSelect;
	private JPanel selectionPanel;
	private JButton openButton;
	private JButton newButton;
	private JButton exitButton;
	private JLabel title;
	private Font titleFont = new Font("TimesRoman", Font.BOLD, 24);
	private Font labelFont = new Font("TimesRoman", Font.BOLD, 18);

	/**
	 * Open the course selection screen.
	 * Allows the user to select which course
	 * should be viewed in the data table.
	 */
	public JGradeBook()
	{
		initSelectionBox();
		initSelectionPanel();
	
		title = new JLabel("Select Course to Open");
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setFont(titleFont);
		title.setForeground(Color.blue);
		
		this.add(selectionPanel,BorderLayout.CENTER);
		this.add(title, BorderLayout.NORTH);
		this.add(new JLabel("  "), BorderLayout.EAST);
		this.add(new JLabel("  "), BorderLayout.WEST);
		this.add(new JLabel("  "), BorderLayout.SOUTH);
		this.setSize(360, 300);
		this.setTitle("JGradebook Launch");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}
	
	/**
	 * Initialize the course selection combo box.
	 * Items in the list are the file names in the
	 * directory "\courses"
	 */
	private void initSelectionBox()
	{
		File courseDir = new File("courses");
		File[] courseFiles = courseDir.listFiles();
		String[] courseNames = new String[courseFiles.length];
		for (int i = 0; i < courseFiles.length; i++){
			String cName = courseFiles[i].toString().split("\\\\")[1];
			courseNames[i] = cName.split("\\.")[0];
		}
		courseSelect = new JComboBox(courseNames);
		courseSelect.setFont(labelFont);
		
	}
	
	/**
	 * Initialize the panel containing the course
	 * selection combo box, open button, and exit 
	 * button. 
	 */
	private void initSelectionPanel()
	{
		selectionPanel = new JPanel(new GridLayout(5,1));
		openButton = new JButton("Open Course");
		openButton.setFont(labelFont);
		openButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new TableGUI(courseSelect.getSelectedItem().toString());
				dispose();
			}
		});
		
		newButton = new JButton("Create New Course");
		newButton.setFont(labelFont);
		newButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new AddCourseGUI();
				dispose();
			}
		});
		
		exitButton = new JButton("Exit");
		exitButton.setFont(labelFont);
		exitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		
		selectionPanel.add(courseSelect);
		selectionPanel.add(new JLabel(""));
		selectionPanel.add(openButton);
		selectionPanel.add(newButton);
		selectionPanel.add(exitButton);
	}
	
	public void createNewCourse()
	{
		
	}
	
	/**
	 * Create new launch window
	 * @param args Command line args not used
	 */
	public static void main(String[] args) {
		new JGradeBook();
	}
}
