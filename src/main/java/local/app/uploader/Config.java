package local.app.uploader;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Config
{
	public static final String API_URL_PREFIX = "-apiURL=";
	public static final String APP_ID_PREFIX = "-appID=";
	public static final String STORAGE_DIR_PREFIX = "-storageDir=";

	private final String appID;
	private final String apiURL;
	private final String storageDir;

	private Config(String appID, String apiURL, String storageDir)
	{
		this.appID = appID;
		this.apiURL = apiURL;
		this.storageDir = storageDir;
	}

	public static Config load(String[] args)
	{
		Set<String> arguments = new HashSet<>(Arrays.asList(args));
		String apiURL = null;
		for (String arg : arguments)
		{
			if (arg.startsWith(API_URL_PREFIX))
				apiURL = arg.substring(API_URL_PREFIX.length(), arg.length());
		}
		String appID = null;
		for (String arg : arguments)
		{
			if (arg.startsWith(APP_ID_PREFIX))
				appID = arg.substring(APP_ID_PREFIX.length(), arg.length());
		}
		String storageDir = null;
		for (String arg : arguments)
		{
			if (arg.startsWith(STORAGE_DIR_PREFIX))
				storageDir = arg.substring(STORAGE_DIR_PREFIX.length(), arg.length());
		}
		if (apiURL == null || appID == null || storageDir == null)
			throw new IllegalArgumentException("invalid configuration arguments");

		return new Config(appID, apiURL, storageDir);
	}

	@Override
	public String toString()
	{
		return "Config [appID=" + appID + ", apiURL=" + apiURL + ", storageDir=" + storageDir + "]";
	}

	public String getAppID()
	{
		return appID;
	}

	public String getApiURL()
	{
		return apiURL;
	}

	public String getStorageDir()
	{
		return storageDir;
	}
}
