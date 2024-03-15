package Ness.Backend.domain.report.entity;

import Ness.Backend.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Data;

import java.time.ZonedDateTime;

@Entity
@Data
public class ReportMemory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_memory_id")
    private Long id;

    private ZonedDateTime createdDate;

    private String pictureUrl;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
