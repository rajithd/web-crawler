package com.crawler.service.impl;

import com.crawler.domain.Hotel;
import com.crawler.repository.HotelRepository;
import com.crawler.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Override
    public void save(Hotel hotel) {
        System.out.println("---------");
//        hotelRepository.save(hotel);
    }
}
