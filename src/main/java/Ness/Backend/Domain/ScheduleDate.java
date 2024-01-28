package Ness.Backend.Domain;


import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDateTime;

@Embeddable
@Getter
public class ScheduleDate {
    private String time;
    private String date;

    public ScheduleDate(){

    }

    public ScheduleDate(String time, String date){
        this.time=time;
        this.date=date;
    }
}
