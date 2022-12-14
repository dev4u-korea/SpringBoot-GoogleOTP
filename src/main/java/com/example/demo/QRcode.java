package com.example.demo;

import kr.co.hectofinancial.util.GoogleOTP;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

@RestController
public class QRcode {

    @RequestMapping(value = "/qrimage", method = RequestMethod.GET)
    public void downloadQrImage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        GoogleOTP otp = new GoogleOTP();
        String userName = request.getParameter("userName");
        String hostName = request.getParameter("hostName");
        String encodedKey = request.getParameter("encodedKey");
        String otpauth = String.format("otpauth://totp/%s?secret=%s&issuer=%s", userName, encodedKey, hostName);

        // QR CODE 이미지 생성
        BufferedImage image = null;
        try {
            image = otp.getQRImage(otpauth, "UTF-8",200,200);
        } catch (Exception ig) {
            ig.printStackTrace();
        }

        response.setContentType("image/png");
        OutputStream os = null;
        try {
            os = response.getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ImageIO.write(image, "PNG", os);

        os.flush();

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
