package com.anabada.anabada_api.domain.user;

public enum AuthType {
    ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

    final private String name;

    public String getName() {
        return name;
    }
    private AuthType(String name){
        this.name = name;
    }
}
