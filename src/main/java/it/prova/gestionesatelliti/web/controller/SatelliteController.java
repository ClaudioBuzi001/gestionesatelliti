package it.prova.gestionesatelliti.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;
import it.prova.gestionesatelliti.service.SatelliteService;

@Controller
@RequestMapping(value = "/satellite")
public class SatelliteController {
	
	@Autowired
	private SatelliteService satelliteService;
	
	@GetMapping("/listAll")
	public ModelAndView listAll() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllElements();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}
	
	@GetMapping("/insert")
	public String create(Model model) {
		model.addAttribute("insert_satellite_attr", new Satellite());
		return "satellite/insert";
	}
	
	
	//Fa il bining automatico solo delle prime due proprieta poi dobbiamo fare i controlli custom per le verie date
	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("insert_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs) {

		if (result.hasErrors())
			return "satellite/insert";
		
		//Se la data di atterraggio c è e 
//		data lancio > data rientro
//		usare rejectValue
//
//		2)Se io imposto in movimento o fisso, la data rientro deve essere a null
//		
//		3)Se imposto disattivato la data di rientro != da null
		
		if(satellite.getDataLancio() != null && satellite.getDataRientro() != null && satellite.getDataLancio().after(satellite.getDataRientro())) {
			result.rejectValue("dataLancio", "", "Errore, data lancio dopo la data di rientro,Per favore inserisci due date corrette");
			return "satellite/insert";
		}
		
		if(satellite.getStato() != null && satellite.getStato().equals(StatoSatellite.IN_MOVIMENTO) || satellite.getStato().equals(StatoSatellite.FISSO) && satellite.getDataRientro() != null) {
			result.rejectValue("dataRientro", "", "Errore, La data rientro è stata inserita anche se il satellite è in movimento o fisso, Per favore non impostare la data di rientro se il satellite è ancora in orbita");
			return "satellite/insert";
		}

		if(satellite.getStato() != null && satellite.getStato().equals(StatoSatellite.DISATTIVATO) && satellite.getDataRientro() == null) {
			result.rejectValue("dataRientro", "", "Errore, la data Rientro non è stata impostata anche se il satellite è stato disattivato, Per favore inserisci la data di rientro dell satellite");
			return "satellite/insert";
		}
			
		
		satelliteService.inserisciNuovo(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite/listAll";
	}

	
}











