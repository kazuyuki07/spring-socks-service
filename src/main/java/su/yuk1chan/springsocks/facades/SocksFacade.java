package su.yuk1chan.springsocks.facades;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import su.yuk1chan.springsocks.dto.SocksDTO;
import su.yuk1chan.springsocks.dto.SocksResponse;
import su.yuk1chan.springsocks.dto.SocksResponseWithDate;
import su.yuk1chan.springsocks.entities.Socks;
import su.yuk1chan.springsocks.entities.Warehouse;
import su.yuk1chan.springsocks.services.SocksService;
import su.yuk1chan.springsocks.services.WarehouseService;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SocksFacade {
    private final SocksService socksService;
    private final WarehouseService warehouseService;

    public List<SocksResponse> showMeSocks(
            String color,
            String operation,
            Integer cottonPart) {
        return socksService.getSocks(color, operation, cottonPart);
    }


    public SocksResponseWithDate income(SocksDTO incomeDTO) {
        Socks socks = socksService.addSocks(incomeDTO.getColor(), incomeDTO.getCottonPart());
        Warehouse warehouse = warehouseService.addIntoWarehouse(socks.getId(), incomeDTO.getQuantity());
        LocalDateTime dateNow = LocalDateTime.now();

        return new SocksResponseWithDate(
                socks.getId(),
                socks.getColor(),
                socks.getCottonPart(),
                warehouse.getQuantity(),
                warehouse.getId(),
                dateNow
        );
    }


    public SocksResponseWithDate outcome(Long id, Integer quantity) {
        Warehouse warehouse = warehouseService.outcomeFromWarehouse(id, quantity);
        LocalDateTime dateNow = LocalDateTime.now();
        Socks socks = socksService.getSocksBySocksId(warehouse.getSocksId());

        return new SocksResponseWithDate(
                id,
                socks.getColor(),
                socks.getCottonPart(),
                warehouse.getQuantity(),
                warehouse.getId(),
                dateNow
        );
    }


    public void deleteSocks(Long id) {
        socksService.deleteSocks(id);
    }
}
