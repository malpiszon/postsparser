package net.malpiszon.stackexchange.postsparser.services.impls;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import net.malpiszon.stackexchange.postsparser.ServiceConfig;
import net.malpiszon.stackexchange.postsparser.dtos.AnalysisDto;
import net.malpiszon.stackexchange.postsparser.dtos.AnalyzeRequestDto;
import net.malpiszon.stackexchange.postsparser.parser.AnalysisResult;
import net.malpiszon.stackexchange.postsparser.services.AnalyzeService;
import net.malpiszon.stackexchange.postsparser.services.ParseService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class XmlAnalyzeServiceTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final String URL = "http://url.pl/file.xml";
    private static final LocalDateTime START_DATE = LocalDateTime.now().minusDays(1);
    private static final LocalDateTime END_DATE = LocalDateTime.now().minusMinutes(1);

    private AnalyzeService analyzeService;
    private AnalyzeRequestDto dto;
    private CompletableFuture<AnalysisResult> analysisResultCompletableFuture;

    @Mock
    private AnalysisResult analysisResult;

    @MockBean
    private ParseService parseService;

    @Before
    public void setUp() throws Exception {
        ServiceConfig config = new ServiceConfig();
        analyzeService = new XmlAnalyzeService(parseService, config);

        dto = new AnalyzeRequestDto();
        dto.setUrl(URL);

        analysisResultCompletableFuture = new CompletableFuture<>();
        analysisResultCompletableFuture.complete(analysisResult);
    }

    @Test
    public void testAnalyze_withParseThrowingException_throwsSameException() throws Exception {
        when(parseService.parse(URL)).thenThrow(new IllegalArgumentException());

        exception.expect(IllegalArgumentException.class);

        analyzeService.analyze(dto);
    }

    @Test
    public void testAnalyze_withPostsInFile_returnsResults() throws Exception {
        when(parseService.parse(URL)).thenReturn(analysisResultCompletableFuture);
        when(analysisResult.getTotalPosts()).thenReturn(8L);
        when(analysisResult.getTotalScore()).thenReturn(24L);
        when(analysisResult.getFirstPost()).thenReturn(START_DATE);
        when(analysisResult.getLastPost()).thenReturn(END_DATE);

        CompletableFuture<AnalysisDto> result = analyzeService.analyze(dto);

        assertNotNull("Expected date", result.get().getDate());
        assertEquals("Expected first post date", START_DATE, result.get().getDetails().getFirstPost());
        assertEquals("Expected last post date", END_DATE, result.get().getDetails().getLastPost());
        assertEquals("Expected total posts", 8, result.get().getDetails().getTotalPosts());
        assertEquals("Expected avg score", 3, result.get().getDetails().getAvgScore(), 0.01);
    }

    @Test
    public void testAnalyze_withNoPostsInFile_returnsResults() throws Exception {
        when(parseService.parse(URL)).thenReturn(analysisResultCompletableFuture);
        when(analysisResult.getTotalPosts()).thenReturn(0L);
        when(analysisResult.getTotalScore()).thenReturn(0L);
        when(analysisResult.getFirstPost()).thenReturn(null);
        when(analysisResult.getLastPost()).thenReturn(null);

        CompletableFuture<AnalysisDto> result = analyzeService.analyze(dto);

        assertNotNull("Expected date", result.get().getDate());
        assertEquals("Expected first post date", null, result.get().getDetails().getFirstPost());
        assertEquals("Expected last post date", null, result.get().getDetails().getLastPost());
        assertEquals("Expected total posts", 0, result.get().getDetails().getTotalPosts());
        assertEquals("Expected avg score", 0, result.get().getDetails().getAvgScore(), 0.01);
    }
}