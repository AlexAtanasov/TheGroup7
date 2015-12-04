import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.IOException;


/* This class implements a Post Http request in java
* */
public class  HttpPostRequest {


    // This method saves the movies infos in the Riak database as json object
    public static void postData(String[] info) {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://127.0.0.1:10018/buckets/MoviesInfos/keys/" + info[0]);
        post.setHeader("Content-Type", "application/json");
        String jsonString = "{\"Title\":\"" + info[0] + "\", " + 
                            "\"Year\":\"" + info[3] + "\", " + 
                            "\"IMDBRating\":\"" + info[4] + "\", " +
                            "\"Plot\":\"" + info[7] + "\", " +
                            "\"Poster\":\"" + info[5] + "\", " +
                            "\"Trailer\":\"" + info[6] + "\", " +
                            "\"HashtagL\":\"" + info[8] + "\", " +
                            "\"HashtagU\":\"" + info[9] + "\"}";

        try {
            post.setEntity(new StringEntity(jsonString, ContentType.create("application/json")));

            HttpResponse response = client.execute(post);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
