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
    public static void postData(String title, String year, String imdbRating, String plot, String posterURL, String hashtagL, String hashtagU ) {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://127.0.0.1:10018/buckets/MoviesInfos/keys/" + title);
        post.setHeader("Content-Type", "application/json");

        try {
            post.setEntity(new StringEntity("{\"title\":\"" + title + " \"year\":\"" + year + " \"imdbRating\":\"" + imdbRating + " \"plot\":\"" +
                    plot + " \"posterURL\":\"" + posterURL + " \"hashtagL\":\"" + hashtagL + " \"hashtagU\":\"" + hashtagU + "\"} ", ContentType.create("application/json")));

            HttpResponse response = client.execute(post);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
