package es.udc.paproject.backend.rest.dtos.AnimalDTOs;

import es.udc.paproject.backend.model.entities.Animal;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class AnimalConversor {

    private AnimalConversor() {}

    public final static AnimalDto toAnimalDto(Animal animal) {
        return new AnimalDto(animal.getId(), animal.getName(), animal.getIdentificationNumber(), toMillis(animal.getBirthDate()),
                animal.getIsMale(), animal.getPhysicalDescription(), animal.getBelongsTo().getId(), animal.getIsDead());
    }

    public final static Animal toAnimal(NewAnimalDto animalDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime sd = LocalDateTime.parse(animalDto.getBirthDateString() + " 23:59:59", formatter);
        return new Animal(animalDto.getName(), animalDto.getIdentificationNumber(), sd, animalDto.getMale(),
                animalDto.getPhysicalDescription(), null, animalDto.getDead());
    }

    public final static List<AnimalDto> toAnimalDtos(List<Animal> animalList) {
        return animalList.stream().map(AnimalConversor::toAnimalDto).collect(Collectors.toList());
    }

    public final static AnimalWithLabelDto toAnimalWithLabelDto(Animal animal) {
        return new AnimalWithLabelDto(animal.getName() + " / " + animal.getIdentificationNumber().toString(),
                animal.getId(), animal.getIsDead());
    }

    public final static List<AnimalWithLabelDto> toAnimalWithLabelDto(List<Animal> animal) {
        return animal.stream().map(AnimalConversor::toAnimalWithLabelDto).collect(Collectors.toList());
    }

    private static long toMillis(LocalDateTime date) {
        return date.truncatedTo(ChronoUnit.MINUTES).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }
}
