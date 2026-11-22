package su.yuk1chan.springsocks.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import su.yuk1chan.springsocks.dto.PagedResponse;
import su.yuk1chan.springsocks.dto.SocksDTO;
import su.yuk1chan.springsocks.entity.Socks;
import su.yuk1chan.springsocks.enums.SocksSort;
import su.yuk1chan.springsocks.exceptions.NotFoundException;
import su.yuk1chan.springsocks.mapper.SocksMapper;
import su.yuk1chan.springsocks.repository.SocksRepository;
import su.yuk1chan.springsocks.repository.specification.SocksSpecification;


@Service
@RequiredArgsConstructor
public class SocksService {
    private final SocksRepository socksRepository;
    private final SocksMapper socksMapper;

    public PagedResponse<SocksDTO> getSocks(
            Integer page,
            Integer size,
            String color,
            String sort) {
        Specification<Socks> socksSpecification = Specification.where(SocksSpecification.colorFilter(color));

        SocksSort socksSort = SocksSort.of(sort);
        Sort sorted = Sort.by(
                socksSort.getDirection(),
                socksSort.getEntityValue()
        );

        Page<Socks> socks = socksRepository.findAll(socksSpecification, PageRequest.of(page, size, sorted));

        return PagedResponse.from(socks.map(socksMapper::socksToSocksDTO));
    }

    public Socks addSocks(SocksDTO socksDTO) {
        return socksRepository.save(socksMapper.socksDTOtoSocks(socksDTO));
    }


    public void deleteSocks(Long id) {
        Socks foundSocks = socksRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Носки id: " + id + " не найдены")
        );

        socksRepository.delete(foundSocks);
    }
}