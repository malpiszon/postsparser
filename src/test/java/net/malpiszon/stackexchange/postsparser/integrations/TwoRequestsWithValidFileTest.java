package net.malpiszon.stackexchange.postsparser.integrations;

import static org.hamcrest.Matchers.is;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.file.Files;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.ResourceUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
@AutoConfigureMockMvc
public class TwoRequestsWithValidFileTest extends IntegrationTest {

    @Test
    public void testValidFile_returnsResults() throws Exception {
        MvcResult requestResult1 = executeRequest();
        MvcResult requestResult2 = executeRequest();

        expectedResults(mockMvc.perform(asyncDispatch(requestResult1)));
        expectedResults(mockMvc.perform(asyncDispatch(requestResult2)));
    }

    void createExpectation() throws IOException {
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
                    .withBody(Files.readAllBytes(ResourceUtils.getFile("classpath:xml/Posts.xml").toPath()))
                    .withDelay(TimeUnit.SECONDS, 1)
            );
    }

    private void expectedResults(ResultActions result) throws Exception {
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$['details']['firstPost']", is("2017-08-02T19:22:36.567")))
                .andExpect(jsonPath("$['details']['lastPost']", is("2017-08-02T19:37:18.247")))
                .andExpect(jsonPath("$['details']['totalPosts']", is(4)))
                .andExpect(jsonPath("$['details']['avgScore']", is(4.75)));
    }
}
