package su.yuk1chan.springsocks.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import su.yuk1chan.springsocks.dto.WarehouseHistoryDTO;

@FeignClient(name = "warehouseServiceClient", url = "${spring.client.warehouse.url}")
public interface WarehouseHistoryClient {
    @PostMapping
    WarehouseHistoryDTO writeHistory(@RequestBody WarehouseHistoryDTO warehouseHistoryDTO);
}
