package dip.lux.configuration;

import dip.lux.service.util.DocsConverter.DocsConverter;
import dip.lux.service.util.DocsConverter.impl.DocsConverterImpl;
import dip.lux.service.util.PdfParser.PdfParser;
import dip.lux.service.util.PdfParser.impl.PdfParserImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "dip.lux")
public class AppConfiguration implements WebMvcConfigurer {
    private final int maxUploadSizeInMb = 100 * 1024 * 1024; // 5 MB

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/scripts/**").addResourceLocations("/WEB-INF/static/js/");
        registry.addResourceHandler("/styles/**").addResourceLocations("/WEB-INF/static/css/");
        registry.addResourceHandler("/templates/**").addResourceLocations("/WEB-INF/static/templates/");
    }

    @Bean
    public ViewResolver freeMarkerViewResolver() {
        FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
        resolver.setSuffix(".ftl");
        return resolver;
    }

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        configurer.setTemplateLoaderPath("/WEB-INF/static/templates/");
        return configurer;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(maxUploadSizeInMb);
        return multipartResolver;
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
