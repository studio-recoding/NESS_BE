package Ness.Backend.domain;


import jakarta.persistence.Embeddable;
import lombok.Getter;

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
