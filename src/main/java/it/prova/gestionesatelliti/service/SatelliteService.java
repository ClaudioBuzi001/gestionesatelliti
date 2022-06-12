package it.prova.gestionesatelliti.service;

import java.util.Date;
import java.util.List;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;

public interface SatelliteService {
	public List<Satellite> listAllElements();

	public Satellite caricaSingoloElemento(Long id);

	public void aggiorna(Satellite satelliteInstance);

	public void inserisciNuovo(Satellite satelliteInstance);

	public void rimuovi(Satellite satelliteInstance);

	public List<Satellite> findByExample(Satellite example);

	public void rimuoviById(Long idSatellite);

	public void settaDataLancioAdOggi(Long idSatellite);

	public void settaDataRientroAdOggi(Long idSatellite);

	public List<Satellite> trovaLanciatiDaPiuDiDueAnniENonDisattivati(Date data, StatoSatellite statoDisattivato);

	public List<Satellite> trovaDisattivatiMaMaiRientrati(StatoSatellite statoDisattivato);

	public List<Satellite> trovaInOrbitaDaDieciAnniEFissi(Date data, StatoSatellite statoFisso);

}
