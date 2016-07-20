package com.gnardini.testapplication.network;

public class UserRequest {

    private UserLoginData user;

    public UserRequest(String email, String password) {
        this.user = new UserLoginData(email, password);
    }

    public class UserLoginData {
        private String email;
        private String password;

        public UserLoginData(String email, String password) {
            this.email = email;
            this.password = password;
        }
    }
}
