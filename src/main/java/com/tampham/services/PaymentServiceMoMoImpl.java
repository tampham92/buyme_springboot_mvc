package com.tampham.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tampham.dtos.MomoRequestDto;
import com.tampham.dtos.MomoResponseDto;
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
    public Object createPayment(long amount, String orderId, String orderInfo) throws JsonProcessingException {

        MomoRequestDto payload = getMomoRequestDto(amount, orderId, orderInfo);

        ObjectMapper mapper = new ObjectMapper();
        String dataJson = mapper.writeValueAsString(payload);
        HttpClient client = HttpClient.newHttpClient();

        MomoResponseDto momoResponse = new MomoResponseDto();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(URL_MOMO+"/create"))
                    .POST(HttpRequest.BodyPublishers.ofString(dataJson))
                    .headers("Content-Type", "application/json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            momoResponse = mapper.readValue(response.body(), MomoResponseDto.class);
        } catch (InterruptedException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return momoResponse;
    }

    private static MomoRequestDto getMomoRequestDto(long amount, String orderId, String orderInfo) {
        HashMap<String, String> extra = new HashMap<>();

        extra.put("username", "typing");
        String base64ExtraData = Base64.getEncoder().encodeToString(extra.toString().getBytes());
        String redirectUrl = "http://localhost:8181/order/orderList";
        String signature = createRawSignature(amount, orderId, orderInfo, base64ExtraData, redirectUrl, redirectUrl,"captureWallet" );

        MomoRequestDto requestBody = new MomoRequestDto();
        requestBody.setAmount(amount);
        requestBody.setOrderId(orderId);
        requestBody.setRequestId(orderId);
        requestBody.setExtraData(base64ExtraData);
        requestBody.setAutoCapture(true);
        requestBody.setRedirectUrl(redirectUrl);
        requestBody.setIpnUrl(redirectUrl);
        requestBody.setOrderInfo(orderInfo);
        requestBody.setPartnerCode(PARTNER_CODE);
        requestBody.setPartnerName("Test");
        requestBody.setRequestType("captureWallet");
        requestBody.setLang("vi");
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
