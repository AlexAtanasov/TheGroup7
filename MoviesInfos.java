import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Scanner;
import java.net.URL;
import java.io.FileWriter;

public class MoviesInfos extends HttpPostRequest {

    public static final String sfURL = "http://www.sf.se/filmer/?city=goteborg";


    /*
    * This function connects to the sf.se page showing the movies in Göteborg
    * and returns the titles in a list.
    */
    public static String[] getMovies() throws Exception {

        String[] movies_temp = new String[500];

        URL url = new URL(sfURL);

        Scanner in = new Scanner(new InputStreamReader(url.openStream()));

        // Go to the line where movies start
        while (in.findInLine("ChooseMovieMenu") == null) {
            in.nextLine();
        }
        in.nextLine();
        in.nextLine();
        in.nextLine();

        // Go through the page and save the titles in movies_temp
        int index = 0;

        while (true) {

            in.findInLine(">");
            String line = in.nextLine();

            if(line.length()>2){

                line = line.substring(0,line.length()-4);

                // Excluding titles that are not movies.
                if(!(line.contains("Balett")||line.contains("Klassiker")||
                        line.contains("Konsertfilm")||line.contains("Museivisning")||
                        line.contains("Opera")||line.contains("Dokumentär")||line.contains("Konst"))) {

                    // Cleaning up the titles
                    if (line.contains("(otextad)")){
                        line = line.substring(0, line.length()-10);
                    } else if (line.contains("(eng text)")) {
                        line = line.substring(0, line.length()-11);
                    }

                    movies_temp[index] = line;
                    index ++;
                }

            } else break;

            // Spaces in between one title and the other
            in.nextLine();
            in.nextLine();
            in.nextLine();

        }

        // Counting how many titles where found
        int count = 0;
        for (String m: movies_temp) {
            if(m != null){
                count ++;
            }
        }

        // Creating the list of correct length to return, trasferring the titles there
        String[] movies = new String[count];
        for (int i=0; i<count; i++) {
            movies[i] = movies_temp[i];
        }

        return movies;
    }

    public static String getPoster(String swedishTitle) throws Exception {
        String poster = "";

        URL url = new URL(sfURL);

        Scanner in = new Scanner(new InputStreamReader(url.openStream()));

        //Go to the point in the page where the poster is.
        while (in.findInLine(swedishTitle) == null) {
            in.nextLine();
        }
        while (in.findInLine(swedishTitle) == null) {
            in.nextLine();
        }
        while (in.findInLine("posterTrigger") == null) {
            in.nextLine();
        }
        while (in.findInLine("<img src=") == null) {
            in.nextLine();
        }

        poster = in.nextLine();

        //Trim poster url
        poster = poster.substring(1);
        poster = poster.substring(0, poster.indexOf('"'));

        //Add the beginning of the url
        poster = "http://www.sf.se" + poster;

        return poster;

    }


    /*
    * This fuction takes in the imdb ID of a movie (String) and returns the link
    * its trailer (String). If no trailer was found it returns "No trailer available".
    */
    public static String getTrailer(String imdbID) throws Exception{
        String trailer = "No trailer available";
        String imdbURL = "http://www.imdb.com/title/" + imdbID;
        URL url = new URL(imdbURL);
        Scanner in = new Scanner(new InputStreamReader(url.openStream()));
        while ((in.findInLine("data-video") == null) && (in.hasNextLine())) {
            in.nextLine();
        }
        if (in.hasNext()) {
            trailer = in.nextLine();
            trailer = trailer.substring(2);
            trailer = trailer.substring(0, trailer.indexOf('"'));
            trailer = "http://www.imdb.com/video/imdb/" + trailer + "/imdb/embed?autoplay=false&width=480";
        }
        return trailer;

    }



    /*
    * This fuction takes in the title of a movie (String) and returns the IMDB ID
    * (String) of the movie.
    */
    public static String getIMDBid(String title) throws Exception {
        String result = "";
        String titlePlus = spacesToPlus(title);

        // Searches for the title on IMDB
        String imdbSearchURL = "http://www.imdb.com/find?ref_=nv_sr_fn&q=" + titlePlus + "&s=all";
        URL url = new URL(imdbSearchURL);
        Scanner in = new Scanner(new InputStreamReader(url.openStream()));

        // Goes to the right point in the page (first result of the search)
        while ((in.findInLine("findResult") == null) && (in.hasNextLine())) {
            in.nextLine();
        }

        // Cleans up the line and gets just what we want
        result = in.nextLine();
        result = result.substring(result.indexOf("/title/")+7);
        result = result.substring(0, result.indexOf("/?ref"));

        return result;
    }



    /*
    * Simple function to replace all spaces in a string with +
    */
    public static String spacesToPlus(String aStr) {
        String result = "";
        int len = aStr.length();
        for (int i=0; i<len; i++) {
            if (aStr.substring(i, i+1).equals(" ")) {
                result += "+";
            } else {
                result += aStr.substring(i, i+1);
            }
        }
        return result;
    }



