package es.udc.paproject.backend.rest.controllers;

import es.udc.paproject.backend.model.entities.Animal;
import es.udc.paproject.backend.model.entities.User;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.model.services.AnimalService;
import es.udc.paproject.backend.model.services.Block;
import es.udc.paproject.backend.model.services.UserService;
import es.udc.paproject.backend.rest.dtos.AnimalDTOs.AnimalConversor;
import es.udc.paproject.backend.rest.dtos.AnimalDTOs.AnimalDto;
import es.udc.paproject.backend.rest.dtos.AnimalDTOs.AnimalWithLabelDto;
import es.udc.paproject.backend.rest.dtos.AnimalDTOs.NewAnimalDto;
import es.udc.paproject.backend.rest.dtos.BlockDto;
import es.udc.paproject.backend.rest.dtos.FoodPurchaseDTOs.ConsumptionChartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/animal")
public class AnimalController {

    @Autowired
    private AnimalService animalService;
    @Autowired
    private UserService userService;


    @PostMapping("/registerAnimal")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void registerAnimal(@RequestAttribute Long userId,
        @Validated({NewAnimalDto.CreateAnimalValidation.class}) @RequestBody NewAnimalDto animalDto)
        throws InstanceNotFoundException {

        User user = userService.loginFromId(userId);

        Animal animal = AnimalConversor.toAnimal(animalDto);
        animal.setBelongsTo(user.getFarm());

        animalService.createAnimal(animal);
    }

    @PutMapping("/{id}")
    public AnimalDto updateAnimal(@PathVariable Long id,
        @Validated({NewAnimalDto.UpdateAnimalValidation.class}) @RequestBody NewAnimalDto animalDto) throws InstanceNotFoundException {

        Animal animal = AnimalConversor.toAnimal(animalDto);
        animal = animalService.updateAnimal(id, animal);
        return AnimalConversor.toAnimalDto(animal);
    }

    @DeleteMapping("/{id}/isDead")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setAnimalDead(@RequestAttribute Long userId, @PathVariable Long id) throws InstanceNotFoundException, PermissionException{
        animalService.setAnimalDead(userId, id);
    }

    @GetMapping("/{id}")
    public AnimalDto getAnimalById(@RequestAttribute Long userId, @PathVariable Long id)  throws InstanceNotFoundException {
        Animal animal = animalService.getAnimalById(userId, id);
        return AnimalConversor.toAnimalDto(animal);
    }

    @GetMapping("/allAnimals")
    public BlockDto<AnimalDto> getAllAnimals(@RequestAttribute Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size,
        @RequestParam(required = false) String name, @RequestParam(required = false) Long identificationNumber, @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate, @RequestParam(required = false) Boolean isMale, @RequestParam(required = false) Boolean dead) throws InstanceNotFoundException {

        Block<Animal> animalBlock = animalService.getAllAnimals(userId, name, identificationNumber, startDate, endDate, isMale, dead, page, size);

        return new BlockDto<>(AnimalConversor.toAnimalDtos(animalBlock.getItems()), animalBlock.getExistMoreItems());
    }

    @GetMapping("/animalsWithLabel")
    public List<AnimalWithLabelDto> getAnimalsWithLabel(@RequestAttribute Long userId, @RequestParam(defaultValue = "false") boolean all, @RequestParam(defaultValue = "false") boolean onlyFemale) throws InstanceNotFoundException {
        List<Animal> animalList = animalService.getAnimalsWithLabel(userId, all, onlyFemale);
        return AnimalConversor.toAnimalWithLabelDto(animalList);
    }

    @GetMapping("/animalFoodConsumptionChart/{id}")
    public List<ConsumptionChartDto> getAnimalFoodConsumptionChart(@RequestAttribute Long userId, @PathVariable Long id) throws InstanceNotFoundException {
        return animalService.getAnimalFoodConsumptionChart(userId, id);
    }

}
