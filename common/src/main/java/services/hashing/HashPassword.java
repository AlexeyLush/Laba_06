package services.hashing;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HashPassword {
    public static String getHashPassword(String password, String salt){
        String hash;
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-512");

            byte[] byteHash = sha.digest((password + salt).getBytes());

            BigInteger no = new BigInteger(1, byteHash);

            hash = no.toString(16);

            while (hash.length() < 32) {
                hash = "0" + hash;
            }


        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return hash;
    }
}
