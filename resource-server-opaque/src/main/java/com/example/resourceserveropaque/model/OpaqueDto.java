package com.example.resourceserveropaque.model;

import org.springframework.security.core.Authentication;

public record OpaqueDto(boolean active, Authentication authentication, Object principal) {

}
