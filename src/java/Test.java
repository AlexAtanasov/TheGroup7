package src.java;


public class Test {

 	public static void main(String[] args){
 		HttpRequests obj = new HttpRequests();
        
        String[] keys = obj.getAllKeysList("Rachele");
        for (String key: keys){
            System.out.println(key + "\n");
        }

        JsonGenerator json = new JsonGenerator();
        json.bucketToJson("Test.json", "RacheleTest");
    }

}