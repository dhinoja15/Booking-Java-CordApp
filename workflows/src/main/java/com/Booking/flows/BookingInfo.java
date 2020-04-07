package com.Booking.flows;

import com.Booking.contracts.BookingContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;
import net.corda.core.serialization.CordaSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

//import io.netty.util.concurrent.AbstractFuture;

// *********
// * State *
// *********
@CordaSerializable
public class BookingInfo {
    private final String custName;
    private final int custAge;
    private final Date checkInDate;
    private final Date checkOutDate;
    private final String roomType;
    private final int roomRate;
    private final String creditCardNumber;
    private final Date creditCardExpDate;
    private final double creditCardAmount;

    public BookingInfo(String custName, int custAge, Date checkInDate, Date checkOutDate, String roomType, int roomRate, String creditCardNumber, Date creditCardExpDate, double creditCardAmount) {
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

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public Date getCreditCardExpDate() {
        return creditCardExpDate;
    }

    public double getCreditCardAmount() {
        return creditCardAmount;
    }


}