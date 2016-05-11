package thread.util;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
public class Work implements Callable<List<String>> {
	private String file;
	private long offset;
	private long size;
	public Work(String file,long offset,long size) {
		// TODO Auto-generated constructor stub
		this.file=file;
		this.offset=offset;
		this.size=size;
	}

	public  List<String> readLargeFile() throws IOException
	{	
		@SuppressWarnings("resource")
		RandomAccessFile f = new RandomAccessFile(file, "r");
		List<String> returnist=new ArrayList<String>();
		FileChannel fc = f.getChannel();
		f.seek(offset);
		String line="";
		MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, offset, size);
		for (int i = 0; i < mbb.limit(); i++)
		{	char c = (char) mbb.get();
			if(c=='\n'||c=='\r')
			{
				if(line.isEmpty())
				continue;
				returnist.add(line);
				line="";
				continue;
			}
			line=line+c;
		}
		if(!line.isEmpty())
		{
			returnist.add(line);
		}
		return returnist;
	}
	@Override
	public List<String> call() throws Exception {
		// TODO Auto-generated method stub
		return readLargeFile();

	}
}


