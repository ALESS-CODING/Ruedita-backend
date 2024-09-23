package edu.cibertec.rueditas_frontend.controller;

import edu.cibertec.rueditas_frontend.dto.RueditaRequestDTO;
import edu.cibertec.rueditas_frontend.viewmodel.RueditaViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/vehiculo")
public class ReuditaController {

    private final String URL = "http://localhost:8080/search";
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("")
    public ModelAndView searchVehicle () {
        return new ModelAndView("search", "error", "");
    }

    @PostMapping("/search")
    public ModelAndView searchVehicle (@RequestParam("placa") String placa){
        ModelAndView model = new ModelAndView("vehiculo");
        if(!StringUtils.hasText(placa) || placa.length() != 8){
            model.setViewName("search");
            model.addObject("error", "La placa ingresado es invalida");
            return model;
        }

        try {
            RueditaRequestDTO request = new RueditaRequestDTO(placa);
            ResponseEntity<RueditaViewModel> response = restTemplate.postForEntity(URL,request, RueditaViewModel.class);
            model.setViewName("vehiculo");
            model.addObject("vehiculo", response.getBody());
            model.addObject("error", "");
        } catch (HttpClientErrorException ex){
            if(HttpStatus.NOT_FOUND.equals(ex.getStatusCode())){
                model.setViewName("search");
                model.addObject("error", "Vehiculo no existe con placa: " + placa);
            }
        } catch (ResourceAccessException rex){
           model.setViewName("search");
           model.addObject("error", "El servicio no este disponible este moemnto");
        }
        return model;
    }

}
