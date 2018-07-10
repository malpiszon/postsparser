package net.malpiszon.stackexchange.postsparser.services;

import java.util.concurrent.CompletableFuture;

import net.malpiszon.stackexchange.postsparser.dtos.AnalysisDto;
import net.malpiszon.stackexchange.postsparser.dtos.AnalyzeRequestDto;

public interface AnalyzeService {
    CompletableFuture<AnalysisDto> analyze(AnalyzeRequestDto analyzeRequestDto);
}
