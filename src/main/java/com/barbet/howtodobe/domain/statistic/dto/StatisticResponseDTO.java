package com.barbet.howtodobe.domain.statistic.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class StatisticResponseDTO {

    /** 투두 달성률 통계 */
    public static class WeekAchievement {

        private Integer prevTodoCnt;
        private Integer prevTodoDoneCnt;
        private Integer nowTodoCnt;
        private Integer nowTodoDoneCnt;
        private Integer rateOfChange;

        public WeekAchievement (Integer prevTodoCnt,
                                Integer prevTodoDoneCnt,
                                Integer nowTodoCnt,
                                Integer nowTodoDoneCnt,
                                Integer rateOfChange) {
            this.prevTodoCnt = prevTodoCnt;
            this.prevTodoDoneCnt = prevTodoDoneCnt;
            this.nowTodoCnt = nowTodoCnt;
            this.nowTodoDoneCnt = nowTodoDoneCnt;
            this.rateOfChange = rateOfChange;

        }
    }

    /** 대분류 통계 */
    public static class WeekCategory {

        private List<NowCategoryData> nowCategoryData;
        private String nowBestCategory;

        public static class NowCategoryData {

            private String nowCategory;
            private Integer nowCategoryRate;

            public NowCategoryData (String nowCategory, Integer nowCategoryRate) {
                this.nowCategory = nowCategory;
                this.nowCategoryRate = nowCategoryRate;
            }
        }

        public WeekCategory (List<NowCategoryData> nowCategoryData,
                                          String nowBestCategory) {
            this.nowCategoryData = nowCategoryData;
            this.nowBestCategory = nowBestCategory;
        }
    }
    
    /** 실패태그 통계 */
    public static class WeekFailtag {

        private List<NowFailtagData> nowFailtagData;
        private String nowWorstFailtag;

        public static class NowFailtagData {

            private String nowFailtag;
            private Integer nowFailtagRate;

            public NowFailtagData (String nowFailtag, Integer nowFailtagRate) {
                this.nowFailtag = nowFailtag;
                this.nowFailtagRate = nowFailtagRate;
            }
        }

        public WeekFailtag (List<NowFailtagData> nowFailtagData,
                             String nowWorstFailtag) {
            this.nowFailtagData = nowFailtagData;
            this.nowWorstFailtag = nowWorstFailtag;
        }
    }
}
