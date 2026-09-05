package su.yuk1chan.springsocks.services;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import su.yuk1chan.springsocks.entities.Socks;
import su.yuk1chan.springsocks.entities.Warehouse;
import su.yuk1chan.springsocks.exceptions.NotFoundException;
import su.yuk1chan.springsocks.exceptions.ValidationException;
import su.yuk1chan.springsocks.repositories.SocksRepository;
import su.yuk1chan.springsocks.repositories.WarehouseRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest()
public class WarehouseServiceTest {
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private WarehouseRepository warehouseRepository;
    @Autowired
    private SocksRepository socksRepository;

    @AfterEach
    void cleanData() {
        socksRepository.deleteAll();
        warehouseRepository.deleteAll();
    }


    @Test
    void addIntoWarehouseSuccess() {
        // Успешное добавление носков в базу данных
        String color = "red";
        Integer cottonPart = 32;
        Integer quantity = 240;


        Socks sock = socksRepository.save(
                Socks.builder()
                    .color(color)
                    .cottonPart(cottonPart)
                    .build()
        );
        Warehouse result = warehouseService.addIntoWarehouse(sock.getId(), quantity);


        assertThat(result.getSocksId()).isEqualTo(sock.getId());
        assertThat(result.getQuantity()).isEqualTo(quantity);
    }


    @Test
    void addIntoWarehouseSameSock() {
        /* Успешное добавление тех же
         носков на тот же склад
         с целью повышение количества */
        String color = "blue";
        Integer cottonPart = 51;
        Integer quantity = 412;
        Integer quantity2 = 551;


        Socks sock = socksRepository.save(
            Socks.builder()
                .color(color)
                .cottonPart(cottonPart)
                .build()
        );


        warehouseService.addIntoWarehouse(sock.getId(), quantity);
        Warehouse result = warehouseService.addIntoWarehouse(sock.getId(), quantity2);


        assertThat(result.getQuantity()).isEqualTo(quantity + quantity2);
    }


    @Test
    void addIntoWarehouseSuccessWithSocksExists() {
        // Добавление новых носков и нового склада в бд при существующих других складов и носков
        Socks socks1 = socksRepository.save(
                Socks.builder()
                    .color("red")
                    .cottonPart(100)
                    .build()
        );
        Socks socks2 = socksRepository.save(
                Socks.builder()
                    .color("blue")
                    .cottonPart(50)
                    .build()
        );


        Warehouse warehouseForSocks2 = new Warehouse();
        warehouseForSocks2.setSocksId(socks2.getId());
        warehouseForSocks2.setQuantity(100);
        warehouseRepository.save(warehouseForSocks2);


        Warehouse warehouseForSocks1 = new Warehouse();
        warehouseForSocks1.setSocksId(socks1.getId());
        warehouseForSocks1.setQuantity(200);
        warehouseRepository.save(warehouseForSocks1);

        Warehouse result = warehouseService.addIntoWarehouse(
                socks1.getId(),
                50
        );

        assertEquals(250, result.getQuantity());
    }


    @Test
    void addWarehouseFail() {
        // Неудачное добавление склада в бд
        Long socksId = 2L;
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
            warehouseService.addIntoWarehouse(socksId, 300)
        );


        assertEquals("Такие носки не существуют на складе с id: " + socksId, exception.getMessage());
    }


    @Test
    void outcomeFromWarehouseSuccess() {
        // Удачный вывоз носков со склада
        Socks socks = socksRepository.save(
                Socks.builder()
                        .color("cinnamon")
                        .cottonPart(20)
                        .build()
        );


        Warehouse warehouse = warehouseRepository.save(
                Warehouse.builder()
                        .socksId(socks.getId())
                        .quantity(200)
                        .build()
        );

        Warehouse result = warehouseService.outcomeFromWarehouse(warehouse.getId(), 50);

        assertThat(result.getQuantity()).isEqualTo(150);
    }


    @Test
    void outcomeQuantityBelowZeroFail() {
        /* Неудачная выгрузка носков
           со склада по причине
           отрицательного количества */
        int quantity = -50;

        Socks socks = socksRepository.save(
                Socks.builder()
                        .color("cinnamon")
                        .cottonPart(20)
                        .build()
        );


        Warehouse warehouse = warehouseRepository.save(
                Warehouse.builder()
                        .socksId(socks.getId())
                        .quantity(200)
                        .build()
        );


        ValidationException exception = assertThrows(ValidationException.class, () ->
                warehouseService.outcomeFromWarehouse(warehouse.getId(), quantity)
        );

        assertEquals("Неверный параметр: " + quantity, exception.getMessage());
    }
}