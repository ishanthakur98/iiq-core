package com.example.kafka.Test;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.net.URL;

@Path("/test")
public class Health {

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Response health(){
        System.out.println("inside health check");
        try {
            URL uri = new URL("http://localhost:8080/iiq/login.jsf");
            HttpURLConnection httpURLConnection = (HttpURLConnection) uri.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.connect();
            int code = httpURLConnection.getResponseCode();
            if(code >= 200 && code < 300){
                return Response.ok("Ok boss").build();
            }

            return Response.status(503).entity("login.jsp code: " + code).build();
        }catch (Exception e){
            return Response.status(503).entity("login.jsp unreachable").build();
        }
    }
}
