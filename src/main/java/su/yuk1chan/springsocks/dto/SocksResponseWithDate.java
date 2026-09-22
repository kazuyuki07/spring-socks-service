package su.yuk1chan.springsocks.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SocksResponseWithDate extends SocksDTO{
    private Long id;
    private Long warehouseId;
    private LocalDateTime date;

    public SocksResponseWithDate(Long id,
                                 String color,
                                 Integer cottonPart,
                                 Integer quantity,
                                 Long warehouseId,
                                 LocalDateTime date) {
        super(color, cottonPart, quantity);
        this.id = id;
        this.warehouseId = warehouseId;
        this.date = date;
    }
}