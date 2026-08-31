package su.yuk1chan.springsocks.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import su.yuk1chan.springsocks.entities.Warehouse;
import su.yuk1chan.springsocks.exceptions.NotFoundException;
import su.yuk1chan.springsocks.exceptions.ValidationException;
import su.yuk1chan.springsocks.repositories.SocksRepository;
import su.yuk1chan.springsocks.repositories.WarehouseRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;
    private final SocksRepository socksRepository;


    public Warehouse addIntoWarehouse(Long socksId, Integer quantity) {
        if (quantity <= 0) {
            throw new ValidationException("Неверный параметр: " + quantity);
        }


        if (!socksRepository.existsById(socksId)) {
            throw new NotFoundException("Такие носки не существуют на складе с id: " + socksId);
        }
        Optional<Warehouse> warehouseOpt = warehouseRepository.findBySocksId(socksId);


        if (warehouseOpt.isEmpty()) {
            return warehouseRepository.save(
                    Warehouse.builder()
                        .socksId(socksId)
                        .quantity(quantity)
                        .build()
            );
        }


        Warehouse warehouse = warehouseOpt.get();
        warehouse.setQuantity(warehouse.getQuantity() + quantity);
        return warehouseRepository.save(warehouse);
    }


    public Warehouse outcomeFromWarehouse(Long id, Integer quantity) {
        if (quantity <= 0) {
            throw new ValidationException("Неверный параметр: " + quantity);
        }


        Optional<Warehouse> warehouseOpt = warehouseRepository.findById(id);


        if (warehouseOpt.isEmpty()) {
            throw new NotFoundException("Не найден склад id: " + id);
        }


        Warehouse warehouse = warehouseOpt.get();
        int warehouseOutcome = warehouse.getQuantity() - quantity;
        if (warehouseOutcome < 0) {
            throw new ValidationException("Невозможно отгрузить носки больше того, что есть на складе");
        }
        warehouse.setQuantity(warehouseOutcome);
        return warehouseRepository.save(warehouse);
    }
}
