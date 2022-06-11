package it.prova.gestionesatelliti.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	// Fa il bining automatico solo delle prime due proprieta poi dobbiamo fare i
	// controlli custom per le verie date
	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("insert_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs) {

		if (result.hasErrors())
			return "satellite/insert";

		// Controlli
		if (satellite.getDataLancio() != null && satellite.getDataRientro() != null
				&& satellite.getDataLancio().after(satellite.getDataRientro())) {
			result.rejectValue("dataLancio", "",
					"Errore, data lancio dopo la data di rientro,Per favore inserisci due date corrette");
			return "satellite/insert";
		}

		if (satellite.getDataLancio() != null && satellite.getStato() == null) {
			result.rejectValue("stato", "",
					"Errore, data lancio inserita ma è lo stato del satellite non è stato inserito,Per favore inserire lo stato del satellite");
			return "satellite/insert";
		}

		if (satellite.getStato() != null && (satellite.getStato().equals(StatoSatellite.IN_MOVIMENTO)
				|| satellite.getStato().equals(StatoSatellite.FISSO)) && satellite.getDataRientro() != null) {
			result.rejectValue("dataRientro", "",
					"Errore, La data rientro è stata inserita anche se il satellite è in movimento o fisso, Per favore non impostare la data di rientro se il satellite è ancora in orbita");
			return "satellite/insert";
		}

		if (satellite.getStato() != null && satellite.getStato().equals(StatoSatellite.DISATTIVATO)
				&& satellite.getDataRientro() == null) {
			result.rejectValue("dataRientro", "",
					"Errore, la data Rientro non è stata impostata anche se il satellite è stato disattivato, Per favore inserisci la data di rientro dell satellite");
			return "satellite/insert";
		}

		if (satellite.getDataLancio() == null && satellite.getDataRientro() != null) {
			result.rejectValue("dataLancio", "",
					"Errore, data lancio Non inserita ma è stata inserita la data di rientro,Per favore inserisci due date corrette");
			return "satellite/insert";
		}

		satelliteService.inserisciNuovo(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite/listAll";
	}

	@GetMapping("/show/{idSatellite}")
	public String show(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("show_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/show";
	}

	@GetMapping("/delete/{idSatellite}")
	public String delete(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("delete_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/delete";
	}

	@PostMapping("/remove")
	public String remove(@RequestParam(required = true) Long idSatellite, RedirectAttributes redirectAttrs) {

		satelliteService.rimuoviById(idSatellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite/listAll";
	}

	@GetMapping("/edit/{idSatellite}")
	public String edit(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("edit_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));

		return "satellite/edit";
	}

	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("edit_satellite_attr") Satellite satellite, BindingResult result,
			RedirectAttributes redirectAttrs) {

		if (result.hasErrors())
			return "satellite/edit";

		// Controlli, Tutti quelli dell insert con anche il controllo sullo stato
		if (satellite.getDataLancio() != null && satellite.getDataRientro() != null
				&& satellite.getDataLancio().after(satellite.getDataRientro())) {
			result.rejectValue("dataLancio", "",
					"Errore, data lancio dopo la data di rientro,Per favore inserisci due date corrette");
			return "satellite/edit";
		}

		if (satellite.getDataLancio() != null && satellite.getStato() == null) {
			result.rejectValue("stato", "",
					"Errore, data lancio inserita ma è lo stato del satellite non è stato inserito,Per favore inserire lo stato del satellite");
			return "satellite/edit";
		}

		if (satellite.getStato() != null && (satellite.getStato().equals(StatoSatellite.IN_MOVIMENTO)
				|| satellite.getStato().equals(StatoSatellite.FISSO)) && satellite.getDataRientro() != null) {
			result.rejectValue("dataRientro", "",
					"Errore, La data rientro è stata inserita anche se il satellite è in movimento o fisso, Per favore non impostare la data di rientro se il satellite è ancora in orbita");
			return "satellite/edit";
		}

		if (satellite.getStato() != null && satellite.getStato().equals(StatoSatellite.DISATTIVATO)
				&& satellite.getDataRientro() == null) {
			result.rejectValue("dataRientro", "",
					"Errore, la data Rientro non è stata impostata anche se il satellite è stato disattivato, Per favore inserisci la data di rientro dell satellite");
			return "satellite/edit";
		}

		if (satellite.getDataLancio() == null && satellite.getDataRientro() != null) {
			result.rejectValue("dataLancio", "",
					"Errore, data lancio Non inserita ma è stata inserita la data di rientro,Per favore inserisci due date corrette");
			return "satellite/edit";
		}

		if (satellite.getStato() == null) {
			result.rejectValue("stato", "",
					"Errore, stato non identificato, per favore inserisci lo Stato del Satellite");
			return "satellite/edit";
		}

		satelliteService.aggiorna(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite/listAll";
	}
	
	@GetMapping("/search")
	public String search() {
		return "satellite/search";
	}

	@PostMapping("/list")
	public String listByExample(Satellite example, ModelMap model) {
		List<Satellite> results = satelliteService.findByExample(example);
		model.addAttribute("satellite_list_attribute", results);
		
		return "satellite/list";
	}
}


























