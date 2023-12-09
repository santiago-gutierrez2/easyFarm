package es.udc.paproject.backend.model.services;

import es.udc.paproject.backend.model.entities.*;
import es.udc.paproject.backend.model.exceptions.InstanceNotFoundException;
import es.udc.paproject.backend.model.exceptions.PermissionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class WeighingServiceImpl implements WeighingService{

    @Autowired
    private WeighingDao weighingDao;
    @Autowired
    private UserDao userDao;

    @Override
    public void createWeighing(Weighing weighing) throws InstanceNotFoundException {
        weighingDao.save(weighing);
    }

    @Override
    public void updateWeighing(Long id, Integer kilos) throws InstanceNotFoundException {
        Optional<Weighing> optionalWeighing = weighingDao.findById(id);

        if (optionalWeighing.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.weighing", id);
        }

        Weighing weighing = optionalWeighing.get();
        weighing.setKilos(kilos);

        weighingDao.save(weighing);
    }

    @Override
    public void deleteWeighing(Long userId, Long id) throws InstanceNotFoundException, PermissionException {
        Optional<Weighing> optionalWeighing = weighingDao.findById(id);
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalWeighing.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.weighing", id);
        } else if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        } else if (optionalUser.get().getRole() == User.RoleType.EMPLOYEE && !Objects.equals(optionalUser.get().getId(), optionalWeighing.get().getMadeBy().getId())) {
            throw new PermissionException();
        }

        weighingDao.delete(optionalWeighing.get());
    }

    @Override
    public Weighing getWeighingById(Long id) throws InstanceNotFoundException {
        Optional<Weighing> optionalWeighing = weighingDao.findById(id);

        if (optionalWeighing.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.weighing", id);
        }

        return optionalWeighing.get();
    }

    @Override
    public Block<Weighing> getAllWeighings(Long userId, Long animalId, Integer startKilos, Integer endKilos, String startDate, String endDate, int page, int size) throws InstanceNotFoundException {
        Optional<User> optionalUser = userDao.findById(userId);

        if (optionalUser.isEmpty()) {
            throw new InstanceNotFoundException("project.entities.user", userId);
        }

        User user = optionalUser.get();
        Farm farm = user.getFarm();

        Slice<Weighing> weighingSlice;
        if (animalId != null || startKilos != null || endKilos != null || startDate != null || endDate != null) {
            weighingSlice = weighingDao.find(farm.getId(), animalId, startKilos, endKilos, startDate, endDate, page, size);
        } else {
            weighingSlice = weighingDao.findByAnimalWeighedBelongsToIdOrderByDateDesc(farm.getId(), PageRequest.of(page,size));
        }

        return new Block<>(weighingSlice.getContent(), weighingSlice.hasNext());

    }
}
