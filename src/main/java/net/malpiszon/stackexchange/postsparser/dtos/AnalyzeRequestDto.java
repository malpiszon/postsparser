package net.malpiszon.stackexchange.postsparser.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

public class AnalyzeRequestDto {
    @URL
    @NotBlank
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
