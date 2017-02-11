/**
 * Show summary statistics and a histogram about a particular 
 * student or assignment. The descriptive statistics displayed are
 * the mean, median, range, and standard deviation. Student and assignment
 * selections are made using combo boxes. The results are updated as soon
 * as a new selection is made. 
 * 
 * @author Jon Janelle
 * @version Modified 5/1/2016
 */
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

public class SummaryGUI extends JFrame{

	private JTable table;
	private String[] headers;
	private JPanel selectionPanel;
	private JPanel resultPanel;
	private JPanel histPanel;
	private JComboBox<String> assignments;
	private JComboBox<String> students;
	private JTextArea resultArea;
	private JLabel titleLabel;
	private JLabel stuLabel;
	private JLabel assignLabel;
	private JButton goButton;
	private JButton exitButton;
	private Font titleFont = new Font("TimesRoman", Font.BOLD, 24);
	private Font resFont = new Font("TimesRoman", Font.BOLD, 18);
	private Font iFont = new Font("TimesRoman", Font.ITALIC, 18);
	//private Course course;

	//Get summary stats about a particular student 
	//or assignment in a class
	public SummaryGUI(JTable dataTable, Course course)
	{
		table = dataTable;
		//	this.course = course;

		headers  = new String[dataTable.getColumnCount()-1]; // 
		headers[0] = ""; //initially have no assignment selected
		int nextPos = 1;
		for (int i = 0; i < dataTable.getColumnCount(); i++){ 
			if (!(table.getColumnName(i).equals("Name")||table.getColumnName(i).equals("ID #"))){
				headers[nextPos] = dataTable.getColumnName(i);
				nextPos++;
			}
		}

		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ButtonListener());

		initComboBoxes();
		initResultArea();

		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(selectionPanel,BorderLayout.CENTER);
		southPanel.add(new JLabel(" "), BorderLayout.SOUTH);

