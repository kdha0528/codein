package com.rainbowflavor.hdcweb.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Schedule extends AuditingTimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.DATE)
    private Date scheduleStartDate;

    @Temporal(TemporalType.DATE)
    private Date scheduleEndDate;

    @Column
    private String scheduleDetail;

    @Builder
    public Schedule(User user, Date scheduleStartDate, Date scheduleEndDate, String scheduleDetail) {
        this.user = user;
        this.scheduleStartDate = scheduleStartDate;
        this.scheduleEndDate = scheduleEndDate;
        this.scheduleDetail = scheduleDetail;
    }
}
