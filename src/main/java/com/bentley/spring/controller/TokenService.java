package com.bentley.spring.controller;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.bentley.spring.model.TokenResponse;

@Service
public class TokenService 
{
    private String accessToken;
    private Instant expiresAt;

    public synchronized String getAccessToken() 
    {
        // First time or token expiring within 1 hour
        if (accessToken == null ||
            expiresAt == null ||
            expiresAt.isBefore(Instant.now().plusSeconds(3600))) {
            refreshToken();
        }
        return accessToken;
    }

    private void refreshToken() 
    {
    	WebClient webClient = WebClient.builder().build();
    	String mulePath = "https://cariad-sys-api-dev-uwd9z4.abnaeh.gbr-e1.cloudhub.io/api/gAuth";
    	
    	System.out.println("=========== Token ===========");
        
    	TokenResponse response = webClient.get()
        									.uri(mulePath)
        					                .header("client_id", "edb095193e694fdbbc155a3f827a4d3a")
        					                .header("client_secret", "270E8197719247bca55a7D8e652865Ad")
        									.retrieve()
							                .bodyToMono(TokenResponse.class)
							                .block();

    	System.out.println("=========== Token After ===========");
    	
        if (response == null) {
            throw new RuntimeException("Failed to obtain access token");
        }
        this.accessToken = response.getAccessToken();
        // expires_in = 604799 (~7 days)
        this.expiresAt = Instant.now().plusSeconds(response.getExpiresIn());
        System.out.println("Token refreshed. Expires at: " + expiresAt);
    }
    
    public static void main(String[] args) throws Exception 
	{
    	TokenService token = new TokenService();
    	token.refreshToken();
	}
}
