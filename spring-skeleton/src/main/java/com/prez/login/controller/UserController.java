package com.prez.login.controller;

import com.prez.login.model.LoginDTO;
import com.prez.login.model.User;
import com.prez.login.model.UserRegistrationDTO;
import com.prez.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

//TODO: consolidate this controller with AuthenticationController
@RestController
@RequestMapping("/PrezGroup/users")
public class UserController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private final UserService userService;

    @Value("${openweather.api.key}")
    private String weatherApiKey;  // Assuming you store your API key in application.properties

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public ResponseEntity<String> register(@RequestBody UserRegistrationDTO userDTO) {
        try {
            userService.register(userDTO);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            String jwtToken = userService.login(loginDTO);

            if (jwtToken != null) {
                //TODO:
                // A successful login should then trigger a method
                // that retrieves the users cities(or whatever info they need from my db or external API's
                String userCity = userService.getUserCity(loginDTO.getUsername());

                // Call the OpenWeatherMap API
                String weatherApiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + userCity + "&appid=" + weatherApiKey;
                ResponseEntity<String> weatherResponse = restTemplate.getForEntity(weatherApiUrl, String.class);

                // Here, we're returning just the weather data as a string. You can process it further if you like.
                String weatherData = weatherResponse.getBody();
                Map<String, Object> response = new HashMap<>();
                response.put("token", jwtToken);

                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    // ... other API endpoints for creating, updating, deleting users, etc.
}