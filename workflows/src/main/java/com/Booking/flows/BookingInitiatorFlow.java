package com.Booking.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.Booking.contracts.BookingContract;
import com.Booking.states.BookingState;
import com.sun.xml.bind.v2.model.core.ID;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;
import static com.Booking.contracts.BookingContract.ID;


import java.util.Date;

// ******************
// * Initiator flow *
// ******************
@InitiatingFlow
@StartableByRPC
public class BookingInitiatorFlow extends FlowLogic<SignedTransaction> {
    private final String custName;
    private final int custAge;
    private final Date checkInDate;
    private final Date checkOutDate;
    private final String roomType;
    private final int roomRate;
    private final long creditCardNumber;
    private final Date creditCardExpDate;
    private final double creditCardAmount;
    private final Party HotelHeaven;


    private final ProgressTracker progressTracker = new ProgressTracker();

    public BookingInitiatorFlow(String custName, int custAge, Date checkInDate, Date checkOutDate, String roomType, int roomRate, long creditCardNumber, Date creditCardExpDate, double creditCardAmount, Party hotelHeaven) {
        this.custName = custName;
        this.custAge = custAge;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.roomType = roomType;
        this.roomRate = roomRate;
        this.creditCardNumber = creditCardNumber;
        this.creditCardExpDate = creditCardExpDate;
        this.creditCardAmount = creditCardAmount;
        HotelHeaven = hotelHeaven;
    }

    @Override
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }

    @Suspendable
    @Override
    public  SignedTransaction call() throws FlowException {
        // Initiator flow logic goes here.
        if (getOurIdentity().getName().getOrganisation().equals("BookYourStay")) {
            System.out.println("Identity Verified!");
        } else {
            throw new FlowException("Booking Request not initiated by BookYourStay");
        }
        //        Get Notary identity from network map

        Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

//        Create the elements for a transaction (Input/ Output states)

         BookingState outputState = new BookingState(custName,custAge,checkInDate,checkOutDate,roomType,roomRate,creditCardNumber,creditCardExpDate,creditCardAmount, getOurIdentity(),HotelHeaven);

//        Transactions in Corda are built using Transaction Builder and elements are added to it

        TransactionBuilder txBuilder = new TransactionBuilder(notary)
                .addOutputState(outputState,ID)
                .addCommand(new BookingContract.Booking(),getOurIdentity().getOwningKey());

        //        Sign the Transaction

        SignedTransaction BookingReqTxn = getServiceHub().signInitialTransaction(txBuilder);

//        Create a session with Bank

        FlowSession bookingReqSession = initiateFlow(HotelHeaven);
//        Finalize the transaction

        return subFlow(new FinalityFlow(BookingReqTxn, bookingReqSession));
    }
}
