package su.yuk1chan.springsocks.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import su.yuk1chan.springsocks.dto.SocksResponse;
import su.yuk1chan.springsocks.entities.Socks;
import java.util.List;
import java.util.Optional;

public interface SocksRepository extends JpaRepository<Socks, Long> {
    @Query(value = """
        SELECT new su.yuk1chan.springsocks.dto.SocksResponse(s.id, s.color, s.cottonPart, w.quantity)
        FROM Socks s
        JOIN Warehouse w ON s.id = w.socksId
        WHERE (:color IS NULL OR :color = '' OR s.color = :color) AND :cottonPart < s.cottonPart
    """)
    List<SocksResponse> getSocksMoreThan(@Param("color") String color, @Param("cottonPart") Integer cottonPart);


    @Query(value = """
        SELECT new su.yuk1chan.springsocks.dto.SocksResponse(s.id, s.color, s.cottonPart, w.quantity)
        FROM Socks s
        JOIN Warehouse w ON s.id = w.socksId
        WHERE (:color IS NULL OR :color = '' OR s.color = :color) AND :cottonPart > s.cottonPart
    """)
    List<SocksResponse> getSocksLessThan(@Param("color") String color, @Param("cottonPart") Integer cottonPart);


    @Query(value = """
        SELECT new su.yuk1chan.springsocks.dto.SocksResponse(s.id, s.color, s.cottonPart, w.quantity)
        FROM Socks s
        JOIN Warehouse w ON s.id = w.socksId
        WHERE (:color IS NULL OR :color = '' OR s.color = :color) AND :cottonPart = s.cottonPart
    """)
    List<SocksResponse> getSocksEqual(@Param("color") String color, @Param("cottonPart") Integer cottonPart);


    Optional<Socks> findByColorAndCottonPart(String color, Integer cottonPart);
}
