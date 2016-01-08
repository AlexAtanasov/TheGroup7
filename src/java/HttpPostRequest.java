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
    static String movies_bucket = "Presentation_Movies_Data";
    //static String hashtags_bucket = "Presentation_Hashtags";
    static String counters_bucket = "Presentation_Counters";
    static String presentation_bucket = "Presentation_Data";
    static HttpClient client = HttpClientBuilder.create().build();


    static String IP = "129.16.155.12";
    static String PORT = "10018";


   private static String  getDate() {
        Date date = new Date( );
        Format dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        String currentDate = dateFormat.format(date);

        return currentDate;
    }


       /* This method saves the movies infos in the Riak database as json */
    public void postMoviesData(String[] info) {

        String movies_url = "http://" + IP + ":" + PORT + "/buckets/" + movies_bucket + "/keys/" + info[9];
        //String hashtags_url = "http://" + IP + ":" + PORT + "/buckets/" + hashtags_bucket + "/keys/" + info[9];
        String counters_url = "http://" + IP + ":" + PORT + "/buckets/" + counters_bucket + "/keys/" + info[9];
        String presentation_url = "http://" + IP + ":" + PORT + "/buckets/" + presentation_bucket + "/keys/" + info[9];

       // String date = getDate();

        String jsonMovies = "{\"title\":\"" + info[0].replaceAll("\\+", " ") + "\", " +
                "\"year\":\"" + info[3] + "\", " +
                "\"rating\":\"" + info[4] + "\", " +
                 "\"imdbID\":\"" + info[2] + "\", " +
                "\"plot\":\"" + info[7] + "\", " +
                "\"poster\":\"" + info[5] + "\", " +
                  "\"genre\":\"" + info[10] + "\", " +
                 "\"director\":\"" + info[11] + "\", " +
                 "\"actors\":\"" + info[12] + "\", " +
                "\"trailer\":\"" + info[6] + "\", " +
                "\"key\":\"" + info[9]
                + "\"}";

       // String jsonHashtag = "{\"hashtag\":\"" + info[9]   + "\"}";
        //String jsonCounter = "{\"Date\":\"" + date +  "\"}";
            String jsonCounter = "";
            String jsonpresentation = "";
        request.post(movies_url, jsonMovies);
      //  request.post(hashtags_url, jsonHashtag);
        request.post(counters_url, jsonCounter);
        request.post(presentation_url, jsonpresentation);

    }



}