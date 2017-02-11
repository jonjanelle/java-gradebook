/**
 * Perform basic descriptive statistics operations on arrays of double
 * values.
 * 
 * Includes methods to find the mean, median, range, and standard deviation
 * of arrays of doubles.
 * 
 * CSCIE-10B
 * Final Project
 * Spring 2016
 * @author Jon Janelle
 * @version Modified 4/29/2016
 */

import java.util.Arrays;

public class Stats {
	/**
	 * Get the arithmetic mean of an array of double values.
	 * The mean is truncated after two decimal digits.
	 * Returns 0 if the array is empty. 
	 * @param nums An array of double values
	 * @return The mean of the doubles
	 */
	public static double getMean(double[] nums)
	{
		if (nums.length==0) return 0;
		
		double sum = 0;
		for (double n: nums){
			sum+=n;
		}
		double mean = sum/nums.length;
		return ((int)(mean*100))/100.0;
	}
	
	/**
	 * Get the standard deviation of an array of doubles.
	 * Returns 0 if the array has length 0.
	 * 
	 * @param nums The array of values to process
	 * @return The standard deviation of the values
	 */
	public static double getSD(double[] nums)
	{
		if (nums.length==0) return 0;
		double xBar = getMean(nums); 
		double ssDev = 0; //sum of squared deviations
		for (double n: nums){
			ssDev += (xBar-n)*(xBar-n);
		}
		double sd = Math.sqrt(ssDev/nums.length);
		return ((int)(sd*100))/100.0;
	}
	
	/**
	 * Get the range of an array of doubles.
	 * The range is defined as the difference
	 * between the maximum and minimum values.
	 * Returns 0 if array empty.
	 * 
	 * @param nums An array of double values
	 * @return The range of the values
	 */
	public static double getRange(double[] nums) 
	{
		if (nums.length==0) return 0;
	
		return getMax(nums)-getMin(nums);
	}

	/**
	 * Get the median of an array of doubles.
	 * Returns 0 if the array is empty.
	 * 
	 * @param nums An array of double values
	 * @return The median of the values
	 */
	public static double getMedian(double[] nums)
	{
		if (nums.length==0) return 0;
		
		Arrays.sort(nums); //sort the numbers in ascending order
		
		if (nums.length%2==0){
			return ((double)nums[nums.length/2]+nums[nums.length/2-1])/2;	
		}
		else {
			return (double)nums[nums.length/2];
		}
	}
	
	/**
	 * Find the maximum value in an array of doubles.
	 * Returns Integer.MIN_VALUE if array empty
	 * @param nums An array of double values
	 * @return The minimum value in the array
	 */
	public static double getMax(double[] nums)
	{
		double max = Integer.MIN_VALUE;
		for (double n: nums){
			max = Math.max(max, n);
		}
		return max;
	}

	/**
	 * Find the maximum value in an array of integers.
	 * Returns Integer.MIN_VALUE if array empty
	 * @param nums An array of int values
	 * @return The minimum value in the array
	 */
	public static int getMax(int[] nums)
	{
		int max = Integer.MIN_VALUE;
		for (int n: nums){
			max = Math.max(max, n);
		}
		return max;
	}
	
	/**
	 * Find the minimum value in an array of doubles.
	 * Returns Integer.MAX_VALUE if array empty
	 * @param nums An array of double values
	 * @return The minimum value in the array
	 */
	public static double getMin(double[] nums)
	{
		double min = Integer.MAX_VALUE;
		for (double n: nums){
			min = Math.min(min, n);
		}
		return min;
	}
}
