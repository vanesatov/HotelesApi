package org.example.hotelesapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class WebController {

    @Autowired
    HotelRepository hotelRepository;

    @GetMapping("/")
    public String index(Model model) {
        var hoteles = hotelRepository.findAll();
        model.addAttribute("titulo", "Listado de hoteles");
        model.addAttribute("hoteles", hoteles);
        return "index";
    }

}
