package org.example.hotelesapi;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "hoteles")
@Data
public class Hotel {
    @Id
    private String _id;
    private String categories;
    private String category_id;
    private String coord_x;
    private String coord_y;
    private String establishment_address;
    private String group;
    private String holder;
    private String identification_doc_num;
    private String mobile;
    private String modalities;
    private String municipalities;
    private String name;
    private String phone;
    private String postal_code;
    private String provinces;
    private String registration_code;
    private String road_name;
}
