package net.malpiszon.stackexchange.postsparser.services.impls;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import javax.xml.parsers.SAXParser;

import net.malpiszon.stackexchange.postsparser.ServiceConfig;
import net.malpiszon.stackexchange.postsparser.parser.AnalysisResult;
import net.malpiszon.stackexchange.postsparser.parser.PostsHandler;
import net.malpiszon.stackexchange.postsparser.services.ParseService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.SAXException;

@RunWith(SpringRunner.class)
public class SaxParseServiceTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private static final String URL = "http://url.pl/file.xml";

    private ParseService parseService;

    @MockBean
    private SAXParser saxParser;

    @MockBean
    private PostsHandler postsHandler;

    @Before
    public void setUp() {
        ServiceConfig config = new ServiceConfig();
        parseService = new SaxParseService(saxParser, postsHandler, config);
    }

    @Test
    public void testAnalyze_withParserThrowingSaxException_throwsSAXException() throws Exception {
        doThrow(new SAXException()).when(saxParser).parse(any(InputStream.class), any(PostsHandler.class));

        exception.expect(ExecutionException.class);
        exception.expectCause(Matchers.isA(SAXException.class));

        CompletableFuture<AnalysisResult> result = parseService.parse(URL);
        result.get();
    }

    @Test
    public void testAnalyze_withInvalidUrl_throwsMalformedURLException() throws Exception {
        exception.expect(ExecutionException.class);
        exception.expectCause(Matchers.isA(MalformedURLException.class));

        CompletableFuture<AnalysisResult> result = parseService.parse("h");
        result.get();
    }

    @Test
    public void testAnalyze_withValidUrl_returnsResults() throws Exception {
        AnalysisResult analysisResult = new AnalysisResult();
        doNothing().when(saxParser).parse(any(InputStream.class), any(PostsHandler.class));
        when(postsHandler.getAnalysisResult()).thenReturn(analysisResult);

        CompletableFuture<AnalysisResult> result = parseService.parse(URL);

        assertEquals("Expected answer", analysisResult, result.get());
    }
}