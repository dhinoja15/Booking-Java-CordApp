package com.Booking.schemas;

import com.Booking.states.BookingState;
import net.corda.core.schemas.PersistentState;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;


@Entity
@Table(name = "BOOKING_DETAIL")
//@Access(value=AccessType.FIELD)
//@DiscriminatorValue("PersistentBookingClass")
public class PersistentBookingClass extends PersistentState implements Serializable {
  //  @Access(value=AccessType.PROPERTY)

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private final long  id;
    @Column
    private final String custName;
    @Column
    private final int custAge;
    @Column
    private final Date checkInDate;
    @Column
    private final Date checkOutDate;
    @Column
    private final String roomType;
    @Column
    private final int roomRate;
    @Column
    private final String creditCardNumber;
    @Column
    private final Date creditCardExpDate;
    @Column
    private final double creditCardAmount;


    public PersistentBookingClass(long id, String custName, int custAge, Date checkInDate, Date checkOutDate, String roomType, int roomRate, String creditCardNumber, Date creditCardExpDate, double creditCardAmount) {
        //super(id);
        this.id = id;
        this.custName = custName;
        this.custAge = custAge;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomType = roomType;
        this.roomRate = roomRate;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpDate = creditCardExpDate;
        this.creditCardAmount = creditCardAmount;
    }



  //  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public long getId(){return id;}
    @Column(name = "custName")
    public String getCustName() {
        return custName;
    }

    @Column(name = "custAge")
    public int getCustAge() {
        return custAge;
    }

    @Column(name = "checkInDate")
    public Date getCheckInDate() {
        return checkInDate;
    }

    @Column(name = "checkOutDate")
    public Date getCheckOutDate() {
        return checkOutDate;
    }

    @Column(name = "roomType")
    public String getRoomType() {
        return roomType;
    }

    @Column(name = "roomRate")
    public int getRoomRate() {
        return roomRate;
    }

    @Column(name = "creditCardNumber")
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    @Column(name = "creditCardExpDate")
    public Date getCreditCardExpDate() {
        return creditCardExpDate;
    }

    @Column(name = "creditCardAmount")
    public double getCreditCardAmount() {
        return creditCardAmount;
    }
}
