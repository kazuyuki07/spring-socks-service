package su.yuk1chan.springsocks.enums;

import lombok.Getter;
import org.springframework.data.domain.Sort;

import java.util.stream.Stream;

@Getter
public enum SocksSort {
    COLOR_ASC("color", "color"),
    COLOR_DESC("-color", "color"),
    COTTON_PART_ASC("cotton_part", "cottonPart"),
    COTTON_PART_DESC("-cotton_part", "cottonPart");

    private final String requestValue;
    private final String entityValue;
    private final Sort.Direction direction;

    SocksSort(String requestValue, String entityValue) {
        this.requestValue = requestValue;
        this.entityValue = entityValue;
        this.direction = requestValue.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC;
    }

    public static SocksSort of(String requestValue) {
        return Stream.of(SocksSort.values())
                .filter(e -> e.requestValue.equals(requestValue))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Вид сортировки не найден"));
    }
}
