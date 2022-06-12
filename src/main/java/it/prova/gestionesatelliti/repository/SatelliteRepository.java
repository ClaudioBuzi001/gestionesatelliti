package it.prova.gestionesatelliti.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.prova.gestionesatelliti.model.Satellite;
import it.prova.gestionesatelliti.model.StatoSatellite;

public interface SatelliteRepository extends CrudRepository<Satellite, Long>, JpaSpecificationExecutor<Satellite> {

	List<Satellite> findByDataLancioBeforeAndStatoNot(Date data, StatoSatellite statoDisattivato);

	List<Satellite> findByStatoAndDataRientroIsNull(StatoSatellite statoDisattivato);

	List<Satellite> findByDataLancioBeforeAndStato(Date data, StatoSatellite statoFisso);

	@Modifying
	@Query("update Satellite s set s.dataLancio = :dataLancio, s.stato = :stato where s.id = :idSatellite")
	int setDataLancioAdOggi(@Param("idSatellite") Long idSatellite, @Param("dataLancio") Date dataLancio,
			@Param("stato") StatoSatellite stato);

	@Modifying
	@Query("update Satellite s set s.dataRientro = :dataRientro, s.stato = :stato where s.id = :idSatellite")
	int settaDataRientroAdOggi(@Param("idSatellite") Long idSatellite, @Param("dataRientro") Date dataRientro,
			@Param("stato") StatoSatellite stato);

}
