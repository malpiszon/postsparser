package net.malpiszon.stackexchange.postsparser.dtos;

import java.time.LocalDateTime;

public class AnalysisDto {

    private LocalDateTime date;
    private String details;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
