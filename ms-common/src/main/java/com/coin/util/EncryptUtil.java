package com.coin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Date;

/**
 * @ClassName EncryptUtil
 * @Description: TODO
 * @Author kh
 * @Date 2020/2/29 12:22
 * @Version V1.0
 **/
public class EncryptUtil {

    public static Claims getTokenClaim(String secret, String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isExpire(String secret, String token) {
        Claims claims = getTokenClaim(secret, token);
        if(null != claims) {
            return claims.getExpiration().before(new Date());
        } else {
            return false;
        }
    }
    
    public static String md5(String str) {
        return DigestUtils.md5Hex(str.getBytes());
    }

    public static void main(String[] args) {
        System.out.println("md5(\"admin\") = " + md5("test"));
    }
}
