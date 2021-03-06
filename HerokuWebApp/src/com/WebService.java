package com;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.glassfish.grizzly.http.server.*;
import org.glassfish.jersey.grizzly2.GrizzlyHttpServerFactory;
import org.glassfish.jersey.media.json.JsonJacksonModule;
import org.glassfish.jersey.server.Application;
import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.core.UriBuilder;

public class WebService 
{
	public static String contentUrl;
	
	private static final String CONTENT_PATH = "/content";
	
	public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException 
	{
		final int port = System.getenv("PORT") != null ? Integer.valueOf(System.getenv("PORT")) : 8080;
		System.out.println("PORT: " + port);
		final URI baseUri = UriBuilder.fromUri("http://0.0.0.0/").port(port).build();
		ResourceConfig rc = ResourceConfig.builder().packages(WebService.class.getPackage().getName()).build();
		//rc.getProperties().put("com.sun.jersey.spi.container.ContainerRequestFilters", WebService.class.getPackage().getName() + ".AuthFilter");
        	final Application application = Application.builder(rc).build();
        	application.addModules(new JsonJacksonModule());
		final HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(baseUri, application);
		Runtime.getRuntime().addShutdownHook(new Thread() {
        		@Override
        		public void run()
        		{
        			httpServer.stop();
        		}
        	});
        
        	contentUrl = System.getenv("CONTENT_URL") != null ? System.getenv("CONTENT_URL") : CONTENT_PATH;
        	Thread.currentThread().join();
	}

}
