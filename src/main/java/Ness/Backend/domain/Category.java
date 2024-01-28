package Ness.Backend.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private String id;

    private String name;

    @OneToMany(mappedBy = "schedule")
    private List<Schedule> schedules = new ArrayList<>();
}
