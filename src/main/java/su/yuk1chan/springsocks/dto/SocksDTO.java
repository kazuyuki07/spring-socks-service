package su.yuk1chan.springsocks.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class SocksDTO {
    private String color;
    private Integer cottonPart;
    private Integer quantity;


    public SocksDTO(String color, Integer cottonPart, Integer quantity){
        this.color = color;
        this.cottonPart = cottonPart;
        this.quantity = quantity;
    }
}
