package net.malpiszon.stackexchange.postsparser.dtos;

import java.time.LocalDateTime;

public class AnalysisDto {

    private final LocalDateTime date;
    private final AnalysisDetailsDto details;

    public AnalysisDto(AnalysisDetailsDto details) {
        this.date = LocalDateTime.now();
        this.details = details;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public AnalysisDetailsDto getDetails() {
        return details;
    }
}
