package src.java;

import java.io.File;
import java.io.FileWriter;

public class JsonGenerator {
	// public static void main(String[] args) throws Exception {
 //        String content;


 //        JSONObject obj = new JSONObject();
 //        obj.put(null, getData());


 //        System.out.print(getData());

 //        System.out.println();
 //        try {

 //         FileWriter file = new FileWriter("/Users/tony/Desktop/cao.json");
 //         file.write(obj.toJSONString());
 //         file.flush();
 //         file.close();

 //     } catch (IOException e) {
 //         e.printStackTrace();
 //     }

 //     System.out.print(obj);




 // }
//http://129.16.155.12:10018/buckets/Counters/keys/Burnt
	//http://129.16.155.12:10018/buckets/Counters/keys?keys=true

    public JsonGenerator(){
        //Constructor for testing purposes
    }

    /*
    * Given a bucket it creates and write a json file (named "fileName") containing
    * all that bucket's data
    */
    public void bucketToJson(String fileName, String bucket){
        HttpRequests obj = new HttpRequests();
        String[] keys = obj.getAllKeysList(bucket);
        int lastIndex = keys.length - 1;

        try {
            /* create the json file in a specific directory */
            File directory = new File("json");
            directory.mkdirs();
            File file = new File(directory, fileName);
            FileWriter fileWriter = new FileWriter(file);
            //FileWriter fileWriter = new FileWriter(fileName);
              fileWriter.append("{");

                for(int i = 0; i <= keys.length - 1; i++) {
                    String key = keys[i];
                    /* Making sure do not append the "," when reach the last index,
                       { "key1": {data1}, "key2": {data2}, "key3": {data3} } */
                    if(i == lastIndex) {
                        fileWriter.append( '"' + key + '"' + ':');
                        fileWriter.append("\n");
                        fileWriter.append(obj.getKeyData(bucket, key));
                        fileWriter.append("\n");

                    }  else{
                        fileWriter.append('"' + key + '"' + ':');
                        fileWriter.append("\n");
                        fileWriter.append(obj.getKeyData(bucket, key));
                        fileWriter.append(",\n");
                    }
                }

            fileWriter.append("}");
            fileWriter.close();
        } catch (Exception e) {
            System.err.println("Error writing file: " + fileName);
        }

    }

//  public static void getvalue(String getData, String[] keys) {
//    HttpClient client = HttpClientBuilder.create().build();
//    String key = "";

// 	       //HttpDelete delete = new HttpDelete("http://127.0.0.1:10018/buckets/Movies_Infos/keys/" + key);
//    try {

//     for (int i = 0; i < keys.length; i++) {
//         key = keys[i].toString();
// 	               // URLEncoder.encode(keys[i], "UTF-8");
//         HttpGet getallkeys = new HttpGet( "http://129.16.155.12:10018/buckets/Counters/keys/" + getData);
//         HttpResponse response = client.execute(getallkeys);
//         System.out.print(key);
//         HttpEntity entity = response.getEntity();
//     }

//     System.out.print(key);

//     return;

// } catch (Exception e) {
//     e.printStackTrace();

// }

// }

// public static String getData() {
//     HttpClient client = HttpClientBuilder.create().build();
//     HttpGet getdata = new HttpGet("http://129.16.155.12:10018/buckets/Counters/keys?keys=true");
//     //HttpGet getdata = new HttpGet("http://129.16.155.12:10018/buckets/Counters/keys/TheGirlKing");
//     //HttpResponse response = client.execute(getdata);
//     String content = "";

    
    
    
//     try {
//         HttpResponse response = client.execute(getdata);
//         HttpEntity entity = response.getEntity();
//         content = EntityUtils.toString(response.getEntity());
//     } catch (IOException e) {
//         e.printStackTrace();
//     }

//     return content;

// }


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
    }




}
