package com.funsoft.magazin.Securité.JWT;


import com.funsoft.magazin.Securité.Service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {
    @Value("${mahran.app.jwtSecret}")
    private String jwtSecret;
    @Value("${mahran.app.jwtExpirationMs}")
    private int jwtEpirationMs;
    public String generateJwtToken(Authentication authentication){
        // le résultat de l'authentification
        UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal(); //principal user (object)
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime()+jwtEpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    public Boolean validateJwtToken(String token){
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }
        catch(Exception e)
        {
            System.out.print("Token invalid");
        }
        return false;
    }
    public String getUserNameFromJwtToken( String token){
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }
}
