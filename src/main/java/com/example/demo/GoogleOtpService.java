package com.example.demo;

import kr.co.hectofinancial.util.GoogleOTP;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

@RestController
public class GoogleOtpService {

    @RequestMapping(value = "/qrimage", method = RequestMethod.GET)
    public void downloadQrImage(HttpServletRequest request, HttpServletResponse response) {

        GoogleOTP otp = new GoogleOTP();
        String userName = request.getParameter("userName");
        String hostName = request.getParameter("hostName");
        String encodedKey = request.getParameter("encodedKey");
        String otpauth = String.format("otpauth://totp/%s?secret=%s&issuer=%s", userName, encodedKey, hostName);

        // QR CODE 이미지 생성
        BufferedImage image;
        OutputStream os;
        try {
            image = otp.getQRImage(otpauth, "UTF-8",200,200);
            response.setContentType("image/png");
            os = response.getOutputStream();
            ImageIO.write(image, "PNG", os);
            os.flush();
        } catch (Exception ig) {
            ig.printStackTrace();
        }

    }

    @GetMapping("/checkOTP")
    public String echo(@RequestParam String otpNo) {

        GoogleOTP otp = new GoogleOTP();

        if ( otp.isAuthCode(otpNo, "7BXKISLMVNPBSDAE")) {
            return "true";
        } else {
            return "false";
        }
    }

}
