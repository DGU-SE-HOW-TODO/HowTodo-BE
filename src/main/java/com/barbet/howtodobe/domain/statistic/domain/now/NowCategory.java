package com.barbet.howtodobe.domain.statistic.domain.now;

import com.barbet.howtodobe.domain.statistic.domain.Statistic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NowCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statistic_id", nullable = false)
    @JsonIgnore
    private Statistic statistic;

    private String nowCategory;
    private Integer nowCategoryRate;
}
