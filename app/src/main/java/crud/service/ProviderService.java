package crud.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import crud.entity.ProviderEntity;
import crud.repository.ProviderRepository;

@Service
public class ProviderService {
    private final ProviderRepository providerRepositry;

    @Autowired
    public ProviderService(ProviderRepository providerRepository) {
        this.providerRepositry = providerRepository;
    }

    // Save a new service
    public ProviderEntity createProvider(ProviderEntity service) {
        return providerRepositry.save(service);
    }

    // Get all services
    public List<ProviderEntity> getAllProviders() {
        return providerRepositry.findAll();
    }

    // Get service by ID
    public ProviderEntity getProviderById(Long id) {
        Optional<ProviderEntity> optionalService = providerRepositry.findById(id);
        return optionalService.orElse(null);
    }

    // Update service by ID
    public ProviderEntity updateProvider(Long id, ProviderEntity updatedProvider) {
        Optional<ProviderEntity> optionalProvider = providerRepositry.findById(id);

        if (optionalProvider.isPresent()) {
            ProviderEntity existingProvider = optionalProvider.get();
            existingProvider.setName(updatedProvider.getName());
            // Add more fields to update if needed
            return providerRepositry.save(existingProvider);
        } else {
            return null; // Service not found
        }
    }

    // Delete service by ID
    public void deleteProvider(Long id) {
        providerRepositry.delete(id);
    }
}
