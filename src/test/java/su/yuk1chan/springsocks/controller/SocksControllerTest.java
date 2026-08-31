package su.yuk1chan.springsocks.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import su.yuk1chan.springsocks.dto.SocksDTO;
import su.yuk1chan.springsocks.dto.SocksResponse;
import su.yuk1chan.springsocks.entities.Socks;
import su.yuk1chan.springsocks.entities.Warehouse;
import su.yuk1chan.springsocks.repositories.SocksRepository;
import su.yuk1chan.springsocks.repositories.WarehouseRepository;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SocksControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SocksRepository socksRepository;


    @Autowired
    private WarehouseRepository warehouseRepository;


    @Autowired
    private ObjectMapper objectMapper;


    @AfterEach
    void cleanData() {
        // Очищает все данные с таблицы
        socksRepository.deleteAll();
        warehouseRepository.deleteAll();
    }


    @Test
    void incomeSuccess() throws Exception {
        // Успешное добавление носков на склад
        SocksDTO socksDTO = new SocksDTO("red", 30, 150);


        mockMvc.perform(post("/api/socks/income")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socksDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.color").value("red"))
                .andExpect(jsonPath("$.cottonPart").value(30))
                .andExpect(jsonPath("$.quantity").value(150));
    }


    @Test
    void incomeSuccessSameSock() throws Exception {
        /* Успешное добавление новую партию носков
           с одинаковым цветом и составом хлопка на склад */
        SocksDTO socksDTO = new SocksDTO("orange", 48, 150);


        Socks socks = socksRepository.save(
                Socks.builder()
                    .color("orange")
                    .cottonPart(48)
                    .build()
        );


        warehouseRepository.save(
                Warehouse.builder()
                    .socksId(socks.getId())
                    .quantity(100)
                    .build());


        mockMvc.perform(post("/api/socks/income")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socksDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.color").value("orange"))
                .andExpect(jsonPath("$.cottonPart").value(48))
                .andExpect(jsonPath("$.quantity").value(250));
    }


    @Test
    void incomeSocksDtoQuantityBelowZeroFail() throws Exception {
        /* Неудачное добавление носков на склад
           по причине отрицательного количества */
        SocksDTO socksDTO = new SocksDTO("blue", 20, -12);


        mockMvc.perform(post("/api/socks/income")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socksDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Неверный параметр: " + socksDTO.getQuantity())
                );
    }


    @Test
    void incomeSocksDtoVoidQuantityFail() throws Exception {
        /* Неудачное добавление носков на склад
           по причине нулевого ее количества  */
        SocksDTO socksDTO = new SocksDTO("black", 41, 0);


        mockMvc.perform(post("/api/socks/income")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socksDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Неверный параметр: " + socksDTO.getQuantity())
                );
    }


    @Test
    void incomeSocksDtoColorFail() throws Exception {
        /* Неудачное добавление носков на склад
           по причине неправильного формата цвета (содержит цифры и прочие символы
           не относящиеся к латинским буквам) */
        SocksDTO socksDTO = new SocksDTO("c@lor1", 38, 21);


        mockMvc.perform(post("/api/socks/income")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socksDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Неверный параметр: " + socksDTO.getColor())
                );
    }


    @Test
    void incomeSocksDtoCottonPartBelowZeroFail() throws Exception {
        /* Неудачное добавление носков на склад
           по причине отрицательного числа состава хлопка (процент) */
        SocksDTO socksDTO = new SocksDTO("white", -12, 200);


        mockMvc.perform(post("/api/socks/income")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socksDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Неверный параметр: " + socksDTO.getCottonPart())
                );
    }


    @Test
    void incomeSocksDtoCottonPartAboveZeroFail() throws Exception {
        /* Неудачное добавление носков на склад
           по причине больше числа 100 состава хлопка */
        SocksDTO socksDTO = new SocksDTO("cinnamon", 121, 194);


        mockMvc.perform(post("/api/socks/income")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(socksDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Неверный параметр: " + socksDTO.getCottonPart())
                );
    }


    @Test
    void outcomeSuccess() throws Exception {
        // Удачная выгрузка носков со склада
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


        SocksDTO socksDTOResponse = new SocksDTO(socks.getColor(), socks.getCottonPart(), 150);


        mockMvc.perform(delete("/api/socks/outcome/{id}", warehouse.getId())
                .param("quantity", "50"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(socksDTOResponse)));
    }


    @Test
    void outcomeQuantityBelowZeroFail() throws Exception {
        /* Неудачная выгрузка носков со склада
           по причине отрицательного количества */
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

        mockMvc.perform(delete("/api/socks/outcome/{id}", warehouse.getId())
                .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Неверный параметр: " + quantity));
    }


    @Test
    void deleteSocksSuccess() throws Exception {
        // Удачное удаление самих носков с таблицы и с таблицы склада
        Socks socks = socksRepository.save(
                Socks.builder()
                        .color("purple")
                        .cottonPart(12)
                        .build()
        );


        Warehouse warehouse = warehouseRepository.save(
                Warehouse.builder()
                        .socksId(socks.getId())
                        .quantity(111)
                        .build()
        );


        mockMvc.perform(delete("/api/socks/delete_socks/{id}", warehouse.getSocksId()))
                .andExpect(status().isNoContent());

        assertFalse(warehouseRepository.existsById(warehouse.getId()));
    }


    @Test
    void deleteSocksIdFail() throws Exception {
        /* Неудачное удаление носков с таблицы
           по причине не существования носка в бд */

        Long id = 99L;
        mockMvc.perform(delete("/api/socks/delete_socks/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Такие носки не существуют с id: " + id)
                );
    }


    @Test
    void showMeSocksMoreThanSuccess() throws Exception {
        /* Показывает количество носков
           большей какой-либо процентов состава хлопка */
        Socks socks1 = socksRepository.save(
                Socks.builder()
                        .color("mint")
                        .cottonPart(84)
                        .build()
        );


        Warehouse warehouse1 = warehouseRepository.save(
                Warehouse.builder()
                        .socksId(socks1.getId())
                        .quantity(831)
                        .build()
        );


        Socks socks2 = socksRepository.save(
                Socks.builder()
                        .color("navy")
                        .cottonPart(53)
                        .build()
        );


        Warehouse warehouse2 = warehouseRepository.save(
                Warehouse.builder()
                        .socksId(socks2.getId())
                        .quantity(210)
                        .build()
        );


        List<SocksResponse> socksResponse = List.of(
                new SocksResponse(
                    socks1.getId(),
                    socks1.getColor(),
                    socks1.getCottonPart(),
                    warehouse1.getQuantity()
                ),
                new SocksResponse(
                    socks2.getId(),
                    socks2.getColor(),
                    socks2.getCottonPart(),
                    warehouse2.getQuantity()
                )
        );



        mockMvc.perform(get("/api/socks")
                .param("operation", "moreThan")
                .param("cotton_part", "50"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(socksResponse)));
    }

    @Test
    void showMeSocksLessThanSuccess() throws Exception {
        /* Показывает количество носков
           меньшей какой-либо процентов состава хлопка */
        Socks socks1 = socksRepository.save(
                Socks.builder()
                        .color("mint")
                        .cottonPart(84)
                        .build()
        );


        Warehouse warehouse1 = warehouseRepository.save(
                Warehouse.builder()
                        .socksId(socks1.getId())
                        .quantity(831)
                        .build()
        );


        Socks socks2 = socksRepository.save(
                Socks.builder()
                        .color("navy")
                        .cottonPart(53)
                        .build()
        );


        Warehouse warehouse2 = warehouseRepository.save(
                Warehouse.builder()
                        .socksId(socks2.getId())
                        .quantity(210)
                        .build()
        );


        List<SocksResponse> socksResponse = List.of(
                new SocksResponse(
                        socks2.getId(),
                        socks2.getColor(),
                        socks2.getCottonPart(),
                        warehouse2.getQuantity()
                )
        );



        mockMvc.perform(get("/api/socks")
                .param("operation", "lessThan")
                .param("cotton_part", "70"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(socksResponse)));


        assertTrue(warehouseRepository.existsById(warehouse1.getId()));
    }

    @Test
    void showMeSocksEqualSuccess() throws Exception {
        /* Показывает количество носков
           равной какой-либо процентов состава хлопка */
        Socks socks1 = socksRepository.save(
                Socks.builder()
                        .color("mint")
                        .cottonPart(84)
                        .build()
        );


        Warehouse warehouse1 = warehouseRepository.save(
                Warehouse.builder()
                        .socksId(socks1.getId())
                        .quantity(831)
                        .build()
        );


        List<SocksResponse> socksResponse = List.of(
                new SocksResponse(
                        socks1.getId(),
                        socks1.getColor(),
                        socks1.getCottonPart(),
                        warehouse1.getQuantity()
                )
        );


        mockMvc.perform(get("/api/socks")
                .param("color", "mint")
                .param("operation", "equal")
                .param("cotton_part", "84"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(socksResponse)));
    }


    @Test
    void showMeSocksMoreThanEmptySuccess() throws Exception {
        /* Показывает пустой список носков
           большей какой-либо процентов состава хлопка */
        List<SocksResponse> socksResponse = List.of();


        mockMvc.perform(get("/api/socks")
                .param("color", "cinnamon")
                .param("operation", "moreThan")
                .param("cotton_part", "12"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(socksResponse)));
    }


    @Test
    void showMeSocksLessThanEmptySuccess() throws Exception {
        /* Показывает пустой список носков
           меньшей какой-либо процентов состава хлопка */
        List<SocksResponse> socksResponse = List.of();


        mockMvc.perform(get("/api/socks")
                .param("color", "navy")
                .param("operation", "lessThan")
                .param("cotton_part", "20"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(socksResponse)));
    }


    @Test
    void showMeSocksEqualEmptySuccess() throws Exception {
        /* Показывает пустой список носков
           равное какой-либо процентов состава хлопка */
        List<SocksResponse> socksResponse = List.of();


        mockMvc.perform(get("/api/socks")
                .param("color", "mint")
                .param("operation", "equal")
                .param("cotton_part", "93"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(socksResponse)));
    }


    @Test
    void showMeSocksUnknownOperationParamFail() throws Exception {
        /* Неудачный вывод список носков
           по причине неизвестного параметра операции */
        String operation = "unknown_operation";


        mockMvc.perform(get("/api/socks")
                .param("color", "mint")
                .param("operation", "unknown_operation")
                .param("cotton_part", "93"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Неизвестная операция: " + operation)
                );
    }

}