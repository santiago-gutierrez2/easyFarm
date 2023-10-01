package es.udc.paproject.backend.rest.dtos.FarmDTOs;

import es.udc.paproject.backend.model.entities.Farm;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class FarmConversor {

    private FarmConversor() {}

    public final static FarmDto toFarmDto(Farm farm) {
        return new FarmDto(farm.getId(), farm.getName(), farm.getAddress(), farm.getSizeHectares(),
                toMillis(farm.getCreationDate()));
    }

    public final static Farm toFarm(FarmDto farmDto) {
        return new Farm(farmDto.getName(), farmDto.getAddress(), farmDto.getSizeHectares());
    }

    private static long toMillis(LocalDateTime date) {
        return date.truncatedTo(ChronoUnit.MINUTES).atZone(ZoneOffset.systemDefault()).toInstant().toEpochMilli();
    }
}
