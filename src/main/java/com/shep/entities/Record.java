package com.shep.entities;


import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Entity
@Table(name = "records")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Transient
    private Long duration;

    public Long getDuration() {
        if (endTime != null) {
            Instant start = startTime.atZone(ZoneId.systemDefault()).toInstant();
            Instant end = endTime.atZone(ZoneId.systemDefault()).toInstant();
            return ChronoUnit.MILLIS.between(start, end);
        }
        return null;
    }
}
