package com.webservice.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Hafiz
 * @version 0.01
 */
@Entity
@Table(name = "t_reservations")
public class Reservation {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", nullable=false)
    @JsonIgnore
    private UUID id;

	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    @Column(name = "check_in_dt", insertable=true, updatable=false)
    private LocalDateTime checkInDate;

	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm")
    @Column(name = "check_out_dt", insertable=false, updatable=true)
    private LocalDateTime checkOutDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_code")
    private Customer customer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_code")
    private Room room;

    @Column(name="reservation_code", unique = true, nullable=false)
    private String reservationCode;

    @Column(name="cancellation", columnDefinition="char(1)")
    private String cancellation;

    @PrePersist
    protected void onCreate() {
    	checkInDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
    	checkOutDate = LocalDateTime.now();
    }

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDateTime getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(LocalDateTime checkInDate) {
		this.checkInDate = checkInDate;
	}

	public LocalDateTime getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(LocalDateTime checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public String getReservationCode() {
		return reservationCode;
	}

	public void setReservationCode(String reservationCode) {
		this.reservationCode = reservationCode;
	}

	public String getCancellation() {
		return cancellation;
	}

	public void setCancellation(String cancellation) {
		this.cancellation = cancellation;
	}

}
