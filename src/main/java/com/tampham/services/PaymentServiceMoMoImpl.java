package com.tampham.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tampham.dtos.MomoRequestDto;
import com.tampham.utils.HashUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.HashMap;

@Service
public class PaymentServiceMoMoImpl implements PaymentService {
    private String URL_MOMO  = "https://test-payment.momo.vn/v2/gateway/api";
    private static String ACCESS_KEY = "sFrob8BUacPnu2xX";
    private static String PARTNER_CODE = "MOMOPHWT20210517";

    private static String SECRET_KEY = "lwCq1WBMd8vvX2rfsW7nwHyfNIATQGBW";

    @Override
    public String createPayment(long amount, String orderId, String orderInfo) throws JsonProcessingException {

        MomoRequestDto requestBody = getMomoRequestDto(amount, orderId, orderInfo);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestBody);
        HttpClient client = HttpClient.newHttpClient();

        String resp = null;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(URL_MOMO+"/create"))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .headers("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            resp = mapper.readValue(response.body(), String.class);
        } catch (InterruptedException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return resp;
    }

    private static MomoRequestDto getMomoRequestDto(long amount, String orderId, String orderInfo) {
        HashMap<String, String> extra = new HashMap<>();
        String redirectUrl = "http://localhost:8181/order/orderList";
        extra.put("username", "typing");
        String base64ExtraData = Base64.getEncoder().encodeToString(extra.toString().getBytes());
        MomoRequestDto requestBody = new MomoRequestDto();
        requestBody.setAmount(amount);
        requestBody.setOrderId(orderId);
        requestBody.setRequestId(orderId);
        requestBody.setAutoCapture(true);
        requestBody.setRedirectUrl(redirectUrl);
        requestBody.setIpnUrl(redirectUrl);
        requestBody.setOrderInfo(orderInfo);
        requestBody.setPartnerCode(PARTNER_CODE);
        requestBody.setPartnerName("Test");
        requestBody.setRequestType("captureWallet");
        requestBody.setLang("vi");

        String signature = createRawSignature(amount, orderId, orderInfo, base64ExtraData, redirectUrl, redirectUrl,"captureWallet" );
        requestBody.setSignature(signature);

        return requestBody;
    }


    private static String createRawSignature(long amount, String orderId,
                                             String orderInfo, String extraData,
                                             String ipnUrl, String redirectUrl,
                                             String requestType) {

        String rawData = "accessKey=" + ACCESS_KEY + "&amount=" + amount + "&extraData="
                + extraData + "&ipnUrl=" + ipnUrl + "&orderId=" + orderId + "&orderInfo="
                + orderInfo + "&partnerCode=" + PARTNER_CODE + "&redirectUrl="
                + redirectUrl + "&requestId=" + orderId + "&requestType="
                + requestType;

        return HashUtils.hmacSha256(rawData, SECRET_KEY);
    }
}
