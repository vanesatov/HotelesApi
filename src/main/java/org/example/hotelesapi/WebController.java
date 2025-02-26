package org.example.hotelesapi;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/web")

public class WebController {

    @Autowired
    HotelRepository hotelRepository;

    /**
     * Muestra la página principal con el listado de hoteles.
     *
     * @param session la sesión HTTP actual
     * @param model el modelo para pasar datos a la vista
     * @return el nombre de la vista a renderizar
     */
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        var hoteles = hotelRepository.findAll();
        model.addAttribute("titulo", "Listado de hoteles");
        model.addAttribute("hoteles", hoteles);
        if(session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            model.addAttribute("usuario", user);
        }
        return "index";
    }

    /**
     * Muestra la página de un hotel específico.
     *
     * @param model el modelo para pasar datos a la vista
     * @param id el ID del hotel
     * @return el nombre de la vista a renderizar
     */
    @GetMapping("/{id}")
    public String single(Model model, @PathVariable String id) {
        var hotel = hotelRepository.findById(id);
        if(hotel.isEmpty()) return "404";

        else {
            model.addAttribute("hotel", hotel.get());
            return "single";
        }
    }

    /**
     * Muestra la página para crear un nuevo hotel.
     *
     * @param session la sesión HTTP actual
     * @return el nombre de la vista a renderizar o redirige a la página de login
     */
    @GetMapping("/new")
    public String createNew(HttpSession session) {
        if(session.getAttribute("user") != null) {
            return "new";
        }else{
            return "redirect:/login";
        }
    }

    /**
     * Procesa la creación de un nuevo hotel.
     *
     * @param session la sesión HTTP actual
     * @param hotel el objeto Hotel a crear
     * @return redirige a la página principal o a la página de login
     */
    @PostMapping("/new")
    public String create(HttpSession session, @ModelAttribute Hotel hotel) {
        if(session.getAttribute("user") != null) {
            hotelRepository.save(hotel);
            return "redirect:/web/";
        }else{
            return "redirect:/login/";
        }
    }

    /**
     * Muestra los hoteles filtrados por provincia, modalidad y ordenados por estrellas.
     *
     * @param provinces la provincia de los hoteles
     * @param modalities la modalidad de los hoteles
     * @param model el modelo para pasar datos a la vista
     * @return el nombre de la vista a renderizar
     */
    @GetMapping("/hoteles/provincia/{provinces}/modalidad/{modalities}/estrellas")
    public String getHotelesPorProvinciaModalidadYEstrellas(
            @PathVariable String provinces,
            @PathVariable String modalities,
            Model model) {

        List<Hotel> hoteles = hotelRepository.findHotelesByProvinces(provinces);

        List<Hotel> hotelesFiltrados = hoteles.stream()
                .filter(h -> h.getModalities() != null && h.getModalities().contains(modalities))
                .sorted((h1, h2) -> {
                    boolean h1EsLujo = esGranLujo(h1.getCategories());
                    boolean h2EsLujo = esGranLujo(h2.getCategories());

                    if (h1EsLujo && !h2EsLujo) return -1;
                    if (!h1EsLujo && h2EsLujo) return 1;

                    int estrellas1 = extraerNumero(h1.getCategories());
                    int estrellas2 = extraerNumero(h2.getCategories());
                    return Integer.compare(estrellas2, estrellas1);
                })
                .toList();

        model.addAttribute("titulo", "Hoteles en " + provinces + " - " + modalities + " - Ordenados por Estrellas");
        model.addAttribute("hoteles", hotelesFiltrados);
        model.addAttribute("provincia", provinces);
        model.addAttribute("modalidad", modalities);

        return "hoteles-filtrados";
    }

    /**
     * Verifica si una categoría es de gran lujo.
     *
     * @param category la categoría del hotel
     * @return true si es de gran lujo, false en caso contrario
     */
    private boolean esGranLujo(String category) {
        if (category == null) return false;
        return category.toLowerCase().contains("lujo");
    }

    /**
     * Extrae el número de estrellas de una categoría.
     *
     * @param category la categoría del hotel
     * @return el número de estrellas o -1 si no se puede extraer
     */
    private int extraerNumero(String category) {
        if (category == null || category.isEmpty()) {
            return 0;
        }
        String[] partes = category.split(" ");
        try {
            return Integer.parseInt(partes[0]);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Muestra la lista de provincias con hoteles.
     *
     * @param model el modelo para pasar datos a la vista
     * @return el nombre de la vista a renderizar
     */
    @GetMapping("/hoteles/provincias")
    public String listarProvincias(Model model) {
        List<String> provincias = hotelRepository.findAll()
                .stream()
                .map(Hotel::getProvinces)
                .distinct()
                .toList();

        model.addAttribute("provincias", provincias);
        model.addAttribute("titulo", "Selecciona una Provincia");
        return "provincias";
    }

    /**
     * Muestra la lista de modalidades de hoteles en una provincia específica.
     *
     * @param provinces la provincia de los hoteles
     * @param model el modelo para pasar datos a la vista
     * @return el nombre de la vista a renderizar
     */
    @GetMapping("/hoteles/provincia/{provinces}/modalidades")
    public String listarModalidades(@PathVariable String provinces, Model model) {
        List<String> modalidades = hotelRepository.findHotelesByProvinces(provinces)
                .stream()
                .map(Hotel::getModalities)
                .filter(modalidad -> modalidad != null && !modalidad.trim().isEmpty())
                .distinct()
                .toList();

        model.addAttribute("modalidades", modalidades);
        model.addAttribute("provincia", provinces);
        model.addAttribute("titulo", "Selecciona una Modalidad en " + provinces);
        return "modalidades";
    }

    /**
     * Muestra la lista de hoteles de gran lujo.
     *
     * @param model el modelo para pasar datos a la vista
     * @return el nombre de la vista a renderizar
     */
    @GetMapping("/hoteles/lujo")
    public String getHotelesDeLujo(Model model) {
        List<Hotel> hoteles = hotelRepository.findAll()
                .stream()
                .filter(h -> esGranLujo(h.getCategories()))
                .toList();

        model.addAttribute("titulo", "Hoteles Gran Lujo");
        model.addAttribute("hoteles", hoteles);

        return "hoteles-lujo";
    }
}
