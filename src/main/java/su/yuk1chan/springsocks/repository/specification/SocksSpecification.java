package su.yuk1chan.springsocks.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import su.yuk1chan.springsocks.entity.Socks;

public class SocksSpecification {
    public static Specification<Socks> colorFilter(String color) {
        return (root, _, builder) ->
            color == null ? null : builder.equal(root.get("color"), color);
    }
}
