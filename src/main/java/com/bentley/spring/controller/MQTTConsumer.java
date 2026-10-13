package com.bentley.spring.controller;

import java.util.Properties;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MQTTConsumer {

	public static void main(String[] args) 
	{
		System.out.println(" ======= Inside =====");
		
		String broker	= "ssl://mnp-con.core-odp10-preprod.eu.dp15.vwg-connect.com:8883";
		
		String clientId	= "mqttxsdf_12311111";
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
	}
}
