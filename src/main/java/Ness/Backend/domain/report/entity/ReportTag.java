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
public class ReportTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_tag_id")
    private Long id;

    private String tagTitle;

    private String tagDesc;

    private ZonedDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public ReportTag(String tagTitle, String tagDesc, ZonedDateTime createdDate, Member member){
        this.tagTitle = tagTitle;
        this.tagDesc = tagDesc;
        this.createdDate = createdDate;
        this.member = member;
    }
}
