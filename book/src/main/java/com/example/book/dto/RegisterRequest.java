package com.example.book.dto;

public record RegisterRequest(
        String username, String password, String [] roles) {
}
