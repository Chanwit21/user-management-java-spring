package com.bbl.usr.management.client;

import com.bbl.usr.management.entities.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class UserClient {

    private final RestTemplate rest = new RestTemplate();

    public List<User> getUserData() {
        try {
            ResponseEntity<List<User>> response =
                    rest.exchange(
                            "https://jsonplaceholder.typicode.com/users",
                            org.springframework.http.HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<User>>() {}
                    );
            return response.getBody();
        } catch (RestClientException e) {
            System.out.println("Error fetching data: " + e.getMessage());
            return List.of();
        }
    }
}
