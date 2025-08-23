package com.bbl.usr.management.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    Long id;
    String name;
    String username;
    String email;
    String phone;
    String website;
}
