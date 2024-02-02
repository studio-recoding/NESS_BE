package Ness.Backend.domain;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class ScheduleDate {
    private String time;
    private String date;

    @Builder
    public ScheduleDate(String time, String date){
        this.time = time;
        this.date = date;
    }
}
