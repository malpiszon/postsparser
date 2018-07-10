package net.malpiszon.stackexchange.postsparser.controllers;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.malpiszon.stackexchange.postsparser.dtos.AnalysisDetailsDto;
import net.malpiszon.stackexchange.postsparser.dtos.AnalysisDto;
import net.malpiszon.stackexchange.postsparser.dtos.AnalyzeRequestDto;
import net.malpiszon.stackexchange.postsparser.services.AnalyzeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = AnalyzeController.class)
public class AnalyzeControllerTest {

    private static final String VALID_URL = "http://test.pl/posts.xml";
    private static final LocalDateTime START_DATE = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime END_DATE = LocalDateTime.now().minusMinutes(1);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AnalyzeService analyzeService;

    @Test
    public void testPostAnalyze_withoutParameters_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostAnalyze_withNullParameter_returnsBadRequest() throws Exception {
        String contentAsJson = mapper.writeValueAsString(new AnalyzeRequestDto());

        mockMvc.perform(post("/analyze")
                .content(contentAsJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostAnalyze_withEmptyParameter_returnsBadRequest() throws Exception {
        AnalyzeRequestDto analyzeRequestDto = new AnalyzeRequestDto();
        analyzeRequestDto.setUrl("");
        String contentAsJson = mapper.writeValueAsString(analyzeRequestDto);

        mockMvc.perform(post("/analyze")
                .content(contentAsJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostAnalyze_withInvalidParameter_returnsBadRequest() throws Exception {
        AnalyzeRequestDto analyzeRequestDto = new AnalyzeRequestDto();
        analyzeRequestDto.setUrl("test@test.pl");
        String contentAsJson = mapper.writeValueAsString(analyzeRequestDto);

        mockMvc.perform(post("/analyze")
                .content(contentAsJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostAnalyze_withValidUrl_returnsData() throws Exception {
        AnalyzeRequestDto analyzeRequestDto = new AnalyzeRequestDto();
        analyzeRequestDto.setUrl(VALID_URL);
        String contentAsJson = mapper.writeValueAsString(analyzeRequestDto);

        AnalysisDetailsDto analysisDetailsDto = new AnalysisDetailsDto(START_DATE, END_DATE, 1, 1);
        AnalysisDto analysisDto = new AnalysisDto(analysisDetailsDto);
        CompletableFuture<AnalysisDto> result = CompletableFuture.completedFuture(analysisDto);

        when(analyzeService.analyze(any(AnalyzeRequestDto.class))).thenReturn(result);

        MvcResult performResult = mockMvc.perform(post("/analyze")
                .content(contentAsJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();

        mockMvc.perform(asyncDispatch(performResult))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['details']['totalPosts']", is(1)))
                .andExpect(jsonPath("$['details']['avgScore']", is(1.0)));

        verifyServiceCalledWithValidUrlOnce();
    }

    private void verifyServiceCalledWithValidUrlOnce() {
        ArgumentCaptor<AnalyzeRequestDto> frdCaptor = ArgumentCaptor.forClass(AnalyzeRequestDto.class);
        verify(analyzeService, times(1)).analyze(frdCaptor.capture());
        assertEquals(VALID_URL, frdCaptor.getValue().getUrl());
        verifyNoMoreInteractions(analyzeService);
    }
}