/**
 * GUI to create a new course file. User is prompted
 * to enter a course name and ID number, and then 
 * a new course data file is created in the "\courses" 
 * directory and a table view is opened. All courses
 * begin with no students and no assignments. New
 * students and assignments can be added from within the 
 * table view. 
 * 
 * CSCIE-10B
 * Final Project
 * Spring 2016
 * @author Jon Janelle
 * @version Modified 5/1/2016
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;


public class AddCourseGUI extends JFrame{

	private JPanel newPanel;
	private JPanel mainPanel; //outermost panel container
	private JPanel buttonPanel;
	private JLabel titleLabel;
	private JLabel nameLabel;
	private JLabel idLabel;
	private JTextField nameField;
	private JTextField idField;
	private JButton addButton;
	private JButton exitButton;

	private Font titleFont = new Font("TimesRoman",Font.BOLD,22);

	/**
	 * Initialize a new add assignment window. 
	 * @param course A course object
	 * @param tm A table model whose data matches course
	 * @param prev A vector of vectors containing previous state of the data table
	 */
	public AddCourseGUI() {


		initNewPanel(); 	 //create area to enter new student info
		initButtonPanel();

		titleLabel = new JLabel("Create New Course");
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
		this.setTitle("Add New Course");
		this.pack();

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);	

	}

	private void initNewPanel()
	{
		nameLabel = new JLabel("Course Name:   ");
		nameLabel.setHorizontalAlignment(JLabel.RIGHT);
		idLabel = new JLabel("Course ID:   ");
		idLabel.setHorizontalAlignment(JLabel.RIGHT);

		nameField = new JTextField();
		nameField.setHorizontalAlignment(JLabel.LEFT);
		idField = new JTextField();
		idField.setHorizontalAlignment(JLabel.LEFT);

		newPanel = new JPanel(new GridLayout(4,2));

		newPanel.add(new JLabel(""));
		newPanel.add(new JLabel(""));
		newPanel.add(nameLabel);
		newPanel.add(nameField);
		newPanel.add(idLabel);
		newPanel.add(idField);

		newPanel.add(new JLabel(""));
		newPanel.add(new JLabel(""));

	}

	/**
	 * Create a new course file based on text field input.
	 * If the course name entered already matches a file in 
	 * the "\courses" directory, then no new course is created.
	 */
	public void addCourse()
	{
		//check whether course name available
		File dir = new File("courses");
		for (File f: dir.listFiles()){
			String fileName = f.getName().split("\\.")[0].trim().toLowerCase();
			if (fileName.equals(nameField.getText().trim().toLowerCase())){
				JOptionPane.showMessageDialog(null, "Error: Course name already in use");
				return;
			}
		}
		
		try{
			PrintWriter out = new PrintWriter("courses\\"+nameField.getText()+".txt");
			out.println("couseid "+idField.getText());
			out.println("studentid ");
			out.println("assignmentid ");
			out.close();
		}
		catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "Error creating course file.");
		}
		
		new TableGUI(nameField.getText());
		dispose();
	}
	
	/**
	 * Initialize the panel that contains the add and exit
	 * buttons, and then add the buttons. 
	 */
	private void initButtonPanel()
	{
		buttonPanel = new JPanel(new FlowLayout());
		addButton = new JButton("Add Course");
		exitButton = new JButton("   Exit   ");
		addButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (nameField.getText().length()>0 && idField.getText().length()>0){
					addCourse();
				}
				else
					JOptionPane.showMessageDialog(null, "Error: Both course name and ID are required.");
			}
			
		});
		//Return to course selection launch screen if exit pressed
		exitButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new JGradeBook();
				dispose();
			}
		});

		buttonPanel.add(new JLabel());
		buttonPanel.add(addButton);
		buttonPanel.add(exitButton);
		buttonPanel.add(new JLabel());
	}
}
