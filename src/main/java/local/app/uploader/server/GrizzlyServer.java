package local.app.uploader.server;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import local.app.uploader.App;

public class GrizzlyServer
{
	private HttpServer server;

	private GrizzlyServer(App app, URI uri)
	{
		final ResourceConfig rc = new ResourceConfig().packages("local.app.uploader")//
				.register(MultiPartFeature.class)//
				.register(new RestService(app));
		server = GrizzlyHttpServerFactory.createHttpServer(uri, rc);
	}

	public static GrizzlyServer init(App app, URI uri)
	{
		return new GrizzlyServer(app, uri);
	}

	public void terminate()
	{
		server.shutdown();
	}

}
