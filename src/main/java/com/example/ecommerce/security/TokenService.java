package com.example.ecommerce.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.ecommerce.exception.BaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${access_token_secret_key}")
    private String ACCESS_TOKEN_SECRET_KEY;

    @Value("${issuer}")
    private String ISSUER;

    @Value("${access_token_expiration}")
    private String ACCESS_TOKEN_EXPIRATION;

    public String genereteToken(Authentication auth) {
        String username = ((UserDetails)auth.getPrincipal()).getUsername();
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + (Integer.parseInt(ACCESS_TOKEN_EXPIRATION) * 60 * 1000)))
                .withIssuer(ISSUER)
                .sign(Algorithm.HMAC256(ACCESS_TOKEN_SECRET_KEY.getBytes()));
    }

    public DecodedJWT verifyJWT(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(ACCESS_TOKEN_SECRET_KEY.getBytes())).build();
                try {
                    var decodedJwt = verifier.verify(token);
                    return decodedJwt;
                } catch(Exception e) {
                    throw BaseException.builder()
                            .errorMessage(e.getMessage())
                            .httpStatus(HttpStatus.BAD_REQUEST)
                            .build();
                }

    }

}
