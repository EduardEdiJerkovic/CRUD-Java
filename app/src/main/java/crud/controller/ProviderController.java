package crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import crud.entity.ProviderEntity;
import crud.entity.ServiceEntity;
import crud.service.ProviderService;
import crud.service.ServiceService;

import java.util.List;

@RestController
@RequestMapping("/providers")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @Autowired
    private ServiceService serviceService;

    // Create a new provider
    @PostMapping
    public ProviderEntity createProvider(@RequestBody ProviderEntity provider) {
        return providerService.createProvider(provider);
    }

    // Get all providers
    @GetMapping
    public List<ProviderEntity> getAllProviders() {
        return providerService.getAllProviders();
    }

    // Get provider by ID
    @GetMapping("/{id}")
    public ProviderEntity getProviderById(@PathVariable Long id) {
        return providerService.getProviderById(id);
    }

    // Update provider by ID
    @PutMapping("/{id}")
    public ProviderEntity updateProvider(@PathVariable Long id, @RequestBody ProviderEntity provider) {
        return providerService.updateProvider(id, provider);
    }

    // Delete provider by ID
    @DeleteMapping("/{id}")
    public void deleteProvider(@PathVariable Long id) {
        providerService.deleteProvider(id);
    }

    // Add service to provider
    @PostMapping("/{providerId}/services/{serviceId}")
    public ProviderEntity addServiceToProvider(
            @PathVariable Long providerId,
            @PathVariable Long serviceId) {
        ProviderEntity provider = providerService.getProviderById(providerId);
        ServiceEntity service = serviceService.getServiceById(serviceId);

        provider.addService(service);
        return providerService.updateProvider(providerId, provider);
    }

    // Remove service from provider
    @DeleteMapping("/{providerId}/services/{serviceId}")
    public ProviderEntity removeServiceFromProvider(
            @PathVariable Long providerId,
            @PathVariable Long serviceId) {
        ProviderEntity provider = providerService.getProviderById(providerId);
        ServiceEntity service = serviceService.getServiceById(serviceId);

        provider.removeService(service);
        return providerService.updateProvider(providerId, provider);
    }
}
