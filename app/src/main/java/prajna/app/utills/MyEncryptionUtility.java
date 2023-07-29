package prajna.app.utills;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MyEncryptionUtility {
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5PADDING";
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";
    public static final int IV_LENGTH = 16;
    private static String strResult;
    public static String encryptSha512noSalt(String passwordToHash) {
        String hashedResult = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashedResult = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashedResult;
    }

    /**
     * takes the mobile access key and decrypts the outer data
     * @param mobAck
     * @param rocData
     * @return
     */
    public static String rocDataDecrypt(String mobAck, String rocData) throws Exception {
        try {
            Cipher decryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);
            String ivHex = rocData.substring(0, IV_LENGTH * 2);
            String encryptedHex = rocData.substring(IV_LENGTH * 2);

            IvParameterSpec ivspec = new IvParameterSpec(HexEncoder.toByte(ivHex));
            decryptionCipher.init(Cipher.DECRYPT_MODE, getSecretKeyAES128(mobAck), ivspec);
            byte[] decryptedText = decryptionCipher.doFinal(HexEncoder.toByte(encryptedHex));
            String decrypted = new String(decryptedText, "UTF-8");
            return decrypted;
        } catch (Exception e) {
            throw e;
        }

    }

    /**
     * takes the mobile access key and encrypts the outer data
     * @param moback
     * @param rocData
     * @return
     */
    public static String rocDataEncrypt(String moback, String rocData) throws Exception {

        try {
            SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
            byte[] iv = new byte[IV_LENGTH];
            random.nextBytes(iv);

            String ivHex = HexEncoder.toHex(iv);
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            Cipher encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);
            encryptionCipher.init(Cipher.ENCRYPT_MODE, getSecretKeyAES128(moback), ivspec);
            byte[] encryptedText = encryptionCipher.doFinal(rocData.getBytes("UTF-8"));
            String encryptedHex = HexEncoder.toHex(encryptedText);

            return ivHex + encryptedHex;
        } catch (Exception e) {
            throw e;
        }
    }


    public static SecretKey getSecretKeyAES128(String key) throws Exception {

        byte[] keyb = key.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        keyb = sha.digest(keyb);
        keyb = Arrays.copyOf(keyb, 16); // 16 for 128 bit encryption, 24 for 192 bit encryption, 32 for 256 bit encryption
        SecretKeySpec secretKey = new SecretKeySpec(keyb, "AES");

        return secretKey;
    }

    public static class HexEncoder {
        final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

        public static String toHex(byte[] bytes) {
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }

        public static byte[] toByte(String hexString) {
            int len = hexString.length() / 2;
            byte[] result = new byte[len];
            for (int i = 0; i < len; i++)
                result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                        16).byteValue();
            return result;
        }
    }

    /**
     * takes the mobile access key and encrypts the outer data
     * @param message
     * @param key
     * @return
     */

    public static String EncryptMessage(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes(StandardCharsets.UTF_8);
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] results = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        Log.v("GET Result from  final:", results.toString());
        strResult = Base64.encodeToString(results, 1);

        Log.v("Encrypt01:", strResult);
        Log.v("Encrypt02:", results.toString());
        return strResult;
    }

    /**
     * takes the mobile access key and decrypts the outer data
     * @param message
     * @param key
     * @return
     */

    public static String DecryptMessage(String message, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //this parameters should not be changed
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes(StandardCharsets.UTF_8);
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] results = new byte[message.length()];
        //BASE64Decoder decoder = new BASE64Decoder();
        try {
            results = cipher.doFinal(Base64.decode(message, Base64.DEFAULT));
        } catch (Exception e) {
            Log.i("Error in Decryption", e.toString());
        }
        Log.i("Data", new String(results, StandardCharsets.UTF_8));
        //return new String(results, "UTF-8"); // it returns the result as a String
        return new String(results, StandardCharsets.UTF_8);
    }

}
