/**
 * Model for a student in an academic course. Each student has a first name, 
 * last name, 4-digit integer ID number, and a list of Score objects 
 * each corresponding to a percentage score on a particular assignment
 *  
 * CSCIE-10B
 * Final Project
 * Spring 2016
 * @author Jon Janelle
 * @version Modified 4/20/2016
 */

import java.util.*;

public class Student {
	//Fields
	private String fName;
	private String lName;
	private int id;
	private List<Score> scores;	
	
	/**
	 * Default constructor makes a new 
	 * student with no name, a student id 
	 * of 0, and an empty score list.
	 */
	public Student() {
		fName = "";
		lName = "";
		id = 0;
		scores = new ArrayList<Score>();
	}
	
	/**
	 * Construct a new student with an empty score list
	 * 
	 * @param first The student's first name
	 * @param last The student's last name
	 * @param id A four digit student id number
	 */
	public Student(String first, String last, int id)
	{
		if (id<1000 || id > 9999){
			throw new IllegalArgumentException("Invalid Student ID number");
		}
		fName = first;
		lName = last;
		this.id = id;
		
		scores = new ArrayList<Score>();
	}
	
	/**
	 * Construct a new student with a given
	 * list of scores
	 * 
	 * @param first The student's first name
	 * @param last The student's last name 
	 * @param id The student's ID number
	 * @param scores A list of Score objects
	 */
	public Student(String first, String last, int id, List<Score> scores)
	{
		this(first,last,id);
		this.scores = scores;
	}
	
	//Accessor methods
	public List<Score> getScores(){ return scores; }
	public int getID() { return id; }
	public String getFName() { return fName; }
	public String getLName() { return lName; }
	
	//Mutator methods
	public void addScore(Score newScore){ scores.add(newScore); }
	public void setID(int newID) { id = newID; }
	public void setFName(String newName) { fName = newName; }
	public void setLName(String newName) { lName = newName; }
	
	/**
	 * Change the score on a given assignment. If this
	 * student does not have a score object for the
	 * assignment, then a new score is added 
	 *
	 * @param assignID The id number of the assignment to change
	 * @param newScore The new percentage score for the assignment
	 */
	public void setScore(int assignID, double newScore)
	{
		boolean hasAssign = false;
		for (int i = 0; i < scores.size(); i++){
			if (scores.get(i).getID()==assignID){ //if assignID match found
				hasAssign = true;
				scores.get(i).setScore(newScore); //then set new score
				break;
			}
		}
		if (hasAssign==false){ //add a new score if assignment id doesn't already exist
			this.addScore(new Score(assignID,newScore));
		}
	}
	
	/**
	 * Get the arithmetic mean of the percentage scores
	 * @return The mean percentage score on all assignments
	 */
	public double getMean()
	{
		double sum = 0;
		for (Score s: scores){
			sum+=s.getScore();
		}
		return sum/scores.size();
	}
	
	/**
	 * Get the standard deviation of the percentage scores
	 * 
	 * @return The standard deviation of scores
	 */
	public double getSD()
	{
		double xBar = getMean(); 
		double ssDev = 0; //sum of squared deviations
		for (Score s: scores){
			ssDev += (xBar-s.getScore())*(xBar-s.getScore());
		}
		double sd = Math.sqrt(ssDev/scores.size());
		return ((int)(sd*100))/100.0;
	}
	
	/**
	 * Get the range of this student's score.
	 * The range is defined as the difference
	 * between the maximum and minimum scores
	 * 
	 * @return The range of this student's scores
	 */
	public double getRange() 
	{
		double max=Integer.MIN_VALUE;
		double min=Integer.MAX_VALUE;
		for (Score s: scores){
			max = Math.max(max, s.getScore());
			min = Math.min(min, s.getScore());
		}
		return max - min;
	}

	/**
	 * Get the median of this student's scores
	 * 
	 * @return The median score
	 */
	public double getMedian()
	{
		double[] nums = new double[scores.size()];
		for(int i = 0; i < scores.size(); i++) {
			nums[i] = scores.get(i).getScore();
		}
		Arrays.sort(nums);
		if (nums.length==0) return -1;
		
		else if (nums.length%2==0){
			return ((double)nums[nums.length/2]+nums[nums.length/2-1])/2;	
		}
		else {
			return (double)nums[nums.length/2];
		}
		
	}
	
	/**
	 * Get student's score on a particular assignment
	 * or -1 if the score cannot be found
	 * @param assignID An integer assignment ID
	 * @return The student's score on the assignment, or -1 if no score found
	 */
	public double getScore(int assignID)
	{
		for (Score s: scores){
			if (s.getID()==assignID){
				return s.getScore();
			}
		}
		return -1;
	}
	
	/**
	 * Get a student's score on a particular assignment
	 * @param assignID A String containing an integer assignment id
	 * @return The student's score on the assignment, or -1 if no score found
	 */
	public double getScore(String assignID)
	{
		return getScore(Integer.parseInt(assignID));
	}
	
	/**
	 * Determine whether two students are equal.
	 * Students with the same first names, last names,
	 * and student id numbers are equal. 
	 * @param other The student to perform a comparison to
	 * @return true if other student is equal, false otherwise.
	 */
	public boolean equals(Student other)
	{
		return (this.fName.equals(other.getFName()) && 
				this.lName.equals(other.getLName()) &&
				this.id==other.getID());
	}
	
	/**
	 * Create a string with student name and identification number
	 * @return A string formatted as Name: <first name> <last name> \n ID: <id number>
	 */
	public String toString() 
	{
		return "Name: "+fName+" "+lName+"\nID: "+id;
	}
	
}
