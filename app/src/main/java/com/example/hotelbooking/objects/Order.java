package com.example.hotelbooking.objects;

public class Order {
	long adult_bed;
	long child_bed;
	int status;
	long check_in, check_out, order_id, price_total;
	String hotel_name;
	String images_id;
	String order_loc;

	public String getOrder_loc() {
		return order_loc;
	}

	public Order setOrder_loc(String order_loc) {
		this.order_loc = order_loc;
		return this;
	}

	public String getImages_id() {
		return images_id;
	}

	public Order setImages_id(String images_id) {
		this.images_id = images_id;
		return this;
	}


	public long getAdult_bed() {
		return adult_bed;
	}

	public Order setAdult_bed(Long adult_bed) {
		this.adult_bed = adult_bed;
		return this;
	}

	public long getChild_bed() {
		return child_bed;
	}

	public Order setChild_bed(Long child_bed) {
		this.child_bed = child_bed;
		return this;
	}

	public int getStatus() {
		return status;
	}

	public Order setStatus(int status) {
		this.status = status;
		return this;
	}

	public long getCheck_in() {
		return check_in;
	}

	public Order setCheck_in(long check_in) {
		this.check_in = check_in;
		return this;
	}

	public long getCheck_out() {
		return check_out;
	}

	public Order setCheck_out(long check_out) {
		this.check_out = check_out;
		return this;
	}

	public long getOrder_id() {
		return order_id;
	}

	public Order setOrder_id(long order_id) {
		this.order_id = order_id;
		return this;
	}

	public long getPrice_total() {
		return price_total;
	}

	public Order setPrice_total(long price_total) {
		this.price_total = price_total;
		return this;
	}

	public String getHotel_name() {
		return hotel_name;
	}

	public Order setHotel_name(String hotel_name) {
		this.hotel_name = hotel_name;
		return this;
	}


}
