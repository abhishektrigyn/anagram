package main;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import services.IDomainService;
import services.IFileService;
import bean.FileBean;

import exception.handling.CustomizedException;
import factory.Factory;

public class AnagramSolverTest {

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		try {
			process();
		} catch (CustomizedException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			//e.printStackTrace();
		}
	}



	public static void process() throws IOException, InterruptedException, ExecutionException, CustomizedException
	{
		int count=0;
		long totalDifference=0;
		int iteration=0;
		//creating services
		IDomainService domainService=Factory.getInstance().getDomainService();
		IFileService fileService=Factory.getInstance().getFileService();

		//checking configuration from property file, whether its properly set or not.
		domainService.checkPropertyConfiguration();

		//getting configuration
		String conf=domainService.getConfiguration();


		// setting up the interations for load testing
		iteration = domainService.getIteration();
		//running load testing in the case of reading huge file and processing on it .
		while(count<iteration)
		{
			//creating random buffer in the file to read the data
			FileBean fb = fileService.prepareDataForRandomFileBuffer(domainService.getFileName(),conf);		

			long start = System.currentTimeMillis();

			//reading file and returing the taskList which is a list of String read from individual file buffer .
			List<FutureTask<List<String>>> taskList= fileService.readFile(fb);

			// business logic to find the anagram from the taskList
			domainService.processToFindAnagram(taskList,conf);			
			long end = System.currentTimeMillis();

			//maintaining the time lapsed during the operation.
			long difference =domainService.getTimeLapsed(start, end);
			totalDifference=totalDifference+difference; 
			count++;
		}

		// printing time lapsed only if its a configuration 2..
		if(conf.equals("2"))
		{
			System.out.println("Time lapsed "+totalDifference);
		}

	}
}
