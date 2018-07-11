package net.malpiszon.stackexchange.postsparser.parser;

import java.time.LocalDateTime;

import org.xml.sax.Attributes;

public class AnalysisResult {
    private LocalDateTime firstPost;
    private LocalDateTime lastPost;
    private long totalPosts = 0;
    private long totalScore = 0;

    public void parseRow(Attributes attributes) {
        totalPosts++;
        var creationDate = attributes.getValue("CreationDate");
        if (firstPost == null) {
            firstPost = LocalDateTime.parse(creationDate);
        }
        lastPost = LocalDateTime.parse(creationDate);
        var score = attributes.getValue("Score");
        totalScore += Long.parseLong(score);
    }

    public LocalDateTime getFirstPost() {
        return firstPost;
    }

    public LocalDateTime getLastPost() {
        return lastPost;
    }

    public long getTotalPosts() {
        return totalPosts;
    }

    public long getTotalScore() {
        return totalScore;
    }
}
