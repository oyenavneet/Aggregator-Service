package com.oyenavneet.aggregatorservice;

import org.junit.jupiter.api.BeforeAll;
import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.springtest.MockServerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@AutoConfigureWebTestClient
@MockServerTest
@SpringBootTest(properties = {
        "stock.service.url=http://localhost:${mockServerPort}", // mockServerPort replaced by the chosen free port for MockServer
        "customer.service.url=http://localhost:${mockServerPort}"
})
public class AbstractIntegrationTest {

    public static final Path TEST_DIR = Path.of("src/test/resources");

    protected MockServerClient mockServerClient; // MockServerClient will handle Autowired

    @Autowired
    protected WebTestClient client;

    @BeforeAll
    public static void beforeAll() {
        ConfigurationProperties.disableLogging(true);
    }

    protected String resourceToString(String relativePath) {
        try {
            return Files.readString(TEST_DIR.resolve(relativePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
