package su.yuk1chan.springsocks.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Socks")
@Table(name = "socks")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Socks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String color;

    @Column(name = "cotton_part", nullable = false)
    private Integer cottonPart;
}