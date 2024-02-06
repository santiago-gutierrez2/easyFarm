package es.udc.paproject.backend.rest.controllers;

import com.sun.xml.bind.unmarshaller.InfosetScanner;
import es.udc.paproject.backend.model.entities.Animal;
import es.udc.paproject.backend.model.entities.AnimalDao;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.entities.Weighing;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.UserService;
import es.udc.paproject.backend.model.services.WeighingService;
import es.udc.paproject.backend.rest.dtos.BlockDto;
import es.udc.paproject.backend.rest.dtos.WeighingDTOs.WeighingConversor;
import es.udc.paproject.backend.rest.dtos.WeighingDTOs.WeighingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/weighing")
public class WeighingController {

    @Autowired
    private WeighingService weighingService;
    @Autowired
    private UserService userService;
    @Autowired
    private AnimalDao animalDao;

    @PostMapping("/registerWeighing")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registerWeighing(@RequestAttribute Long userId,
            @Validated({WeighingDto.CreateWeighingValidation.class}) @RequestBody WeighingDto weighingDto) throws InstanceNotFoundException {

        User user = userService.loginFromId(userId);
        Optional<Animal> optionalAnimal = animalDao.findById(weighingDto.getAnimalWeighed());

        if (optionalAnimal.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.weighing", weighingDto.getAnimalWeighed());
        }

        Weighing weighing = WeighingConversor.toWeighing(weighingDto);
        weighing.setMadeBy(user);
        weighing.setAnimalWeighed(optionalAnimal.get());

        weighingService.createWeighing(weighing);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateWeighing(@RequestAttribute Long userId, @PathVariable Long id,
                       @Validated({WeighingDto.UpdateWeighingValidation.class}) @RequestBody WeighingDto weighingDto) throws InstanceNotFoundException {
        weighingService.updateWeighing(id, weighingDto.getKilos());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWeighing(@RequestAttribute Long userId, @PathVariable Long id) throws InstanceNotFoundException, PermissionException {
        weighingService.deleteWeighing(userId, id);
    }

    @GetMapping("/{id}")
    public WeighingDto getWeighingById(@RequestAttribute Long userId, @PathVariable Long id) throws InstanceNotFoundException {
        Weighing weighing = weighingService.getWeighingById(id);
        return WeighingConversor.toWeighingDto(weighing);
    }

    @GetMapping("/AllWeighingByFarmId")
    public BlockDto<WeighingDto> getAllWeighing(@RequestAttribute Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Long animalId, @RequestParam(required = false) Integer startKilos, @RequestParam(required = false) Integer endKilos,
            @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) throws InstanceNotFoundException {

        Block<Weighing> weighingBlock = weighingService.getAllWeighings(userId, animalId, startKilos, endKilos, startDate, endDate, page, size);

        return new BlockDto<>(WeighingConversor.toWeighingDtos(weighingBlock.getItems()), weighingBlock.getExistMoreItems());
    }

    @GetMapping("/allWeighingByAnimalId/{id}")
    public List<WeighingDto> getAllWeighingByAnimalId(@RequestAttribute Long userId, @PathVariable Long id) throws InstanceNotFoundException {
        return weighingService.getAllWeighingByAnimalId(userId, id);
    }
}
