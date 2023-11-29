package com.barbet.howtodobe.domain.nowFailtag.domain;

import com.barbet.howtodobe.domain.statistic.domain.Statistic;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NowFailtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statistic_id", nullable = false)
    private Statistic statistic;

    private String nowFailtag;
    private Integer nowFailtagRate;

    public NowFailtag(String nowFailtag, Integer nowFailtagRate) {
        this.id = id;
        this.nowFailtag = nowFailtag;
        this.nowFailtagRate = nowFailtagRate;
    }

}
