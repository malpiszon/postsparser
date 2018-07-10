package net.malpiszon.stackexchange.postsparser.services.impls;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

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

    public XmlAnalyzeService(ParseService parseService) {
        this.parseService = parseService;
    }

    @Override
    public CompletableFuture<AnalysisDto> analyze(AnalyzeRequestDto analyzeRequestDto) {
        return parseService.parse(analyzeRequestDto.getUrl())
                .thenApply(analysisResult -> prepareAnalysisDto(analysisResult))
                .orTimeout(5, TimeUnit.MINUTES);
    }

    private AnalysisDto prepareAnalysisDto(AnalysisResult result) {
        AnalysisDto analysisDto = new AnalysisDto(prepareAnalysisDetailsDto(result));

        return analysisDto;
    }

    private AnalysisDetailsDto prepareAnalysisDetailsDto(AnalysisResult result) {
        double avgScore = 0;
        if (result.getTotalPosts() > 0) {
            avgScore = (double) result.getTotalScore() / result.getTotalPosts();
        }

        return new AnalysisDetailsDto(result.getFirstPost(), result.getLastPost(),
                result.getTotalPosts(), avgScore);
    }
}
