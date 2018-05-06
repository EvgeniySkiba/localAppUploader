package local.app.uploader;

import static org.junit.Assert.*;

import java.io.File;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.junit.Test;

import local.app.uploader.util.Tuple;

public class Example
{
	@Test
	public void testStart()
	{
		try
		{
			String appID = UUID.randomUUID().toString();
			String apiURL = "http://127.0.0.1:8081/";
			String storageFolder = "C:\\storage";
			String[] args =
			{ Config.API_URL_PREFIX + apiURL, Config.APP_ID_PREFIX + appID, Config.STORAGE_DIR_PREFIX + storageFolder };

			Config config = Config.load(args);
			System.out.println(config);
			App app = new App(config);

			Response response = app.getRequester().get(apiURL + "rest/list");
			assertEquals(200, response.getStatus());

			response = app.getRequester().get(apiURL + "rest/proxy/get");
			assertEquals(200, response.getStatus());

			String filename = "foo.bar";			
			response = app.getRequester().post(new File("C:/test/uploader/" + filename), apiURL + "rest/upload");
			assertEquals(200, response.getStatus());
			
			response = app.getRequester().get(apiURL + "rest/get", Tuple.of("filename", filename));
			assertEquals(200, response.getStatus());
			
			app.terminate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}
}
