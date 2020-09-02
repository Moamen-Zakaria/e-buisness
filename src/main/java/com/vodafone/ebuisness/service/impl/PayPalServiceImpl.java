package com.vodafone.ebuisness.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vodafone.ebuisness.dto.paypal.Token;
import com.vodafone.ebuisness.dto.paypal.invoicedraftcreation.CreateInvoiceDraftRequest;
import com.vodafone.ebuisness.exception.ConnectionErrorException;
import com.vodafone.ebuisness.model.main.Account;
import com.vodafone.ebuisness.model.main.ProductsInDeal;
import com.vodafone.ebuisness.service.PayPalService;
import com.vodafone.ebuisness.service.AuthService;
import com.vodafone.ebuisness.service.CartService;
import com.vodafone.ebuisness.util.Adapters;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Properties;

public class PayPalServiceImpl implements PayPalService {

    //used to throw exceptions occur while sending http requests to PayPal
    private String defaultErrorMessage = "Problem connecting to PayPal services";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuthService authService;

    @Autowired
    private CartService cartService;

    @Autowired
    ApplicationContext applicationContext;

    private Properties properties;
    private String url;
    private String clientId;
    private String clientSecret;
    private Token token;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public PayPalServiceImpl(Properties properties) {

        this.properties = properties;
        url = properties.getProperty("paypal.api.url");
        clientId = properties.getProperty("paypal.client.id");
        clientSecret = properties.getProperty("paypal.client.secret");

        if (url == null) {
            throw new IllegalArgumentException("No url found for PayPal api!");
        }
        if (clientId == null) {
            throw new IllegalArgumentException("No client id found for PayPal api!");
        }
        if (clientSecret == null) {
            throw new IllegalArgumentException("No client secret found for PayPal api!");
        }

    }

    private String getRecentToken() throws ConnectionErrorException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", encodeBasicAuth(clientId, clientSecret));

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Token> response = null;
        try {
            response = restTemplate.postForEntity(url + "/v1/oauth2/token", request, Token.class);
        } catch (Exception e) {

            throw new ConnectionErrorException(defaultErrorMessage);
        }

        if (response == null || response.getStatusCode() != HttpStatus.OK) {
            throw new ConnectionErrorException(defaultErrorMessage);
        }

        token = response.getBody();
        return response.getBody().getAccess_token();
    }

    @Override
    public String sendInvoice(String invoiceId) throws ConnectionErrorException {

        var headers = getAuthorizedHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        var httpEntity = new HttpEntity<>(headers);

        //make the post request
        ResponseEntity<String> response = null;
        try {
            response
                    = restTemplate.postForEntity(url + "/v2/invoicing/invoices/" + invoiceId +
                    "/send", httpEntity, String.class);
        } catch (Exception e) {

            throw new ConnectionErrorException(defaultErrorMessage);
        }

        if (response == null || response.getStatusCode() != HttpStatus.ACCEPTED) {
            throw new ConnectionErrorException(defaultErrorMessage);
        }

        return extractLinkFromResponse(response.getBody());
    }

    private String extractLinkFromResponse(String body) {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonObject = null;
        try {
            jsonObject = objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonObject.path("href").asText();
    }

    @Override
    public String createDraftInvoice(ProductsInDeal productsInDeal, Account account)
            throws ConnectionErrorException {

        //creation of the request object
        var createInvoiceDraftRequest
                = applicationContext.getBean(CreateInvoiceDraftRequest.class);

        productsInDeal.getProductInCartList().stream().parallel()
                .forEach(productInCart -> createInvoiceDraftRequest.getItems()
                        .add(Adapters.convertProductToItem(productInCart)));

        createInvoiceDraftRequest.getPrimary_recipients()
                .add(Adapters.convertAccountToPrimaryRecipient(account));

        //preparing the http entity
        var headers = getAuthorizedHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        var httpEntity
                = new HttpEntity<>(createInvoiceDraftRequest, headers);

        //make the post request
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(url + "/v2/invoicing/invoices", httpEntity, String.class);
        } catch (Exception e) {

            throw new ConnectionErrorException(defaultErrorMessage);
        }

        if (response == null || response.getStatusCode() != HttpStatus.CREATED) {
            throw new ConnectionErrorException(defaultErrorMessage);
        }

        return extractInvoiceIdFromLink(response.getBody());

    }

    private String extractInvoiceIdFromLink(String json) {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonObject = null;
        try {
            jsonObject = objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String invoiceLink = jsonObject.path("href").asText();

        var arrayOfStrings = invoiceLink.split("/");

        return arrayOfStrings[arrayOfStrings.length - 1];

    }

    //
    public String generateInvoiceNumber() throws ConnectionErrorException {
        var httpEntity
                = new HttpEntity<>(new LinkedMultiValueMap<>(), getAuthorizedHeaders());
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.postForEntity(url + "/v2/invoicing/generate-next-invoice-number",
                    httpEntity,
                    String.class);

        } catch (Exception e) {

            throw new ConnectionErrorException(defaultErrorMessage);
        }

        if (response == null || response.getStatusCode() != HttpStatus.OK) {
            throw new ConnectionErrorException(defaultErrorMessage);
        }
        var json = response.getBody();
        return json.split(":")[1].split("\"")[1];
    }

    private HttpHeaders getAuthorizedHeaders() throws ConnectionErrorException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + getRecentToken());
        return headers;
    }

    private String encodeBasicAuth(String username, String password) {
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
        String authHeader = "Basic " + new String(encodedAuth);
        return authHeader;
    }

    @Override
    public void cancelInvoice(String invoiceId) throws ConnectionErrorException {

        var headers = getAuthorizedHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var httpEntity
                = new HttpEntity<>(new LinkedMultiValueMap<>(), headers);

        try {
            restTemplate.postForLocation(url + "/v2/invoicing/invoices/" + invoiceId +
                    "/cancel", httpEntity);
        } catch (HttpClientErrorException e) {

            if (e.getMessage().equals("The requested action could not be performed," +
                    " semantically incorrect, or failed business validation.")) {
                System.out.println("invoice already cancelled");
            } else {
                e.printStackTrace();
            }
        } catch (Exception e) {

            throw new ConnectionErrorException(defaultErrorMessage);

        }

    }

}
