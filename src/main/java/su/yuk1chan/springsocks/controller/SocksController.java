package su.yuk1chan.springsocks.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import su.yuk1chan.springsocks.dto.SocksDTO;
import su.yuk1chan.springsocks.dto.SocksResponse;
import su.yuk1chan.springsocks.facades.SocksFacade;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/socks")
public class SocksController {
    private final SocksFacade socksFacade;

    @GetMapping()
    public List<SocksResponse> showMeSocks(
            @RequestParam(required = false) String color,
            @RequestParam String operation,
            @RequestParam("cotton_part") Integer cottonPart) {
        return socksFacade.showMeSocks(color, operation, cottonPart);
    }


    @PostMapping("/income")
    @ResponseStatus(HttpStatus.CREATED)
    public SocksDTO income(@RequestBody SocksDTO incomeDTO) {
        return socksFacade.income(incomeDTO);
    }


    @DeleteMapping("/outcome/{warehouseId}")
    public SocksDTO outcome(@PathVariable Long warehouseId, @RequestParam Integer quantity){
        return socksFacade.outcome(warehouseId, quantity);
    }


    @DeleteMapping("/delete_socks/{socksId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSocks(@PathVariable Long socksId) {
        socksFacade.deleteSocks(socksId);
    }
}
