package es.udc.paproject.backend.rest.dtos.MilkingDTOs;

import es.udc.paproject.backend.model.entities.Milking;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class MilkingConversor {

    private MilkingConversor() {}

    public final static MilkingDto toMilkingDto(Milking milking) {
        return new MilkingDto(milking.getId(), toMillis(milking.getDate()), milking.getLiters(), milking.getMadeBy().getId(),
                milking.getAnimalMilked().getId());
    }

    public final static Milking toMilking(MilkingDto milkingDto) {
        return new Milking(milkingDto.getLiters(), null, null);
    }

    public final static List<MilkingDto> toMilkingDtos(List<Milking> milkings) {
        return milkings.stream().map(MilkingConversor::toMilkingDto).collect(Collectors.toList());
    }

    private static long toMillis(LocalDateTime date) {
        return date.truncatedTo(ChronoUnit.MINUTES).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }
}
