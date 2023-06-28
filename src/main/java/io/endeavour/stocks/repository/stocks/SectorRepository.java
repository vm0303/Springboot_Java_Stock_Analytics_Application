package io.endeavour.stocks.repository.stocks;
import io.endeavour.stocks.entity.stocks.Sector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SectorRepository extends JpaRepository<Sector, Integer>
{

}
