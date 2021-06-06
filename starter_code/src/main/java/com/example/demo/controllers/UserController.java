package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {
		User user = new User();

		if(CheckRequestObject(createUserRequest)){
			try{
				user.setUsername(createUserRequest.getUsername());
				Cart cart = new Cart();
				cartRepository.save(cart);
				user.setCart(cart);
				user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
				userRepository.save(user);
				logger.info("User " + user.getUsername() + " successfully created.");
			} catch (Exception ex){
				logger.error("Error while create user request.", ex.toString());
				return ResponseEntity.badRequest().build();
			}

			return ResponseEntity.ok(user);
		}
		else {
			return ResponseEntity.badRequest().build();
		}
	}

	private boolean CheckRequestObject(CreateUserRequest createUserRequest){
		if(createUserRequest == null){
			logger.error("No user request object transmitted.");
			return false;
		}

		if(isNullOrEmpty("Username", createUserRequest.getUsername()) ||
				isNullOrEmpty("Username", createUserRequest.getPassword()) ||
				isNullOrEmpty("Username", createUserRequest.getConfirmPassword())) {
			return false;
		}

		if(createUserRequest.getPassword().length()<7 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			logger.error("Password and confirmation password are different");
			return false;
		}

		return true;
	}

	private boolean isNullOrEmpty(String stringName, String str){
		if(str == null || str.isEmpty()){
			logger.error(stringName + " is null or Empty.");
			return true;
		}
		else{
			return false;
		}
	}
}
