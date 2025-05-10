package com.paymentpanel.dto.response;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class UserResponse {
    
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}