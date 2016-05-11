package bean;

import java.io.RandomAccessFile;

public class FileBean {
	
	private RandomAccessFile raf ;
	private String fileName;
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	private long totalLength;
	private int totalLineNumber;
	private int threadCount;
	private long rem;
	public long getRem() {
		return rem;
	}

	public void setRem(long rem) {
		this.rem = rem;
	}

	public int getTotalLineNumber() {
		return totalLineNumber;
	}

	public void setTotalLineNumber(int totalLineNumber) {
		this.totalLineNumber = totalLineNumber;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public FileBean(RandomAccessFile raf,long totalLength,int threadCount,int totalLineNumber,long rem,String fileName) {
		// TODO Auto-generated constructor stub
		this.raf=raf;
		this.totalLength=totalLength;
		this.threadCount=threadCount;
		this.totalLineNumber=totalLineNumber;
		this.rem=rem;
		this.fileName=fileName;
	}
	
	public RandomAccessFile getRaf() {
		return raf;
	}
	public void setRaf(RandomAccessFile raf) {
		this.raf = raf;
	}
	
	public long getTotalLength() {
		return totalLength;
	}
	public void setTotalLength(long totalLength) {
		this.totalLength = totalLength;
	}
	
	

}
