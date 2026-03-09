package com.oyenavneet.aggregatorservice;

import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

public class CustomerInformationTest extends AbstractIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(CustomerInformationTest.class);

    @Test
    public void testCustomerInformation() {
        //given mock
        mockCustomerInformation("customer-services/customer-information-200.json", 200);


        //response
        getCustomerInformation(HttpStatus.OK)
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("nav")
                .jsonPath("$.balance").isEqualTo(10000)
                .jsonPath("$.holding").isNotEmpty();


    }


    @Test
    public void customerNotFound() {
        //given mock
        mockCustomerInformation("customer-services/customer-information-404.json", 404);


        //response
        getCustomerInformation(HttpStatus.NOT_FOUND)
                .jsonPath("$.detail").isEqualTo("Customer [id=1] is not found")
                .jsonPath("$.title").isNotEmpty();


    }


    private void mockCustomerInformation(String path, int responseCode) {
        // mock customer service
        var responseBody = this.resourceToString(path);
        mockServerClient
                .when(HttpRequest.request("/customers/1"))
                .respond(
                        HttpResponse.response(responseBody)
                                .withStatusCode(responseCode)
                                .withContentType(MediaType.APPLICATION_JSON)
                );
    }


    private WebTestClient.BodyContentSpec getCustomerInformation(HttpStatus status) {
        return this.client.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().isEqualTo(status)
                .expectBody()
                .consumeWith(e -> logger.info("{}", new String(Objects.requireNonNull(e.getResponseBody()))));
    }
}
