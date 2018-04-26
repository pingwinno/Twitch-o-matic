package com.pingwinno.SubscriptionHandler;


import com.google.gson.Gson;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class SubscriptionQuery {

   private String serverAddress;
   private String postData;
   private Gson gson = new Gson();

public SubscriptionQuery(String serverAddress, JsonSubObject jsonSubObject)
{
   this.serverAddress = serverAddress;
   postData = gson.toJson(jsonSubObject);
   System.out.println(postData);
}

public void startSub () throws IOException {

   CloseableHttpClient client = HttpClients.createDefault();
   HttpPost httpPost = new HttpPost(serverAddress);
   StringEntity postBody = new StringEntity(postData);

   httpPost.addHeader("Content-Type", "application/json");
   httpPost.addHeader("Client-ID", "4zswqk0crwt2wy4b76aaltk2z02m67");
   httpPost.setEntity(postBody);

   CloseableHttpResponse response = client.execute(httpPost);
   System.out.println(response.getStatusLine().getStatusCode());
   System.out.println(response.getEntity());
   client.close();


}



}
