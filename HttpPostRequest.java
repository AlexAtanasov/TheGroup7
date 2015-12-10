
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.IOException;


/* This class implements a Post Http request in java
* */
public class  HttpPostRequest {


    /* This method saves the movies infos in the Riak database as json */
    public static void postMoviesData(String[] info) {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost postData = new HttpPost("http://127.0.0.1:10018/buckets/MoviesInfo/keys/" + info[9]);
        HttpPost postHashtags = new HttpPost("http://127.0.0.1:10018/buckets/Hashtags/keys/" + info[9]);
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






}