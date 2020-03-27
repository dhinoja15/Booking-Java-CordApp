package com.Booking.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(BookingInitiatorFlow.class)
public class BookingResponderFlow extends FlowLogic<SignedTransaction> {
    private FlowSession counterpartySession;

    public BookingResponderFlow(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // Responder flow logic goes here.
        System.out.println("Booking received from BookYourStay: " + counterpartySession.getCounterparty().getName().getOrganisation());

        return subFlow(new ReceiveFinalityFlow(counterpartySession));

    }
}
