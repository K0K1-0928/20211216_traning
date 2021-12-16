package jp.evolveit.kouki_murakami.rest_api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {
    private String username;
    private String password;
}
