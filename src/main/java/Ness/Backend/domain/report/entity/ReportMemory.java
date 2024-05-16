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
public class ReportMemory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_memory_id")
    private Long id;

    private ZonedDateTime createdDate;

    private String memory;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ReportMemory(ZonedDateTime createdDate, String memory, Member member){
        this.createdDate = createdDate;
        this.memory = memory;
        this.member = member;

    }
}
