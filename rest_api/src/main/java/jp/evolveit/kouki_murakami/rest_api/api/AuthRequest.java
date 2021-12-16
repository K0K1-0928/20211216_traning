package jp.evolveit.kouki_murakami.rest_api.api;

import lombok.Getter;

@Getter
public class AuthRequest {
    private String username;
    private String password;
}
