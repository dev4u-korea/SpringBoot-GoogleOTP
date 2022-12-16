package com.demo.api;

import com.demo.util.GoogleOTP;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;

@RestController
public class GoogleOtpAPI {

    @RequestMapping(value = "/api/otp/genAccount", method = RequestMethod.GET)
    public String genOtpAccount(HttpServletRequest request) {

        String userName = request.getParameter("userName");
        String hostName = request.getParameter("hostName");

        GoogleOTP otp = new GoogleOTP();

        HashMap<String, String> map = otp.generateOTP(userName, hostName);

        return String.format("encodedKey=%s", map.get("encodedKey"));
    }

    @RequestMapping(value = "/api/otp/getQrImage", method = RequestMethod.GET)
    public void getQrImage(HttpServletRequest request, HttpServletResponse response) {

        GoogleOTP otp = new GoogleOTP();
        String userName = request.getParameter("userName");
        String hostName = request.getParameter("hostName");
        String encodedKey = request.getParameter("encodedKey");
        String otpAuth = String.format("otpauth://totp/%s?secret=%s&issuer=%s", userName, encodedKey, hostName);

        // QR CODE 이미지 생성
        BufferedImage image;
        OutputStream os;
        try {
            image = otp.getQRImage(otpAuth, "UTF-8",200,200);
            response.setContentType("image/png");
            os = response.getOutputStream();
            ImageIO.write(image, "PNG", os);
            os.flush();
        } catch (Exception ig) {
            ig.printStackTrace();
        }
    }

    @GetMapping("/api/otp/authOtpNo")
    public String authOtpNo(@RequestParam String otpNo, @RequestParam String encodedKey) {

        GoogleOTP otp = new GoogleOTP();

        if (otp.isAuthCode(otpNo, encodedKey)) {
            return "true";
        } else {
            return "false";
        }
    }
}
