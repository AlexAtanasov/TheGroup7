package src.java;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;


/* This class implements a Post Http request in java
* */
public class  HttpPostRequest {

    HttpRequests request = new HttpRequests();
    String bucket;
    static String movies_bucket = "Cinema_Chatter_Movies_Data";
    static String hashtags_bucket = "Cinema_Chatter_Hashtags";
    static String counters_bucket = "Cinema_Chatter_Counters";
    static HttpClient client = HttpClientBuilder.create().build();

    static String IP = "127.0.0.1";
    static String PORT = "10018";


    public static String  getDate() {
        Date date = new Date( );
        Format dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String currentDate = dateFormat.format(date);

        return currentDate;
    }


       /* This method saves the movies infos in the Riak database as json */
    public void postMoviesData(String[] info) {

        String movies_url = "http://" + IP + ":" + PORT + "/buckets/" + movies_bucket + "/keys/" + info[9];
        String hashtags_url = "http://" + IP + ":" + PORT + "/buckets/" + hashtags_bucket + "/keys/" + info[9];
        String counters_url = "http://" + IP + ":" + PORT + "/buckets/" + counters_bucket + "/keys/" + info[9];

        String date = getDate();

        String jsonMovies = "{\"title\":\"" + info[0] + "\", " +
                "\"year\":\"" + info[3] + "\", " +
                "\"imdbrating\":\"" + info[4] + "\", " +
                "\"plot\":\"" + info[7] + "\", " +
                "\"poster\":\"" + info[5] + "\", " +
                "\"trailer\":\"" + info[6] + "\""
                + "\"}";

        String jsonHashtag = "{\"hashtag\":\"" + info[9]   + "\"}";
        String jsonCounter = "{\"Date\":\"" + date +  "\"}";
        request.post(movies_url, jsonMovies);
        request.post(hashtags_url, jsonHashtag);
        request.post(counters_url, jsonCounter);

    }


}