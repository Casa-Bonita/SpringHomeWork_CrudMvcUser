package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// контроллеры являются сервлетами, т.к. обрабатывают URL-запросы

@Controller
// данная аннотация определяет класс как контроллер Spring MVC
public class IndexController {

    @GetMapping("/")
    // данная аннотация используется для маппинга (связывания) URL и метода
    // т.е. метод относится к URL-адресу "/".
    // при обращении по адресу: /, сработает данный метод
    public String index(){
        return "index";
        // метод возвращает строку "index",
        // которую движок thymleaf преобразует в следующую строку: //WEB-INF/templates/index.html
        // используя настройки, которые указаны в WebConfig
        // файл index.html вернётся в качестве ответа на запрос из браузера
    }
}
