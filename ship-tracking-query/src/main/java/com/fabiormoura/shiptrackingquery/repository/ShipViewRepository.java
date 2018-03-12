package com.fabiormoura.shiptrackingquery.repository;

import com.fabiormoura.shiptrackingquery.view.ShipView;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "ships", path = "ships")
public interface ShipViewRepository extends PagingAndSortingRepository<ShipView, String> {
}
