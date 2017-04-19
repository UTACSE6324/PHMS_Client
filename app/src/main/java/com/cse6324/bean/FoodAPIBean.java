package com.cse6324.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jarvis on 2017/2/26.
 */

public class FoodAPIBean {
    int total;
    int max_score;
    List<FoodResultBean> hits;


    public List<FoodResultBean.FoodBean> getResult(){
        List<FoodResultBean.FoodBean> list = new ArrayList<>();

        if(hits != null)
            for(int i = 0; i < hits.size(); i++){
                list.add(hits.get(i).getFields());
            }

        return list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getMax_score() {
        return max_score;
    }

    public void setMax_score(int max_score) {
        this.max_score = max_score;
    }

    public List<FoodResultBean> getHits() {
        return hits;
    }

    public void setHits(List<FoodResultBean> hits) {
        this.hits = hits;
    }

    public class FoodResultBean{
        String _index;
        String _type;
        String _id;
        String _score;
        FoodBean fields;

        public String get_index() {
            return _index;
        }

        public void set_index(String _index) {
            this._index = _index;
        }

        public String get_type() {
            return _type;
        }

        public void set_type(String _type) {
            this._type = _type;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String get_score() {
            return _score;
        }

        public void set_score(String _score) {
            this._score = _score;
        }


        public FoodBean getFields() {
            return fields;
        }

        public void setFields(FoodBean fields) {
            this.fields = fields;
        }

        public class FoodBean{
            String item_name;
            String nf_calories;


            public String getItem_name() {
                return item_name;
            }

            public void setItem_name(String item_name) {
                this.item_name = item_name;
            }
            public String getNf_calories() {
                return nf_calories;
            }

            public void setNf_calories(String nf_calories) {
                this.nf_calories = nf_calories;
            }
        }
    }

}
