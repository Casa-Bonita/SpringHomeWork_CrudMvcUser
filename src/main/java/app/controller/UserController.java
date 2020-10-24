package app.controller;

import app.model.User;
import app.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UserController {

    @GetMapping("/allUser")
    // в качестве парметра принимаем Model
    // Model - это управляемый Spring-ом класс, который мы не создаём
    // Model нужна, чтобы с её помощью передать данные из кода в страницу html
    // "данные из кода" - в нашем случае, это список userList
    public String allUserIndex(Model model){
        model.addAttribute("userList", UserService.userList);
        // Model будет передана во view allUser
        return "allUser";
    }

    @GetMapping("/addUser")
    // аннотация используется для маппинга (связывания) URL и метода
    // обрабатывается GET-запрос /addUser
    // метод возвращает страницу addUser
    // метод относится к URL-адресу "/addUser".
    public String addUserIndex(){
        return "addUser";
    }

    @PostMapping("/addUser")
    // обрабатывается POST-запрос /addUser
    // аннотация @RequestParam означает, что ожидаем значения из формы, из соответствующих полей для ввода значений: id, login, password
    // имена полей формы заданы в файле addUser.html, в переменной "name"
    public String adduser(@RequestParam(name = "id") int id, @RequestParam(name = "login") String login, @RequestParam(name = "password") String password){
        // создаем объект user
        User user = new User (id, login, password);
        // добавляем user в список
        UserService.userList.add(user);
        // возвращаем строницу addUser, т.е. она откроется заново
        return "addUser";
    }

    @GetMapping("/updateUser/{userId}")
    // аннотация @PathVariable означает, что ожидаем значения из URL (из самого пути), т.е. "переменная пути"
    // указываем название переменной, которую нужно достать и параметр, в который она будет записана
    // в Model будет записан объект User с указанным id и с помощью Model объект будет передан на страницу userCard
    public String updateUser(@PathVariable(name = "userId") int userId, Model model){
        // поиск User с указанным id
        Optional<User> first = UserService.userList.stream().filter(user -> user.getId() == userId).findFirst();
        first.ifPresent(user ->{
            // если такое User существует, то он добавляется в Model, в указанную переменную
            model.addAttribute("user", user);
        });
        // Model будет передана во view userCard
        return "userCard";
    }

    @PostMapping("/updateUser")
    public String updateUser(User newUser, Model model){
        // можно использовать аннотацию @RequestParam и самим создавать объект User newUser
        // в данном случае указан параметр - объект User newUser, а Spring самостоятельно создал объект newUser, использую рефлексию
        // т.е. он видит поля в форме "userCard" и поля в классе User (хоть они и "private")
        // Spring сопоставляе названия полей из класса User и полей, которые приходят из формы userCard
        // для этого должно быть обязательное полное совпадение названия полей
        Optional<User> first = UserService.userList.stream().filter(user -> user.getId() == newUser.getId()).findFirst();
        first.ifPresent(user ->{
            user.setLogin(newUser.getLogin());
            user.setPassword(newUser.getPassword());
            model.addAttribute("user", user);
        });
        return "userCard";
    }

    @GetMapping("/removeUser/{userId}")
    public String updateUser(@PathVariable (name = "userId") int userId){
        Optional<User> first = UserService.userList.stream().filter(user -> user.getId() == userId).findFirst();
        first.ifPresent(user ->{
            UserService.userList.remove(user);
        });
        // используется команда redirect на страницу allUser, т.к. если просто вернуть страницу allUser (без redirect), то туда должна вставиться Model со списком всех user
        // но в данном методе Model не вставляется, поэтому без redirect получим пустую страницу, чтоб этого не было нужно использовать конструкцию model.addAttribute
        // в методе allUser происходит вставка Model, поэтому делается redirect, после которого Spring сделает запрос на открытие страницы и вставит туда allUser
        return "redirect:/allUser";
    }
}
