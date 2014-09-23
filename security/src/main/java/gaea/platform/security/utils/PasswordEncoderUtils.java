package gaea.platform.security.utils;

/**
 * Created by jsc on 14-6-4.
 */
public class PasswordEncoderUtils {

    private PasswordEncoderUtils(){

    }

    public static String encryptPassword(String password)
            throws java.security.NoSuchAlgorithmException {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        String b = "\u00a3\u00ac\u00a1\u00a3";
        String c = "fdjf,jkgfkl";
        String s = password + b + c;
        byte[] btInput = s.getBytes(java.nio.charset.Charset.forName("Latin1"));
        java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
        md5.update(btInput);
        byte[] md = md5.digest();
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = hexDigits[byte0 >>> 4 & 0xf];
            str[k++] = hexDigits[byte0 & 0xf];
        }
        return new String(str);
    }


}
