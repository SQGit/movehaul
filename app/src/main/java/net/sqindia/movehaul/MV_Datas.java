package net.sqindia.movehaul;

/**
 * Created by SQINDIA on 12/9/2016.
 */

public class MV_Datas {


    String driver_id;
    String goods_type;
    String time;
    String date;
    String name;
    String driver_image;
    String truck_side;
    String truck_back;
    String truck_front;
    String damage_control;
    String truck_type;
    String bidding;


    public String getTruck_type() {
        return truck_type;
    }

    public String getBidding() {
        return bidding;
    }

    public String getDamage_control() {
        return damage_control;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public String getDriver_image() {
        return driver_image;
    }

    public String getTruck_back() {
        return truck_back;
    }

    public String getTruck_front() {
        return truck_front;
    }

    public String getTruck_side() {
        return truck_side;
    }

    public void setTruck_type(String truck_type) {
        this.truck_type = truck_type;
    }

    public void setBidding(String bidding) {
        this.bidding = bidding;
    }

    public void setDamage_control(String damage_control) {
        this.damage_control = damage_control;
    }

    public void setDriver_image(String driver_image) {
        this.driver_image = driver_image;
    }

    public void setTruck_back(String truck_back) {
        this.truck_back = truck_back;
    }

    public void setTruck_front(String truck_front) {
        this.truck_front = truck_front;
    }

    public void setTruck_side(String truck_side) {
        this.truck_side = truck_side;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }


    public String getGoods_type() {
        return goods_type;
    }

    public void setGoods_type(String goods_type) {
        this.goods_type = goods_type;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
