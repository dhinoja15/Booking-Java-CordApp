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
import java.util.List;

// ************
// * Contract *
// ************
public class BookingContract implements Contract {
    // This is used to identify our contract when building a transaction.
    public static final String ID = "com.Booking.contracts.BookingContract";

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
        System.out.println("command "+ command);
        System.out.println("commandType = " + commandType);
        System.out.println("Required Signer " + reqSigners);

        if(commandType instanceof Booking ){

            //            //Shape Rule

            if(tx.getInputStates().size() != 0) {
                throw new IllegalArgumentException("No input in Booking ");
            }
            if(tx.getOutputs().size() != 1) {
                throw new IllegalAddException("There can be only one output");
            }
            //    Content Rules

            ContractState outputState = tx.getOutput(0);

            if (!(outputState instanceof BookingState )) {
                throw new IllegalArgumentException("Output state has to be of TemplateState");
            }
            BookingState bookState = (BookingState) outputState;
            System.out.println("Official Room Rent : " + bookState.getRoomRate());
            System.out.println("Credit Card Amount :  " + bookState.getCreditCardAmount());
            //    Signer Rules

            PublicKey BookYourStayPartyKey = bookState.getBookYourStay().getOwningKey();

            if ((!reqSigners.contains(BookYourStayPartyKey))) {
                throw new IllegalArgumentException("BookYourStay party must sign the transaction");
           }
        }
    }

    // Used to indicate the transaction's intent.
    public static class Booking implements CommandData {
    }
    // Used to indicate the transaction's intent.
        public interface Commands extends CommandData {
            class Action implements Commands {
            }
        }
    }
