package net.malpiszon.stackexchange.postsparser.integrations;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.concurrent.TimeUnit;

import net.malpiszon.stackexchange.postsparser.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.model.Header;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
public class SlowFileTest extends IntegrationTest {

    @Test
    public void testValidFile_returnsResults() throws Exception {
        MvcResult requestResult = executeRequest();

        mockMvc.perform(asyncDispatch(requestResult))
                .andExpect(status().isRequestTimeout());
    }

    void createExpectation() {
        mockServerClient
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/Posts.xml"))
            .respond(
                response()
                    .withStatusCode(200)
                    .withHeaders(
                        new Header(HttpHeaders.CONTENT_TYPE, "application/xml"))
                    .withBody("")
                    .withDelay(TimeUnit.SECONDS, 6)
            );
    }
}
