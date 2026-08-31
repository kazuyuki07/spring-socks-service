package su.yuk1chan.springsocks.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SocksResponse extends SocksDTO{
    private Long id;


    public SocksResponse(Long id, String color, Integer cottonPart, Integer quantity) {
        super(color, cottonPart, quantity);
        this.id = id;
    }
}