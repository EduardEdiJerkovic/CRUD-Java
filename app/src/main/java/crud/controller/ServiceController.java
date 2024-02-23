package crud.controller;

import java.security.Provider;
import java.security.Provider.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import crud.entity.ProviderEntity;
import crud.entity.ServiceEntity;
import crud.service.ProviderService;
import crud.service.ServiceService;

@RestController
@RequestMapping("/services")
public class ServiceController {
    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ProviderService providerService;

    // Create a new service
    @PostMapping
    public ServiceEntity createService(@RequestBody ServiceEntity service) {
        return serviceService.createService(service);
    }

    // Get all services
    @GetMapping
    public List<ServiceEntity> getAllServices() {
        return serviceService.getAllServices();
    }

    // Get service by ID
    @GetMapping("/{id}")
    public ServiceEntity getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id);
    }

    // Update service by ID
    @PutMapping("/{id}")
    public ServiceEntity updateService(@PathVariable Long id, @RequestBody ServiceEntity service) {
        return serviceService.updateService(id, service);
    }

    // Delete service by ID
    @DeleteMapping("/{id}")
    public void deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
    }

    // Add provider to service
    @PostMapping("/{serviceId}/providers/{providerId}")
    public ServiceEntity addProviderToService(
            @PathVariable Long serviceId,
            @PathVariable Long providerId) {
        ServiceEntity service = serviceService.getServiceById(serviceId);
        ProviderEntity provider = providerService.getProviderById(providerId);

        service.addProvider(provider);
        return serviceService.updateService(serviceId, service);
    }

    // Remove provider from service
    @DeleteMapping("/{serviceId}/providers/{providerId}")
    public ServiceEntity removeProviderFromService(
            @PathVariable Long serviceId,
            @PathVariable Long providerId) {
        ServiceEntity service = serviceService.getServiceById(serviceId);
        ProviderEntity provider = providerService.getProviderById(providerId);

        service.removeProvider(provider);
        return serviceService.updateService(serviceId, service);
    }
}
