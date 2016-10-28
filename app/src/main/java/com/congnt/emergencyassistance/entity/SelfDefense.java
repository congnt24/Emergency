package com.congnt.emergencyassistance.entity;

import java.util.List;

/**
 * Created by congnt24 on 28/10/2016.
 */

public class SelfDefense {
    public List<ItemSelfDefense> data;

    public class ItemSelfDefense {

        public String title;
        public String img;
        public String description;

        @Override
        public String toString() {
            return "SelfDefenseObject{" +
                    "title='" + title + '\'' +
                    ", img='" + img + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }

}
