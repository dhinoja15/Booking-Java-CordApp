package com.Booking.contracts;

import com.Booking.states.BookingState;
import com.sun.istack.NotNull;
import net.corda.core.contracts.Command;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.contracts.ContractState;
import net.corda.core.transactions.LedgerTransaction;
import org.dom4j.IllegalAddException;

import java.awt.*;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.time.MonthDay.now;
import static net.corda.core.contracts.ContractsDSL.requireThat;

// ************
// * Contract *
// ************
public class BookingContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String BOOKING_CONTRACT_ID = "com.Booking.contracts.BookingContract";

    // A transaction is valid if the verify() function of the contract of all the transaction's input and output states
    // does not throw an exception.
    @Override
    public void verify(@NotNull final LedgerTransaction tx) {
        if(tx.getCommands().size() != 1){
            throw new IllegalArgumentException("Command input can be only one") ;
        }

        Command command = tx.getCommand(0);
        CommandData commandType = command.getValue();
        List<PublicKey> reqSigners = command.getSigners();
        //System.out.println("command "+ command);
        //System.out.println("commandType = " + commandType);
        //System.out.println("Required Signer " + reqSigners);

        if(commandType instanceof Commands.Booking){

            //            //Shape Rule

            if(!(tx.getInputStates().size() == 0)) {
                throw new IllegalArgumentException("No input in Booking ");
            }
            if(!(tx.getOutputs().size() == 1)) {
                throw new IllegalAddException("There can be only one output");
            }
            //    Content Rules

            ContractState outputState = tx.getOutput(0);

            if (!(outputState instanceof BookingState )) {
                throw new IllegalArgumentException("Output state has to be of TemplateState");
            }
            BookingState bookState = (BookingState) outputState;
            if((bookState.getHotelHeaven() == bookState.getBookYourStay() )){
                throw new IllegalArgumentException("Both parties can't be same") ;
            }

            //System.out.println("Official Room Rent : " + bookState.getRoomRate());
            //System.out.println("Credit Card Amount :  " + bookState.getCreditCardAmount());

            // 1	Customer Age should be greater than 18.
            // 2	Check Out date should be greater than Check in date.
            // 5	After commission price should 85% of Original room price.
            //6 	Credit Card number length should be 16.
            //7 	Credit Card Exp date should not be in past.
            Date today = Calendar.getInstance().getTime();
            requireThat(require -> {
                //System.out.println("Ageeee   :  " + bookState.getRoomRate());
                require.using("Customer's age should be more than 18", bookState.getCustAge()>= 18);
                require.using( "Check out date should be after check in date", bookState.getCheckOutDate().after(bookState.getCheckInDate()));
                require.using("Check in date should be today or future date",bookState.getCheckInDate().after(today));
                require.using("Check out date should be future date",bookState.getCheckOutDate().after(today) );
                require.using("Room Type Must from K || NK || DD || NDD",
                        bookState.getRoomType().equals("N")||bookState.getRoomType().equals("NK")
                                ||bookState.getRoomType().equals("DD")||bookState.getRoomType().equals("NDD"));
                require.using("5\tAfter commission price should 85% of Original room price.",bookState.getCreditCardAmount() == (bookState.getRoomRate())*0.85);
                //require.using("Credit card number is invalid", bookState.getCreditCardNumber().length() == 16);
                require.using("Credit card EXP Date is Invalid", bookState.getCreditCardExpDate().after(today) );
                return null;
            });


            //    Signer Rules

            PublicKey BookYourStayPartyKey = bookState.getBookYourStay().getOwningKey();

            if ((!reqSigners.contains(BookYourStayPartyKey))) {
                throw new IllegalArgumentException("BookYourStay party must sign the transaction");
           }
        }
    }

    // Used to indicate the transaction's intent.
    public interface Commands extends CommandData {
        class Booking implements Commands {}
    }
    //public static class Booking implements CommandData {
    //}
}