    /*
    * The function takes in the IMDB ID of a movie (as a String) and returns a list
    * with [title, year, IMDB rating, plot, hashtagL, hashtagU].
    */
    public static String[] getIMDBinfo(String imdbID) throws Exception {

        String[] new_info = new String[6];

        // We use the OMDB api
        String omdbURL = "http://www.omdbapi.com/?i=" + imdbID + "&y=&plot=full&r=json";

        URL url = new URL(omdbURL);
        Scanner in = new Scanner(new InputStreamReader(url.openStream()));

        String infoStr = in.nextLine();

        // How to handle an error.
        String notFound = "{\"Response\":\"False\",\"Error\":\"Movie not found!\"}";
        if (infoStr.equals(notFound)) {
            for (int i=1; i<new_info.length; i++) {
                new_info[i] = "ERROR";
            }
        } else {

            // The string is split, the info are fetched and saved in the list.
            String[] info = infoStr.split("\",\"");

            new_info[0] = info[0].substring(10, info[0].length());
            new_info[1] = info[1].substring(info[1].length()-4, info[1].length());
            new_info[2] = info[15].substring(13, info[15].length());
            new_info[3] = info[9].substring(7, info[9].length());

            new_info[4] = generateHashtags(new_info[0])[0];
            new_info[5] = generateHashtags(new_info[0])[1];
        }

        return new_info;
    }

    /*
    * This function generates possible hashtags for a given title (given as a String).
    */
    public static String[] generateHashtags (String title) {
        String[] hashtags = new String[2];
        int len = title.length();

        //Generating hashtag as title without spaces and all lowercase
        String hashtagL = "#";
        for (int i=0; i<len; i++) {
            if (!title.substring(i, i+1).equals(" ")) {
                hashtagL += title.substring(i, i+1).toLowerCase();
            }
        }

        //Generating hashtag as title without spaces and capitalization
        String hashtagU = "#";
        hashtagU += title.substring(0,1).toUpperCase();
        for (int i=1; i<len; i++) {
            if (title.substring(i, i+1).equals(" ")) {
                hashtagU += title.substring(i+1, i+2).toUpperCase();
                i++;
            } else {
                hashtagU += title.substring(i, i+1).toLowerCase();
            }
        }

        String[] result = new String[2];
        result[0] = hashtagL;
        result[1] = hashtagU;
        return result;

    }




    /*
    * This function uses the previous ones to fecth the movies, their infos and
    * it prints them.
    */
    public static void printMoviesInfos() throws Exception {
        String[] movies = getMovies();
        System.out.println("Movie list:");
        for (String movie : movies) {
            try {
                String imdbID = getIMDBid(movie);
                String[] info = getIMDBinfo(imdbID);
                // URLEncoder.encode(info[6], "UTF-8").replaceAll("\\+", "%20")
                System.out.println("Title: " + URLEncoder.encode(info[0], "UTF-8").replaceAll("\\+", "%20"));
                System.out.println("Year: " + info[1]);
                System.out.println("Rating: " + info[2]);
                System.out.println("Plot: " + info[3]);
                System.out.println("PosterURL: " +info[4]);
                System.out.println("-----------------------------------------");
            } catch (Exception e) {
                System.out.println("ERROR with movie: " + movie);
            }
        }
    }

    /*
    * This function creates a JSON file (file_name) collecting all the movies and their
    * infos.
    */
    public static void writeToJSON(String file_name) {
        String json = "";
        String new_line = "\n";
        String comma = ",";
        String open = "{";
        String close = "}";

        String title = "\"movie\": ";
        String pop = "\"pop\": ";
        String rating = "\"rating\": ";
        String plot = "\"plot\": ";
        String trailer = "\"trailer\": ";
        String poster = "\"poster\": ";


        try{
            FileWriter file_writer = new FileWriter(file_name);

            file_writer.append("[");
            file_writer.append(new_line);

            String[] movies = getMovies();

            for (String movie : movies) {
                try {
                    String imdbID = getIMDBid(movie);
                    String[] info = getIMDBinfo(imdbID);
                    file_writer.append(open);
                    file_writer.append(new_line);
                    file_writer.append(title + '"'+info[0]+'"');
                    file_writer.append(comma);
                    file_writer.append(new_line);
                    file_writer.append(pop + "\"2,547\"");
                    file_writer.append(comma);
                    file_writer.append(new_line);
                    file_writer.append(rating + '"' + info[2] + '"');
                    file_writer.append(comma);
                    file_writer.append(new_line);
                    file_writer.append(plot + '"'+info[3]+'"');
                    file_writer.append(comma);
                    file_writer.append(new_line);
                    file_writer.append(trailer + "\"http://www.imdb.com/video/imdb/vi2482677785/imdb/embed?autoplay=false&width=480\"");
                    file_writer.append(comma);
                    file_writer.append(new_line);
                    file_writer.append(poster+'"'+info[4]+'"');
                    file_writer.append(new_line);
                    file_writer.append(close);
                    file_writer.append(comma);
                    file_writer.append(new_line);
                } catch (Exception e) {
                    System.out.println("ERROR with movie: " + movie);
                }
            }

            file_writer.close(); // Closing the file so everything gets written

        } catch (Exception e){
            System.out.println("ERROR");
        }
    }




