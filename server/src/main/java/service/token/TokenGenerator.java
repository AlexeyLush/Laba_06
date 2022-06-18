package service.token;

import services.hashing.SaltGenerator;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TokenGenerator {

    public static String generateToken(String login, String passwordHash){
        String loginAndPassword = login + ":" +  passwordHash + ":" + SaltGenerator.getSalt();
        byte[] bytes = Base64.getEncoder().encode(loginAndPassword.getBytes());
        return new String(bytes);
    }

    public static UserField decodeToken(String token) {
        String decodeToken = new String(Base64.getDecoder().decode(token.getBytes(StandardCharsets.UTF_8)));
        String[] splitDecodeToken = decodeToken.split(":");
        return new UserField(splitDecodeToken[0], splitDecodeToken[1]);
    }

    public static class UserField {
        public String userName;
        public String passwordHash;

        public UserField(String userName, String passwordHash) {
            this.userName = userName;
            this.passwordHash = passwordHash;
        }
    }

}
