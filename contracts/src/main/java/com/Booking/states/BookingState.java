package com.Booking.states;

import com.Booking.contracts.BookingContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(BookingContract.class)
public class BookingState implements ContractState {
    private final String custName;
    private final int custAge;
    private final Date checkInDate;
    private final Date checkOutDate;
    private final String roomType;
    private final int roomRate;
    private final long creditCardNumber;
    private final Date creditCardExpDate;
    private final double creditCardAmount;
    private final Party BookYourStay;
    private final Party HotelHeaven;



    public BookingState(String custName, int custAge, Date checkInDate, Date checkOutDate, String roomType, int roomRate, long creditCardNumber, Date creditCardExpDate, double creditCardAmount, Party bookYourStay, Party hotelHeaven) {
        this.custName = custName;
        this.custAge = custAge;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomType = roomType;
        this.roomRate = roomRate;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpDate = creditCardExpDate;
        this.creditCardAmount = creditCardAmount;
        this.BookYourStay = bookYourStay;
        this. HotelHeaven = hotelHeaven;
    }

    public String getCustName() {
        return custName;
    }

    public int getCustAge() {
        return custAge;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getRoomRate() {
        return roomRate;
    }

    public long getCreditCardNumber() {
        return creditCardNumber;
    }

    public Date getCreditCardExpDate() {
        return creditCardExpDate;
    }

    public double getCreditCardAmount() {
        return creditCardAmount;
    }

    public Party getBookYourStay() {
        return BookYourStay;
    }

    public Party getHotelHeaven() {
        return HotelHeaven;
    }

    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(BookYourStay,HotelHeaven);
    }
}