package local.app.uploader.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class FileManager
{
	public static File store(InputStream is, String filePath) throws IOException
	{
		File file = new File(filePath);
		FileUtils.copyInputStreamToFile(is, file);
		return file;
	}

	public static File get(String filePath)
	{
		return FileUtils.getFile(filePath);
	}

	public static List<File> list(String storageDir)
	{
		return (List<File>) FileUtils.listFiles(new File(storageDir), TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	}
}
