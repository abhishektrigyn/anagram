package serviceimpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import main.PropertyReader;
import exception.handling.CustomizedException;
import bean.FileBean;
import services.IFileService;
import thread.util.ThreadManager;
import thread.util.Work;

public class FileServiceImpl implements IFileService{


/*
 * Creates the buffer chunk from the file that will be read by single or mutiple threads
 * and will be put into ArrayList .
 * (non-Javadoc)
 * @see services.IFileService#createBuffer(java.io.RandomAccessFile, java.lang.String, long, int, long)
 */
	@Override
	public List<FutureTask<List<String>>> createBuffer(RandomAccessFile raf,String fileName,
			long buffer, int threadCount, long rem) throws IOException {
		// TODO Auto-generated method stub

		ThreadManager.clearTaskList();
		List<FutureTask<List<String>>> taskList = ThreadManager.getTaskList();
		ExecutorService executor = new ThreadManager(threadCount).getExecutor();
		long first=0;
		long last=0;
		for(int i=0;i<threadCount;i++)
		{
			Long[] index = new Long[2];

			if(i==0)
			{
				first=0;
			}

			last=last+buffer;

			if(i==threadCount-1)
			{
				if(rem==0)
				{
					buffer=last-first; 
					last=(int) adjustBuffer(raf, buffer,first,last);
					index[0]=first;
					index[1]=raf.length();
					Work work=new Work(fileName,index[0],index[1]-index[0]);
					FutureTask<List<String>> ft = new FutureTask<List<String>>(work);
					taskList.add(ft);
					executor.execute(ft);
					break;
				}
				if(rem>0)
				{
					buffer=buffer+rem; 
					last=(int) adjustBuffer(raf, buffer,first,last);
					if(last>raf.length())
					{
						index[0]=first;
						index[1]=raf.length();
						Work work=new Work(fileName,index[0],index[1]-index[0]);
						FutureTask<List<String>> ft = new FutureTask<List<String>>(work);
						taskList.add(ft);
						executor.execute(ft);
						break;
					}
				}
			}     	
			while(first>last)
			{
				last=last+buffer;
			}
			last=(int) adjustBuffer(raf, buffer,first,last);
			index[0]=first;
			index[1]=last;
			Work work=new Work(fileName,index[0],last-first);
			FutureTask<List<String>> ft = new FutureTask<List<String>>(work);
			taskList.add(ft);
			executor.execute(ft);
			if(last==raf.length())
			{
				break;
			}

			first=last;
		}
		executor.shutdown();

		return taskList;
	}

	/*
	 * Adjusting the buffer when readline method takes file pointer at the end of line
	 * (non-Javadoc)
	 * @see services.IFileService#adjustBuffer(java.io.RandomAccessFile, long, long, long)
	 */
	@Override
	public long adjustBuffer(RandomAccessFile raf, long buffer, long first,
			long last) throws IOException {
		// TODO Auto-generated method stub
		if(buffer<raf.length()-last)
		{
			raf.seek(first+buffer);
		}
		else
		{
			raf.seek(first);

		}
		raf.readLine();
		long f2=raf.getFilePointer();
		return f2;

	}

	/*
	 * Get total line number in the file.
	 * (non-Javadoc)
	 * @see services.IFileService#getTotalLineNumber(java.lang.String)
	 */
	@Override
	public int getTotalLineNumber(String fileName) throws IOException {
		java.io.FileReader fr = null;
		fr =  new java.io.FileReader(fileName);
		LineNumberReader lineNumberReader = new LineNumberReader(fr);
		lineNumberReader.skip(Long.MAX_VALUE);
		int lineCount = lineNumberReader.getLineNumber();
		return lineCount;	
	}

	/*
	 * Get total length of file 
	 * (non-Javadoc)
	 * @see services.IFileService#getFileTotalLength(java.io.RandomAccessFile)
	 */
	@Override
	public long getFileTotalLength(RandomAccessFile file) throws IOException {
		return  file.length();
	}

	/*
	 * Get indivdual buffer length on which 
	 * single or multiple threads wil operate
	 * (non-Javadoc)
	 * @see services.IFileService#getBufferLength(long, int)
	 */
	@Override
	public long getBufferLength(long length, int numberOfThreads) {
		// TODO Auto-generated method stub
		return  length/numberOfThreads;

	}

	/*
	 * After getting buffer chunks of same bytes remaining buffer bytes will
	 * be added in the last buffer 
	 * (non-Javadoc)
	 * @see services.IFileService#getRemainder(long, int)
	 */
	@Override
	public long getRemainder(long length, int numberOfThreads) {
		// TODO Auto-generated method stub
		return  length%numberOfThreads;
	}

	/*
	 * Calculating max possible thread for given length of file.
	 * 
	 * (non-Javadoc)
	 * @see services.IFileService#getMaxPossibleThread(int)
	 */
	@Override
	public int getMaxPossibleThread(int fileTotalLength) {
		int max=0;
		for(int i=1;i<=10;i++)
		{
			if(fileTotalLength%i==0)
			{
				max=i;
			}
		}
		return max;
	}

	/*
	 * Getting Random file reference.
	 * (non-Javadoc)
	 * @see services.IFileService#getRandomAccessFile(java.lang.String)
	 */
	@Override
	public RandomAccessFile getRandomAccessFile(String fileName) throws FileNotFoundException {
		// TODO Auto-generated method stub
		RandomAccessFile f = new RandomAccessFile(fileName, "r");
		return  f;

	}
	
	/*
	 * In this method we decide whether to create multiple thread or not 
	 * and we calcuate total length of file and remainder from dividing total length by number of threads
	 * depending upon the file size.
	 * (non-Javadoc)
	 * @see services.IFileService#prepareDataForRandomFileBuffer(java.lang.String, java.lang.String)
	 */

	@Override
	public FileBean prepareDataForRandomFileBuffer(String fileName,String conf) throws IOException, CustomizedException {
		// TODO Auto-generated method stub
		int threadCount=0;
		int totalLineNumber = getTotalLineNumber(fileName);

		if(conf.equals("1"))
		{
			threadCount=1;
		}
		if(conf.equals("2"))
		{

			String multiThread=PropertyReader.getProperty("multi_thread");
			if(multiThread==null || multiThread.isEmpty())
			{
				throw new CustomizedException("multiThread property not set,it must be true or false only for configuration 2");
			}
			if(PropertyReader.getProperty("multi_thread").equals("true"))
			{
				// Getting appropriate number of threads to read the large file by creating mutiple threads for predecided buffer length in the large file .

				threadCount = getMaxPossibleThread(totalLineNumber);
			}
			else
			{
				// single threaded reading for large file , setting thread count to 1
				threadCount = 1;

			}
		}
		long totalLength = getRandomAccessFile(fileName).length();
		long rem = getRemainder(totalLength, threadCount);
		FileBean fb = new FileBean(getRandomAccessFile(fileName), totalLength,threadCount,totalLineNumber,rem,fileName);
		return fb;
	}

	
	/*
	 * This method read the file into the ArrayList of String.
	 * 
	 * (non-Javadoc)
	 * @see services.IFileService#readFile(bean.FileBean)
	 */
	@Override
	public List<FutureTask<List<String>>> readFile(FileBean fb)
			throws IOException {
		// TODO Auto-generated method stub
		long buffer = getBufferLength(fb.getTotalLength(), fb.getThreadCount());
		List<FutureTask<List<String>>> taskList = ThreadManager.getTaskList();
		taskList=createBuffer(fb.getRaf(), fb.getFileName(), buffer, fb.getThreadCount(), fb.getRem());
		return taskList;	
	}

}
