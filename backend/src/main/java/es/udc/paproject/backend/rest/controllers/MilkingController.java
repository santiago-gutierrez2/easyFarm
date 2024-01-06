package es.udc.paproject.backend.rest.controllers;

import ch.qos.logback.core.pattern.color.BlackCompositeConverter;
import es.udc.paproject.backend.model.entities.Animal;
import es.udc.paproject.backend.model.entities.AnimalDao;
import es.udc.paproject.backend.model.entities.Milking;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.MilkingService;
import es.udc.paproject.backend.model.services.UserService;
import es.udc.paproject.backend.rest.dtos.BlockDto;
import es.udc.paproject.backend.rest.dtos.MilkingDTOs.MilkingConversor;
import es.udc.paproject.backend.rest.dtos.MilkingDTOs.MilkingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Optional;

@RestController
@RequestMapping("/milking")
public class MilkingController {

    @Autowired
    private MilkingService milkingService;
    @Autowired
    private UserService userService;
    @Autowired
    private AnimalDao animalDao;

    @PostMapping("/registerMilking")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registerMilking(@RequestAttribute Long userId,
                                @Validated({MilkingDto.CreateMilkingValidation.class}) @RequestBody MilkingDto milkingDto) throws InstanceNotFoundException {
        User user = userService.loginFromId(userId);
        Optional<Animal> optionalAnimal = animalDao.findById(milkingDto.getAnimalMilked());

        if (optionalAnimal.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.weighing", milkingDto.getAnimalMilked());
        }

        Milking milking = MilkingConversor.toMilking(milkingDto);
        milking.setMadeBy(user);
        milking.setAnimalMilked(optionalAnimal.get());

        milkingService.createMilking(milking);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMilking(@RequestAttribute Long userId, @PathVariable Long id,
                              @Validated({MilkingDto.UpdateMilkingValidation.class}) @RequestBody MilkingDto milkingDto) throws InstanceNotFoundException {
        milkingService.updateMilking(id, milkingDto.getLiters());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMilking(@RequestAttribute Long userId, @PathVariable Long id) throws InstanceNotFoundException, PermissionException {
        milkingService.deleteMilkingRecord(userId, id);
    }

    @GetMapping("/{id}")
    public MilkingDto getMilkingById(@RequestAttribute Long userId, @PathVariable Long id) throws InstanceNotFoundException {
        Milking milking = milkingService.getMilkingById(id);
        return MilkingConversor.toMilkingDto(milking);
    }

    @GetMapping("/AllMilkingByFarmId")
    public BlockDto<MilkingDto> getAllMilking(@RequestAttribute Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
          @RequestParam(required = false) Long animalId, @RequestParam(required = false) Integer startLiters, @RequestParam(required = false) Integer endLiters,
          @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) throws InstanceNotFoundException {
        Block<Milking> milkingBlock = milkingService.getAllMilkings(userId, animalId, startLiters, endLiters, startDate, endDate, page, size);

        return new BlockDto<>(MilkingConversor.toMilkingDtos(milkingBlock.getItems()), milkingBlock.getExistMoreItems());
    }


}
