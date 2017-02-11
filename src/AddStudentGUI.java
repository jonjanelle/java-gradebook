import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * Should provide option to select from students who already have files
 * in students folder OR to create new student.
 *
 * A JComboBox allows user to select from a list of existing students. The
 * box initially is set to an empty string. If the add button is pressed
 * and an existing student is selected, attempt to add this student to course
 *  
 * CSCIE-10B
 * Final Project
 * Spring 2016
 * @author Jon Janelle
 * @version Modified 5/1/2016
 */
public class AddStudentGUI extends JFrame{

	private JPanel newPanel;
	private JPanel existingPanel;
	private JPanel mainPanel; //outermost panel container
	private Course course;
	private DefaultTableModel model;
	private JLabel fNameLabel;
	private JLabel lNameLabel;
	private JTextField fNameField;
	private JTextField lNameField;
	private JButton addButton;
	private JButton exitButton;
	private JLabel comboLabel;
	private JComboBox<String> studentList;
	private ArrayList<Student> allStudents;
	private Font titleFont = new Font("TimesRoman",Font.BOLD,22);
	private Vector<Vector> prev;

	/**
	 * Initialize a window that allows a user to add a new student to a 
	 * course. When a new student is added, both the course and table model
	 * are updated
	 * 
	 * @param course The course to which a student will be added
	 * @param tm The table model for the JTable that displays the course data
	 */
	public AddStudentGUI(Course course, DefaultTableModel tm, Vector<Vector> prev)
	{
		this.prev = prev;
		model = tm;
		this.course = course;

		allStudents = CourseCreator.getAllStudents(); //get all students for whom data files exist in students directory

		initExistingPanel(); //create list of existing students
		initNewPanel(); 	 //create area to enter new student info


		//Top cell of grid is existingPanel, bottom cell is newPanel and confirmation buttons
		mainPanel = new JPanel(new GridLayout(2,1));
		mainPanel.add(existingPanel);
		mainPanel.add(newPanel);
		
		//Add all components to main frame and make visible
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(new JLabel(" "), BorderLayout.SOUTH);
		this.add(new JLabel(" "), BorderLayout.WEST);
		this.add(new JLabel(" "), BorderLayout.EAST);
		this.setTitle("Add New Student");
		this.setSize(350, 300);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

	/**
	 * Create panel containing fields to enter the name
	 * of a new student. Also contains a submission button
	 * and an exit button.
	 */
	private void initNewPanel()
	{

		newPanel = new JPanel(new GridLayout(4,2));

		fNameLabel = new JLabel("First Name");
		fNameLabel.setHorizontalAlignment(JLabel.CENTER);
		lNameLabel = new JLabel("Last Name");
		lNameLabel.setHorizontalAlignment(JLabel.CENTER); 

		fNameField = new JTextField();
		lNameField = new JTextField();

		addButton = new JButton("Add Student");
		exitButton = new JButton("Exit");

		addButton.addActionListener(new ButtonListener());
		exitButton.addActionListener(new ButtonListener());

		newPanel.add(fNameLabel);
		newPanel.add(lNameLabel);

		newPanel.add(fNameField);
		newPanel.add(lNameField);

		newPanel.add(new JLabel(""));
		newPanel.add(new JLabel(""));

		newPanel.add(addButton);
		newPanel.add(exitButton);
	}

	/**
	 * Create section in which user can choose from existing students
	 * that have data files in the "\students" directory
	 * The list text is initially set to an empty string
	 */
	private void initExistingPanel()
	{
		ArrayList<String> stuNameList = new ArrayList<String>();
		stuNameList.add("");
		for (Student s:allStudents){
			stuNameList.add(s.getLName()+", "+s.getFName());
		}
		
		Collections.sort(stuNameList);
		Object[] names = stuNameList.toArray(); //Sort names in alphabetical order
		
		studentList = new JComboBox(names); 

		comboLabel = new JLabel("Select Existing Student");
		comboLabel.setHorizontalAlignment(JLabel.CENTER);
		comboLabel.setForeground(Color.BLUE);
		comboLabel.setFont(titleFont);

		existingPanel = new JPanel(new BorderLayout());
		JPanel selectionPanel = new JPanel(new BorderLayout());
		JPanel innerPanel = new JPanel(new GridLayout(3,1));
		innerPanel.add(comboLabel);
		innerPanel.add(studentList);

		selectionPanel.add(new JLabel(" "),BorderLayout.NORTH);
		selectionPanel.add(innerPanel,BorderLayout.CENTER);
		existingPanel.add(selectionPanel,BorderLayout.CENTER);

		//create text heading for the new student creation area
		JLabel newLabel = new JLabel("Create New Student");
		newLabel.setFont(titleFont);
		newLabel.setForeground(Color.BLUE);
		newLabel.setHorizontalAlignment(JLabel.CENTER);
		existingPanel.add(newLabel,BorderLayout.SOUTH);
	}

	/**
	 * Process the addition of a student to the class. 
	 * Either add an existing student from list or create
	 * a new student. A file is not created for new students
	 * created. 
	 * 
	 */
	private void processAddStudent()
	{
		Vector<String> v = new Vector<String>();

		if (studentList.getSelectedIndex()!=0){ //An existing student has been selected
			if (!processAddExisting(v)) 
				return; 						//end method if student already in course
		}
		
		else { //If no selection made from from dropdown list, then make new student
			processAddNew(v);
		}
		
		model.addRow(v); 			//add new row to table model
		
		prev.add(new Vector(v));
		
	}
	
	/**
	 * Process the addition of a new student to the data table.
	 * Adds the student to the course object
	 * @param v A vector that will hold the contents of the new student row
	 */
	private void processAddNew(Vector<String> v)
	{
		String fName = fNameField.getText();
		String lName = lNameField.getText();
		if (fName.equals("")||lName.equals("")) { 
			JOptionPane.showMessageDialog(null, "Please provide both a first and last name.");
			return; // do nothing if first or last name missing
		}
		
		Student newStu = new Student(fName,lName,findNextStuID());
		for (Assignment asn : course.getAssignments()){ //Add all course assignments to new student
			newStu.addScore(new Score(asn.getID(),-1)); //and set each to have no grade (-1)
		}
		
		course.addStudent(newStu);
		
		v.add((lName+", "+fName)); //This currently assumes name and ID # are first two columns.
		v.add(""+newStu.getID());
		for (int i = 0; i < course.numAssignments(); i++){ //assumed that assignments are in order.
			v.add("");	//Grade for each assignment starts empty
		}		
	}
	
	/**
	 * Find the next available unused student ID number
	 * @return An unused integer student id number
	 */
	private int findNextStuID()
	{
		File[] stuFiles = new File("students").listFiles();
		int maxID = 0;
		for (int i = 0; i < stuFiles.length;i++){
			String sID = stuFiles[i].toString().split("\\\\")[1];
			sID = sID.split("\\.")[0];
			maxID = Math.max(maxID, Integer.parseInt(sID));
		}
		return maxID+1;
	}
	
	/**
	 * Add a student for whom a data file already exits to course.
	 * Course roster checked to make sure that student not already in class first 
	 * @param v A vector that will contain the data for the new student row
	 * @return true if existing student added, false if student already in course
	 */
	private boolean processAddExisting(Vector<String> v)
	{
		Student selectedStu = new Student();
		String selectedName = (String)studentList.getSelectedItem();
		String fName = selectedName.split(",")[1].trim();
		String lName = selectedName.split(",")[0].trim();
		
		//First get which student has been selected
		for (Student s: allStudents){
			if (s.getFName().equals(fName) && s.getLName().equals(lName)){
				selectedStu = s;
				break;
			}
		}
		
		//Next check whether this student is already in the course
		for (Student s: course.getStudents()){
			if (s.equals(selectedStu)) { 
				return false; //exit method if selected student aleady in course
			}
		}
		//If not, add student to course and construct new table row
		course.addStudent(selectedStu);
		
		v.add(selectedStu.getLName()+", "+selectedStu.getFName()); //Add student name to vector
		
		//add student scores for each assignment in course, if they exist
		double nextScore;
		for (int i =0; i < course.numAssignments(); i++){
			nextScore = selectedStu.getScore(course.getAssignments().get(i).getID()); //-1 if score not found
			if (nextScore > 0)
				v.add(""+nextScore);
			else
				v.add("");
		}
		return true; 
	}
	
	/**
	 * React to button presses. Either exits
	 * the frame or directs the program to add a new student
	 *
	 */
	class ButtonListener implements ActionListener 
	{
		public void actionPerformed(ActionEvent e){
			if (e.getActionCommand().equals("Exit")) { 
				dispose(); 
			}	
			else if (e.getActionCommand().equals("Add Student")){
				processAddStudent();
			}
		}
	}
}
