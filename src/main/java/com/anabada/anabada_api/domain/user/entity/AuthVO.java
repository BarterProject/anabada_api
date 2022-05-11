package com.anabada.anabada_api.domain.user.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "AUTH_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthVO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "name", length = 45, updatable = false)
    private String name;

    @Column(name="description", length = 200, nullable = false, updatable = true)
    private String description;

    public enum Type {
        ADMIN("ROLE_ADMIN"), USER("ROLE_USER");

        final private String name;
        public String value() {
            return name;
        }
        private Type(String name){
            this.name = name;
        }
    }

}
