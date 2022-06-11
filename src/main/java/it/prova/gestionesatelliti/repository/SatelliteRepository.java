package it.prova.gestionesatelliti.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;

public interface SatelliteRepository extends CrudRepository<Satellite, Long>, JpaSpecificationExecutor<Satellite> {

//	o   Lanciati da più di due anni (fornisce una lista di satelliti lanciati da più di due anni che non sono disattivati);
	List<Satellite> findByDataLancioBeforeAndStatoNot(Date data, StatoSatellite statoDisattivato);
	
	List<Satellite> findByStatoAndDataRientroIsNull(StatoSatellite statoDisattivato);
	
}
