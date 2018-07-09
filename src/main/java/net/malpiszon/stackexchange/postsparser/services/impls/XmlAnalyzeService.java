package net.malpiszon.stackexchange.postsparser.services.impls;

import java.util.concurrent.Future;

import net.malpiszon.stackexchange.postsparser.dtos.AnalysisDto;
import net.malpiszon.stackexchange.postsparser.dtos.AnalyzeRequestDto;
import net.malpiszon.stackexchange.postsparser.services.AnalyzeService;
import org.springframework.stereotype.Service;

@Service
public class XmlAnalyzeService implements AnalyzeService {

    @Override
    public Future<AnalysisDto> analyze(AnalyzeRequestDto analyzeRequestDto) {
        return null;
    }
}
