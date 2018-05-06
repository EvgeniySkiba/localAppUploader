package local.app.uploader.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import local.app.uploader.App;
import local.app.uploader.util.FileManager;

@Path("rest")
public class RestService
{
	final static Logger logger = Logger.getLogger(RestService.class);

	private App app;

	public RestService(App app)
	{
		this.app = app;
	}

	@POST
	@Path("proxy/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.TEXT_PLAIN)
	public Response proxyUpload(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail)
	{
		logger.info(app.getAppID() + ", invoked proxy/upload");
		return Response.ok().build();
	}

	@GET
	@Path("proxy/get")
	@Produces(MediaType.TEXT_PLAIN)
	public Response proxyGet()
	{
		logger.info(app.getAppID() + ", invoked proxy/get");
		return Response.ok().build();
	}

	@GET
	@Path("list")
	@Produces(MediaType.TEXT_PLAIN)
	public Response list()
	{
		logger.info(app.getAppID() + ", invoked list");
		return Response.ok().build();
	}

	@POST
	@Path("upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(@FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("file") FormDataContentDisposition fileDetail)
	{
		logger.info(app.getAppID() + ", invoked upload");
		try
		{
			String filepath = app.getStorageDir() + File.separator + fileDetail.getFileName();
			logger.info("storing uploaded file: " + filepath);
			FileManager.store(uploadedInputStream, filepath);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return Response.serverError().entity("an error occured while storing file: " + e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@GET
	@Path("get")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response get(@QueryParam("filename") String filename)
	{
		logger.info("requested file " + filename);
		String filepath = app.getStorageDir() + File.separator + filename;
		File file = FileManager.get(filepath);
		logger.info("file lookup:" + filepath);
		if (file == null)
			Response.serverError().entity("no such file");
		return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)//
				.header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"")//
				.build();
	}
}
