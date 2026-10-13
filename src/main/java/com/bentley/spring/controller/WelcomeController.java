package com.bentley.spring.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bentley.spring.model.MqttSubscribeModel;

@RestController
public class WelcomeController {

	@GetMapping("/welcome")
	public String welcome() {
		return "Welcome to Spring Boot";
	}
	
	@GetMapping("subscribe")
    public List<MqttSubscribeModel> subscribeChannel()
            throws InterruptedException, org.eclipse.paho.client.mqttv3.MqttException 
	 {
        List<MqttSubscribeModel> messages = new ArrayList<>();
//        String topic = "EMAIL/223660/XID_BS_MDK2/Bentley/MDKv2/#"; 
//        
//        CountDownLatch countDownLatch = new CountDownLatch(10);
//        
//        Mqtt.getInstance().subscribeWithResponse(topic, (s, mqttMessage) -> 
//        {
//            MqttSubscribeModel mqttSubscribeModel = new MqttSubscribeModel();
//            mqttSubscribeModel.setId(mqttMessage.getId());
//            mqttSubscribeModel.setMessage(new String(mqttMessage.getPayload()));
//            mqttSubscribeModel.setQos(mqttMessage.getQos());
//            messages.add(mqttSubscribeModel);
//            countDownLatch.countDown();
//        });
//
//        countDownLatch.await(1, TimeUnit.MILLISECONDS);
		
		System.out.println(" ======= Inside MQTT =====");
		
		String broker	= "ssl://mnp-con.core-odp10-preprod.eu.dp15.vwg-connect.com:8883";
		
		String clientId	= "mqttxsdf_56eb9a09234";
		String topic	= "EMAIL/223660/XID_BS_MDK2/Bentley/MDKv2/#";
		
		String username = "GAUTH";
		String password = "eyJhbGciOiJFUzI1NiIsImtpZCI6IjAyZjI5N2I3LWE5MTctNWYzOS00YjVkLWFjMzQ0MzIwYmU4NyIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsibXF0dCJdLCJjY2giOiJUSElSRF9QQVJUWSIsImNuZiI6eyJ4NWRuI3JmYzIyNTMiOiJTVD1DaGVzaGlyZSxMPUNyZXdlLENOPWFwaS10ZXN0LmJlbnRsZXltb3RvcnMuY29tLE89QmVudGxleSBNb3RvcnMgTGltaXRlZCxDPUdCIiwieDV0Ijoia0FRckRyWjhHWk1fM3ZWcWNnb1VnUEh3bEVFIn0sImV4cCI6MTc5MTk3MzAyNCwiaWF0IjoxNzkxMzY4MjI0LCJpc3MiOiJodHRwczovL2FwcC5lY2UuZ2F1dGgtdndhYy5jb20iLCJqdGkiOiI1ZmE5Yjg0OC1iZTY5LTQ0MjMtODg0MS1kN2MxOTZkZjk0YzYiLCJzY3AiOlsibXF0dCIsIm1ucCIsInQwX2Q6YmVudGxleW1kayJdLCJzdHlwIjoiVDAiLCJzeXMiOiJYSURfQkVOVExFWV9NVUxFU09GVCIsInRudCI6IkJlbnRsZXksRUNFIiwidHlwIjoiQVQiLCJ2ZXIiOiIxIn0.G4jRtAwa5WJ54RD9_2j7nadDqVMV9EwIQRZFEOvwQFXlxJhbXfWcRd9JBlPnwR11kpg-rNxXRMcXY8SzHzKTVA";
		
		try 
		{
			Properties sslProps = new Properties();
			sslProps.setProperty("com.ibm.ssl.trustStore", "C:/temp/cert/truststore.jks");
			sslProps.setProperty("com.ibm.ssl.trustStorePassword", "pass1234");
			
			
			MqttClient client = new MqttClient(broker, clientId);
	
			MqttConnectOptions options = new MqttConnectOptions();
				options.setUserName(username);
				options.setPassword(password.toCharArray());
				options.setCleanSession(false);
				options.setAutomaticReconnect(true);
				options.setSSLProperties(sslProps);
			
			System.out.println("b4--Connected to broker");
			client.connect(options);
			System.out.println("Af--Connected to broker");
			
			client.subscribe(topic, (receivedTopic, message) -> 
			{
				String payload = new String(message.getPayload());
				
				System.out.println("Topic : " + receivedTopic);
				System.out.println("Message : " + payload);
				System.out.println("QoS : " + message.getQos());
			});
			System.out.println("Subscribed to: " + topic);
			
		} catch (MqttException e) {
			e.printStackTrace();
		}	

        return messages;
    }
}
