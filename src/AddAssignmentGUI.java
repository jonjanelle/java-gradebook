/**
 * GUI window that allows a user to add a new assignment to a course.
 * To add an assignment, a name and integer identification number must
 * be provided.
 * 
 * CSCIE-10B
 * Final Project
 * Spring 2016
 * 
 * @author Jon Janelle
 * @version Modified 5/1/2016
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class AddAssignmentGUI extends JFrame{
	
	private JPanel newPanel;
	private JPanel mainPanel; //outermost panel container
	private JPanel buttonPanel;
	
	private Course course;
	private DefaultTableModel tm;
	
	private JLabel titleLabel;
	private JLabel nameLabel;
	private JLabel idLabel;
	private JLabel descLabel;
	
	private JTextField nameField;
	private JTextField idField;
	private JTextField descField;
	
	private JButton addButton;
	private JButton exitButton;

	private Font titleFont = new Font("TimesRoman",Font.BOLD,22);
	
	private Vector<Vector> prev;

	/**
	 * Initialize a new add assignment window. 
	 * @param course A course object
	 * @param tm A table model whose data matches course
	 * @param prev A vector of vectors containing previous state of the data table
	 */
	public AddAssignmentGUI(Course course, DefaultTableModel tm, Vector<Vector> prev)
	{
		this.tm = tm;
		this.prev = prev;
		this.course = course;

		initNewPanel(); 	 //create area to enter new student info
		initButtonPanel();
		
		titleLabel = new JLabel("Create New Assignment");
		titleLabel.setFont(titleFont);
		titleLabel.setForeground(Color.blue);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		
		//Text header north, 3x2 grid panel center, buttons south
		mainPanel = new JPanel(new BorderLayout());

		mainPanel.add(titleLabel,BorderLayout.NORTH);
		mainPanel.add(newPanel,BorderLayout.CENTER);
		mainPanel.add(buttonPanel,BorderLayout.SOUTH);
		
		
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(new JLabel("  "), BorderLayout.NORTH);
		this.add(new JLabel("  "), BorderLayout.SOUTH);
		this.add(new JLabel("  "), BorderLayout.WEST);
		this.add(new JLabel("  "), BorderLayout.EAST);
		this.setTitle("Add New Assignment");
		this.pack();
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);	
		
	}
	
	/**
	 * Initialize the panel that contains the text
	 * fields in which a user can enter the details
	 * about a new assignment
	 */
	private void initNewPanel()
	{
		nameLabel = new JLabel("Assignment name:   ");
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);
		idLabel = new JLabel("Assignment ID:   ");
		idLabel.setHorizontalAlignment(JLabel.RIGHT);
		descLabel = new JLabel("Description:   ");
		descLabel.setHorizontalAlignment(JLabel.RIGHT);
		
		
		nameField = new JTextField();
		nameField.setHorizontalAlignment(JLabel.LEFT);
		idField = new JTextField();
		idField.setHorizontalAlignment(JLabel.LEFT);
		descField = new JTextField();
		descField.setHorizontalAlignment(JLabel.LEFT);
		
		newPanel = new JPanel(new GridLayout(5,2));

		newPanel.add(new JLabel(""));
		newPanel.add(new JLabel(""));
		newPanel.add(nameLabel);
		newPanel.add(nameField);
		newPanel.add(idLabel);
		newPanel.add(idField);
		newPanel.add(descLabel);
		newPanel.add(descField);
		newPanel.add(new JLabel(""));
		newPanel.add(new JLabel(""));
		
	}
	
	/**
	 * Initialize the panel that contains the add and exit
	 * buttons, and then add the buttons. 
	 */
	private void initButtonPanel()
	{
		buttonPanel = new JPanel(new GridLayout(1,2));
		addButton = new JButton("Add Assignment");
		exitButton = new JButton("Exit");
		addButton.addActionListener(new ButtonListener());
		exitButton.addActionListener(new ButtonListener());

		buttonPanel.add(addButton);
		buttonPanel.add(exitButton);
	}
	
	/**
	 * Add a new assignment to the course and update the data 
	 * table model. 
	 */
	private void addAssignment()
	{
		try{
			int idNum = Integer.parseInt(idField.getText());
		}
		catch (Exception ex){
			JOptionPane.showMessageDialog(null, "Enter an integer ID number.");
			return;
		}
		
		if (nameField.getText().trim().equals("")){
			JOptionPane.showMessageDialog(null, "Error: Assignment name required.");
			return;
		}
		else if (!nameAvailable(nameField.getText())){
			JOptionPane.showMessageDialog(null, "Error: Name is already in use.");
			return;
		}
		if (idAvailable(idField.getText())==false){
			return;
		}
		
		
		Assignment a = new Assignment(nameField.getText(),
									  Integer.parseInt(idField.getText()),
									  descField.getText());
	
		course.addAssignment(a);
		
		for (Student s: course.getStudents()){ //add assignment to each student
			s.addScore(new Score(a.getID(),-1)); //start with empty score
		}
		
		for (int i = 0; i < tm.getRowCount();i++){
			prev.get(i).add("-1");
		}
		
		tm.addColumn(nameField.getText());
	}
	
	/**
	 * Check whether a specified assignment id number is
	 * available for use. 
	 * @param id An id number
	 * @return true if id is available, false otherwise
	 */
	public boolean idAvailable(String id)
	{
		for (File f: new File("assignments").listFiles())
		{
			if (f.getName().split("\\.")[0].equals(id)){
				int x = JOptionPane.showConfirmDialog(null, "ID already in use.\nShow list of used ID numbers?");
				if (x == 0){
					showUsedIDs();
				}
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Determine whether a given assignment name is 
	 * already in use by comparing it to the table
	 * headers.
	 * 
	 * @param name An assignment name
	 * @return true if name is in use, false otherwise.
	 */
	public boolean nameAvailable(String name)
	{
		name = name.trim().toLowerCase();
		
		for (int i = 0; i < tm.getColumnCount(); i++){
			if (name.equals(tm.getColumnName(i).trim().toLowerCase())){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Show a message dialog box that contains a list of all
	 * assignment id numbers that are in use. 
	 */
	public void showUsedIDs()
	{
		String idList = "";
		int added = 0;
		for (File f: new File("assignments").listFiles())
		{
			idList += f.getName().split("\\.")[0]+",  ";
			added +=1;
			if (added%5==0) idList +="\n";
		}
		idList = idList.substring(0,idList.length()-3);
		JOptionPane.showMessageDialog(null, "Used Assignment IDs:\n"+idList);
	}
	
	/**
	 * React to and process button presses
	 */
	class ButtonListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e){
			if (e.getActionCommand().equals("Exit")) { dispose(); }	
			else if (e.getActionCommand().equals("Add Assignment")){
				addAssignment();
			}
		}
	}	
}