# SpringBoot-RestAPI

### Google OTP API Usage ###
STEP 1. OTP ACCOUNT GENERATE
http://localhost:8080/api/otp/genAccount?userName={user name}&hostName={host name}
ex) http://localhost:8080/api/otp/genAccount?userName=dev4u&hostName=dev4u.co.kr

STEP 2. Show QR Image 
http://localhost:8080/api/otp/getQrImage??userName={user name}&hostName={host name}&encodedKey={Step 1 Retrun value}
ex) http://localhost:8080/api/otp/genAccount?userName=dev4u&hostName=dev4u.co.kr&encodedKey=LGKNF63HGZ5XANGS

STEP 3. Auth By OTP NO
http://localhost:8080/api/otp/authOtpNo?otpNo={Google Authenticator}&encodedKey={Step 1 Retrun value}
ex) http://localhost:8080/api/otp/authOtpNo?otpNo=123456&encodedKey=LGKNF63HGZ5XANGS

