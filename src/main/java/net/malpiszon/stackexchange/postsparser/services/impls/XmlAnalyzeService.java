package net.malpiszon.stackexchange.postsparser.services.impls;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import net.malpiszon.stackexchange.postsparser.ServiceConfig;
import net.malpiszon.stackexchange.postsparser.dtos.AnalysisDetailsDto;
import net.malpiszon.stackexchange.postsparser.dtos.AnalysisDto;
import net.malpiszon.stackexchange.postsparser.dtos.AnalyzeRequestDto;
import net.malpiszon.stackexchange.postsparser.parser.AnalysisResult;
import net.malpiszon.stackexchange.postsparser.services.AnalyzeService;
import net.malpiszon.stackexchange.postsparser.services.ParseService;
import org.springframework.stereotype.Service;

@Service
public class XmlAnalyzeService implements AnalyzeService {

    private final ParseService parseService;
    private final ServiceConfig config;

    public XmlAnalyzeService(ParseService parseService, ServiceConfig config) {
        this.parseService = parseService;
        this.config = config;
    }

    @Override
    public CompletableFuture<AnalysisDto> analyze(AnalyzeRequestDto analyzeRequestDto) {
        return parseService.parse(analyzeRequestDto.getUrl())
                .thenApply(this::prepareAnalysisDto)
                .orTimeout(config.getAsyncTimeout(), TimeUnit.SECONDS);
    }

    private AnalysisDto prepareAnalysisDto(AnalysisResult result) {
        return new AnalysisDto(prepareAnalysisDetailsDto(result));
    }

    private AnalysisDetailsDto prepareAnalysisDetailsDto(AnalysisResult result) {
        var avgScore = 0D;
        if (result.getTotalPosts() > 0) {
            avgScore = (double) result.getTotalScore() / result.getTotalPosts();
        }

        return new AnalysisDetailsDto(result.getFirstPost(), result.getLastPost(),
                result.getTotalPosts(), avgScore);
    }
}
