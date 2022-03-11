package com.test.controller.v1.commission;

import com.test.controller.v1.commission.exception.CommissionException;
import com.test.controller.v1.commission.exception.ExchangeCurrencyException;
import com.test.controller.v1.commission.model.CommissionRequest;
import com.test.controller.v1.commission.model.CommissionResponse;
import com.test.controller.v1.commission.service.ClientService;
import com.test.controller.v1.commission.service.CommissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableAutoConfiguration
class CommissionControllerTest {
    @Autowired
    private CommissionController controller;
    @SpyBean
    private CommissionService service;

    @Test
    void testGetCommissionOk() {
        final CommissionResponse response = CommissionResponse.builder().amount("0.05").currency("EUR").build();

        //GIVEN a correct request
        final CommissionRequest request = CommissionRequest
                .builder()
                .clientId(BigInteger.valueOf(43))
                .amount("1.00")
                .currency("USD")
                .date(new Date())
                .build();

        doReturn(Mono.just(response)).when(service).calculateCommission(request);

        //WHEN I ask to get commission
        final CommissionResponse resp = controller.getCommission(request).block();

        //THEN I expect the response not null
        assertNotNull(resp);
    }

    @Test
    void testGetCommissionKo() {
        //GIVEN a wrong request
        final CommissionRequest request = CommissionRequest
                .builder()
                .clientId(BigInteger.valueOf(43))
                .amount("1.00")
                .currency("USD")
                .build();

        //WHEN I ask to get commission
        final Mono<CommissionResponse> resp = controller.getCommission(request);
        final Exception exception = assertThrows(CommissionException.class, resp::block);

        //THEN I expect the response not null
        assertNotNull(exception);
    }

    @Test
    void testGetCommissionKoExchange() {
        //GIVEN a wrong request
        final CommissionRequest request = CommissionRequest
                .builder()
                .clientId(BigInteger.valueOf(43))
                .amount("1.00")
                .currency("dwdwadwadw")
                .date(new Date())
                .build();

        //WHEN I ask to get commission
        final Mono<CommissionResponse> resp = controller.getCommission(request);
        final Exception exception = assertThrows(ExchangeCurrencyException.class, resp::block);

        //THEN I expect the response not null
        assertNotNull(exception);
    }
}
