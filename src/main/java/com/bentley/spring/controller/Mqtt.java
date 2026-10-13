package com.bentley.spring.controller;

import java.util.Properties;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Mqtt {

    private static final String MQTT_PUBLISHER_ID = "mqttx_56eb9a09345";
    private static final String MQTT_SERVER_ADDRES= "ssl://mnp-con.core-odp10-preprod.eu.dp15.vwg-connect.com:8883";
    private static IMqttClient instance;

    public static IMqttClient getInstance() 
    {
        try {
            if (instance == null) {
                instance = new MqttClient(MQTT_SERVER_ADDRES, MQTT_PUBLISHER_ID);
            }
    		String password = "eyJhbGciOiJFUzI1NiIsImtpZCI6IjAyZjI5N2I3LWE5MTctNWYzOS00YjVkLWFjMzQ0MzIwYmU4NyIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsibXF0dCJdLCJjY2giOiJUSElSRF9QQVJUWSIsImNuZiI6eyJ4NWRuI3JmYzIyNTMiOiJTVD1DaGVzaGlyZSxMPUNyZXdlLENOPWFwaS10ZXN0LmJlbnRsZXltb3RvcnMuY29tLE89QmVudGxleSBNb3RvcnMgTGltaXRlZCxDPUdCIiwieDV0Ijoia0FRckRyWjhHWk1fM3ZWcWNnb1VnUEh3bEVFIn0sImV4cCI6MTc5MTk3MzAyNCwiaWF0IjoxNzkxMzY4MjI0LCJpc3MiOiJodHRwczovL2FwcC5lY2UuZ2F1dGgtdndhYy5jb20iLCJqdGkiOiI1ZmE5Yjg0OC1iZTY5LTQ0MjMtODg0MS1kN2MxOTZkZjk0YzYiLCJzY3AiOlsibXF0dCIsIm1ucCIsInQwX2Q6YmVudGxleW1kayJdLCJzdHlwIjoiVDAiLCJzeXMiOiJYSURfQkVOVExFWV9NVUxFU09GVCIsInRudCI6IkJlbnRsZXksRUNFIiwidHlwIjoiQVQiLCJ2ZXIiOiIxIn0.G4jRtAwa5WJ54RD9_2j7nadDqVMV9EwIQRZFEOvwQFXlxJhbXfWcRd9JBlPnwR11kpg-rNxXRMcXY8SzHzKTVA";

			Properties sslProps = new Properties();
			sslProps.setProperty("com.ibm.ssl.trustStore", "cert/truststore.jks");
			sslProps.setProperty("com.ibm.ssl.trustStorePassword", "pass1234");
    		
    		
            MqttConnectOptions options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setUserName("GAUTH");
            options.setPassword(password.toCharArray());
            options.setCleanSession(true);
            options.setSSLProperties(sslProps);
            
            System.out.println("====== Before Connection =====");
            if (!instance.isConnected()) {
                instance.connect(options);
            }
            System.out.println("====== After Connection =====");
        } catch (MqttException e) {
            e.printStackTrace();
        }
        return instance;
    }

    private Mqtt() {

    }
}
