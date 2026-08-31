package su.yuk1chan.springsocks.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import su.yuk1chan.springsocks.dto.SocksResponse;
import su.yuk1chan.springsocks.entities.Socks;
import su.yuk1chan.springsocks.entities.Warehouse;
import su.yuk1chan.springsocks.exceptions.NotFoundException;
import su.yuk1chan.springsocks.exceptions.ValidationException;
import su.yuk1chan.springsocks.repositories.SocksRepository;
import su.yuk1chan.springsocks.repositories.WarehouseRepository;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
public class SocksServiceTest{
    @Autowired
    private SocksService socksService;
    @Autowired
    private SocksRepository socksRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;


    @AfterEach
    void cleanData() {
        socksRepository.deleteAll();
        warehouseRepository.deleteAll();
    }


    @Test
    void addSocksSuccess() {
        // Успешно добавление носков
        String color = "red";
        Integer cottonPart = 91;


        Socks result = socksService.addSocks(color, cottonPart);


        assertThat(result.getColor()).isEqualTo(color);
        assertThat(result.getCottonPart()).isEqualTo(cottonPart);
    }

    @Test
    void addSocksSuccessIfIsPresent() {
        // Добавление носков, если существуют

        String color = "red";
        Integer cottonPart = 20;
        Socks socks = Socks.builder()
                .color(color)
                .cottonPart(cottonPart)
                .build();
        socksRepository.save(socks);

        Socks result = socksService.addSocks(color, cottonPart);

        assertThat(result).isEqualTo(socks);
    }

    @Test
    void addSocksColorFail() {
        // Неудачное добавление носков в базу данных, в случае не соответствий по цвету
        String color = "C3q211";
        Integer cottonPart = 32;


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            socksService.addSocks(color, cottonPart)
        );


        assertEquals("Неверный параметр: " + color, exception.getMessage());
    }


    @Test
    void addSocksCottonPartAboveZeroFail() {
        /* Неудачное добавление носков в базу данных,
          в случае не соответствий с процентом состава хлопка:
          Больше допустимого интервала */
        String color = "red";
        Integer cottonPart = 102;


        ValidationException exception = assertThrows(ValidationException.class, () ->
            socksService.addSocks(color, cottonPart)
        );


        assertEquals("Неверный параметр: " + cottonPart, exception.getMessage());
    }


    @Test
    void addSocksCottonPartBelowZeroFail() {
        /* Неудачное добавление носков в базу данных,
          в случае не соответствий с процентом состава хлопка:
          Меньше допустимого интервала */
        String color = "red";
        Integer cottonPart = -10;


        ValidationException exception = assertThrows(ValidationException.class, () ->
            socksService.addSocks(color, cottonPart)
        );


        assertEquals("Неверный параметр: " + cottonPart, exception.getMessage());
    }


    @Test
    void deleteSocksSuccess() {
        // Успешное удаление носков
        String color = "red";
        Integer cottonPart = 20;


        Socks sock = socksRepository.save(
            Socks.builder()
                .color(color)
                .cottonPart(cottonPart)
                .build()
        );


        socksService.deleteSocks(sock.getId());


        assertThat(socksRepository.existsById(sock.getId())).isFalse();
    }


    @Test
    void deleteSocksFail() {
        // Неудачное удаление носков
        Long id = 999L;
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                socksService.deleteSocks(id)
        );


        assertEquals("Такие носки не существуют с id: " + id, exception.getMessage());
    }


    @Test
    void getSocksMoreThanSuccess() {
        /*  Выводит список носков больше 
            процента состава хлопков */
        Socks sock1 = socksRepository.save(
            Socks.builder()
                .color("red")
                .cottonPart(20)
                .build()
        );
        warehouseRepository.save(
            Warehouse.builder()
                .socksId(sock1.getId())
                .quantity(300)
                .build()
        );


        Socks sock2 = socksRepository.save(
            Socks.builder()
                .color("red")
                .cottonPart(43)
                .build()
        );
        warehouseRepository.save(
            Warehouse.builder()
                .socksId(sock2.getId())
                .quantity(452)
                .build()
        );


        List<SocksResponse> result = socksService.getSocks("red", "moreThan", 10);


        assertThat(result)
            .isEqualTo(List.of(
                new SocksResponse(sock1.getId(), "red", 20, 300),
                new SocksResponse(sock2.getId(), "red", 43, 452)
            )
        );
    }


    @Test
    void getSocksLessThanSuccess() {
        /*  Выводит список носков меньше 
            процента состава хлопков */
        Socks sock1 = socksRepository.save(
            Socks.builder()
                .color("orange")
                .cottonPart(31)
                .build()
        );
         warehouseRepository.save(
            Warehouse.builder()
                .socksId(sock1.getId())
                .quantity(1300)
                .build()
        );


        Socks sock2 = socksRepository.save(
            Socks.builder()
                .color("orange")
                .cottonPart(58)
                .build()
        );
        warehouseRepository.save(
            Warehouse.builder()
                .socksId(sock2.getId())
                .quantity(374)
                .build()
        );


        List<SocksResponse> result = socksService.getSocks("orange", "lessThan", 60);


        assertThat(result)
            .isEqualTo(List.of(
                new SocksResponse(sock1.getId(), "orange", 31, 1300),
                new SocksResponse(sock2.getId(), "orange", 58, 374)
            )
        );
    }


    @Test
    void getSocksEqualSuccess() {
        /*  Выводит список носков равное
            проценту состава хлопков */
        Socks sock1 = socksRepository.save(
            Socks.builder()
                .color("white")
                .cottonPart(73)
                .build()
        );
         warehouseRepository.save(
            Warehouse.builder()
                .socksId(sock1.getId())
                .quantity(231)
                .build()
        );


        Socks sock2 = socksRepository.save(
            Socks.builder()
                .color("blue")
                .cottonPart(73)
                .build()
        );
        warehouseRepository.save(
            Warehouse.builder()
                .socksId(sock2.getId())
                .quantity(127)
                .build()
        );


        List<SocksResponse> result = socksService.getSocks("", "equal", 73);


        assertThat(result)
            .isEqualTo(List.of(
                new SocksResponse(sock1.getId(), "white", 73, 231),
                new SocksResponse(sock2.getId(), "blue", 73, 127)
            )
        );
    }


    @Test
    void getSocksMoreThanEmpty() {
        // Выводит пустой список
        List<SocksResponse> result = socksService.getSocks("black", "moreThan", 20);


        assertThat(result).isEmpty();
    }


    @Test
    void getSocksLessThanEmpty() {
        // Выводит пустой список
        List<SocksResponse> result = socksService.getSocks("black", "lessThan", 20);


        assertThat(result).isEmpty();
    }


    @Test
    void getSocksEqualEmpty() {
        // Выводит пустой список
        List<SocksResponse> result = socksService.getSocks("black", "equal", 20);


        assertThat(result).isEmpty();
    }


    @Test
    void getSocksUnknownOperationException() {
        /*  Выбрасывает исключение 
            при попытки указать несуществующую операцию */
        String operation = "unknown_one";
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            socksService.getSocks("red", operation, 30)
        );


        assertEquals("Неизвестная операция: " + operation, exception.getMessage());
    }
}
