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
public class ReportRecommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_recommend_id")
    private Long id;

    private ZonedDateTime createdDate;

    private String recommendText;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ReportRecommend(ZonedDateTime createdDate, String recommendText, Member member) {
        this.createdDate = createdDate;
        this.recommendText = recommendText;
        this.member = member;
    }
}
