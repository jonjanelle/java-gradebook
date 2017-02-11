/**
 * Update the course, student, and assignment files.
 * Overwrites all student and assignment files corresponding
 * to students and assignments in the course object argument.
 * The course data file is also updated to reflect any new 
 * students or assignments 
 *  
 * CSCIE-10B
 * Final Project
 * Spring 2016
 * @author Jon Janelle
 * @version Modified 5/4/2016
 */

import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

public class CourseSaver {

	private Course course;
	/**
	 * Constructor updates the student, assignment, and 
	 * course data files by overwriting them with current
	 * table data.
	 * 
	 * @param course The Course object for the data table
	 */
	public CourseSaver(Course course)
	{
		this.course = course;
		updateStudentFiles();
		updateAssignmentFiles();
		updateCourseFile();
	}
	
	/**
	 * Update student data files with current score and assignment
	 * information from the data table. Files are assumed to be 
	 * located in the "\students" directory. If a student file doesn't 
	 * exist, then a new one is created. If a student file does exist, 
	 * then its contents are overwritten with new data. 
	 */
	public void updateStudentFiles()
	{
		PrintWriter out;
		for (Student s: course.getStudents())
		{
			String sid = ""+s.getID();
			try {
				out = new PrintWriter("students\\"+sid+".txt");
				out.println("name: "+s.getFName()+" "+s.getLName());
				out.println("id: "+s.getID());
				out.print("scores: ");
				List<Score> scoreList = s.getScores();
				for (int i = 0; i < scoreList.size();i++){
					Score sc = scoreList.get(i);
					out.print(sc.getID()+","+sc.getScore());
					if (i!=scoreList.size()-1)
						out.print(";");
				}
				out.close();
			}
			catch(FileNotFoundException e){
				JOptionPane.showMessageDialog(null, "Error saving student data: Unable to open destination file");
			}
		}
	
	}	
	
	/**
	 * Update assignment data files to reflect any
	 * new assignment additions. Assignment data files
	 * are assumed to be located in the "\assignments"
	 * directory. All assignment data files from the 
	 * current course are overwritten. Assignment data
	 * files for other courses are not altered.
	 * 
	 */
	public void updateAssignmentFiles()
	{
		PrintWriter out;
		for (Assignment s: course.getAssignments()){
			try{
				out = new PrintWriter("assignments\\"+s.getID()+".txt");
				out.println("id: "+s.getID());
				out.println("name: "+s.getName().trim());
				out.println("desc: "+s.getDescription().trim());
				out.close();
			}
			catch (FileNotFoundException e){
				JOptionPane.showMessageDialog(null, "Error saving assignment data: Unable to open file.");
			}
		}
	}
	
	/**
	 * Update course data file to reflect any new assignment
	 * or student additions. The course data file is assumed to 
	 * be in the "\courses" directory.  
	 */
	public void updateCourseFile()
	{
		try {
			PrintWriter out = new PrintWriter("courses\\"+course.getName()+".txt");
			out.println("courseid "+course.getID());
			out.print("studentid ");
			
			for (int i = 0; i < course.numStudents();i++){
				out.print(course.getStudents().get(i).getID());
				if ( i != course.numStudents()-1)
					out.print(";");
			}
			out.println();
			out.print("assignmentid ");
			for (int i = 0; i < course.numAssignments();i++){
				out.print(course.getAssignments().get(i).getID());
				if ( i != course.numAssignments()-1)
					out.print(";");
			}
			out.close();
		}
		catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Error saving course data: Unable to open file.");
		}
	}
	
}
