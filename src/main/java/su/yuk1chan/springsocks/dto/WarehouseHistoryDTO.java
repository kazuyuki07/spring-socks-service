package su.yuk1chan.springsocks.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import su.yuk1chan.springsocks.enums.Event;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WarehouseHistoryDTO {
    private Event event;
    private String color;
    private Integer cottonPart;
    private Integer quantity;
    private Integer currentQuantity;
    private Long warehouseId;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private LocalDateTime date;
}