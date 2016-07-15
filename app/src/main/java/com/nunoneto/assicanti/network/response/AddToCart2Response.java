package com.nunoneto.assicanti.network.response;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by NB20301 on 15/07/2016.
 */
public class AddToCart2Response {

    private String currency;
    private String amendorderlink;
    private String button;
    private String currencyiso;
    private String currency_left;
    private String currency_right;
    private String itemsajax;
    private String nocheckout;
    private String orderpagelink;
    private String pricing_delivery;
    private int increase_decrease;
    private int shopopen;
    private int taxrate;
    private int taxrate_alt;
    private int taxrate_avg;
    private String tax_applied;

    @SerializedName("order_value")
    private OrderValue orderValue;

    public class OrderValue{

        @SerializedName("delivery_charges")
        private OrderItem deliveryCharges;

        @SerializedName("discount")
        private OrderItem discount;

        @SerializedName("item_tax")
        private OrderItem itemTax;

        @SerializedName("taxes_included")
        private OrderItem taxesIncluded;

        @SerializedName("total")
        private OrderItem total;

        @SerializedName("total_price_items")
        private OrderItem totalPriceItems;

        private HashMap<String,Item> items;

        public OrderItem getDeliveryCharges() {
            return deliveryCharges;
        }

        public OrderItem getDiscount() {
            return discount;
        }

        public OrderItem getItemTax() {
            return itemTax;
        }

        public OrderItem getTaxesIncluded() {
            return taxesIncluded;
        }

        public OrderItem getTotal() {
            return total;
        }

        public OrderItem getTotalPriceItems() {
            return totalPriceItems;
        }

        public HashMap<String, Item> getItems() {
            return items;
        }

        public class OrderItem{

            private String formatted;
            private String lbl;
            private String val;

            public String getFormatted() {
                return formatted;
            }

            public String getLbl() {
                return lbl;
            }

            public String getVal() {
                return val;
            }
        }
    }

    public class Item{

        private String blogid;
        private String catIdSelected;
        private String count;
        private String name;
        private String postId;
        private String price;
        private String price_formatted;
        private String pricetotal;
        private String size;
        private String taxrate;
        private List<Category> categories;

        public String getBlogid() {
            return blogid;
        }

        public String getCatIdSelected() {
            return catIdSelected;
        }

        public String getCount() {
            return count;
        }

        public String getName() {
            return name;
        }

        public String getPostId() {
            return postId;
        }

        public String getPrice() {
            return price;
        }

        public String getPrice_formatted() {
            return price_formatted;
        }

        public String getPricetotal() {
            return pricetotal;
        }

        public String getSize() {
            return size;
        }

        public String getTaxrate() {
            return taxrate;
        }

        public List<Category> getCategories() {
            return categories;
        }

        public class Category{

            private String count;
            private String description;
            private String filter;
            private String name;
            private String object_id;
            private String slug;
            private String taxonomy;
            private String term_group;
            private String term_id;
            private String term_taxonomy_id;

            public String getCount() {
                return count;
            }

            public String getDescription() {
                return description;
            }

            public String getFilter() {
                return filter;
            }

            public String getName() {
                return name;
            }

            public String getObject_id() {
                return object_id;
            }

            public String getSlug() {
                return slug;
            }

            public String getTaxonomy() {
                return taxonomy;
            }

            public String getTerm_group() {
                return term_group;
            }

            public String getTerm_id() {
                return term_id;
            }

            public String getTerm_taxonomy_id() {
                return term_taxonomy_id;
            }
        }

    }

}
