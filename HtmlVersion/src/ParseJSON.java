import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class ParseJSON {
	static final String HTML_START = "<html>" + "<title>HTTP Unittest Server in java</title>" + "<body>";
	static final String HTML_END = "</body>" + "</html>";
	String sHTMLTag="";
	public ParseJSON(JsonArray jsonArray) {
	
		sHTMLTag+=HTML_START;
		if(jsonArray!=null)
		{
			for(JsonElement jsonElement:jsonArray){
				for (Map.Entry<String, JsonElement> entry : ((JsonObject)jsonElement).entrySet()){
		            String key = entry.getKey();
		            String value = entry.getValue().getAsString();
		            System.out.println(key+":"+value);
		            if(key.equalsIgnoreCase("P"))
		            {
		            	sHTMLTag+= "<"+key+">"+value+"</"+key+">";
		            	
		            }
		            else if(key.equalsIgnoreCase("img"))
		            {
		            	sHTMLTag+= "<"+key+" src='"+value+"'/>";
		            }
				}
			}
		}
		sHTMLTag+=HTML_END;
	}

	public void createHTML(){
		 try{
			File file = new File("web");
			if(!file.exists()){
				file.mkdirs();
			}
		    FileWriter fstream = new FileWriter(file+"/index.html");
		    BufferedWriter out = new BufferedWriter(fstream);
		    out.write(sHTMLTag.toString().trim());
		    out.close();
		    
	    }
		catch (Exception e){
			e.printStackTrace();
	    }
	}
}
