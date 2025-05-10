package com.paymentpanel.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.paymentpanel.exception.ResourceNotFoundException;
import com.paymentpanel.exception.ValidationException;
import com.paymentpanel.model.User;
import com.paymentpanel.repository.UserRepository;
import com.paymentpanel.dto.request.UserRequest;
import com.paymentpanel.dto.response.UserResponse;


@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Get all users
    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        
        return users.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    // Get user by ID
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        return mapToResponse(user);
    }
    
    // Get user by username
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
        
        return mapToResponse(user);
    }
    
    // Create new user
    @Transactional
    public UserResponse createUser(UserRequest request) {
        // Check if username is already taken
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ValidationException("Username is already taken: " + request.getUsername());
        }
        
        // Check if email is already in use
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email is already in use: " + request.getEmail());
        }
        
        User user = new User();
        mapRequestToEntity(request, user);
        
        // In a real application, you would hash the password here
        // user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        User savedUser = userRepository.save(user);
        
        return mapToResponse(savedUser);
    }
    
    // Update user
    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        // Check if username is already taken by another user
        if (!user.getUsername().equals(request.getUsername()) && 
                userRepository.existsByUsername(request.getUsername())) {
            throw new ValidationException("Username is already taken: " + request.getUsername());
        }
        
        // Check if email is already in use by another user
        if (!user.getEmail().equals(request.getEmail()) && 
                userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email is already in use: " + request.getEmail());
        }
        
        mapRequestToEntity(request, user);
        
        // In a real application, you would hash the password here if it's being updated
        // if (request.getPassword() != null && !request.getPassword().isEmpty()) {
        //     user.setPassword(passwordEncoder.encode(request.getPassword()));
        // }
        
        User updatedUser = userRepository.save(user);
        
        return mapToResponse(updatedUser);
    }
    
    // Toggle user active status
    @Transactional
    public UserResponse toggleUserStatus(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        
        user.setActive(!user.isActive());
        
        User updatedUser = userRepository.save(user);
        
        return mapToResponse(updatedUser);
    }
    
    // Delete user
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        
        userRepository.deleteById(id);
    }
    
    // Helper methods
    private void mapRequestToEntity(UserRequest request, User user) {
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setActive(request.isActive());
        
        // Only set password if provided
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(request.getPassword());
        }
    }
    
    private UserResponse mapToResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setActive(user.isActive());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        
        return response;
    }
}
