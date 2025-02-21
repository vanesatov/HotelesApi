package org.example.hotelesapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar las operaciones relacionadas con los hoteles.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityService securityService;

    /**
     * Obtiene todos los hoteles.
     *
     * @return Lista de todos los hoteles.
     */
    @GetMapping("/hoteles")
    public List<Hotel> all() {
        return hotelRepository.findAll();
    }

    /**
     * Busca un hotel por su ID.
     *
     * @param id ID del hotel.
     * @return ResponseEntity con el hotel encontrado o NOT_FOUND si no existe.
     */
    @GetMapping("/hoteles/id/{id}")
    public ResponseEntity<Hotel> findById(@PathVariable String id) {
        if (hotelRepository.existsById(id)) {
            var hotel = hotelRepository.findById(id).get();
            return (new ResponseEntity<Hotel>(hotel, HttpStatus.OK));
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Busca hoteles por provincia.
     *
     * @param provinces Provincia de los hoteles.
     * @return Lista de hoteles en la provincia especificada.
     */
    @GetMapping ("/hoteles/provincia/{provinces}")
    public List<Hotel> findByProvinces(@PathVariable String provinces) {
        return hotelRepository.findHotelesByProvinces(provinces);
    }

    /**
     * Obtiene hoteles ordenados por estrellas.
     *
     * @return Lista de hoteles ordenados por estrellas.
     */
    @GetMapping("/hoteles/estrellas")
    public List<Hotel> getHotelesOrdenadosPorEstrellas() {
        List<Hotel> hoteles = hotelRepository.findAll();
        return hoteles.stream()
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
    }

    /**
     * Verifica si una categoría es de gran lujo.
     *
     * @param category Categoría del hotel.
     * @return true si es de gran lujo, false en caso contrario.
     */
    private boolean esGranLujo(String category) {
        if (category == null) return false;
        return category.toLowerCase().contains("lujo");
    }

    /**
     * Extrae el número de estrellas de una categoría.
     *
     * @param category Categoría del hotel.
     * @return Número de estrellas.
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
     * Obtiene hoteles por número de estrellas.
     *
     * @param estrellas Número de estrellas.
     * @return Lista de hoteles con el número de estrellas especificado.
     */
    @GetMapping("/hoteles/estrellas/{estrellas}")
    public List<Hotel> getHotelesPorEstrellas(@PathVariable int estrellas) {
        List<Hotel> hoteles = hotelRepository.findAll();
        return hoteles.stream()
                .filter(h -> extraerNumero(h.getCategories()) == estrellas)
                .toList();
    }

    /**
     * Obtiene hoteles por provincia y número de estrellas.
     *
     * @param provinces Provincia de los hoteles.
     * @param estrellas Número de estrellas.
     * @return Lista de hoteles en la provincia especificada con el número de estrellas especificado.
     */
    @GetMapping("/hoteles/provincia/{provinces}/estrellas/{estrellas}")
    public List<Hotel> getHotelesPorProvinciaYEstrellas(@PathVariable String provinces, @PathVariable int estrellas) {
        List<Hotel> hoteles = hotelRepository.findHotelesByProvinces(provinces);
        return hoteles.stream()
                .filter(h -> extraerNumero(h.getCategories()) == estrellas)
                .toList();
    }

    /**
     * Obtiene hoteles por provincia ordenados por estrellas.
     *
     * @param provincies Provincia de los hoteles.
     * @return Lista de hoteles en la provincia especificada ordenados por estrellas.
     */
    @GetMapping("hoteles/provincia/{provincies}/estrellas")
    public List<Hotel> getHotelesPorProvinciaOrdenadosPorEstrellas(@PathVariable String provincies) {
        List<Hotel> hoteles = hotelRepository.findHotelesByProvinces(provincies);
        return hoteles.stream()
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
    }

    /**
     * Obtiene hoteles de lujo.
     *
     * @return Lista de hoteles de lujo.
     */
    @GetMapping("/hoteles/lujo")
    public List<Hotel> getHotelesDeLujo() {
        List<Hotel> hoteles = hotelRepository.findAll();
        return hoteles.stream()
                .filter(h -> esGranLujo(h.getCategories()))
                .toList();
    }

    /**
     * Obtiene hoteles de lujo por provincia.
     *
     * @param provinces Provincia de los hoteles.
     * @return Lista de hoteles de lujo en la provincia especificada.
     */
    @GetMapping("/hoteles/provincia/{provinces}/lujo")
    public List<Hotel> getHotelesDeLujoPorProvincia(@PathVariable String provinces) {
        List<Hotel> hoteles = hotelRepository.findHotelesByProvinces(provinces);
        return hoteles.stream()
                .filter(h -> esGranLujo(h.getCategories()))
                .toList();
    }

    /**
     * Obtiene hoteles por modalidad.
     *
     * @param modalities Modalidad de los hoteles.
     * @return Lista de hoteles con la modalidad especificada.
     */
    @GetMapping("/hoteles/modalidad/{modalities}")
    public List<Hotel> getHotelesPorModalidad(@PathVariable String modalities) {
        List<Hotel> hoteles = hotelRepository.findHotelesByModalities(modalities);
        return hoteles.stream()
                .filter(h -> h.getModalities().contains(modalities))
                .toList();
    }

    /**
     * Obtiene hoteles por modalidad ordenados por estrellas.
     *
     * @param modalities Modalidad de los hoteles.
     * @return Lista de hoteles con la modalidad especificada ordenados por estrellas.
     */
    @GetMapping("/hoteles/modalidad/{modalities}/estrellas")
    public List<Hotel> getHotelesPorModalidadOrdenadosPorEstrellas(@PathVariable String modalities) {
        List<Hotel> hoteles = hotelRepository.findHotelesByModalities(modalities);
        return hoteles.stream()
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
    }

    /**
     * Obtiene hoteles por modalidad y número de estrellas.
     *
     * @param modalities Modalidad de los hoteles.
     * @param estrellas Número de estrellas.
     * @return Lista de hoteles con la modalidad y número de estrellas especificados.
     */
    @GetMapping("/hoteles/modalidad/{modalities}/estrellas/{estrellas}")
    public List<Hotel> getHotelesPorModalidadYEstrellas(@PathVariable String modalities, @PathVariable int estrellas) {
        List<Hotel> hoteles = hotelRepository.findHotelesByModalities(modalities);
        return hoteles.stream()
                .filter(h -> h.getModalities() != null && h.getModalities().contains(modalities))
                .filter(h -> extraerNumero(h.getCategories()) == estrellas)
                .toList();
    }

    /**
     * Obtiene hoteles por provincia y modalidad.
     *
     * @param provinces Provincia de los hoteles.
     * @param modalities Modalidad de los hoteles.
     * @return Lista de hoteles en la provincia especificada con la modalidad especificada.
     */
    @GetMapping("/hoteles/provincia/{provinces}/modalidad/{modalities}")
    public List<Hotel> getHotelesPorProvinciaYModalidad(@PathVariable String provinces, @PathVariable String modalities) {
        List<Hotel> hoteles = hotelRepository.findHotelesByProvinces(provinces);
        return hoteles.stream()
                .filter(h -> h.getModalities().contains(modalities))
                .toList();
    }

    /**
     * Obtiene hoteles por provincia, modalidad y número de estrellas.
     *
     * @param provinces Provincia de los hoteles.
     * @param modalities Modalidad de los hoteles.
     * @param estrellas Número de estrellas.
     * @return Lista de hoteles en la provincia especificada con la modalidad y número de estrellas especificados.
     */
    @GetMapping("/hoteles/provincia/{provinces}/modalidad/{modalities}/estrellas/{estrellas}")
    public List<Hotel> getHotelesPorProvinciaModalidadYEstrellas(@PathVariable String provinces, @PathVariable String modalities, @PathVariable int estrellas) {
        List<Hotel> hoteles = hotelRepository.findHotelesByProvinces(provinces);
        return hoteles.stream()
                .filter(h -> h.getModalities() != null && h.getModalities().contains(modalities))
                .filter(h -> extraerNumero(h.getCategories()) == estrellas)
                .toList();
    }

    /**
     * Obtiene hoteles por provincia, modalidad y ordenados por estrellas.
     *
     * @param provinces Provincia de los hoteles.
     * @param modalities Modalidad de los hoteles.
     * @return Lista de hoteles en la provincia especificada con la modalidad especificada ordenados por estrellas.
     */
    @GetMapping("/hoteles/provincia/{provinces}/modalidad/{modalities}/estrellas")
    public List<Hotel> getHotelesPorProvinciaModalidadYEstrellas(@PathVariable String provinces, @PathVariable String modalities) {
        List<Hotel> hoteles = hotelRepository.findHotelesByProvinces(provinces);
        return hoteles.stream()
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
    }

    /**
     * Obtiene hoteles por provincia, modalidad y de lujo.
     *
     * @param provinces Provincia de los hoteles.
     * @param modalities Modalidad de los hoteles.
     * @return Lista de hoteles en la provincia especificada con la modalidad especificada y de lujo.
     */
    @GetMapping("/hoteles/provincia/{provinces}/modalidad/{modalities}/lujo")
    public List<Hotel> getHotelesPorProvinciaModalidadYLujo(@PathVariable String provinces, @PathVariable String modalities) {
        List<Hotel> hoteles = hotelRepository.findHotelesByProvinces(provinces);
        return hoteles.stream()
                .filter(h -> h.getModalities() != null && h.getModalities().contains(modalities))
                .filter(h -> esGranLujo(h.getCategories()))
                .toList();
    }

    /**
     * Elimina un hotel por su ID.
     *
     * @param id ID del hotel.
     * @param token Token de seguridad.
     * @return ResponseEntity con el estado de la operación.
     */
    @DeleteMapping("/hoteles/{id}")
    public ResponseEntity delete(@PathVariable String id, @RequestParam String token) {
        if (securityService.requestValidation(token)) {
            hotelRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * Crea un nuevo hotel.
     *
     * @param hotel Objeto Hotel a crear.
     * @return ResponseEntity con el hotel creado.
     */
    @PostMapping("/")
    public ResponseEntity<Hotel> create(@RequestBody Hotel hotel) {
            hotelRepository.save(hotel);
            return new ResponseEntity<>(hotel, HttpStatus.CREATED);
        }
}
