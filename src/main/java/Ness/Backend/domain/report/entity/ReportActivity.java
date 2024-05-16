package Ness.Backend.domain.report.entity;

import Ness.Backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
public class ReportActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_activity_id")
    private Long id;

    private ZonedDateTime createdDate;

    private String activityText;

    private String imageTag;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ReportActivity(ZonedDateTime createdDate, String activityText, String imageTag, Member member) {
        this.createdDate = createdDate;
        this.activityText = activityText;
        this.member = member;
        this.imageTag = imageTag;
    }
}
