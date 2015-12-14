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

import org.json.JSONArray;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.json.simple.JSONObject;






public class name {
	public static void main(String[] args) throws Exception {
            String content;
           
            
            JSONObject obj = new JSONObject();
            obj.put(null, getData());
           
    	       
    	       System.out.print(getData());
    	   
    	       System.out.println();
    		try {

    			FileWriter file = new FileWriter("/Users/tony/Desktop/cao.json");
    			file.write(obj.toJSONString());
    			file.flush();
    			file.close();

    		} catch (IOException e) {
    			e.printStackTrace();
    		}

    		System.out.print(obj);
   
    
    		

    	     }
//http://129.16.155.12:10018/buckets/Counters/keys/Burnt
	//http://129.16.155.12:10018/buckets/Counters/keys?keys=true
	
	
	   public static void getvalue(String getData, String[] keys) {
		   HttpClient client = HttpClientBuilder.create().build();
	       String key = "";
	      
	       //HttpDelete delete = new HttpDelete("http://127.0.0.1:10018/buckets/Movies_Infos/keys/" + key);
	       try {

	           for (int i = 0; i < keys.length; i++) {
	               key = keys[i].toString();
	               // URLEncoder.encode(keys[i], "UTF-8");
	               HttpGet getallkeys = new HttpGet( "http://129.16.155.12:10018/buckets/Counters/keys/" + getData);
	               HttpResponse response = client.execute(getallkeys);
	               System.out.print(key);
	               HttpEntity entity = response.getEntity();
	           }
	           
	           System.out.print(key);
	           
	           return;
	           
	       } catch (Exception e) {
	           e.printStackTrace();
	           
	       }
	    
	   }
    	
public static String getData() {
    HttpClient client = HttpClientBuilder.create().build();
    HttpGet getdata = new HttpGet("http://129.16.155.12:10018/buckets/Counters/keys?keys=true");
    //HttpGet getdata = new HttpGet("http://129.16.155.12:10018/buckets/Counters/keys/TheGirlKing");
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
}
