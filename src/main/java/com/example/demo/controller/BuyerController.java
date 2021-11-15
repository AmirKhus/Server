package com.example.demo.controller;

import com.example.demo.entity.Buyer;
import com.example.demo.repository.BuyerRepository;
import com.example.demo.repository.RegionRepository;
import com.example.demo.resource.BuyerResource;
import com.example.demo.resource.RegionResource;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping(value = "/buyer")
public class BuyerController {
    private final BuyerRepository buyerRepository;

    private final RegionRepository regionRepository;

    public BuyerController(BuyerRepository buyerRepository, RegionRepository regionRepository) {
        this.buyerRepository = buyerRepository;
        this.regionRepository = regionRepository;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    BuyerResource[] getAll(@RequestParam(required = false) Integer sourceId,
                           @RequestParam(required = false) Object expand) {
        Buyer[] entities = sourceId == null ?
                buyerRepository.select() :
                buyerRepository.selectByRegionId(sourceId);
        return Arrays.stream(entities)
                .map(entity -> {
                    BuyerResource resource = new BuyerResource(entity);
                    if (expand != null)
                        resource.setRegionResource(new RegionResource(
                                regionRepository.select(entity.getRegion_id()))
                        );
                    return resource;
                })
                .toArray(BuyerResource[]::new);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    BuyerResource get(@PathVariable Integer id,
                        @RequestParam(required = false) Object expand) {
        Buyer entity = buyerRepository.select(id);
        if (entity == null) return null;
        BuyerResource resource = new BuyerResource(entity);
        if (expand != null)
            resource.setRegionResource(
                    new RegionResource(regionRepository.select(entity.getRegion_id()))
            );
        return resource;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    BuyerResource post(@RequestBody BuyerResource resource) {
        Buyer entity = buyerRepository.insert(resource.toEntity());
        if (entity == null) return null;
        resource = new BuyerResource(entity);
        return resource;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    BuyerResource put(@PathVariable Integer id,
                        @RequestBody BuyerResource resource) {
        Buyer entity = buyerRepository.update(id, resource.toEntity());
        if (entity == null) return null;
        resource = new BuyerResource(entity);
        return resource;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    BuyerResource delete(@PathVariable Integer id) {
        Buyer entity = buyerRepository.delete(id);
        if (entity == null) return null;
        BuyerResource resource = new BuyerResource(entity);
        return resource;
    }
}
