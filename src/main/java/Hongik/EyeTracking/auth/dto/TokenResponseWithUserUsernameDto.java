package Hongik.EyeTracking.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TokenResponseWithUserUsernameDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpirationTime;
    private String username;

    @Builder
    public TokenResponseWithUserUsernameDto(String grantType, String accessToken, String refreshToken, Long accessTokenExpirationTime,
                                            String username){
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.username = username;
    }
}
