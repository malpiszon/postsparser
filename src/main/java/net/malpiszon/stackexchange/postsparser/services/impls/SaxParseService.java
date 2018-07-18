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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import net.malpiszon.stackexchange.postsparser.ServiceConfig;
import net.malpiszon.stackexchange.postsparser.parser.AnalysisResult;
import net.malpiszon.stackexchange.postsparser.parser.PostsHandler;
import net.malpiszon.stackexchange.postsparser.services.ParseService;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

@Service
public class SaxParseService implements ParseService {

    private final SAXParserFactory saxParserFactory;
    private final PostsHandler.PostsHandlerFactory handlerFactory;
    private final ServiceConfig config;

    public SaxParseService(SAXParserFactory saxParserFactory, PostsHandler.PostsHandlerFactory handlerFactory,
                           ServiceConfig config) {
        this.saxParserFactory = saxParserFactory;
        this.handlerFactory = handlerFactory;
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
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new CompletionException(e);
        }
    }

    private AnalysisResult parseFile(String fileUrl) throws SAXException, IOException, ParserConfigurationException {
        PostsHandler handler = handlerFactory.createPostsHandler();
        saxParserFactory.newSAXParser().parse(getInputStream(fileUrl), handler);

        return handler.getAnalysisResult();
    }

    private InputStream getInputStream(String fileUrl) throws IOException {
        var url = new URL(fileUrl);
        var httpUrlConnection = (HttpURLConnection) url.openConnection();
        return new BufferedInputStream(httpUrlConnection.getInputStream());
    }
}
