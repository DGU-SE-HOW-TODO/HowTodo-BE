package com.barbet.howtodobe.domain.nowCategory.domain;

import com.barbet.howtodobe.domain.statistic.domain.Statistic;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statistic_id", nullable = false)
    private Statistic statistic;

    private String nowCategory;
    private Integer nowCategoryRate;

    public NowCategory(String nowCategory, Integer nowCategoryRate) {
        this.id = id;
        this.nowCategory = nowCategory;
        this.nowCategoryRate = nowCategoryRate;
    }
}
