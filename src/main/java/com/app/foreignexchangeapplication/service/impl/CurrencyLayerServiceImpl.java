package com.app.foreignexchangeapplication.service.impl;

import com.app.foreignexchangeapplication.entity.dto.CurrencyDto;
import com.app.foreignexchangeapplication.exception.ExternalServiceException;
import com.app.foreignexchangeapplication.service.CurrencyLayerService;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyLayerServiceImpl implements CurrencyLayerService {
    @Value("${currencylayer.access_key}")
    public String ACCESS_KEY;
    public static final String BASE_URL = "http://api.currencylayer.com/";
    public static final String CONVERT_ENDPOINT = "convert";

    private static final String CURRENT_CURRENCIES = "list";
    static CloseableHttpClient httpClient = HttpClients.createDefault();
    @Override
    public Double makeConvertRequest(String sourceCurrencyCode, BigDecimal amountSourceCurrencyCode, String targetCurrencyCode ) throws IOException {
        HttpGet get = new HttpGet(BASE_URL + CONVERT_ENDPOINT + "?access_key=" + ACCESS_KEY + "&from=" + sourceCurrencyCode +"&to="+targetCurrencyCode+"&amount="+amountSourceCurrencyCode+"&format=1");
        try {
            CloseableHttpResponse response =  httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            double result;

            JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));
            boolean isSuccess = exchangeRates.getBoolean("success");
            if (isSuccess){
                result = exchangeRates.getDouble("result");
            }else{
                JSONObject errorObject =  exchangeRates.getJSONObject("error");
                String error = errorObject.getString("info");
                String code = String.valueOf(errorObject.getInt("code"));
                throw new ExternalServiceException(code, error);
            }
            response.close();
            return result;
        } catch (IOException | ParseException e) {
            throw new IOException();
        } catch (JSONException e) {
            throw new JSONException(e.getMessage());
        }
    }
    @Override
    public List<CurrencyDto> getAllAvailableCurrencies(){
        HttpGet get = new HttpGet(BASE_URL + CURRENT_CURRENCIES + "?access_key=" + ACCESS_KEY);
        try {
            CloseableHttpResponse response =  httpClient.execute(get);
            HttpEntity entity = response.getEntity();
            List<CurrencyDto> currencyDtoList = null;

           JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));
            boolean isSuccess = exchangeRates.getBoolean("success");
            if (isSuccess){
                JSONObject currencies = exchangeRates.getJSONObject("currencies");
                currencyDtoList = new ArrayList<>();

                for (String currencyCode : currencies.keySet()) {
                    String currencyName = currencies.getString(currencyCode);

                    CurrencyDto currencyDto = new CurrencyDto();
                    currencyDto.setCurrencyCode(currencyCode);
                    currencyDto.setCurrencyName(currencyName);

                    currencyDtoList.add(currencyDto);
                }
            }
            response.close();
            return currencyDtoList;
        } catch (JSONException e) {
            throw new JSONException(e.getMessage());
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
