package ca.bc.gov.nrs.dm.microservice.model;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MessageHelper {

	public static Response generateMessage(Status status, String messageInfo, String description) {
		ResponseBuilder responseBuilder = Response.status(status);
		Message message = new Message();
		message.setCode("-1");
		message.setMessage(messageInfo);
		message.setDescription(description);
		
		Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(message);
         
		responseBuilder.entity(jsonString);
		return responseBuilder.build();
	}
}
