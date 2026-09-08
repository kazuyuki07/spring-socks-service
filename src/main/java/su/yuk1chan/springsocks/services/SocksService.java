package su.yuk1chan.springsocks.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import su.yuk1chan.springsocks.dto.SocksResponse;
import su.yuk1chan.springsocks.entities.Socks;
import su.yuk1chan.springsocks.exceptions.NotFoundException;
import su.yuk1chan.springsocks.exceptions.ValidationException;
import su.yuk1chan.springsocks.repositories.SocksRepository;

import java.util.List;
import java.util.Optional;

import static su.yuk1chan.springsocks.util.Constants.*;

@Service
@RequiredArgsConstructor
public class SocksService {
    private final SocksRepository socksRepository;


    public Socks getSocksBySocksId(Long socksId) {
        return socksRepository.findById(socksId)
                .orElseThrow(() -> new NotFoundException("Носки не найдены id:" + socksId));
    }


    public Socks addSocks(String color, Integer cottonPart) {
        if (!color.matches("[a-zA-z]+")) {
            throw new IllegalArgumentException("Неверный параметр: " + color);
        }


        if (cottonPart > 100 || cottonPart < 0) {
            throw new ValidationException("Неверный параметр: " + cottonPart);
        }


        Optional<Socks> socksOpt = socksRepository.findByColorAndCottonPart(color, cottonPart);


        if (socksOpt.isPresent()) {
            return socksOpt.get();
        }


        Socks socks = Socks.builder()
                .color(color)
                .cottonPart(cottonPart)
                .build();
        return socksRepository.save(socks);
    }


    public void deleteSocks(Long id) {
        if (!socksRepository.existsById(id)) {
            throw new NotFoundException("Такие носки не существуют с id: " + id);
        }

        socksRepository.deleteById(id);
    }


    public List<SocksResponse> getSocks(String color, String operation, Integer cottonPart) {
        if (cottonPart > 100 || cottonPart < 0) {
            throw new ValidationException("Неверный параметр: " + cottonPart);
        }


        return switch (operation) {
            case MORE_THAN_OPERATION -> socksRepository.getSocksMoreThan(color, cottonPart);
            case LESS_THAN_OPERATION -> socksRepository.getSocksLessThan(color, cottonPart);
            case EQUAL_OPERATION -> socksRepository.getSocksEqual(color, cottonPart);
            default -> throw new IllegalArgumentException("Неизвестная операция: " + operation);
        };
    }
}