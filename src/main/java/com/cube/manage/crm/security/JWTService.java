//package com.cube.manage.crm.security;
//
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import java.security.Key;
//import java.util.Base64;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.function.Function;
//
//@Service
//public class JWTService {
//
//
//    public String secretKey = "";
//
//    public JWTService(){
//        try{
//            KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
//            SecretKey sk = keyGen.generateKey();
//            secretKey = Base64.getEncoder().encodeToString(sk.getEncoded());
//        } catch (Exception e){
//
//        }
//    }
//
//
//    public String generateToken(String userName) {
//
//        Map<String,String> claims = new HashMap<>();
//
//        return Jwts.builder()
//                .claims()
//                .add(claims)
//                .subject(userName)
//                .issuedAt(new Date())
//                .expiration(new Date(System.currentTimeMillis() + 60 * 60 * 30))
//                .and()
//                .signWith(getKey())
//                .compact();
//
//    }
//
//    private SecretKey getKey(){
//        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
//    }
//
//    public String extractUserNameFromToken(String token) {
//        return extractClaims(token, Claims::getSubject);
//    }
//
//    private <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimResolver.apply(claims);
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) {
//        String username = extractUserNameFromToken(token);
//        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private Date extractExpiration(String token) {
//        return extractClaims(token, Claims::getExpiration);
//    }
//}
