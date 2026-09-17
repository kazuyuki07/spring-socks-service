package su.yuk1chan.springsocks.facades;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import su.yuk1chan.springsocks.dto.SocksDTO;
import su.yuk1chan.springsocks.dto.SocksResponse;
import su.yuk1chan.springsocks.entities.Socks;
import su.yuk1chan.springsocks.entities.Warehouse;
import su.yuk1chan.springsocks.services.SocksService;
import su.yuk1chan.springsocks.services.WarehouseService;

import java.util.List;

@RequiredArgsConstructor
@Component
public class SocksFacade {
    private final SocksService socksService;
    private final WarehouseService warehouseService;

    public SocksDTO getSocksByWarehouseId(Long warehouseId) {
        Warehouse warehouse = warehouseService.getWarehouseById(warehouseId);
        Socks foundSock = socksService.getSocksBySocksId(warehouse.getSocksId());

        return new SocksDTO(
                foundSock.getColor(),
                foundSock.getCottonPart(),
                warehouse.getQuantity()
        );
    }

    public SocksDTO getSocksBySocksId(Long socksId) {
        Socks foundSock = socksService.getSocksBySocksId(socksId);

        return new SocksDTO(
                foundSock.getColor(),
                foundSock.getCottonPart(),
                warehouseService.getWarehouseBySocksId(socksId).getQuantity()
        );
    }

    public List<SocksResponse> showMeSocks(
            String color,
            String operation,
            Integer cottonPart) {
        return socksService.getSocks(color, operation, cottonPart);
    }


    public SocksDTO income(SocksDTO incomeDTO) {
        Socks socks = socksService.addSocks(incomeDTO.getColor(), incomeDTO.getCottonPart());
        Warehouse warehouse = warehouseService.addIntoWarehouse(socks.getId(), incomeDTO.getQuantity());

        return new SocksDTO(socks.getColor(), socks.getCottonPart(), warehouse.getQuantity());
    }


    public SocksDTO outcome(Long id, Integer quantity) {
        Warehouse warehouse = warehouseService.outcomeFromWarehouse(id, quantity);
        Socks socks = socksService.getSocksBySocksId(warehouse.getSocksId());

        return new SocksDTO(socks.getColor(), socks.getCottonPart(), warehouse.getQuantity());
    }


    public void deleteSocks(Long id) {
        socksService.deleteSocks(id);
    }
}
