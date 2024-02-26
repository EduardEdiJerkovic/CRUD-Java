package crud.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import crud.entity.ServiceEntity;
import crud.entity.ProviderEntity;
import crud.service.ServiceService;
import crud.service.ProviderService;
import crud.utils.DepthLevel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/services")
@Api(tags = "Service Management", description = "APIs for managing services")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ProviderService providerService;

    @PostMapping
    @ApiOperation(value = "Create a new service")
    public ServiceEntity createService(
            @ApiParam(value = "Service data", required = true) @RequestBody ServiceEntity service) {
        return serviceService.createService(service);
    }

    @GetMapping
    @ApiOperation(value = "Get all services")
    public List<ServiceEntity> getAllServices(
            @ApiParam(value = "Depth level for fetching related entities", defaultValue = "shallow") @RequestParam(defaultValue = "shallow") String depth) {
        DepthLevel depthLevel = DepthLevel.fromString(depth);
        return serviceService.getAllServices(depthLevel);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get service by ID")
    public ServiceEntity getServiceById(
            @ApiParam(value = "Service ID", required = true) @PathVariable Long id,
            @ApiParam(value = "Depth level for fetching related entities", defaultValue = "shallow") @RequestParam(defaultValue = "shallow") String depth) {
        DepthLevel depthLevel = DepthLevel.fromString(depth);
        return serviceService.getServiceById(id, depthLevel);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update service by ID")
    public ServiceEntity updateService(
            @ApiParam(value = "Service ID", required = true) @PathVariable Long id,
            @ApiParam(value = "Updated service data", required = true) @RequestBody ServiceEntity service) {
        return serviceService.updateService(id, service);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete service by ID")
    public void deleteService(
            @ApiParam(value = "Service ID", required = true) @PathVariable Long id) {
        serviceService.deleteService(id);
    }

    @PostMapping("/{serviceId}/providers/{providerId}")
    @ApiOperation(value = "Add provider to service")
    public ServiceEntity addProviderToService(
            @ApiParam(value = "Service ID", required = true) @PathVariable Long serviceId,
            @ApiParam(value = "Provider ID", required = true) @PathVariable Long providerId) {
        ServiceEntity service = serviceService.getServiceById(serviceId, DepthLevel.MEDIUM);
        ProviderEntity provider = providerService.getProviderById(providerId, DepthLevel.SHALLOW);

        service.addProvider(provider);
        return serviceService.updateService(serviceId, service);
    }

    @DeleteMapping("/{serviceId}/providers/{providerId}")
    @ApiOperation(value = "Remove provider from service")
    public ServiceEntity removeProviderFromService(
            @ApiParam(value = "Service ID", required = true) @PathVariable Long serviceId,
            @ApiParam(value = "Provider ID", required = true) @PathVariable Long providerId) {
        ServiceEntity service = serviceService.getServiceById(serviceId, DepthLevel.MEDIUM);
        ProviderEntity provider = providerService.getProviderById(providerId, DepthLevel.SHALLOW);

        service.removeProvider(provider);
        return serviceService.updateService(serviceId, service);
    }
}
