package com.bentley.spring.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ExternalAPI {
	
	public static void main(String[] args) throws Exception 
	{
//		callMule("");
		 String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n"
		 		+ "<notification>\r\n"
		 		+ "    <servicedata>\r\n"
		 		+ "        <fbdKeyValueNotificationServiceData>\r\n"
		 		+ "            <parameter>\r\n"
		 		+ "                <name>VIN</name>\r\n"
		 		+ "                <value>SJAAD1Z76WC001211</value>\r\n"
		 		+ "            </parameter>       \r\n"
		 		+ "            <parameter>\r\n"
		 		+ "                <name>FriendlyName</name>\r\n"
		 		+ "                <value>FriendPixel10</value>\r\n"
		 		+ "            </parameter>\r\n"
		 		+ "      </fbdKeyValueNotificationServiceData>\r\n"
		 		+ "    </servicedata>\r\n"
		 		+ "</notification>";
	        
	        // Match anything inside <name>...</name> non-greedily using (.*?)
	        Pattern pattern = Pattern.compile("<value>(.*?)</value>");
	        Matcher matcher = pattern.matcher(xml);
	        
	        while (matcher.find()) {
	            System.out.println("Found Name: " + matcher.group(1));
	            break;
	        }
	}
	
	
	
	public void postMessage(String postPayload)  
	{
		System.out.println("=======  Post Message ======");
		System.out.println("=======  Post:: ======"+postPayload);
		WebClient webClient = WebClient.builder().build();
		
		String mulePath = "https://cariad-sys-api-dev-uwd9z4.abnaeh.gbr-e1.cloudhub.io/api/mdk";
		String response = webClient.post()
					                .uri(mulePath)
					                .header("client_id", "edb095193e694fdbbc155a3f827a4d3a")
					                .header("client_secret", "270E8197719247bca55a7D8e652865Ad")
					                .contentType(MediaType.APPLICATION_JSON)
					                .bodyValue(postPayload)
					                .retrieve()
					                .bodyToMono(String.class)
					                .block();
        System.out.println(" Response :: "+response);
		System.out.println("=======  Post After Request ======");
	}

}
