package net.malpiszon.stackexchange.postsparser.services.impls;

import static java.util.concurrent.CompletableFuture.supplyAsync;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.SAXParser;

import net.malpiszon.stackexchange.postsparser.ServiceConfig;
import net.malpiszon.stackexchange.postsparser.parser.AnalysisResult;
import net.malpiszon.stackexchange.postsparser.parser.PostsHandler;
import net.malpiszon.stackexchange.postsparser.services.ParseService;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

@Service
public class SaxParseService implements ParseService {

    private final SAXParser saxParser;
    private final PostsHandler handler;
    private final ServiceConfig config;

    public SaxParseService(SAXParser saxParser, PostsHandler handler, ServiceConfig config) {
        this.saxParser = saxParser;
        this.handler = handler;
        this.config = config;
    }

    @Override
    public CompletableFuture<AnalysisResult> parse(String fileUrl) {
        return supplyAsync(() -> executeParser(fileUrl))
                .orTimeout(config.getAsyncTimeout(), TimeUnit.SECONDS);
    }

    private AnalysisResult executeParser(String fileUrl) {
        try {
            return parseFile(fileUrl);
        } catch (SAXException | IOException e) {
            throw new CompletionException(e);
        }
    }

    private AnalysisResult parseFile(String fileUrl) throws SAXException, IOException {
        saxParser.parse(getInputStream(fileUrl), handler);

        return handler.getAnalysisResult();
    }

    private InputStream getInputStream(String fileUrl) throws IOException {
        var url = new URL(fileUrl);
        var httpUrlConnection = (HttpURLConnection) url.openConnection();
        return new BufferedInputStream(httpUrlConnection.getInputStream());
    }
}
