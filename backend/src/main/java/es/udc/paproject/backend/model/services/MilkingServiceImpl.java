package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import es.udc.paproject.backend.rest.dtos.MilkingDTOs.MilkingChartDto;
import es.udc.paproject.backend.rest.dtos.MilkingDTOs.MilkingConversor;
import es.udc.paproject.backend.rest.dtos.MilkingDTOs.MilkingDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MilkingServiceImpl implements MilkingService{

    @Autowired
    private MilkingDao milkingDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private AnimalDao animalDao;

    @Override
    public void createMilking(Milking milking) throws InstanceNotFoundException {
        milkingDao.save(milking);
    }

    @Override
    public void updateMilking(Long id, Integer liters) throws InstanceNotFoundException {
        Optional<Milking> optionalMilking = milkingDao.findById(id);

        if (optionalMilking.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.milking", id);
        }

        Milking milking = optionalMilking.get();
        milking.setLiters(liters);

        milkingDao.save(milking);
    }

    @Override
    public void deleteMilkingRecord(Long userId, Long id) throws InstanceNotFoundException, PermissionException {
        Optional<Milking> optionalMilking = milkingDao.findById(id);
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalMilking.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.milking", id);
        } else if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        } else if (optionalUser.get().getRole() == User.RoleType.EMPLOYEE && !Objects.equals(optionalUser.get().getId(), optionalMilking.get().getMadeBy().getId())) {
            throw new PermissionException();
        }

        milkingDao.delete(optionalMilking.get());
    }

    @Override
    public Milking getMilkingById(Long id) throws InstanceNotFoundException {
        Optional<Milking> optionalMilking = milkingDao.findById(id);

        if (optionalMilking.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.milking", id);
        }

        return optionalMilking.get();
    }

    @Override
    public Block<Milking> getAllMilkings(Long userId, Long animalId, Integer startLiters, Integer endLiters, String startDate, String endDate, int page, int size) throws InstanceNotFoundException {
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        User user = optionalUser.get();
        Farm farm = user.getFarm();

        Slice<Milking> milkingSlice;
        if (animalId != null || startLiters != null || endLiters != null || startDate != null || endDate != null) {
            milkingSlice = milkingDao.findAll(farm.getId(), animalId, startLiters, endLiters, startDate, endDate, page, size);
        } else {
            milkingSlice = milkingDao.findByAnimalMilkedBelongsToIdOrderByDateDesc(farm.getId(), PageRequest.of(page, size));
        }

        return new Block<>(milkingSlice.getContent(), milkingSlice.hasNext());
    }

    @Override
    public List<MilkingChartDto> findMilkingByAnimalMilkedId(Long userId, Long animalId) throws InstanceNotFoundException {
        Optional<User> optionalUser = userDao.findById(userId);
        Optional<Animal> optionalAnimal = animalDao.findById(animalId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }
        if (optionalAnimal.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.animal", animalId);
        }

        return milkingDao.findMilkingByAnimalMilkedId(animalId);
    }
}
