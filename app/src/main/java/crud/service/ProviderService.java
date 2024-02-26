package crud.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crud.entity.ProviderEntity;
import crud.repository.ProviderRepository;
import crud.utils.DepthLevel;

/**
 * Service class for managing providers in the system.
 */
@Service
public class ProviderService {

    private final ProviderRepository providerRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProviderService.class);

    /**
     * Constructs a new ProviderService with the specified ProviderRepository.
     *
     * @param providerRepository The repository for managing provider entities.
     */
    @Autowired
    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    /**
     * Saves a new provider.
     *
     * @param provider The provider to be saved.
     * @return The saved provider entity.
     */
    public ProviderEntity createProvider(ProviderEntity provider) {
        logger.info("Creating a new provider: {}", provider);
        ProviderEntity savedProvider = providerRepository.save(provider);
        logger.info("Provider created successfully. Details: {}", savedProvider);
        return savedProvider;
    }

    /**
     * Retrieves all providers with the specified depth level.
     *
     * @param depth The depth level for retrieving associated entities.
     * @return List of all providers.
     */
    public List<ProviderEntity> getAllProviders(DepthLevel depth) {
        logger.info("Retrieving all providers with depth level: {}", depth);
        List<ProviderEntity> providers = providerRepository.findAll(depth);
        logger.info("Retrieved {} providers.", providers.size());
        return providers;
    }

    /**
     * Retrieves a provider by its ID with the specified depth level.
     *
     * @param id    The unique identifier of the provider.
     * @param depth The depth level for retrieving associated entities.
     * @return The provider entity if found, otherwise null.
     */
    public ProviderEntity getProviderById(Long id, DepthLevel depth) {
        logger.info("Retrieving provider by ID: {} with depth level: {}", id, depth);
        Optional<ProviderEntity> optionalProvider = providerRepository.findById(id, depth);

        if (optionalProvider.isPresent()) {
            ProviderEntity foundProvider = optionalProvider.get();
            logger.info("Provider found: {}", foundProvider);
            return foundProvider;
        } else {
            logger.info("Provider not found for ID: {}", id);
            return null;
        }
    }

    /**
     * Updates a provider with the specified ID.
     *
     * @param id              The unique identifier of the provider to be updated.
     * @param updatedProvider The updated provider entity.
     * @return The updated provider entity if found, otherwise null.
     */
    public ProviderEntity updateProvider(Long id, ProviderEntity updatedProvider) {
        logger.info("Updating provider with ID: {}", id);

        Optional<ProviderEntity> optionalProvider = providerRepository.findById(id, DepthLevel.MEDIUM);

        if (optionalProvider.isPresent()) {
            ProviderEntity existingProvider = optionalProvider.get();
            existingProvider.setName(updatedProvider.getName());
            // Add more fields to update if needed
            ProviderEntity updatedEntity = providerRepository.update(existingProvider);

            if (updatedEntity != null) {
                logger.info("Provider updated successfully. Updated details: {}", updatedEntity);
            } else {
                logger.error("Failed to update provider. Repository returned null.");
            }

            return updatedEntity;
        } else {
            logger.warn("Provider with ID {} not found for update.", id);
            return null; // Provider not found
        }
    }

    /**
     * Deletes a provider by its ID.
     *
     * @param id The unique identifier of the provider to be deleted.
     */
    public void deleteProvider(Long id) {
        logger.info("Deleting provider with ID: {}", id);
        providerRepository.delete(id);
        logger.info("Provider deleted successfully.");
    }
}