    /*
    * This function creates a file (file_name) collecting all the movies and their
    * infos.
    */
    public static void writeToCSV(String file_name) {

        String comma = ",";
        String new_line = "\n";
        String header = "Title,Year,Plot,PosterURL,IMDBrating,hashtagL,hashtagU Close";


        try{
            FileWriter file_writer = new FileWriter(file_name);

            file_writer.append(header);
            file_writer.append(new_line);

            String[] movies = getMovies();

            for (String movie : movies) {
                try {
                    String imdbID = getIMDBid(movie);
                    String[] info = getIMDBinfo(imdbID);
                    file_writer.append(info[0]);
                    file_writer.append(comma);
                    file_writer.append(info[1]);
                    file_writer.append(comma);
                    file_writer.append('"' +info[3] + '"');
                    file_writer.append(comma);
                    file_writer.append(info[4]);
                    file_writer.append(comma);
                    file_writer.append(info[2]);
                    file_writer.append(comma);
                    file_writer.append(info[5]);
                    file_writer.append(comma);
                    file_writer.append(info[6]);
                    file_writer.append(new_line);
                } catch (Exception e) {
                    System.out.println("ERROR with movie: " + movie);
                }
            }

            file_writer.close(); // Closing the file so everything gets written

        } catch (Exception e){
            System.out.println("ERROR");
        }
    }



    // [English Title, Swedish Title, IMDB ID, Year, IMDB Rating, Poster, Trailer, Plot, hastag_lowercase, hastag_camelcase]
    public static String[] getAllInfos(String movie) throws Exception {
        String[] result = new String[10];

        result[1] = movie;

        result[5] = getPoster(movie);
        result[2] = getIMDBid(movie);
        result[6] = getTrailer(result[2]);
        String[] imdbInfo = getIMDBinfo(result[2]);
        result[0] = spacesToPlus(imdbInfo[0]);
        result[3] = imdbInfo[1];
        result[4] = imdbInfo[2];
        result[7] = imdbInfo[3];
        result[8] = imdbInfo[4];
        //URLEncoder.encode(info[0], "UTF-8").replaceAll("\\+", "%20")
       // result[9] = imdbInfo[5];
        result[9] = URLEncoder.encode(imdbInfo[5], "UTF-8");


        return result;
    }

    // This method saves all movies infos in the Riak database
    public static void saveInRiak() throws Exception {
        String[] movies = getMovies();
        for(String movie : movies ) {
            // System.out.println("Current: " + movie);
            try {

                String[] info = getAllInfos(movie);
                postMoviesData(info);
            } catch (Exception e) {
                System.out.println("ERROR with movie: " + movie);
            }
            // System.out.println("Saving: " + movie);
        }
    }


    /*This method compares two string arrays
    * */
    public static void checkTitles(String[] array1, String[] array2) {

        boolean found;
        for (int i = 0; i < array1.length; i++)
        {
            found = false;
            for (int x = 0; x < array2.length; x++)
                if(array1[i].equals(array2[x]))
                {
                    System.out.println(array1[i] + " is equal to " + array2[x]);
                    found = true;
                }
            if (!found)
                System.out.println(array1[i] + " is not equal to any element");

        }
    }


    public static void main(String[] args) throws Exception {
        // writeToCSV("MovieTitles.csv");
        //  printMoviesInfos();
         //   saveInRiak();
          String data = getData();
          System.out.println(data);
        //  String[] titles = new String[] {get.getContent()};
          String[] titles = data.split(",");
          titles[0] = titles[0].substring(10, titles[0].length()-1);
          for (int i=1; i<titles.length;i++) {
             titles[i] = titles[i].substring(1, titles[i].length()-1);
          }
          titles[titles.length-1] = titles[titles.length-1].substring(0, titles[titles.length-1].length()-2);

        //  JSONObject jsnObject = new JSONObject(get.getContent());
        // JSONArray jsonArray = new JSONArray(jsnObject);
        //  System.out.println(jsonArray);
        // JSONArray array = jsnObject.getJSONArray("keys");
        //  for (int i = 0; i < array.length(); i++) {
        //     JSONObject result = array.getJSONObject(i);
        //  System.out.println(result);
        //  }

         System.out.println("Array length: " + titles.length);
      //  System.out.println("Content: " + Arrays.toString(titles));

          deleteKeys(titles);

         for(int i = 0; i < titles.length; i++)
            System.out.println(titles[i]);

        //    String[] array1 = {"An", "Spectre", "The Nut Job", "Legend"};
        //   String[] array2 = {"An", "Spectre", "The Here After", "Burnt", "Legend"};
        //   checkTitles(array1, array2);

        //writeToJSON("movies.json");
        //System.out.println(getTrailer("tt1355683"));
        //   String[][] allmovies = getAllInfos();
        //   for (String[] movie : allmovies) {
        //      for (String info: movie) {
        //          System.out.println(info);
        //     }
        //   }
    }



}