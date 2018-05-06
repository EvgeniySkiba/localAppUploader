package local.app.uploader.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Stream;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

public class Requester
{
	private String storageFolder;
	private Client client;

	public Requester(String storageFolder)
	{
		this.storageFolder = storageFolder;
		final ClientConfig clientConfig = new ClientConfig();
		clientConfig.register(MultiPartFeature.class);
		client = ClientBuilder.newClient(clientConfig);
	}

	public File getMultipart(String url) throws IOException
	{
		Response response = client.target(url).request().accept(MediaType.MULTIPART_FORM_DATA).get();
		if (response.getStatus() != 200)
			throw new IOException("file get api returned an error status:" + response.getStatus());

		String filename = response.getHeaderString("Content-Disposition");
		return FileManager.store(response.readEntity(InputStream.class), storageFolder + "/" + filename);
	}

	public Response post(File file, String url) throws IOException
	{
		FileDataBodyPart filePart = new FileDataBodyPart("file", file);
		filePart.setContentDisposition(FormDataContentDisposition.name("file").fileName(file.getName()).build());
		try (FormDataMultiPart fdmp = new FormDataMultiPart())
		{
			MultiPart multipartEntity = fdmp.bodyPart(filePart);
			return client.target(url).request().post(Entity.entity(multipartEntity, multipartEntity.getMediaType()));
		}
	}

	public Response get(String url)
	{
		return client.target(url).request().get();
	}

	@SafeVarargs
	public final Response get(String url, @SuppressWarnings("unchecked") Tuple<String, String>... params)
	{
		WebTarget target = client.target(url);
		for (Tuple<String, String> param : params)
			target = target.queryParam(param.getLeft(), param.getRight());
		return target.request().accept(MediaType.APPLICATION_OCTET_STREAM).get();
	}

	public void stopClient()
	{
		client.close();
	}
}
