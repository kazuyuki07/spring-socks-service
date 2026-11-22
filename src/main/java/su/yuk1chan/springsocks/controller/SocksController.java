package su.yuk1chan.springsocks.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import su.yuk1chan.springsocks.dto.PagedResponse;
import su.yuk1chan.springsocks.dto.SocksDTO;
import su.yuk1chan.springsocks.mapper.SocksMapper;
import su.yuk1chan.springsocks.services.SocksService;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/socks")
public class SocksController {
    private final SocksService socksService;
    private final SocksMapper socksMapper;

    @GetMapping
    public PagedResponse<SocksDTO> getSocks(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String sort) {
        return socksService.getSocks(page, size, color, sort);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SocksDTO addSocks(@RequestBody SocksDTO socksDTO) {
        return socksMapper.socksToSocksDTO(socksService.addSocks(socksDTO));
    }

    @DeleteMapping("/{socksId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSocks(@PathVariable Long socksId) {
        socksService.deleteSocks(socksId);
    }
}
