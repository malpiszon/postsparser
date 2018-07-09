package net.malpiszon.stackexchange.postsparser.services;

import java.util.concurrent.Future;

import net.malpiszon.stackexchange.postsparser.dtos.AnalysisDto;
import net.malpiszon.stackexchange.postsparser.dtos.AnalyzeRequestDto;

public interface AnalyzeService {
    Future<AnalysisDto> analyze(AnalyzeRequestDto analyzeRequestDto);
}
