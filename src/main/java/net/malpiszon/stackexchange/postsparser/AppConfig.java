package net.malpiszon.stackexchange.postsparser;

import javax.xml.parsers.SAXParserFactory;

import net.malpiszon.stackexchange.postsparser.parser.PostsHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {

    @Bean
    public SAXParserFactory saxParserFactory() {
        return SAXParserFactory.newInstance();
    }

    @Bean
    public PostsHandler.PostsHandlerFactory postsHandlerFactory() {
        return new PostsHandler.PostsHandlerFactory();
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

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfig() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
