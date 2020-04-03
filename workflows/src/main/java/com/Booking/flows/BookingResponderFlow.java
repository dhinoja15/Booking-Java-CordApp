package com.Booking.flows;

import co.paralleluniverse.fibers.Suspendable;
import com.Booking.states.BookingState;
import net.corda.core.contracts.ContractState;
import net.corda.core.crypto.SecureHash;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.utilities.ProgressTracker;

import static net.corda.core.contracts.ContractsDSL.requireThat;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(BookingInitiatorFlow.class)
public class BookingResponderFlow extends FlowLogic<SignedTransaction> {
    private FlowSession counterpartySession;

    public BookingResponderFlow(FlowSession counterpartySession) {this.counterpartySession = counterpartySession; }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {
        // Responder flow logic goes here.
       /* class SignTxFlow extends SignTransactionFlow {
            private SignTxFlow(FlowSession otherPartyFlow, ProgressTracker progressTracker) {
                super(otherPartyFlow, progressTracker);
            }
            @Override
            protected void checkTransaction(SignedTransaction stx) {
                requireThat(require -> {
                    ContractState output = stx.getTx().getOutputs().get(0).getData();
                    BookingState bookStateObj = (BookingState) output;
                    require.using("This must be an Booking transaction.", output instanceof BookingState);
                    require.using("Credit Card not Valid Ha Ha ", bookStateObj.getCreditCardNumber().length() == 16);
                    String lastFourDigitCC = bookStateObj.getCreditCardNumber().substring(13,16);
                    System.out.println("Reservation is confirmed : " + counterpartySession.getCounterparty().getName().getOrganisation());
                    System.out.println("Credit Card Ends with "+ lastFourDigitCC + " is charged for Amount: "+bookStateObj.getCreditCardAmount());
                    return null;
                });
            }
        }
*/
        //final SignTxFlow signTxFlow = new SignTxFlow(counterpartySession, SignTransactionFlow.Companion.tracker());
        //final SecureHash txId = subFlow(signTxFlow).getId();

        System.out.println("Booking Request received from : " + counterpartySession.getCounterparty().getName().getOrganisation());

        return subFlow(new ReceiveFinalityFlow(counterpartySession));
        //return subFlow(new SignTxFlow(counterpartySession, SignTransactionFlow.Companion.tracker()));
    }
}
