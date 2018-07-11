package net.malpiszon.stackexchange.postsparser.parser;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class PostsHandler extends DefaultHandler {

    private final AnalysisResult result = new AnalysisResult();

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) {
        if (name.equalsIgnoreCase("row")) {
            result.parseRow(attributes);
        }
    }

    public AnalysisResult getAnalysisResult() {
        return result;
    }
}