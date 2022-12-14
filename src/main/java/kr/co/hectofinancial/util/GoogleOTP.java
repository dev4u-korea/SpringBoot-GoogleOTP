package kr.co.hectofinancial.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Random;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import org.apache.commons.codec.binary.Base32;

public class GoogleOTP {

    public final static String GOOGLE_URL = "https://www.google.com/chart?chs=200x200&chld=M|0&cht=qr&chl=";
    public HashMap<String, String> generateOTP(String userName, String hostName) {
        HashMap<String, String> map = new HashMap<>();
        byte[] buffer = new byte[5 + 5 * 5];
        new Random().nextBytes(buffer);
        Base32 codec = new Base32();
        byte[] secretKey = Arrays.copyOf(buffer, 10);
        byte[] bEncodedKey = codec.encode(secretKey);

        String encodedKey = new String(bEncodedKey);
        String otpauth = getOtpAuthURL(encodedKey, userName, hostName);
        // Google OTP 앱에 userName@hostName 으로 저장됨
        // key를 입력하거나 생성된 QR코드를 바코드 스캔하여 등록
        map.put("encodedKey", encodedKey);
        map.put("otpauth", otpauth);
        return map;
    }

    public boolean isAuthCode(String userCode, String otpkey) {
        long otpnum = Integer.parseInt(userCode); // Google OTP 앱에 표시되는 6자리 숫자
        long wave = new Date().getTime() / 30000; // Google OTP의 주기는 30초
        boolean result = false;
        try {
            Base32 codec = new Base32();
            byte[] decodedKey = codec.decode(otpkey);
            int window = 3;
            for (int i = -window; i <= window; ++i) {
                long hash = verifyCode(decodedKey, wave + i);
                if (hash == otpnum) result = true;
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    private int verifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {

        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }

        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);

        int offset = hash[20 - 1] & 0xF;

        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            // We are dealing with signed bytes:
            // we just keep the first byte.
            truncatedHash |= (hash[offset + i] & 0xFF);
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;

        return (int) truncatedHash;
    }

    public String getOtpAuthURL(String encodedKey, String userName, String userHost) {
        try {
            return  "otpauth://totp/"
                    + URLEncoder.encode(userName, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(encodedKey, "UTF-8").replace("+", "%20")
                    + "&issuer=" + URLEncoder.encode(userHost, "UTF-8").replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public void generateQRcode(String data, String path, String charset,int h, int w) throws WriterException, IOException {
        //the BitMatrix class represents the 2D matrix of bits
        //MultiFormatWriter is a factory class that finds the appropriate Writer subclass for the BarcodeFormat requested and encodes the barcode with the supplied contents.
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        //MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
        MatrixToImageWriter.writeToPath(matrix, path.substring(path.lastIndexOf('.') + 1), Paths.get(path));
    }

    public BufferedImage getQRImage(String data, String charset, int h, int w) throws WriterException, IOException {
        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, w, h);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

}
