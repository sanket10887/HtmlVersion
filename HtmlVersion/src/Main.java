import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Main {

	public static void main(String[] args) {
		
		
	
		JsonArray jsonArray=new JsonArray();
		
	    JsonObject jsonObject1=new JsonObject();
	    jsonObject1.addProperty("P", "hi this is sanket");
	    jsonArray.add(jsonObject1);
	    
	    JsonObject jsonObject2=new JsonObject();
	    jsonObject2.addProperty("P", "hi this is sanket1");
	    jsonArray.add(jsonObject2);
	    
	    JsonObject jsonObject3=new JsonObject();
	    jsonObject3.addProperty("img", "images/test.png");
	    jsonArray.add(jsonObject3);

		new HTTPUnitTestServer(jsonArray);
	}

}
