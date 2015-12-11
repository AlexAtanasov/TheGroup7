
import org.apache.commons.logging.Log;
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
import java.net.URLEncoder;


/* This class implements a Post Http request in java
* */
public class  HttpPostRequest {


    /* This method saves the movies infos in the Riak database as json */
    public static void postMoviesData(String[] info) {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost postData = new HttpPost("http://127.0.0.1:10018/buckets/Movie_Info/keys/" + info[9]);
        HttpPost postHashtags = new HttpPost("http://127.0.0.1:10018/buckets/Hashtags21/keys/" + info[9]);
        postData.setHeader("Content-Type", "application/json");
        postHashtags.setHeader("Content-Type", "application/json");
        String jsonString = "{\"Title\":\"" + info[0] + "\", " +
                "\"Year\":\"" + info[3] + "\", " +
                "\"IMDBRating\":\"" + info[4] + "\", " +
                "\"Plot\":\"" + info[7] + "\", " +
                "\"Poster\":\"" + info[5] + "\", " +
                "\"Trailer\":\"" + info[6] + "\", "
                //  "\"HashtagL\":\"" + info[8] + "\", " +
                //  "\"HashtagU\":\"" + info[9]
                + "\"}";

        String hashtag = "{\"Hashtag\":\"" + info[9]   + "\"}";

        try {
            //postData.setEntity(new StringEntity(jsonString, ContentType.create("application/json")));


            postData.setEntity(new StringEntity(jsonString));
            postHashtags.setEntity(new StringEntity(hashtag));

            HttpResponse response = client.execute(postData);
            HttpResponse responseHashtag = client.execute(postHashtags);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public static String getData() {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet getdata = new HttpGet("http://127.0.0.1:10018/buckets/MoviesInfo21/keys?keys=true");
       //HttpResponse response = client.execute(getdata);
         String content = "";
        try {
            HttpResponse response = client.execute(getdata);
            HttpEntity entity = response.getEntity();
             content = EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content;

    }



    public static void deleteKeys(String[] keys) {
       // String key = "%23TheHereAfter";
        String key = "";

        HttpClient client = HttpClientBuilder.create().build();
        //HttpDelete deleteData = new HttpDelete("http://127.0.0.1:10018/buckets/MoviesInfos/keys/" + key);


        try {

            for (int i = 0; i < keys.length; i++) {
               key = keys[i].toString();
              // URLEncoder.encode(keys[i], "UTF-8");
               HttpDelete deleteData = new HttpDelete("http://127.0.0.1:10018/buckets/MoviesInfo21/keys/" + key);
               deleteData.setHeader("Accept", "application/json");
               HttpResponse response = client.execute(deleteData);
           // String status = response.getStatusLine().toString();
            //Log.i(TAG, status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }




    }



}