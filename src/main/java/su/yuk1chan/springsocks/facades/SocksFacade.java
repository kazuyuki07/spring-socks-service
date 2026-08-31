package su.yuk1chan.springsocks.facades;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import su.yuk1chan.springsocks.client.WarehouseHistoryClient;
import su.yuk1chan.springsocks.dto.SocksDTO;
import su.yuk1chan.springsocks.dto.SocksResponse;
import su.yuk1chan.springsocks.dto.WarehouseHistoryDTO;
import su.yuk1chan.springsocks.entities.Socks;
import su.yuk1chan.springsocks.entities.Warehouse;
import su.yuk1chan.springsocks.enums.Event;
import su.yuk1chan.springsocks.services.SocksService;
import su.yuk1chan.springsocks.services.WarehouseService;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class SocksFacade {
    private final SocksService socksService;
    private final WarehouseService warehouseService;
    private final WarehouseHistoryClient warehouseHistoryClient;


    public List<SocksResponse> showMeSocks(
            String color,
            String operation,
            Integer cottonPart) {
        return socksService.getSocks(color, operation, cottonPart);
    }


    public SocksDTO income(SocksDTO incomeDTO) {
        Socks socks = socksService.addSocks(incomeDTO.getColor(), incomeDTO.getCottonPart());
        Warehouse warehouse = warehouseService.addIntoWarehouse(socks.getId(), incomeDTO.getQuantity());
        LocalDateTime dateNow = LocalDateTime.now();

        warehouseHistoryClient.writeHistory(new WarehouseHistoryDTO(
                Event.INCOME,
                socks.getColor(),
                socks.getCottonPart(),
                incomeDTO.getQuantity(),
                warehouse.getQuantity(),
                warehouse.getId(),
                dateNow)
        );

        return new SocksDTO(socks.getColor(), socks.getCottonPart(), warehouse.getQuantity());
    }


    public SocksDTO outcome(Long id, Integer quantity) {
        Warehouse warehouse = warehouseService.outcomeFromWarehouse(id, quantity);
        Socks socks = socksService.getSocksBySocksId(warehouse.getSocksId());
        LocalDateTime dateNow = LocalDateTime.now();

        warehouseHistoryClient.writeHistory(new WarehouseHistoryDTO(
                   Event.OUTCOME,
                   socks.getColor(),
                   socks.getCottonPart(),
                   quantity,
                   warehouse.getQuantity(),
                   warehouse.getId(),
                   dateNow)
        );

        return new SocksDTO(socks.getColor(), socks.getCottonPart(), warehouse.getQuantity());
    }


    public void deleteSocks(Long id) {
        socksService.deleteSocks(id);
    }
}
