package su.yuk1chan.springsocks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import su.yuk1chan.springsocks.entities.Warehouse;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findBySocksId(Long socksId);
}
