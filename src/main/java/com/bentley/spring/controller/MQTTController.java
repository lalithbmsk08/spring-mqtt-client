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
		System.out.println(" ======= Test Post Call Mule 1 =====");
		String password = tokenService.getAccessToken();
		System.out.println(" Token ::"+password);
		
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
	        
		StringBuilder stb = new StringBuilder();
		  stb.append("{");
		  stb.append("\"topic\":\"");
		  stb.append("EMAIL/223660/XID_BS_MDK2/Bentley/MDKv2/PairingPassword/default/jjoVCZ5glLPCYxlKk65nNaPWZS21");
		  stb.append("\",");
		  stb.append("\"vin\":\"");
		  stb.append(parseXML(xml));
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
		
		System.out.println(" ======= Sub MQTT Message Mule =====");
		
		String broker	= "ssl://mnp-con.core-odp10-preprod.eu.dp15.vwg-connect.com:8883";
		String clientId	= "mqttxsdf_56eb9a09234";
		String topic	= "EMAIL/223660/XID_BS_MDK2/Bentley/MDKv2/#";
		String username = "GAUTH";
		String password = tokenService.getAccessToken();
		
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
				options.setAutomaticReconnect(true);
				options.setSSLProperties(sslProps);
			
			System.out.println("==== b4 Connected to broker ====");
			client.connect(options);
			System.out.println("==== Af Connected to broker ====");

			System.out.println("*************** Mule Post ********************");
			client.subscribe(topic, (receivedTopic, message) -> 
			{
				String payload = new String(message.getPayload());
				
				System.out.println("Topic   :" + receivedTopic);
				System.out.println("Message :" + payload);
				System.out.println("QoS     :" + message.getQos());
				
				StringBuilder stb = new StringBuilder();
							  stb.append("{");
							  stb.append("\"topic:\"");
							  stb.append(topic);
							  stb.append("\",");
							  stb.append("\"vin:\"");
							  stb.append(parseXML(payload));
							  stb.append("\"}");
							  
				muleAPI.postMessage(stb.toString());
				
				
			});
			System.out.println("****************  Mule Post Sucessful********************");
			
		} catch (MqttException | IOException e) {
			e.printStackTrace();
		}	

        return messages;
    }
	
	public static String parseXML(String payload) {
        Pattern pattern = Pattern.compile("<value>(.*?)</value>");
        Matcher matcher = pattern.matcher(payload);
        while (matcher.find()) {
            System.out.println("VIN Name: " + matcher.group(1));
            return matcher.group(1);
        }
        return null;
	}
	
}
