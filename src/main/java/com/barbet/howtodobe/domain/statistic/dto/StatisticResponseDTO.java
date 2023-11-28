package com.barbet.howtodobe.domain.statistic.dto;

import com.barbet.howtodobe.domain.nowCategory.domain.NowCategory;
import com.barbet.howtodobe.domain.nowFailtag.domain.NowFailtag;
import lombok.Getter;

import java.util.List;

@Getter
public class StatisticResponseDTO {

    /** 투두 통계 */
    private Integer prevTodoCnt;
    private Integer prevTodoDoneCnt;
    private Integer nowTodoCnt;
    private Integer nowTodoDoneCnt;
    private Integer rateOfChange;

    /** 대분류 통계 */
    private List<NowCategory> nowCategoryDate;
    private String nowBestCategory;

    /** 실패태그 통계 */
    private List<NowFailtag> nowFailtagList;
    private String nowWorstFailtag;

    public StatisticResponseDTO (Integer prevTodoCnt,
                                 Integer prevTodoDoneCnt,
                                 Integer nowTodoCnt,
                                 Integer nowTodoDoneCnt,
                                 Integer rateOfChange,
                                 List<NowCategory> nowCategoryDate,
                                 String nowBestCategory,
                                 List<NowFailtag> nowFailtagList,
                                 String nowWorstFailtag) {
        this.prevTodoCnt = prevTodoCnt;
        this.prevTodoDoneCnt = prevTodoDoneCnt;
        this.nowTodoCnt = nowTodoCnt;
        this.nowTodoDoneCnt = nowTodoDoneCnt;
        this.rateOfChange = rateOfChange;
        this.nowCategoryDate = nowCategoryDate;
        this.nowBestCategory = nowBestCategory;
        this.nowFailtagList = nowFailtagList;
        this.nowWorstFailtag = nowWorstFailtag;
    }
}
