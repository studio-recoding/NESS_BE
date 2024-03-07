package Ness.Backend.entity;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum MemberRole {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String role;

    MemberRole(String role) {
        this.role=role;
    }
}