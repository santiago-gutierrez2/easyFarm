package es.udc.paproject.backend.rest.dtos.WeighingDTOs;

import es.udc.paproject.backend.model.entities.Weighing;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

public class WeighingConversor {

    private WeighingConversor() {}

    public final static WeighingDto toWeighingDto(Weighing weighing) {
        return new WeighingDto(weighing.getId(), toMillis(weighing.getDate()), weighing.getKilos(), weighing.getMadeBy().getId(),
                weighing.getAnimalWeighed().getId());
    }

    public final static Weighing toWeighing(WeighingDto weighingDto) {
        return new Weighing(weighingDto.getKilos(), null, null);
    }

    public final static List<WeighingDto> toWeighingDtos(List<Weighing> weighings) {
        return weighings.stream().map(WeighingConversor::toWeighingDto).collect(Collectors.toList());
    }

    private static long toMillis(LocalDateTime date) {
        return date.truncatedTo(ChronoUnit.MINUTES).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }
}
