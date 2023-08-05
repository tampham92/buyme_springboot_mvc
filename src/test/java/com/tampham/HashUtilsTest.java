package com.tampham;

import com.tampham.utils.HashUtils;
import org.junit.jupiter.api.Test;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HashUtilsTest {

    @Test
    public void hashMacSha256Test() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String data = "accessKey=mTCKt9W3eU1m39TW&amount=1000000&extraData=e3VzZXJuYW1lPXR5cGluZ30=&ipnUrl=http://localhost:8181/buyer/account&orderId=MPSW1&oderInfo=Mua goi Gói gold&redirectUrl=http://localhost:8181/buyer/account&partnerCode=MOMOLRJZ20181206&requestId=MPSW1&requestType=captureWallet";
        String SECRET_KEY = "SetA5RDnLHvt51AULf51DyauxUo3kDU6";
        String valueMacSha256 = "c8f2284d6756dcc65b6fbe66adb9cd1eb3499b858cae7f3afbb23515272b6b9c";

        String result = HashUtils.hmacSha256(data, SECRET_KEY);

        assertEquals(valueMacSha256, result);
    }
}
