package crud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import crud.entity.ProviderEntity;
import crud.entity.ServiceEntity;
import crud.service.ProviderService;
import crud.service.ServiceService;
import crud.utils.DepthLevel;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/providers")
@Api(tags = "Provider Management", description = "APIs for managing providers")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @Autowired
    private ServiceService serviceService;

    @PostMapping
    @ApiOperation(value = "Create a new provider")
    public ProviderEntity createProvider(@RequestBody ProviderEntity provider) {
        return providerService.createProvider(provider);
    }

    @GetMapping
    @ApiOperation(value = "Get all providers")
    public List<ProviderEntity> getAllProviders(
            @ApiParam(value = "Depth level for fetching related entities", defaultValue = "shallow") @RequestParam(defaultValue = "shallow") String depth) {
        DepthLevel depthLevel = DepthLevel.fromString(depth);
        return providerService.getAllProviders(depthLevel);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get provider by ID")
    public ProviderEntity getProviderById(
            @ApiParam(value = "Provider ID", required = true) @PathVariable Long id,
            @ApiParam(value = "Depth level for fetching related entities", defaultValue = "shallow") @RequestParam(defaultValue = "shallow") String depth) {
        DepthLevel depthLevel = DepthLevel.fromString(depth);
        return providerService.getProviderById(id, depthLevel);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update provider by ID")
    public ProviderEntity updateProvider(
            @ApiParam(value = "Provider ID", required = true) @PathVariable Long id,
            @RequestBody ProviderEntity provider) {
        return providerService.updateProvider(id, provider);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete provider by ID")
    public void deleteProvider(
            @ApiParam(value = "Provider ID", required = true) @PathVariable Long id) {
        providerService.deleteProvider(id);
    }

    @PostMapping("/{providerId}/services/{serviceId}")
    @ApiOperation(value = "Add service to provider")
    public ProviderEntity addServiceToProvider(
            @ApiParam(value = "Provider ID", required = true) @PathVariable Long providerId,
            @ApiParam(value = "Service ID", required = true) @PathVariable Long serviceId) {
        ProviderEntity provider = providerService.getProviderById(providerId, DepthLevel.MEDIUM);
        ServiceEntity service = serviceService.getServiceById(serviceId, DepthLevel.SHALLOW);

        provider.addService(service);
        return providerService.updateProvider(providerId, provider);
    }

    @DeleteMapping("/{providerId}/services/{serviceId}")
    @ApiOperation(value = "Remove service from provider")
    public ProviderEntity removeServiceFromProvider(
            @ApiParam(value = "Provider ID", required = true) @PathVariable Long providerId,
            @ApiParam(value = "Service ID", required = true) @PathVariable Long serviceId) {
        ProviderEntity provider = providerService.getProviderById(providerId, DepthLevel.MEDIUM);
        ServiceEntity service = serviceService.getServiceById(serviceId, DepthLevel.SHALLOW);

        provider.removeService(service);
        return providerService.updateProvider(providerId, provider);
    }
}
