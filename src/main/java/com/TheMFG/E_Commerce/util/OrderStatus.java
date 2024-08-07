package com.TheMFG.E_Commerce.util;

public enum OrderStatus {
    IN_PROGRESS(1,"Sipariş Alındı"),
    ORDER_RECIVED(2,"Ürün Hazırlanıyor"),
    PRODUCT_PACKED(3,"Ürün Kargoya Verildi"),
    OUT_FOR_DELIVERY(4,"Kargo Dağıtıma Çıktı"),
    DELIVERED(5,"Teslim Edildi"),
    CANCEL(6,"İptal Edildi!"),
    SUCCESS(7, "Başarılı!");

    private Integer id;
    private String name;

    private OrderStatus(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
