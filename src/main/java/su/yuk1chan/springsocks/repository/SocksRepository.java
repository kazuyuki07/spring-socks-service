package su.yuk1chan.springsocks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import su.yuk1chan.springsocks.entity.Socks;

import java.util.Optional;

public interface SocksRepository extends JpaRepository<Socks, Long>, JpaSpecificationExecutor<Socks> {
}
