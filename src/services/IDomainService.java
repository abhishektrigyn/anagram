package services;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import com.google.common.collect.Multimap;

import exception.handling.CustomizedException;

public interface IDomainService {


	/* Check whether two strings are anagram or not*/ 
	public  boolean checkAnagram(String firstString, String secondString);
	/*After reading the file successfully it passes the entire list to this method
	to sort  the key and put the multiple values against the sorted key*/ 
	public  Multimap<String, List<String>> fileRecordsIntoMap(List<String> list);
	//remove the space between the string 
	public  String spaceRemover(String input);
	/*We put sorted  key and non sorted value in Multiple map where we can have multiple 
    values against one unique key*/
	public  Multimap putIntoMAP(String key,String val,Multimap multiMap);

	// This method calculate start seconds and  end seconds difference
	public  long getTimeLapsed(long start,long end);

	// sort the string alphabetically 
	public  String sortString(String input);

	// Removes non anagram words from the map
	public  Multimap<String , List<String>> removeNonAnagram( Multimap<String , List<String>> multiMap);
   
	// Print values
	public  void print(Multimap<String , List<String>> multimap,List<String> finalList);

	/*
	 * Business logic to find anagram
	 */
	public  void processToFindAnagram(List<FutureTask<List<String>>> taskList,String conf) throws InterruptedException, ExecutionException;
	
	/*
	 * To check property configuration , wether properties are set properly or not
	 * in the property file config.properties
	 */
	public void checkPropertyConfiguration()throws CustomizedException, IOException;

	/*
	 * This iteration to set load testing to run n number of times
	 *  when we read a huge file , the file will be read by either single thread 
	 * or multiple thread. file will be read as per as iteration times , so we can
	 * check the performance at the end by analyzing time lapsed
	 *  with single thread and with multiple thread. 
	 */
	public int getIteration();
	
	/*
	 * Returns the configuration 1 or 2 
	 */
	public String getConfiguration();
	
	/*
	 * Returns the file name.
	 */
	public String getFileName();
}
