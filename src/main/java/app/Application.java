package app;

import app.config.WebConfig;
import app.model.User;
import app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class Application implements WebApplicationInitializer { // реализуется интерфейс для выполнения действий при запуске приложения - инициализация контекста Spring MVC
                                                                // без использования XML

    Logger logger = LoggerFactory.getLogger(Application.class);

    @Override
    public void onStartup (ServletContext servletContext) throws ServletException{ // передается в метод Servlet-контекст, инициализацию которого и проводится в методе
        UserService.userList.add(new User(1, "Andrey", "Sergeev"));
        UserService.userList.add(new User(2, "Olga", "Lobanova"));

        // создание "корневого" контекста (Spring-контекст) для описания бинов приложения,
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        // регистрируем конфигурацию, с указанием конфигурационного класса
        context.register(WebConfig.class);
        // регистрация сервлета-диспетчера, связывание двух контекстов: Servlet-контекст и Spring-контекст
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        // настраиваем маппинг (связывание)
        // при обращении к адресам, которые начинаютсся с главной страницы будет подключаться DispatcherServlet
        dispatcher.addMapping("/");
        // указываем, что при загрузке сервлет запустится первым
        dispatcher.setLoadOnStartup(1);
    }
}
