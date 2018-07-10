package net.malpiszon.stackexchange.postsparser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.malpiszon.stackexchange.postsparser.parser.PostsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.xml.sax.SAXException;

@Configuration
public class AppConfig {

    @Bean
    public SAXParser saxParser() throws ParserConfigurationException, SAXException {
        return SAXParserFactory.newInstance().newSAXParser();
    }

    @Bean
    public PostsHandler PostsHandler() {
        return new PostsHandler();
    }

    @Bean
    public WebMvcConfigurer asyncTimeoutConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
                configurer.setDefaultTimeout(300_000);
            }
        };
    }
}
