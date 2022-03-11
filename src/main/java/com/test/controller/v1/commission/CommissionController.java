package com.test.controller.v1.commission;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.controller.v1.commission.model.CommissionRequest;
import com.test.controller.v1.commission.model.CommissionResponse;
import com.test.controller.v1.commission.service.CommissionService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping(value = "api/v1/", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
public class CommissionController {
    private final CommissionService service;

    @PostMapping(value = "commission-calculation")
    public Mono<CommissionResponse> getCommission(@RequestBody final CommissionRequest request) {
        return service.calculateCommission(request);
    }
}
