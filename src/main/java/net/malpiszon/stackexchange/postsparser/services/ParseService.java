package net.malpiszon.stackexchange.postsparser.services;

import java.util.concurrent.CompletableFuture;

import net.malpiszon.stackexchange.postsparser.parser.AnalysisResult;

public interface ParseService {
    CompletableFuture<AnalysisResult> parse(String fileToParse);
}
