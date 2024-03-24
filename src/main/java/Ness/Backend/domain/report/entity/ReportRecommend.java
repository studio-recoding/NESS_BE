package Ness.Backend.domain.report.entity;

import Ness.Backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
public class ReportRecommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private ZonedDateTime createdDate;

    private String recommendText;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
