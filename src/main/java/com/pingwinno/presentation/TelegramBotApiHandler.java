package com.pingwinno.presentation;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.*;
import java.util.Base64;

@Path("/cluster_api")
public class TelegramBotApiHandler {

    @Path("/addressCheck")
    @GET
    public Response getAddress(@HeaderParam("challenge") String challenge) {
        Response response;
        response = Response.status(Response.Status.OK).entity(challenge).build();
        System.out.println("challenge is:" + challenge);

        return response;
    }


    @Path("/auth")
    @GET
    public Response getAuthentication(@Context UriInfo info) {
        try {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpget = new HttpGet(Base64.getDecoder().
                decode(info.getQueryParameters().getFirst("callback_url")).toString());
            CloseableHttpResponse response = httpClient.execute(httpget);
        HttpEntity entity = response.getEntity();
            if (entity != null) {


                BufferedInputStream bis = new BufferedInputStream(entity.getContent());
                String filePath = "sample.txt";
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                int inByte;
                while((inByte = bis.read()) != -1) bos.write(inByte);
                bis.close();
                bos.close();
            }
            httpClient.close();
            response.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return Response.status(Response.Status.ACCEPTED).build();
    }
}