		this.add(southPanel,BorderLayout.SOUTH);
		this.setSize(500,500);
		this.setTitle("Summary Statistics");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		this.setVisible(true);
	}


	/**
	 * Initialize window that shows result statistics and 
	 * score histogram
	 *  
	 */
	private void initResultArea()
	{


		titleLabel = new JLabel("View Summary Statistics");
		titleLabel.setFont(titleFont);
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setForeground(Color.BLUE);
		this.add(titleLabel,BorderLayout.NORTH);

		resultPanel = new JPanel(new GridLayout(2,1));

		JPanel centerResArea = new JPanel(new FlowLayout());

		resultArea = new JTextArea();
		resultArea.setSize(30,10);
		resultArea.setEditable(false);
		resultArea.setFont(resFont);
		centerResArea.setBackground(Color.white);
		centerResArea.add(new JLabel(""));
		centerResArea.add(resultArea);
		centerResArea.add(new JLabel(""));
		resultPanel.add(centerResArea);
		this.add(resultPanel,BorderLayout.CENTER);
	}


	/**
	 * Initialize combo boxes to list all students
	 * and all assignments in the course. The boxes
	 * are initially set to an empty string to   
	 * indicate that no string has been selected
	 */
	private void initComboBoxes()
	{
		//Create an array of student names from the data table
		String[] stuNames = new String[table.getRowCount()+1];
		stuNames[0]=""; //initially have no student selected	

		//first determine the index of the Names column.
		int nameColIdx = 0;
		for (int i = 0; i < table.getColumnCount();i++){
			if (table.getColumnName(i).equals("Name")){
				nameColIdx = i;
				break;
			}
		}

		for (int i = 0; i < table.getRowCount(); i++) { //add student names to array one by one
			stuNames[i+1]=(String)table.getValueAt(i, nameColIdx);
		}

		students = new JComboBox<String>(stuNames); //initialize combo box
		students.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) { //and add listener to show stats when selection made
				showStuStats();
			}
		});

		//next initialize assignment list
		
		assignments = new JComboBox<String>(headers);
		assignments.addActionListener (new ActionListener () {
			public void actionPerformed(ActionEvent e) { //and add listener to show stats when selection made
				showAssignStats();
			}
		});

		initComboPanel(); //setup the JPanel that contains the selection combo boxes
	}

	/**
	 * Initialize the JPanel that contains the student and
	 * assignment combo boxes. The panel consists of a label
	 * directing the user to make a selection, a student 
	 * selection combo box, an assignment selection combo box,
	 * and an exit button that closes the frame
	 */
	public void initComboPanel()
	{
		selectionPanel = new JPanel(new BorderLayout());
		JLabel instructLabel = new JLabel("Please make a selection");
		instructLabel.setFont(iFont);
		instructLabel.setForeground(Color.red);
		instructLabel.setHorizontalAlignment(JLabel.CENTER);

		selectionPanel.add(instructLabel,BorderLayout.NORTH);

		JPanel innerPanel = new JPanel(new GridLayout(4,2));

		stuLabel = new JLabel("Students");
		stuLabel.setHorizontalAlignment(JLabel.CENTER);
		stuLabel.setForeground(Color.BLUE);
		stuLabel.setFont(iFont);
		assignLabel = new JLabel("Assignments");
		assignLabel.setHorizontalAlignment(JLabel.CENTER);
		assignLabel.setForeground(Color.BLUE);
		assignLabel.setFont(iFont);

		innerPanel.add(stuLabel);
		innerPanel.add(assignLabel);
		innerPanel.add(students);
		innerPanel.add(assignments);
		innerPanel.add(new JLabel(""));
		innerPanel.add(new JLabel(""));
		innerPanel.add(new JLabel(""));
		innerPanel.add(exitButton);

		selectionPanel.add(new JLabel(" "),BorderLayout.WEST);
		selectionPanel.add(new JLabel(" "), BorderLayout.EAST);
		selectionPanel.add(innerPanel,BorderLayout.CENTER);	
	}

	/**
	 *  Show summary statistics for the student selected via the 
	 *  students combo box. The student's mean, median, range,
	 *  and standard deviation are shown, one per line, in the
	 *  result window.
	 */
	private void showStuStats()
	{
		if (students.getSelectedIndex()==0) return; //do nothing if blank selected

		Student selectedStu = new Student();
		String selectedName = students.getSelectedItem().toString();
		String fName = selectedName.split(",")[1].trim();
		String lName = selectedName.split(",")[0].trim();

		ArrayList<Double> scoreList = new ArrayList<Double>();

		//Determine location of Names column
		int nameIdx = 0;
		for (int i = 0; i < table.getColumnCount(); i++){
			if (table.getColumnName(i).equals("Name")){
				nameIdx = i;
				break;
			}
		}
		
		int rowIdx=0;
		//Find the index of the selected student row
		for (int i = 0; i<table.getRowCount();i++){ //Find the row containing selected student
			if (table.getValueAt(i,nameIdx).toString().equals(lName+", "+fName)){
				rowIdx = i;
				break;
			}
		}

		for (int i = 0; i < table.getColumnCount();i++){ //first two columns are name and id
			if (!(table.getColumnName(i).equals("Name")||table.getColumnName(i).equals("ID #"))){
				double currentScore = -1;
				try {
					currentScore = Double.parseDouble(table.getValueAt(rowIdx, i).toString());
				}
				catch(Exception e){}
				if (currentScore >= 0){ //nothing added if negative or blank score
					scoreList.add(currentScore);
				}
			}
		}

		double[] scoreArr = new double[scoreList.size()]; //Copy scores to array to use Stats class methods
		for (int i = 0; i < scoreList.size();i++){ scoreArr[i] = scoreList.get(i); }

		//Then show stats for that student
		String resString = "Summary for "+fName+" "+lName+"\n";
		resString +="  Mean Score: "+ Stats.getMean(scoreArr);
		resString +="\n  Median Score: "+ Stats.getMedian(scoreArr);
		resString +="\n  Range: "+ Stats.getRange(scoreArr);
		resString +="\n  Standard Deviation: "+ Stats.getSD(scoreArr);
		resString +="\n";
		resultArea.setText(resString);

		updateHistPanel(scoreArr);
	}

	/**
	 * Update the panel showing a score histogram to reflect
	 * the selection of new data.  
	 * @param values An array of double values to represent with a histogram
	 */
	private void updateHistPanel(double[] values)
	{		
		histPanel = new HistogramPanel(values,this.getWidth(), this.getHeight()/2);
		if (resultPanel.getComponentCount()>1) //If a histogram is already being shown
			resultPanel.remove(1);			   //Remove it to make space for the new one

		resultPanel.add(histPanel,1);
	}


	/**
	 * Show summary statistics for a particular assignment. Updates
	 * the summary statistics area to show the mean, median, range, and
	 * standard deviation for the selected assignment. 
	 */
	private void showAssignStats()
	{
		String selectedAssign = assignments.getSelectedItem().toString();
		//find the column index of the selected assignment
		int selectedIdx = 0; //will hold index of assignment column
		for(int i = 0; i < table.getColumnCount();i++){
			if (table.getColumnName(i).equals(selectedAssign)){
				selectedIdx = i;
				break;
			}
		}
		
		if (selectedAssign.equals("")) return; //do nothing if blank selected
		
		ArrayList<Double> assignScoreList = new ArrayList<Double>();
		
		for (int i = 0; i < table.getRowCount(); i++){
			if (table.getValueAt(i, selectedIdx).toString().equals("")==false){
				assignScoreList.add(Double.parseDouble(table.getValueAt(i, selectedIdx).toString()));
			}
		}
		
		double[] assignScores = new double[assignScoreList.size()];
		for (int i = 0; i < assignScores.length; i++){
			assignScores[i]=assignScoreList.get(i);
		}
		
		String resString = "SUMMARY FOR: "+assignments.getSelectedItem()+"\n";
		resString+="  Mean Score: "+Stats.getMean(assignScores);
		resString+="\n  Median Score: "+Stats.getMedian(assignScores);
		resString+="\n  Range: "+Stats.getRange(assignScores);
		resString+="\n  Standard Deviation: "+Stats.getSD(assignScores);

		resultArea.setText(resString); //update the results area
		updateHistPanel(assignScores);
	}

	/**
	 * Class to specify what actions should occur when a button is pressed.
	 */
	private class ButtonListener implements ActionListener 
	{ 
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand(); //Command holds the text of the button pushed
			if (command.equals("Exit")){
				dispose();
			}
		}
	} 	
}
