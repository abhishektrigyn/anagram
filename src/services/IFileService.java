package services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.concurrent.FutureTask;

import exception.handling.CustomizedException;
import bean.FileBean;

public interface IFileService {

	//Create Buffer for parallel processing of multiple threads as per file size in bytes
	List<FutureTask<List<String>>> createBuffer(RandomAccessFile raf,String fileName,long buffer,int threadCount,long rem) throws IOException;
	
	//This method adjust the buffer at take the cursor/file pointer at end of the line, and returns the last buffer pointer of that altered buffer  
	long adjustBuffer(RandomAccessFile raf,long buffer,long first,long last) throws IOException;
	
	//Get total number of lines in File.
	public  int getTotalLineNumber(String fileName) throws IOException;
	
	//Total file length
	public  long getFileTotalLength(RandomAccessFile file) throws IOException;

	/*Get Buffer length by passing total length as input devided by
	 * number of Threads as input which will calculate that ,what will be the buffer 
	 * size per thread to read out from the file 
	 * */
	public  long getBufferLength(long length,int numberOfThreads);
	
	/*  Get  remainder of buffer to append it in the final
	 *  buffer  that will be read from file
	 **/
	public  long getRemainder(long length,int numberOfThreads) ;
	
	/*Get max possible thread between 1 to 10 to process the full file
	 * 
	 * */
	public int getMaxPossibleThread(int fileTotalLength);

	/* Get random file instance*/
	public RandomAccessFile getRandomAccessFile(String fileName) throws FileNotFoundException;

	/* 
	 * This method preparr input data to make buffer 
	 * which will possibly be run by single or multiple threads. 
	 * */
	public  FileBean prepareDataForRandomFileBuffer(String fileName,String conf) throws IOException, CustomizedException;

	/*
	 * This method reads out entire file from  individual buffers into ArrayList
	 * 
	 */
	public  List<FutureTask<List<String>>> readFile(FileBean fb) throws IOException;
	

}
