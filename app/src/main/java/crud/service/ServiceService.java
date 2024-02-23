package crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crud.entity.ServiceEntity;
import crud.repository.ServiceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceService {
    private final ServiceRepository serviceRepository;

    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    // Save a new service
    public ServiceEntity createService(ServiceEntity service) {
        return serviceRepository.save(service);
    }

    // Get all services
    public List<ServiceEntity> getAllServices() {
        return serviceRepository.findAll();
    }

    // Get service by ID
    public ServiceEntity getServiceById(Long id) {
        Optional<ServiceEntity> optionalService = serviceRepository.findById(id);
        return optionalService.orElse(null);
    }

    // Update service by ID
    public ServiceEntity updateService(Long id, ServiceEntity updatedService) {
        Optional<ServiceEntity> optionalService = serviceRepository.findById(id);

        if (optionalService.isPresent()) {
            ServiceEntity existingService = optionalService.get();
            existingService.setName(updatedService.getName());
            // Add more fields to update if needed
            return serviceRepository.save(existingService);
        } else {
            return null; // Service not found
        }
    }

    // Delete service by ID
    public void deleteService(Long id) {
        serviceRepository.delete(id);
    }
}
