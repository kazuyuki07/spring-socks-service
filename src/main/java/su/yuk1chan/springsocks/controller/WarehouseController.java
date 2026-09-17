package su.yuk1chan.springsocks.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.yuk1chan.springsocks.dto.SocksDTO;
import su.yuk1chan.springsocks.facades.SocksFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/warehouse")
public class WarehouseController {
    private final SocksFacade socksFacade;

    @GetMapping("/{warehouseId}")
    public SocksDTO getSocksByWarehouseId(@PathVariable Long warehouseId) {
        return socksFacade.getSocksByWarehouseId(warehouseId);
    }
}
