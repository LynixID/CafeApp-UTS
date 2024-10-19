package com.example.cafeapp

data class user(
    val username: String,
    val password: String
)

val dataLogin =  listOf(
    user("staff", "staff"),
    user("admin", "admin")
)



