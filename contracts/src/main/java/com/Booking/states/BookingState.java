package com.Booking.states;

import com.Booking.contracts.BookingContract;
import com.Booking.schemas.BookingSchemaV1;
import com.Booking.schemas.PersistentBookingClass;
import com.google.common.collect.ImmutableList;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.schemas.MappedSchema;
import net.corda.core.schemas.PersistentState;
import net.corda.core.schemas.QueryableState;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

// *********
// * State *
// *********
@BelongsToContract(BookingContract.class)
public class BookingState implements ContractState,QueryableState {

    private final String custName;
    private final int custAge;
    private final Date checkInDate;
    private final Date checkOutDate;
    private final String roomType;
    private final int roomRate;
    private final String creditCardNumber;
    private final Date creditCardExpDate;
    private final double creditCardAmount;
    private final Party BookYourStay;
    private final Party HotelHeaven;


    public BookingState(String custName, int custAge, Date checkInDate, Date checkOutDate, String roomType, int roomRate, String creditCardNumber, Date creditCardExpDate, double creditCardAmount, Party bookYourStay, Party hotelHeaven) {
        this.custName = custName;
        this.custAge = custAge;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomType = roomType;
        this.roomRate = roomRate;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpDate = creditCardExpDate;
        this.creditCardAmount = creditCardAmount;
        BookYourStay = bookYourStay;
        HotelHeaven = hotelHeaven;
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

    @NotNull
    @Override
    public PersistentState generateMappedObject(@NotNull MappedSchema schemas) {
        int id;
        int numInstances=0;

        if(schemas instanceof BookingSchemaV1) {

            return new PersistentBookingClass(

                    id = ++numInstances,
                    this.custName,
                    this.custAge,
                    this.checkInDate,
                    this.checkOutDate,
                    this.roomType,
                    this.roomRate,
                    this.creditCardNumber,
                    this.creditCardExpDate,
                    this.creditCardAmount

            );
        }
        else{
            throw new IllegalArgumentException("Unsupported Schema");
        }

    }
    @NotNull
    @Override
    public Iterable<MappedSchema> supportedSchemas() {
        return ImmutableList.of(new BookingSchemaV1());
    }
}