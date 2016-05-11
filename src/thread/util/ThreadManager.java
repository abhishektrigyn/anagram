package thread.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ThreadManager {
	public  static  List<FutureTask<List<String>>> taskList = new ArrayList<FutureTask<List<String>>>();
	private static ExecutorService executor ;
	static{


	}
	public ThreadManager(int threadCount) {
		// TODO Auto-generated constructor stub
		executor = Executors.newFixedThreadPool(threadCount);
	}
	public static List<FutureTask<List<String>>> getTaskList() {
		return taskList;
	}
	public static void setTaskList(List<FutureTask<List<String>>> taskList) {
		ThreadManager.taskList = taskList;
	}
	public static ExecutorService getExecutor() {
		return executor;
	}
	public static void setExecutor(ExecutorService executor) {
		ThreadManager.executor = executor;
	}

	public static void clearTaskList()
	{
		if(taskList.size()>0)
		{
		taskList.clear();
		}
	}

}
