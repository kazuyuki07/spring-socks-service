package su.yuk1chan.springsocks.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Warehouse")
@Table(name = "warehouse")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "socks_id", unique = true, nullable = false)
    private Long socksId;

    @Column(nullable = false)
    private Integer quantity;
}
