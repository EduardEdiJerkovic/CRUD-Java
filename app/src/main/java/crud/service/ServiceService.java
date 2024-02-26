package crud.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crud.entity.ServiceEntity;
import crud.repository.ServiceRepository;
import crud.utils.DepthLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing services in the system.
 */
@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private static final Logger logger = LoggerFactory.getLogger(ServiceService.class);

    /**
     * Constructs a new ServiceService with the specified ServiceRepository.
     *
     * @param serviceRepository The repository for managing service entities.
     */
    @Autowired
    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    /**
     * Saves a new service.
     *
     * @param service The service to be saved.
     * @return The saved service entity.
     */
    public ServiceEntity createService(ServiceEntity service) {
        logger.info("Creating a new service: {}", service);
        ServiceEntity savedService = serviceRepository.save(service);
        logger.info("Service created successfully. Details: {}", savedService);
        return savedService;
    }

    /**
     * Retrieves all services with the specified depth level.
     *
     * @param depth The depth level for retrieving associated entities.
     * @return List of all services.
     */
    public List<ServiceEntity> getAllServices(DepthLevel depth) {
        logger.info("Retrieving all services with depth level: {}", depth);
        List<ServiceEntity> services = serviceRepository.findAll(depth);
        logger.info("Retrieved {} services.", services.size());
        return services;
    }

    /**
     * Retrieves a service by its ID with the specified depth level.
     *
     * @param id    The unique identifier of the service.
     * @param depth The depth level for retrieving associated entities.
     * @return The service entity if found, otherwise null.
     */
    public ServiceEntity getServiceById(Long id, DepthLevel depth) {
        logger.info("Retrieving service by ID: {} with depth level: {}", id, depth);
        Optional<ServiceEntity> optionalService = serviceRepository.findById(id, depth);

        if (optionalService.isPresent()) {
            ServiceEntity foundService = optionalService.get();
            logger.info("Service found: {}", foundService);
            return foundService;
        } else {
            logger.info("Service not found for ID: {}", id);
            return null;
        }
    }

    /**
     * Updates a service with the specified ID.
     *
     * @param id             The unique identifier of the service to be updated.
     * @param updatedService The updated service entity.
     * @return The updated service entity if found, otherwise null.
     */
    public ServiceEntity updateService(Long id, ServiceEntity updatedService) {
        logger.info("Updating service with ID: {}", id);

        Optional<ServiceEntity> optionalService = serviceRepository.findById(id, DepthLevel.MEDIUM);

        if (optionalService.isPresent()) {
            ServiceEntity existingService = optionalService.get();
            existingService.setName(updatedService.getName());
            existingService.setProviders(updatedService.getProviders());

            ServiceEntity updatedEntity = serviceRepository.update(existingService);

            if (updatedEntity != null) {
                logger.info("Service updated successfully. Updated details: {}", updatedEntity);
            } else {
                logger.error("Failed to update service. Repository returned null.");
            }

            return updatedEntity;
        } else {
            logger.warn("Service with ID {} not found for update.", id);
            return null; // Service not found
        }
    }

    /**
     * Deletes a service by its ID.
     *
     * @param id The unique identifier of the service to be deleted.
     */
    public void deleteService(Long id) {
        logger.info("Deleting service with ID: {}", id);
        serviceRepository.delete(id);
        logger.info("Service deleted successfully.");
    }
}
