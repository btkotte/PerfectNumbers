package com.example.perfect;


import com.example.perfect.model.PerfectNumbers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "30000")
public class PerfectNumbersApplicationTests {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void checkPerfect_GivenPerfectNumber_Then200Response() {
        String number = "2305843008139952128";
        createRequestForCheckingPerfectNumber(number)
                .exchange()
                .expectStatus().isNoContent();
    }

    @Test
    public void checkPerfect_GivenNonPerfectNumber_Then404Response() {
        createRequestForCheckingPerfectNumber("5")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void checkPerfect_GivenInvalidPerfectNumber_Then400Response() {
        createRequestForCheckingPerfectNumber("abc")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void checkPerfect_GivenIncorrectPerfectNumber_Then400Response() {
        createRequestForCheckingPerfectNumber("-100")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("message").isEqualTo("number should be greater than 0.");
    }

    @Test
    public void checkPerfect_GivenEmptyPerfectNumber_Then404Response() {
        createRequestForCheckingPerfectNumber("")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void getAllPerfectNumbers_GivenValidStartEnd_Then200Response() {
        List<Long> perfNums = Arrays.asList(6L, 28L, 496L);
        createRequestForGettingPerfectNumbers("1", "500")
                .exchange()
                .expectStatus().isOk()
                .expectBody(PerfectNumbers.class)
                .consumeWith(list -> Assert.assertEquals(perfNums, list.getResponseBody().getPerfectNumbers()));
    }

    @Test
    public void getAllPerfectNumbers_GivenInvalidStart_Then400Response() {
        createRequestForGettingPerfectNumbers("abc", "500")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void getAllPerfectNumbers_GivenIncorrectStart_Then400Response() {
        createRequestForGettingPerfectNumbers("-1", "500")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("message").isEqualTo("start should be greater than 0.");
    }

    @Test
    public void getAllPerfectNumbers_GivenInvalidEnd_Then400Response() {
        createRequestForGettingPerfectNumbers("1", "678a")
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void getAllPerfectNumbers_GivenIncorrectEnd_Then400Response() {
        createRequestForGettingPerfectNumbers("1", "-5000")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("message").isEqualTo("end should be greater than 0.");
    }

    @Test
    public void getAllPerfectNumbers_GivenInvalidRange_Then400Response() {
        createRequestForGettingPerfectNumbers("500", "1")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("message").isEqualTo("Invalid range. Start should be less than end");
    }

    @Test
    public void getAllPerfectNumbers_GivenInvalidLongValues_Then400Response() {
        createRequestForGettingPerfectNumbers("1", "50000000000000000000000000")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("message").isEqualTo("Input numbers should be between 1 and " + Long.MAX_VALUE);
    }


    private WebTestClient.RequestHeadersSpec<?> createRequestForCheckingPerfectNumber(String number) {
        return webTestClient.get()
                .uri("/perfects/check/{number}", number);
    }

    private WebTestClient.RequestHeadersSpec<?> createRequestForGettingPerfectNumbers(String start, String end) {
        return webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/perfects")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .build());
    }

}
