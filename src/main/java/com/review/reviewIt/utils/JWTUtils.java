package com.review.reviewIt.utils;


import com.review.reviewIt.entity.CustomUserDetails;

import io.jsonwebtoken.*;


import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.stereotype.Component;


import java.security.Key;
import java.util.*;

@Component
public class JWTUtils {

    public static final long EXPIRATION_TIME =  1000 * 60 * 24 * 7 ; // for 7 days

//    @Value("${auth.token.jwtSecret}")
    private String jwtSecret = "231HB3HJ1B3139123KJ123H213123913Y123B21K3H13G12313123K13G17388123KNB";


    public String generateTokenForUser(Authentication authentication){
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail())
                .claim("id" , userPrincipal.getId())
                .claim("roles" , roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + EXPIRATION_TIME))
                .signWith(key() , SignatureAlgorithm.HS256).compact();
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getUsernameFromToken(String token){
         return  Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public boolean validateToken(String token){
            try {
                Jwts.parserBuilder()
                        .setSigningKey(key())
                        .build()
                        .parseClaimsJws(token);
                return true;
            }catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e){
                throw new JwtException(e.getMessage());
            }
    }

}
