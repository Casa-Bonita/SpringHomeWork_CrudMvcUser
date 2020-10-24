package app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration // аннотация указывает, что это конфигурационный класс
@EnableWebMvc // аннотация включает паттерны MVC, чтобы работали контроллеры
@ComponentScan (basePackages = "app") // указывает, что в пакете "app" находятся классы, которыми будет управлять Spring (в данном случае - контроллеры)
public class WebConfig implements WebMvcConfigurer { // реализация интерфейса WebMvcConfigurer необходимо, чтобы Spring определил класс WebConfig как класс,
                                                    // в котором находятся настройки по отображению html-страниц
                                                    // методы интерфейса не переопределяются, а дополняются, т.е. производится ручное конфигурирование настроек по отображению html-файлов

    @Autowired // добавляем/инжектим созданный applicationContext, добавляем в дальнейшем в движок thymleaf
    private ApplicationContext applicationContext;

    @Bean // инициализация бина, который будет создан с помощью Dependency Injection
    public ViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver(); // указание, что будет использоваться движок для работы с view из модели MVC (в данном случае, html-файлы под управлением thymeleaf)
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        viewResolver.setContentType("text/html; charset = UTF-8");
        return viewResolver;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver(){ // основной блок ручной настройки/конфигурирования
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/templates/"); // указание на местонахождение html-файлов
        templateResolver.setSuffix(".html"); // указание на расширение файла
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(true);
        templateResolver.setCharacterEncoding("UTF-8"); // указание кодировки файла
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine(){ // указание, что thymleaf будет двжиком
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }
}
