package com.micro.ykh.utils.sign;

import com.micro.ykh.constant.FwtConstant;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.apache.commons.codec.binary.Base64;


/**
 * @ClassName JwtUtils
 * @Description TODO
 * @Author xiongh
 * @Date 2020/12/24 11:32
 * @Version 1.0
 **/
public class JwtUtils {

    public static PublicKey getPublicKey(String tokenKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String dealTokenKey = tokenKey.replaceAll(FwtConstant.PUBLIC_KEY_FRONT, "").replaceAll(FwtConstant.PUBLIC_KEY_END, "").trim();

        java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(dealTokenKey));

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        return keyFactory.generatePublic(pubKeySpec);
    }

    public static Jwt<JwsHeader, Claims> getJwtByTokenKey(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * token的过期日期
     *
     * @param token token
     * @param publicKey 公钥
     * @return
     */
    public static Date getExpirationDate(String token, PublicKey publicKey) {
        Jwt<JwsHeader, Claims> jwt = getJwtByTokenKey(token, publicKey);
        Claims claims = jwt.getBody();
        return claims.getExpiration();
    }


}
