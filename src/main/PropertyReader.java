package main;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

	static Properties prop = null;

	public static void loadProperties() throws IOException 
	{
		prop=new Properties();
		String filename = "config.properties";
		InputStream input = PropertyReader.class.getClassLoader().getResourceAsStream(filename);
		if(input==null){
			System.out.println("Sorry, unable to find " + filename);
		}
		else
		{
			prop.load(input);

		}
	}

	public static String getProperty(String key) throws IOException
	{
		if(prop==null)
		{

			loadProperties();
		}

		return prop.getProperty(key);
	}
}

