package net.malpiszon.stackexchange.postsparser.controllers;

import java.util.concurrent.Future;
import javax.validation.Valid;

import net.malpiszon.stackexchange.postsparser.dtos.AnalysisDto;
import net.malpiszon.stackexchange.postsparser.dtos.AnalyzeRequestDto;
import net.malpiszon.stackexchange.postsparser.services.AnalyzeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("analyze")
public class AnalyzeController {

    private final AnalyzeService analyzeService;

    public AnalyzeController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public Future<AnalysisDto> analyzeXmlFile(@RequestBody @Valid AnalyzeRequestDto requestDto) {
        return analyzeService.analyze(requestDto);
    }
}
