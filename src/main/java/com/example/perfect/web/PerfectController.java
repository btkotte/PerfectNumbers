package com.example.perfect.web;

import com.example.perfect.model.PerfectNumbers;
import com.example.perfect.service.PerfectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.constraints.Min;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController()
@Validated
@RequestMapping(value = "perfects", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class PerfectController {

    public final PerfectService perfectService;

    @GetMapping("/check/{number}")
    public Mono<Long> checkPerfect(@PathVariable @Min(value = 1, message = "number should be greater than 0") Long number,
                                   ServerHttpResponse response) {
        return perfectService.findPerfect(number)
                .flatMap(isPerfect -> {
                    if (isPerfect) {
                        response.setStatusCode(HttpStatus.NO_CONTENT);
                    } else {
                        response.setStatusCode(HttpStatus.NOT_FOUND);
                    }
                    return Mono.empty();
                });
    }

    @GetMapping
    public Mono<PerfectNumbers> getAllPerfects(@RequestParam
                                               @Min(value = 1, message = "start should be greater than 0") Long start,
                                               @RequestParam
                                               @Min(value = 1, message = "end should be greater than 0") Long end) {
        return perfectService.findAllPerfects(start, end).subscribeOn(Schedulers.elastic());
    }

}

