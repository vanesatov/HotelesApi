package org.example.hotelesapi;


import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    SecurityService securityService;

    /**
     * Maneja la solicitud GET para la página de inicio de sesión.
     *
     * @param model el modelo para la vista
     * @return el nombre de la vista de inicio de sesión
     */
    @GetMapping
    public String login(Model model) {
        return "login";
    }

    /**
     * Procesa la solicitud POST para el inicio de sesión.
     *
     * @param session la sesión HTTP
     * @param model el modelo para la vista
     * @param login el objeto de usuario con los datos de inicio de sesión
     * @return redirige a la página principal si el inicio de sesión es exitoso, de lo contrario, vuelve a la página de inicio de sesión
     */
    @PostMapping
    public String processLogin(HttpSession session, Model model, @ModelAttribute User login) {
        var result = securityService.login(login.getUser(), login.getEmail());
        if(result.isPresent()){
            session.setAttribute("user", result.get());
            return "redirect:/web/";
        } else return "login";
    }

    /**
     * Maneja la solicitud GET para cerrar sesión.
     *
     * @param session la sesión HTTP
     * @param model el modelo para la vista
     * @return redirige a la página principal después de cerrar sesión
     */
    @GetMapping("/exit")
    public String exit(HttpSession session, Model model) {
        session.removeAttribute("user");
        return "redirect:/web/";
    }

}

