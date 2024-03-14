package Ness.Backend.domain.report.entity;

import Ness.Backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
public class ReportTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tagTitle;

    private String tagDesc;

    private ZonedDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
