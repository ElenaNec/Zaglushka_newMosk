package com.example.newMock.Controller;

import com.example.newMock.Model.RequestDTO;
import com.example.newMock.Model.ResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Random;

@RestController
public class MainController {

    // Для логирования
    private Logger log = LoggerFactory.getLogger(MainController.class);

    // Для мапинга
    ObjectMapper mapper = new ObjectMapper();

    // какой метод будем использовать: для примера будем исп POST
    @PostMapping(
            value = "/info/postBalances",                      // адрес заглушки
            produces = MediaType.APPLICATION_JSON_VALUE,      // описываем типы, которые будут прилетать (в нашем случае Json)
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    // на входе метод должен получать экземпляр нашего класса request Json, для этого исп анотацию @RequestBody
    public Object postBalances(@RequestBody RequestDTO requestDTO) {
        try {
            String clientId = requestDTO.getClientId();     // вытащили из json clientId для дальнейшего парсинга
            char firstDigit = clientId.charAt(0);           // вытащили из clientId 1-й символ
            BigDecimal maxLimit;                            // объявляем переменную максимального лимита
            String rqUID = requestDTO.getRqUID();
            Random random = new Random();
            String currency = null;
            int balance = 0;

            // Если номер клиента начинается с 8 то валюта счета(currency) доллар - US, максимальный лимит(maxLimit) - 2000.00

            if (firstDigit == '8') {
                maxLimit = new BigDecimal(2000);
                currency = "US";
                balance = random.nextInt(2000);

                // Если номер клиента начинается с 9 то валюта счета(currency) евро - EU, максимальный лимит(maxLimit) - 1000.00
            } else if (firstDigit == '9') {
                maxLimit = new BigDecimal(1000);
                currency = "EU";
                balance = random.nextInt(1000);

                // Если начинается с любой другой цифру то валюта счета(currency) доллар - RUB, максимальный лимит(maxLimit) - 10000.00
            } else {
                maxLimit = new BigDecimal(10000);
                currency = "RUB";
                balance = random.nextInt(10000);
            }

            ResponseDTO responseDTO = new ResponseDTO();   // создаем экземпляр класса ResponseDTO
            // заполняем поля экземпляра
            responseDTO.setRqUID(rqUID);
            responseDTO.setClientId(clientId);
            responseDTO.setAccount(requestDTO.getAccount());
            responseDTO.setCurrency(currency);
            responseDTO.setBalance(balance);
            responseDTO.setMaxLimit(maxLimit);

            // залогируем ответ
            log.error("******** RequestDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestDTO));
            log.error("******** ResponseDTO **********" + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseDTO));

            return responseDTO;

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
