/**
 * Create a new spreadsheet GUI window based on an 
 * academic Course object. A editable JTable of students, 
 * assignments, and scores is displayed. Buttons are also
 * available to add a student, add an assignment, or to 
 * view basic summary statistics about the data shown in the 
 * table
 * 
 * CSCIE-10B
 * Final Project
 * Spring 2016
 * 
 * @author Jon Janelle
 * @version Modified 5/1/2016
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.util.*;

public class TableGUI extends JFrame{
	private Course course;
	private JTable dataTable;
	private DefaultTableModel tm;
	private JScrollPane tableScrollPane;
	private String[][] scoreMatrix;    
	private Vector<Vector> scoreVector; //holds most recent version of table data
	private Vector<Vector> prevScoreVector; //holds table data before last edit - used for data validation
	private String[] assignHeaders;
	private JButton newStuButton;
	private JButton newAssignButton;
	private JButton summaryButton;
	private JButton exitButton;
	private JLabel mainTitle;
	private JLabel searchLabel;
	private JPanel tablePanel;
	private JPanel titlePanel;
	private JPanel searchPanel;
	private JMenuBar menuBar;
	private JTextField searchField;
	private Font titleFont = new Font("TimesRoman", Font.BOLD, 28);

	/**
	 * Create a new course data table based on the course name.
	 * The course name is the same as the name of the course 
	 * data file in "\courses", which is used to create the table.
	 * @param courseName The course name
	 */
	public TableGUI(String courseName)
	{
		this(new CourseCreator(courseName).getCourse());	
	}

	/**
	 * Create a new data table based on a course object
	 * @param course A course object
	 */
	public TableGUI(Course course){
		this.course = course;
		initTable();		//create scoreMatrix, a 2d array of scores
		initMenu();			//setup menu bar
		initTablePanel();	//Add the table to a panel
		initSearchPanel();	//setup search bar panel
		initTitlePanel();	//setup top title banner panel

		this.setTitle("JGradebook");
		this.setJMenuBar(menuBar);
		this.add(tablePanel,BorderLayout.CENTER);
		this.add(titlePanel,BorderLayout.NORTH);
		//this.add(searchPanel,BorderLayout.SOUTH);

		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);	
	}

	/**
	 *	Initialize the title area to display which course is
	 *	currently being viewed. The title area contains buttons
	 *  to add a new student, add a new assignment, show summary 
	 *  statistics, and exit the program
	 */
	private void initTitlePanel()
	{
		//Initialize buttons
		newStuButton = new JButton("Add Student");
		newAssignButton = new JButton("Add Assignment");
		summaryButton = new JButton("Summary Stats");
		exitButton = new JButton("Exit");
	
		//Attach listeners to buttons
		newStuButton.addActionListener(new ButtonListener());
		newAssignButton.addActionListener(new ButtonListener());
		summaryButton.addActionListener(new ButtonListener());
		exitButton.addActionListener(new ButtonListener());

		titlePanel = new JPanel(new FlowLayout());
		mainTitle = new JLabel(course.getName()+" Gradebook");
		mainTitle.setForeground(Color.blue);
		mainTitle.setHorizontalAlignment(JLabel.LEFT);
		mainTitle.setFont(titleFont);

		titlePanel.add(mainTitle,BorderLayout.CENTER);
		titlePanel.add(new JLabel("  "));
		titlePanel.add(newStuButton);
		titlePanel.add(newAssignButton);
		titlePanel.add(summaryButton);
		titlePanel.add(exitButton);
	}

	/**
	 * Initialize the panel containing
	 * the student search text field
	 */
	private void initSearchPanel()
	{
		searchField = new JTextField();
		searchField.setHorizontalAlignment(JLabel.CENTER);
		searchField.setText("Enter name here");
		searchLabel = new JLabel("Search by Name: ");
		searchLabel.setHorizontalAlignment(JLabel.RIGHT);
		searchPanel = new JPanel();
		searchPanel.add(searchLabel);
		searchPanel.add(searchField);
		searchPanel.add(new JLabel(" "));
	}

	
	/**
	 * Initialize the menu bar. User can choose to 
	 * switch the current course being viewed, save 
	 * changes made to the course data, or quit
	 * the program. 
	 * 
	 */
	private void initMenu()
	{
		JMenu fileMenu = new JMenu("File");

		menuBar = new JMenuBar();
		menuBar.add(fileMenu);

		JMenuItem switchItem = new JMenuItem("Switch Course");
		switchItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				new JGradeBook();
				dispose();
			}
		});

		JMenuItem saveItem = new JMenuItem("Save Data");
		saveItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				new CourseSaver(course);
			}
		});

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				System.exit(0);	
			}			
		});

		fileMenu.add(switchItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);
	}

	/**
	 * Add the dataTable to a panel and format the panel
	 */
	private void initTablePanel()
	{		
		//add table to scroll pane
		tableScrollPane = new JScrollPane(dataTable);
		tableScrollPane.setBorder(BorderFactory.createLineBorder(Color.black));

		//create table panel, add table scroll pane
		//and lightly format
		tablePanel = new JPanel(new BorderLayout());
		tablePanel.add(tableScrollPane, BorderLayout.CENTER);
		//Add some left/right padding spaces
		tablePanel.add(new JLabel("  "), BorderLayout.WEST);
		tablePanel.add(new JLabel("  "), BorderLayout.EAST);
	}

	/**
	 * initialize the data JTable
	 */
	private void initTable()
	{
		//Construct 2D scores matrix
		scoreMatrix = makeScoreMatrix();
		scoreVector = makeScoreVector(scoreMatrix);
		prevScoreVector = makeScoreVector(scoreMatrix);

		//construct array of column headers
		assignHeaders = getTableHeaders();

		tm = new DefaultTableModel(assignHeaders,0);
		dataTable = new JTable(tm); //use DefaultTableModel

		//Construct table by adding each row
		for (Vector<String> row : scoreVector){
			tm.addRow(row);
		}
		scoreVector = tm.getDataVector(); //scoreVector now references tm's data vector

		//Add change listener to table model to deal with data validation
		dataTable.getModel().addTableModelListener(new TableChangeListener());
		
		//Enable column sorting
		dataTable.setAutoCreateRowSorter(true); 
	}

	
	/**
	 * Construct an array of assignment names to be
	 * used as table headers
	 * @return An array of string assignment names
	 */
	public String[] getTableHeaders()
	{
		String[] headers = new String[course.numAssignments()+2];
		headers[0] = "Name";
		headers[1] = "ID #";
		for (int i = 2; i < headers.length; i++){
			headers[i] = course.getAssignments().get(i-2).getName();
		}
		return headers;
	}

	/**
	 * Get the ID number of the student whose row
	 * is selected
	 * @param row The selected row
	 * @return The student ID number for the selected row
	 */
	public String getRowStudentID(int row)
	{
		String sid = "";
		for (int i = 0; i < tm.getColumnCount(); i++){
			if (tm.getColumnName(i).equals("ID #")){
				sid = tm.getValueAt(row, i).toString();
				break;
			}
		}
		return sid;
	}
	
	/**
	 * Construct a matrix of student scores. Each
	 * column represents one assignment percent score, 
	 * and each row a student. 
	 * The first column contains student names
	 *  
	 * @return A 2-D array of percentage scores
	 */
	public String[][] makeScoreMatrix()
	{
		int numS = course.numStudents();
		int numA = course.numAssignments();

		String[][] scoreMat = new String[numS][numA+2];

		//first Set name column
		for (int i = 0; i < numS;i++){
			Student temp = course.getStudents().get(i);
			scoreMat[i][0] = temp.getLName()+", "+temp.getFName();
		}
		//set student id column
		for (int i = 0; i < numS;i++){
			Student temp = course.getStudents().get(i);
			scoreMat[i][1] = ""+temp.getID();
		}
		
		//Then fill in score columns for each assignment
		for (int i = 0; i < numA ; i++){ 
			Assignment a = course.getAssignments().get(i);
			for (int j = 0; j < numS; j++){
				Student s = course.getStudents().get(j);
				if (s.getScore(a.getID())>=0)
					scoreMat[j][i+2] = ""+s.getScore(a.getID()); 
				else
					scoreMat[j][i+2]="";
			}
		}
		return scoreMat;
	}

	/**
	 * Create a Vector of Vectors to represent the course scores
	 * @param mat A matrix containing course score data
	 * @return A vector of vectors (i.e. 2D matrix) of scores
	 */
	public static Vector<Vector> makeScoreVector(String[][] mat)
	{
		Vector<Vector> scoreVec = new Vector<Vector>();
		Vector<String> currentRow;

		for (String[] row: mat){
			currentRow = new Vector<String>();
			for (int i = 0; i < row.length; i++){
				currentRow.add(row[i]);
			}
			scoreVec.add(currentRow);
		}
		return scoreVec;
	}

	/**
	 * Class to specify data validation when a table cell change is made.
	 * Changes to the "Name" and "ID #" columns are discarded. 
	 * Negative numbers or characters cannot be entered in percentage score cells. 
	 */
	private class TableChangeListener implements TableModelListener
	{
		/**
		 * Process changes to table cell values 
		 */
		public void tableChanged(TableModelEvent e) {
			if (e.getColumn()!=-1) {
				if (e.getType()!= 0){ tm.fireTableRowsUpdated(tm.getRowCount(), tm.getRowCount()); } //quit if the event is add row
				int row = e.getFirstRow();
				int column = e.getColumn();
				
				double newVal = -1;
				String stringNew;
				try { //exception thrown if non-numeric argument entered in a score cell	
					
					if (tm.getColumnName(column).equals("Name")||tm.getColumnName(column).equals("ID #")) { //revert to old value -- ID or name edit attempted
						throw new IllegalArgumentException();
					} else if (dataTable.getValueAt(row, column).toString().trim().equals(""))
					{
						scoreVector.get(row).set(column, ""); //If score deleted, allow blank.
						prevScoreVector.get(row).set(column,scoreVector.get(row).get(column));
						course.updateStudentScore(-1,getRowStudentID(row),tm.getColumnName(column));
						return;
					} else {
						newVal = Double.parseDouble(dataTable.getValueAt(row, column).toString()); //exception if non-numeric entered
						if (newVal < 0) { throw new IllegalArgumentException(); } //revert to old value if negative entered
						stringNew = "" + newVal;
					}
					
					//update course object
					course.updateStudentScore(newVal,getRowStudentID(row),tm.getColumnName(column));
					
					String valAtRC = tm.getValueAt(row, column).toString();
	
					if( valAtRC.equals(""+newVal) == false)  //to prevent table changed from being fired in an infinite loop.
					{	 
						tm.setValueAt(stringNew, row, column); 
						tm.fireTableCellUpdated(row, column);				
					}
				}
				catch (Exception ex){ //revert to old value -- non-numeric char entered	
					scoreVector.get(row).set(column,prevScoreVector.get(row).get(column));
				}
				//update prevScoreVector to reflect most recent valid state of table
				prevScoreVector.get(row).set(column,scoreVector.get(row).get(column)); 
			}
		}
	}
		
	
	/**
	 * Class to specify what actions should occur when a button is pressed.
	 */
	private class ButtonListener implements ActionListener 
	{ 
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand(); //Command holds the text of the button pushed

			if (command.equals("Add Student")){
				new AddStudentGUI(course, tm, prevScoreVector);
			}
			else if (command.equals("Add Assignment")){
				new AddAssignmentGUI(course, tm, prevScoreVector);
			}
			else if (command.equals("Summary Stats")){
				assignHeaders = getTableHeaders();
				new SummaryGUI(dataTable,course);

			}

			else if (command.equals("Exit")){
				System.exit(0);
			}
		}
	} 
}

