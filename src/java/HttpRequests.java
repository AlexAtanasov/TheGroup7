package src.java;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.io.IOException;



/* This class implements POST, GET and DELETE HttpRequests in java
* */
public class HttpRequests {
    HttpClient client = HttpClientBuilder.create().build();
    HttpGet get;
    HttpPost post;
    HttpDelete delete;
    HttpResponse response;
    HttpEntity entity;
    String bucket;
    String key;
    static String IP = "127.0.0.1";
    static String PORT = "10018";

    public HttpRequests(){
        //Constructor for testing purposes
    }


    public void post(String url, String entity) {
        post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json");
        try {
            post.setEntity(new StringEntity(entity));
            response = client.execute(post);
        } catch (IOException e) {
            e.printStackTrace();
        }

     }


    public String getAllKeys(String bucket) {
        this.bucket = bucket;
        String url = "http://" + IP + ":" + PORT + "/buckets/" + bucket + "/keys?keys=true";
        this.get = new HttpGet(url);
        String content = "";
        try {
            this.response = client.execute(get);
            this.entity = response.getEntity();
            content = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /*
    * Given a bucket, it returns all an array of its keys
    */
    public String[] getAllKeysList(String bucket) {
        String url = "http://" + IP + ":" + PORT + "/buckets/" + bucket + "/keys?keys=true";
        String keysStr = httpGet(url);
        keysStr = keysStr.substring(10, keysStr.length()-3);
        String[] keys = keysStr.split("\",\"");
        return keys;
    }

    /*
    * Helper method: given a url it returns a string of its content
    */
    private String httpGet(String url) {
        this.get = new HttpGet(url);
        String content = "";
        try {
            this.response = client.execute(get);
            this.entity = response.getEntity();
            content = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            System.err.println("Error in httpGet(url) with url = " + url);
            e.printStackTrace();
        }
        return content;
    }


   /*This function returns all buckets in a given riak IP/Port*/
    public String getAllBuckets(String ip, String port) {
        String url = "http://" + ip + ":" + port + "/buckets?buckets=true";
        this.get = new HttpGet(url);
        String content = "";
        try {
            this.response = client.execute(get);
            this.entity = response.getEntity();
            content = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;

    }


    /* This function returns the values of a given bucket/key */
    public String getKeyData(String bucket, String key) {
        this.bucket = bucket;
        this.key = key;
        String url = "http://" + IP + ":" + PORT + "/buckets/" + bucket + "/keys/" + key;
        this.get = new HttpGet(url);
        String content = "";
        try {
            this.response = client.execute(get);
            this.entity = response.getEntity();
            content = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;

    }

    /* This function deletes the values of a given key/bucket*/
    public void deleteKey(String bucket, String key) {
        this.bucket = bucket;
        this.key = key;
        String url = "http://" + IP + ":" + PORT + "/buckets/" + bucket + "/keys/" + key;
        HttpClient client = HttpClientBuilder.create().build();
        //HttpDelete deleteData = new HttpDelete("http://127.0.0.1:10018/buckets/Movies_Infos/keys/" + key);
        try {
                delete = new HttpDelete(url);
                delete.setHeader("Accept", "application/json");
               this.response = client.execute(delete);

        } catch (Exception e) {
            e.printStackTrace();
    }

    }

    /* This function deletes all keys of a given bucket/keys */
   public void deleteAllKeys(String bucket, String[] keys) {
       this.bucket = bucket;
       String key = "";
       String url = "http://" + IP + ":" + PORT + "/buckets/" + bucket + "/keys/" + key;
       //HttpDelete delete = new HttpDelete("http://127.0.0.1:10018/buckets/Movies_Infos/keys/" + key);
       try {

           for (int i = 0; i < keys.length; i++) {
               key = keys[i].toString();
               // URLEncoder.encode(keys[i], "UTF-8");
               this.delete = new HttpDelete(url);
               delete.setHeader("Accept", "application/json");
              this.response = client.execute(delete);
           }
       } catch (Exception e) {
           e.printStackTrace();
       }

   }


    public static void deleteAllKeys(String[] keys) {
        String key = "";
        HttpClient client = HttpClientBuilder.create().build();

        try {

            for (int i = 0; i < keys.length; i++) {
                key = keys[i].toString();
                HttpDelete deleteData = new HttpDelete("http://" + IP + ":" + PORT + "/buckets/Movie_Info/keys/" + key);
                deleteData.setHeader("Accept", "application/json");
                HttpResponse response = client.execute(deleteData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}