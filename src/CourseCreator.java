/**
 *  Creates a new Course object given a course file name. Data 
 *  files for courses, students, and assignments must be located 
 *  in the "\courses", "\students", and "\assignments" directories, 
 *  respectively. 
 * 
 * CSCIE-10B
 * Final Project
 * Spring 2016
 * @author Jon Janelle
 * @version Modified 5/1/2016
 */
import java.io.*;
import java.util.*;

public class CourseCreator {

	private Course myCourse;

	/**
	 * Create a new course object by course name.
	 * The course name corresponds to the name of the
	 * course data file in the "\courses" directory. 
	 * This file specifies which students and assignments
	 * are in the course. 
	 * 
	 * @param courseName The name of the course
	 */
	public CourseCreator(String courseName)
	{
		String[] sids = {}; 		//student id numbers
		String[] assignID = {};		//assignment id numbers
		int courseID = 0;			//course identification number
		
		try{
			Scanner fh = new Scanner(new File("courses\\"+courseName+".txt")); //open course file
			courseID = Integer.parseInt(fh.nextLine().split(" ")[1]); 
			
			//Get array of student IDs. Make sure that at least one exist 
			String[] tempSids = fh.nextLine().split(" ");
			if (tempSids.length>1)
				sids = tempSids[1].split(";");
	
			//Get array of assignment IDs
			String[] tempAssignID = fh.nextLine().split(" ");
			if (tempAssignID.length>1)
				assignID = tempAssignID[1].split(";");
	
			fh.close();
		}
		catch (IOException e)
		{
			System.out.println(e.toString());
		}

		//Create Student list
		ArrayList<Student> students = makeStudentList(sids);

		//Create Assignment list
		ArrayList<Assignment> assignments = makeAssignmentList(assignID);

		//Construct the new Course object
		myCourse = new Course(courseName,courseID,students,assignments);
	}
	
	

	/**
	 * Construct an ArrayList of Student objects given an array of
	 * student id numbers. 
	 * @param sids An array of student id numbers as strings
	 * @return An ArrayList of Student objects
	 * @throws IOException
	 */
	public static ArrayList<Student> makeStudentList(String[] sids) 
	{
		ArrayList<Student> students = new ArrayList<Student>();
		Student tempStu;
		Scanner fh;
		for (String id: sids) {
			try {
				fh = new Scanner(new File("students\\"+id+".txt"));

				String[] name = fh.nextLine().split(" ");
				tempStu = new Student(name[1],name[2],Integer.parseInt(id));

				fh.nextLine(); //discard student id line

				//create Score array. Scores for all assignments, not just
				//those in the course currently being opened, are included
				String[] scores = fh.nextLine().split(" ")[1].split(";");
				double grade;
				int aid;
				for (String s : scores){	
					aid = Integer.parseInt(s.split(",")[0]); //assignment id
					if (s.split(",").length>1){
						grade = Double.parseDouble(s.split(",")[1]);
					}
					else grade = -1; //a grade of -1 means that no grade is available
					tempStu.addScore(new Score(aid, grade));
				}
				students.add(tempStu);	
				fh.close();
			}
			catch (IOException e){
				System.out.println(e);
			}
		}
		return students;
	}

	/**
	 * Construct an ArrayList of Assignment objects
	 * given an array of Assignment ID numbers.
	 */
	public static ArrayList<Assignment> makeAssignmentList(String[] assignID)
	{
		ArrayList<Assignment> assignments = new ArrayList<Assignment>();
		Assignment tempAssign;
		Scanner fh;
		for (String id: assignID) {
			try {
			fh = new Scanner(new File("assignments\\"+id+".txt"));
			fh.nextLine(); //first line has ID, not needed
			String name = fh.nextLine();
			name = name.substring(name.indexOf(' '));  //assignment name
			String desc = fh.nextLine();
			desc = desc.substring(desc.indexOf(' '));
			assignments.add(new Assignment(name, Integer.parseInt(id),desc));
			fh.close();
			}
			catch (IOException e){
				System.out.println(e.toString());
			}
		}
		return assignments;
	}

	/**
	 * Return the initialized Course object 
	 * @return The course object
	 */
	public Course getCourse()
	{
		return myCourse;
	}
	
	/**
	 * Create a list of all students for which data files
	 * exist in the students directory
	 * @return An ArrayList of all students
	 */
	public static ArrayList<Student> getAllStudents()
	{
		ArrayList<Student> students = new ArrayList<Student>();

		File stuDir = new File("students");
		File[] stuFiles = stuDir.listFiles();
		
		String[] ids = new String[stuFiles.length];
		
		for (int i = 0; i < stuFiles.length; i++){
			ids[i]= stuFiles[i].getName().substring(0,stuFiles[i].getName().length()-4);
		}
		
		students = makeStudentList(ids); 	
		return students;
	}
	
	/**
	 * Create a list of all assignments for which data files
	 * exist in the assignments directory
	 * @return An ArrayList of all assignments
	 */
	public static ArrayList<Assignment> getAllAssignments()
	{
		ArrayList<Assignment> assigns = new ArrayList<Assignment>();
		File aDir = new File("assignments");
		Scanner fh;
		String name, desc, line;
		int id;
		
		try{
			for (File f : aDir.listFiles()){
				fh = new Scanner(f);
	
				id = Integer.parseInt(fh.nextLine().split(" ")[1]);
				line = fh.nextLine();
				name = 
				line = fh.nextLine();
				desc = line.substring(line.indexOf(' '));
			
				assigns.add(new Assignment(name,id, desc));
			}
			return assigns;
		}
		catch (IOException e){
			return assigns;
		}
	}

}
