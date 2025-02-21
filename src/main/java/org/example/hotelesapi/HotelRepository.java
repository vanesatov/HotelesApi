package org.example.hotelesapi;

import org.springframework.data.mongodb.repository.MongoRepository;



import java.util.List;

public interface HotelRepository extends MongoRepository<Hotel, String> {


     public List<Hotel> findHotelesByProvinces(String provinces);

     public List<Hotel> findHotelesByModalities(String modalities);


}
