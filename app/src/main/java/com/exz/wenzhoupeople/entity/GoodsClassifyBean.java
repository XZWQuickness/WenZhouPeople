package com.exz.wenzhoupeople.entity;

import java.util.List;

public class GoodsClassifyBean {


    private String headerImg;
    private String price="";
    private String stock="";
    private String typeName;
    private String skuid="0";
    private String rankCombId="";

    public String getSkuid() {
        return skuid;
    }

    public String getRankCombId() {
        return rankCombId;
    }

    public void setRankCombId(String rankCombId) {
        this.rankCombId = rankCombId;
    }

    public void setSkuid(String skuid) {
        this.skuid = skuid;
    }

    public String getHeaderImg() {
        return headerImg;
    }

    public void setHeaderImg(String headerImg) {
        this.headerImg = headerImg;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    private List<RankInfoBean> rankInfo;
    private List<RankCombBean> rankComb;


    public List<RankInfoBean> getRankInfo() {
        return rankInfo;
    }

    public void setRankInfo(List<RankInfoBean> rankInfo) {
        this.rankInfo = rankInfo;
    }

    public List<RankCombBean> getRankComb() {
        return rankComb;
    }

    public void setRankComb(List<RankCombBean> rankComb) {
        this.rankComb = rankComb;
    }


    public static class RankInfoBean {
        /**
         * rankId : 1
         * rankName : 颜色
         * subRank : [{"rankId":"11","rankName":"红色"},{"rankId":"12","rankName":"蓝色"}]
         */

        private String rankId;
        private String rankName;
        private List<SubRankBean> subRank;

        public String getRankId() {
            return rankId;
        }

        public void setRankId(String rankId) {
            this.rankId = rankId;
        }

        public String getRankName() {
            return rankName;
        }

        public void setRankName(String rankName) {
            this.rankName = rankName;
        }

        public List<SubRankBean> getSubRank() {
            return subRank;
        }

        public void setSubRank(List<SubRankBean> subRank) {
            this.subRank = subRank;
        }

        public static class SubRankBean {
            /**
             * rankId : 11
             * rankName : 红色
             */

            private String rankId;
            private String rankName;
            private String state="2";
            private boolean check;

            public String getRankId() {
                return rankId;
            }

            public void setRankId(String rankId) {
                this.rankId = rankId;
            }

            public String getRankName() {
                return rankName;
            }

            public void setRankName(String rankName) {
                this.rankName = rankName;
            }

            public String getState() {
                return state;
            }

            public void setState(String state) {
                this.state = state;
            }

            public boolean isCheck() {
                return check;
            }

            public void setCheck(boolean check) {
                this.check = check;
            }


        }
    }

    public static class RankCombBean {
        /**
         * skuid : 1
         * rankCombId : 11,21
         * stock : 10
         * price : 48.8
         * image : http://123.png
         */

        private String skuid;
        private String rankCombId;
        private int stock;
        private String price;
        private String image;

        public String getSkuid() {
            return skuid;
        }

        public void setSkuid(String skuid) {
            this.skuid = skuid;
        }

        public String getRankCombId() {
            return rankCombId;
        }

        public void setRankCombId(String rankCombId) {
            this.rankCombId = rankCombId;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}