package com.sasoop.server.service;

import com.sasoop.server.domain.locations.Locations;
import com.sasoop.server.domain.locations.LocationsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationsRepository locationsRepository;
    public void saveLocation(String location, String address, double latitude, double longitude){
        locationsRepository.findByLatitudeAndLongitude(latitude, longitude)
                .orElseGet(() -> locationsRepository.save(Locations.toEntity(location, address, latitude, longitude)));
    }
}
