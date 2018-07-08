package net.malpiszon.stackexchange.postsparser.controllers;

import javax.validation.Valid;

import net.malpiszon.stackexchange.postsparser.dtos.AnalyzeRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("analyze")
public class AnalyzeController {

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> analyzeFile(@RequestBody @Valid AnalyzeRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
