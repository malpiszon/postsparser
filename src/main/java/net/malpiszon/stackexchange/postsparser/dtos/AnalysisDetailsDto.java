package net.malpiszon.stackexchange.postsparser.dtos;

import java.time.LocalDateTime;

public class AnalysisDetailsDto {
    private final LocalDateTime firstPost;
    private final LocalDateTime lastPost;
    private final long totalPosts;
    private final double avgScore;

    public AnalysisDetailsDto(LocalDateTime firstPost, LocalDateTime lastPost, long totalPosts, double avgScore) {
        this.firstPost = firstPost;
        this.lastPost = lastPost;
        this.totalPosts = totalPosts;
        this.avgScore = avgScore;
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

    public double getAvgScore() {
        return avgScore;
    }
}
