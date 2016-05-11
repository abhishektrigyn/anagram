package serviceimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import main.PropertyReader;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import exception.handling.CustomizedException;
import services.IDomainService;

public class DomainServiceImpl implements IDomainService {

	private int iteration;
	private String configuration;
	private String fileName;

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override 
	public int getIteration() {
		return iteration;
	}
	
	@Override 
	public String getConfiguration() {
		return configuration;
	}
	
	@Override	
	public  Multimap<String , List<String>> removeNonAnagram( Multimap<String , List<String>> multiMap)
	{
		Set keySet=multiMap.keySet();
		Iterator<String> it = keySet.iterator();
		while(it.hasNext())
		{
			Collection tempCollection = multiMap.get(it.next());
			if(tempCollection.size()==1)
			{
				it.remove();

			}
		}
		return multiMap;
	}
	@Override
	public boolean checkAnagram(String firstString, String secondString) {
		// TODO Auto-generated method stub
		char[] characters = firstString.toCharArray();
		StringBuilder sbSecond = new StringBuilder(secondString);

		for(char ch : characters){
			int index = sbSecond.indexOf("" + ch);
			if(index != -1){
				sbSecond.deleteCharAt(index);
			}else{
				return false;
			}
		}
		return false;
	}

	@Override
	public Multimap<String, List<String>> fileRecordsIntoMap(List<String> list) {
		// TODO Auto-generated method stub
		Multimap<String, List<String>> multimap = ArrayListMultimap.create();
		Collections.sort(list);
		for(String key :list)
		{
			key=key.trim().toLowerCase();
			if(!key.isEmpty())
			{
				spaceRemover(key);
				String withoutSort=key;
				key=sortString(key);
				multimap = putIntoMAP(key , withoutSort, multimap);
			}
		}
		return  multimap;
	}

	@Override
	public String spaceRemover(String input) {
		// TODO Auto-generated method stub
		return input.replaceAll("\\s+","");

	}

	@Override
	public Multimap putIntoMAP(String key, String val, Multimap multiMap) {
		// TODO Auto-generated method stub
		multiMap.put(key, val);
		return multiMap;
	}

	@Override
	public long getTimeLapsed(long start, long end) {
		// TODO Auto-generated method stub
		long difference=(end - start);
		return difference;	}

	@Override
	public String sortString(String input) {
		// TODO Auto-generated method stub
		char[] chars = input.toCharArray();
		Arrays.sort(chars);
		String sorted = new String(chars);
		return sorted;	
	}
	@Override
	public void print(Multimap<String, List<String>> multimap,List<String> finalList) {
		// TODO Auto-generated method stub
		multimap= fileRecordsIntoMap(finalList);
		multimap= 	removeNonAnagram(multimap);
		Set<String> set=multimap.keySet();
		Iterator<String> it=set.iterator();
		while(it.hasNext())
		{
			List<String> list=(List) multimap.get(String.valueOf(it.next()));
			for(String value:list)
			{
				System.out.print(value+"  ");
			}
			System.out.println();
		}
	}
	@Override
	public void processToFindAnagram(List<FutureTask<List<String>>> taskList,String conf)
			throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		List<String> finalList=new ArrayList<String>();
		for (FutureTask<List<String>> futureTask : taskList) {
			List<String> result =   futureTask.get();
			finalList.addAll(result);
		}
		Multimap<String , List<String>> multimap= fileRecordsIntoMap(finalList);
		if(conf.equals("1"))
		{
		print(multimap, finalList);
		}
		
	}
	@Override
	public void checkPropertyConfiguration() throws IOException, CustomizedException {
		// TODO Auto-generated method stub
		String conf=PropertyReader.getProperty("configuration");
		String	fileName="";
		configuration=conf;
		if(conf == null || conf.isEmpty())
		{
			throw new CustomizedException("Configuration property is not set,it must be set to  1 or 2 ");

		}
		if(conf.equals("1"))
		{
			iteration=1;
			fileName=PropertyReader.getProperty("anagram_file");
		
			if(fileName == null || fileName.isEmpty())
			{
				throw new CustomizedException("Filename property is not set,it must be set to sample against the key anagramFile ");

			}
			
		}
		else if(conf.equals("2"))
		{
			iteration=Integer.parseInt(PropertyReader.getProperty("load_testing_iteration"));
			fileName=PropertyReader.getProperty("large_file");
			if(fileName == null || fileName.isEmpty())
			{
				throw new CustomizedException("Filename property is not set,it must be bigFile against the key largeFile  ");

			}
			
		}
		this.fileName=fileName;
		
	}

}
