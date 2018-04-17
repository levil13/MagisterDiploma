package dip.lux.configuration;

import dip.lux.service.util.DocsConverter.DocsConverter;
import dip.lux.service.util.DocsConverter.impl.DocsConverterImpl;
import dip.lux.service.util.PdfParser.PdfParser;
import dip.lux.service.util.PdfParser.impl.PdfParserImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "dip.lux")
public class AppConfiguration {
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }

    @Bean
    public PdfParser pdfParser() {
        return new PdfParserImpl();
    }

    @Bean
    public DocsConverter docsConverter() {
        return new DocsConverterImpl();
    }
}
