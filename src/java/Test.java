package src.java;


public class Test {

 	public static void main(String[] args){
 		HttpRequests obj = new HttpRequests();
        
        String[] keys = obj.getAllKeysList("Presentation_Movies_Data");
        System.out.println(keys.length);
        for (String key: keys){
            System.out.println(key + "\n");
        }

        JsonGenerator json = new JsonGenerator();
        json.bucketToJson("templates.json", "Presentation_Movies_Data");
        json.bucketToJson("data.json", "Presentation_Data");


      //  HttpRequests req = new HttpRequests();
       // req.cleanBucket("");

    }

}