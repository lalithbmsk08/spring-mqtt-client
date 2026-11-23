package com.bentley.spring.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bentley.spring.model.MqttSubscribeModel;

import jakarta.servlet.http.HttpSession;

@RestController
public class MQTTController {

	private final TokenService tokenService;
	private final ExternalAPI muleAPI;
	
	public MQTTController(TokenService tokenService, ExternalAPI muleAPI) {
		this.tokenService = tokenService;
		this.muleAPI = muleAPI;
	}
	
	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome to Spring Boot";
	}
	
	@GetMapping("/testMessage")
	public String testMessage() {
		System.out.println(" ======= Test Post Call Mule 2 =====");
		
		String password = tokenService.getAccessToken();
		
		System.out.println(" Token ::"+password);
		
		String xml = """
                <?xml version="1.0" encoding="UTF-8"?>
                <notification>
                    <servicedata>
                        <fbdKeyValueNotificationServiceData>
                            <parameter>
                                <name>VIN</name>
                                <value>SJAAD1Z76WC001211</value>
                            </parameter>
                            <parameter>
                                <name>PASSWORD</name>
                                <value>14530308</value>
                            </parameter>
                            <parameter>
                                <name>FriendlyName</name>
                                <value>FriendPixel10</value>
                            </parameter>
                        </fbdKeyValueNotificationServiceData>
                    </servicedata>
                </notification>
                """;

		StringBuilder stb = new StringBuilder();
		  stb.append("{");
		  stb.append("\"topic\":\"");
		  stb.append("EMAIL/223660/XID_BS_MDK2/Bentley/MDKv2/PairingPassword/default/jjoVCZ5glLPCYxlKk65nNaPWZS21");
		  stb.append("\",");
		  stb.append("\"data\":");
		  stb.append(extractParametersAsJson(xml));
		  stb.append("\"}");
		  
		muleAPI.postMessage(stb.toString());		
		System.out.println("*****************Test Message sent successfully *****************************");
		return "Successfully Tested";
	}

	@GetMapping("subscribe")
    public List<MqttSubscribeModel> subscribeChannel(HttpSession session)
            throws InterruptedException, org.eclipse.paho.client.mqttv3.MqttException 
	 {
        List<MqttSubscribeModel> messages = new ArrayList<>();
		System.out.println(" ======= Sub MQTT Message Mule 12 =====");

		 String broker	= "ssl://mnp-con.core-odp10-preprod.eu.dp15.vwg-connect.com:8883";
		String clientId	= "mqttxsdf_56eb9a09234";
		String topic	= "EMAIL/223660/XID_BS_MDK2/Bentley/MDKv2/#";
		String username = "GAUTH";
		//String password = tokenService.getAccessToken();
		String password = "eyJhbGciOiJFUzI1NiIsImtpZCI6IjQwZDVmZmJjLTU1OTctMjIwYS0zOWY4LTUxMDI2MTI0MGE1ZCIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsibXF0dCJdLCJjY2giOiJUSElSRF9QQVJUWSIsImNuZiI6eyJ4NWRuI3JmYzIyNTMiOiJTVD1DaGVzaGlyZSxMPUNyZXdlLENOPWFwaS10ZXN0LmJlbnRsZXltb3RvcnMuY29tLE89QmVudGxleSBNb3RvcnMgTGltaXRlZCxDPUdCIiwieDV0Ijoia0FRckRyWjhHWk1fM3ZWcWNnb1VnUEh3bEVFIn0sImV4cCI6MTc5NjA1ODEwMCwiaWF0IjoxNzk1NDUzMzAwLCJpc3MiOiJodHRwczovL2FwcC5lY2UuZ2F1dGgtdndhYy5jb20iLCJqdGkiOiI3ZmI4NWVhMC1jYzMwLTQ1OGEtYjUzOS1mODExMzNjYTRiNzAiLCJzY3AiOlsibXF0dCIsIm1ucCIsInQwX2Q6YmVudGxleW1kayJdLCJzdHlwIjoiVDAiLCJzeXMiOiJYSURfQkVOVExFWV9NVUxFU09GVCIsInRudCI6IkJlbnRsZXksRUNFIiwidHlwIjoiQVQiLCJ2ZXIiOiIxIn0.jZUkmmNH4t9s-2PP7DF9tvjQoieLoFqud6IpFuXJqvXUEgIyeHLIFdBZs6JYY21jAPIBPiyG1VrBWcoK20F1tA";
		 
		System.out.println(" Token ::"+password);
		try 
		{
			ClassPathResource resource = new ClassPathResource("truststore.jks");
			File trustStoreFile = File.createTempFile("truststore", ".jks");
			try (InputStream is = resource.getInputStream()) {
	            Files.copy(is,
	                    trustStoreFile.toPath(),
	                    StandardCopyOption.REPLACE_EXISTING);
	        }
			Properties sslProps = new Properties();
			sslProps.setProperty("com.ibm.ssl.trustStore", trustStoreFile.getAbsolutePath());
			sslProps.setProperty("com.ibm.ssl.trustStorePassword", "pass1234");
			
			MqttClient client = new MqttClient(broker, clientId);
			MqttConnectOptions options = new MqttConnectOptions();
				options.setUserName(username);
				options.setPassword(password.toCharArray());
				options.setCleanSession(false);
			    options.setKeepAliveInterval(30);
				options.setAutomaticReconnect(true);
				options.setSSLProperties(sslProps);
			
			System.out.println("==== b4 Connected to broker ====");
			client.connect(options);
			System.out.println("==== Af Connected to broker ====");
			System.out.println("Client IsConnected ::" + client.isConnected());

			System.out.println("*************** Mule Post ********************");
			client.subscribe(topic, (receivedTopic, message) -> 
			{
				System.out.println("*************** Subscrie Message ********************");
				String payload = new String(message.getPayload());
				System.out.println("*****************************************************");
				
				System.out.println("Topic   :" + receivedTopic);
				System.out.println("Message :" + payload);
				System.out.println("QoS     :" + message.getQos());

				StringBuilder stb = new StringBuilder();
							  stb.append("{");
							  stb.append("\"topic\":\"");
							  stb.append(receivedTopic);
							  stb.append("\",");
							  stb.append("\"data\":");
							  stb.append(extractParametersAsJson(payload));
							  stb.append("\"}");
				
				System.out.println("*************** after String Builder ********************");
				ExternalAPI extMuleAPI = new ExternalAPI();
				extMuleAPI.postMessage(stb.toString());
				System.out.println("*************** Post Mule API ********************");
				
			});
			System.out.println("****************  Post MQTT Sucessful********************");
		} catch (MqttException | IOException e) {
			e.printStackTrace();
		}	

        return messages;
    }
	
	public static String parseXML(String payload) 
	{
        try {
        	Pattern pattern = Pattern.compile("<value>(.*?)</value>");
            Matcher matcher = pattern.matcher(payload);
            while (matcher.find()) {
                System.out.println("VIN Name: " + matcher.group(1));
                return matcher.group(1);
            }
        } catch (Exception e) {
			e.printStackTrace();
			System.out.println("parse Error ::"+e.getMessage());
		}	
        return "dummy";
	}

	public static String extractParametersAsJson(String xml) throws Exception 
	{
		   var document = DocumentBuilderFactory.newInstance()
		            .newDocumentBuilder()
		            .parse(new InputSource(new StringReader(xml)));

		    var parameters = document.getElementsByTagName("parameter");

		    var jsonArray = IntStream.range(0, parameters.getLength())
		            .mapToObj(i -> (Element) parameters.item(i))
		            .map(parameter -> Map.of(
		                    "name",
		                    parameter.getElementsByTagName("name")
		                            .item(0)
		                            .getTextContent(),
		                    "value",
		                    parameter.getElementsByTagName("value")
		                            .item(0)
		                            .getTextContent()))
		            .toList();
		    return new ObjectMapper()
		            .writerWithDefaultPrettyPrinter()
		            .writeValueAsString(jsonArray);
	}

	
}
