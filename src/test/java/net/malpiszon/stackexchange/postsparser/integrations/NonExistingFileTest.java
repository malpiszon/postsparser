package net.malpiszon.stackexchange.postsparser.integrations;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import net.malpiszon.stackexchange.postsparser.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
public class NonExistingFileTest extends IntegrationTest {

    @Test
    public void testNonExistingFile_returnsNotFound() throws Exception {
        MvcResult requestResult = executeRequest();

        mockMvc.perform(asyncDispatch(requestResult))
                .andExpect(status().isNotFound());
    }

    void createExpectation() throws IOException {
        mockServerClient
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/Posts.xml"))
                .respond(
                        response()
                                .withStatusCode(404)
                                .withDelay(TimeUnit.SECONDS, 1)
                );
    }
}
