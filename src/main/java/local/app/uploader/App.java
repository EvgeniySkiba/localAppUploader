package local.app.uploader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import local.app.uploader.server.GrizzlyServer;
import local.app.uploader.util.Requester;

public class App
{
	private Config config;
	private GrizzlyServer server;
	private Requester requester;

	public App(Config config)
	{
		this.config = config;
		this.server = GrizzlyServer.init(this, URI.create(config.getApiURL()));
		this.requester = new Requester(config.getStorageDir());
	}

	public static void main(String[] args)
	{
		try
		{
			App app = new App(Config.load(args));

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Press any key to stop:");
			br.readLine();

			app.terminate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.exit(0);
		}
	}

	public void terminate()
	{
		server.terminate();
	}

	public String getAppID()
	{
		return this.config.getAppID();
	}

	public String getStorageDir()
	{
		return config.getStorageDir();
	}

	public Requester getRequester()
	{
		return this.requester;
	}
}
