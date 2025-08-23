package com.bbl.usr.management.model;


import com.bbl.usr.management.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllUserResp {
    private List<User> users;
}
