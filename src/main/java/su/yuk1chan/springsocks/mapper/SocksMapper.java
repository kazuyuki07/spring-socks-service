package su.yuk1chan.springsocks.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import su.yuk1chan.springsocks.dto.SocksDTO;
import su.yuk1chan.springsocks.entity.Socks;

@Mapper(componentModel = "spring")
public interface SocksMapper {
    SocksDTO socksToSocksDTO(Socks socks);
    Socks socksDTOtoSocks(SocksDTO socksDTO);

    void fullUpdateSocks(SocksDTO socksDTO, @MappingTarget Socks socks);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partUpdateSocks(SocksDTO socksDTO, @MappingTarget Socks socks);
}
