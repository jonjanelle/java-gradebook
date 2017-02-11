/**
 * Model for an academic course.
 * 
 * A course has a name and identification number
 * and consists of a collection of students and a 
 * collection of assignments.
 * 
 * CSCIE-10B
 * Final Project
 * Spring 2016
 * 
 * @author Jon Janelle
 * @version Modified 5/1/2016
 */

import java.util.*;

public class Course {

	private String name; // The name of the course.
	private int id;		 //course identification number
	private List<Student> students; 		//A list of students
	private List<Assignment> assignments;  //A list of assignments
	
	/**
	 * Create a course by name and identification number.
	 * Course starts with an empty student list and an 
	 * empty assignment list
	 * 
	 * @param name The course name
	 * @param id Course identification number
	 */
	public Course(String name, int id)
	{
		this.name = name;
		this.id = id;
		students = new ArrayList<Student>();
		assignments = new ArrayList<Assignment>();
	}
	
	public Course(String name, int id, List<Student> students, List<Assignment> assigns)
	{
		this.name = name;
		this.id = id;
		this.students = students;
		this.assignments = assigns;
	}
	

	/**
	 * Add a new assignment to the course. If the assignment
	 * is already in the course, then no action is performed
	 * @param a The new assignment to add
	 */
	public void addAssignment(Assignment a) 
	{ 
		for (Assignment temp : assignments){
			if (a.equals(temp)){
				return;
			}
		}
		assignments.add(a); 
	}
	
	/**
	 * Add a new student to the course. If the student is
	 * already in the course, then no action is performed
	 * @param s The new student to add
	 */
	public void addStudent(Student s) 
	{ 
		for (Student temp : students){
			if (s.equals(temp)){
				return;
			}
		}
		students.add(s); 
	}
	
	/**
	 * Get student list
	 * @return A list of all students in the course
	 */
	public List<Student> getStudents() 
	{
		return students;
	}
	
	/**
	 * Get assignment list
	 * @return a list of all assignments in the course
	 */
	public List<Assignment> getAssignments()
	{
		return assignments;
	}
	
	/**
	 * Get number of students
	 * @return The number of students in the course
	 */
	public int numStudents()
	{
		return students.size();
	}
	
	/**
	 * Get number of assignments
	 * @return The number of assignments in the course
	 */
	public int numAssignments()
	{
		return assignments.size();
	}
	
	/**get course name
	 * 
	 * @return The name of the course
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Get course identification number
	 * @return The course ID number
	 */
	public int getID()
	{
		return id;
	}
	
	/**
	 * Update a student's score on a specified assignment.
	 * @param newScore A new percentage score
	 * @param sid A student ID number
	 * @param assignName An assignment ID number
	 */
	public void updateStudentScore(double newScore, String sid, String assignName)
	{
		assignName = assignName.trim();
		int id = Integer.parseInt(sid);
		for (Student s:students){
			if (s.getID()==id){
				for (Assignment a: assignments){
					if (a.getName().trim().equals(assignName)){
						s.setScore(a.getID(), newScore);
				
					}
				}
			}
		}
	}
	
	/**
	 * Print info about Students in course.
	 * Outputs name, ID number, mean score, and an assignment listing
	 * for each student in the course
	 */
	public void printStudentInfo(){
		for (Student s : this.getStudents()){
			System.out.println("\n"+s);
			System.out.println("Average: " +s.getMean());
			ArrayList<Score> sScore = (ArrayList)s.getScores();
			for (Score x: sScore){
				System.out.print("Assignment: "+x.getID()+" Score: "+x.getScore()+"\n");

			}

		}
	}
	
	/**
	 * Print info about Assignments in course.
	 * Prints out the Assignment name, description,
	 * and ID number for each assignment in the course.
	 */
	public void printAssignmentInfo()
	{
		for (Assignment a : this.getAssignments())
			System.out.println(a);
	}
	

}
