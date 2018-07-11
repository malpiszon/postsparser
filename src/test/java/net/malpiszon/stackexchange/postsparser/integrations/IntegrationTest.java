package net.malpiszon.stackexchange.postsparser.integrations;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.malpiszon.stackexchange.postsparser.dtos.AnalyzeRequestDto;
import org.junit.Rule;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

public abstract class IntegrationTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this);
    MockServerClient mockServerClient;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper mapper;

    MvcResult executeRequest() throws Exception {
        createExpectation();

        AnalyzeRequestDto analyzeRequestDto = new AnalyzeRequestDto();
        analyzeRequestDto.setUrl("http:/" + mockServerClient.remoteAddress() + "/Posts.xml");
        String contentAsJson = mapper.writeValueAsString(analyzeRequestDto);

        return mockMvc.perform(post("/analyze")
                .content(contentAsJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    abstract void createExpectation() throws Exception;
}
