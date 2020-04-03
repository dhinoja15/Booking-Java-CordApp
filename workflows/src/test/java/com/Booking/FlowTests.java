package com.Booking;

import com.Booking.flows.BookingInitiatorFlow;
import com.Booking.states.BookingState;
import com.google.common.collect.ImmutableList;
import com.Booking.flows.Responder;
import net.corda.core.concurrent.CordaFuture;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.contracts.TransactionState;
import net.corda.core.contracts.TransactionVerificationException;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.finance.Currencies;
import net.corda.testing.node.MockNetwork;
import net.corda.testing.node.MockNetworkParameters;
import net.corda.testing.node.StartedMockNode;
import net.corda.testing.node.TestCordapp;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import static javafx.scene.input.KeyCode.Z;
import static org.assertj.core.util.DateUtil.now;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;

public class FlowTests {
    private final MockNetwork network = new MockNetwork(new MockNetworkParameters(ImmutableList.of(
        TestCordapp.findCordapp("com.Booking.contracts"),
        TestCordapp.findCordapp("com.Booking.flows")
    )));
    private final StartedMockNode bookYourStay = network.createNode();
    private final StartedMockNode hotelHeaven = network.createNode();

    public FlowTests() {
        bookYourStay.registerInitiatedFlow(Responder.class);
        hotelHeaven.registerInitiatedFlow(Responder.class);
    }
    //private MockNetwork mockNetwork;
   // private StartedMockNode a, b;

    @Before
    public void setup() {
        network.runNetwork();
    }

    @After
    public void tearDown() {
        network.stopNodes();
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void dummyTest() {

    }

      /* Now we have a well formed transaction, we need to properly verify it using the {@link IOUContract}.
       * TODO: Amend the {@link BookingInitiatorFlow} to verify the transaction as well as sign it.
       * Hint: You can verify on the builder directly prior to finalizing the transaction. This way
       * you can confirm the transaction prior to making it immutable with the signature.
       */
      Party BookYourStay = bookYourStay.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();
      Party HotelHeaven = hotelHeaven.getInfo().getLegalIdentitiesAndCerts().get(0).getParty();


    @Test
    public void flowReturnsVerifiedPartiallySignedTransaction() throws Exception {
        // Check that a zero amount IOU fails.

        String checkIn="4/7/2020";
        String checkOut = "4/9/2020";
        String creditCardExp = "4/4/2022";
        Date checkInDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkIn);
        Date checkOutDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkOut);
        Date creditCardExpDate=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);
        // All data correct
        Future<SignedTransaction> correctTx = bookYourStay.startFlow(new BookingInitiatorFlow
                ("Sonal", 27, checkInDate, checkOutDate, "NK", 100,
                        "12345678901234", creditCardExpDate , 85, HotelHeaven));
        network.runNetwork();
        correctTx.get();
        //System.out.println("What is stored    " + bookYourStay);

        //When Customer age is less than 18 year
        Future<SignedTransaction> ageLess = bookYourStay.startFlow(new BookingInitiatorFlow
                ("Sonal", 17, checkInDate, checkOutDate, "NK", 100,
                        "12345678901234", creditCardExpDate , 85, HotelHeaven));
        network.runNetwork();
        exception.expectCause(instanceOf(TransactionVerificationException.class));
        ageLess.get();


        //when Credit card is expire

        String creditCardExpNew = "4/1/2020";
        Date creditCardExpDateNew=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);
        Future<SignedTransaction> creditCardExpire = bookYourStay.startFlow(new BookingInitiatorFlow
                ("Sonal", 17, checkInDate, checkOutDate, "NK", 100,
                        "12345678901234", creditCardExpDateNew , 85, HotelHeaven));
        network.runNetwork();
        exception.expectCause(instanceOf(TransactionVerificationException.class));
        creditCardExpire.get();


    }
    @Test
    public void flowRejectsInvalidAge() throws Exception {
        String checkIn="4/7/2020";
        String checkOut = "4/9/2020";
        String creditCardExp = "4/4/2022";
        Date checkInDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkIn);
        Date checkOutDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkOut);
        Date creditCardExpDate=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);

        // The IOUContract specifies that IOUs cannot have negative values.
        BookingInitiatorFlow flow = new BookingInitiatorFlow("Sonal", 17, checkInDate, checkOutDate, "NK", 100,
                "12345678901234", creditCardExpDate , 85, HotelHeaven);
        CordaFuture<SignedTransaction> future = bookYourStay.startFlow(flow);
        network.runNetwork();

        // The IOUContract specifies that IOUs cannot have negative values.
        exception.expectCause(CoreMatchers.instanceOf(TransactionVerificationException.class));
        future.get();
    }
    @Test
    public void signedTransactionReturnedByTheFlowIsSignedByTheInitiator() throws Exception {
        String checkIn="4/7/2020";
        String checkOut = "4/9/2020";
        String creditCardExp = "4/4/2022";
        Date checkInDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkIn);
        Date checkOutDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkOut);
        Date creditCardExpDate=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);

        BookingInitiatorFlow flow = new BookingInitiatorFlow("Sonal", 27, checkInDate, checkOutDate, "NK", 100,
                "12345678901234", creditCardExpDate , 85, HotelHeaven);
        CordaFuture<SignedTransaction> future = bookYourStay.startFlow(flow);
        network.runNetwork();

        SignedTransaction signedTx = future.get();
        signedTx.verifySignaturesExcept(hotelHeaven.getInfo().getLegalIdentities().get(0).getOwningKey());
    }
    @Test
    public void signedTransactionReturnedByTheFlowIsSignedByTheAcceptor() throws Exception {
        String checkIn="4/7/2020";
        String checkOut = "4/9/2020";
        String creditCardExp = "4/4/2022";
        Date checkInDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkIn);
        Date checkOutDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkOut);
        Date creditCardExpDate=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);
        BookingInitiatorFlow flow = new BookingInitiatorFlow("Sonal", 27, checkInDate, checkOutDate, "NK", 100,
                "12345678901234", creditCardExpDate , 85, HotelHeaven);
        CordaFuture<SignedTransaction> future = bookYourStay.startFlow(flow);
        network.runNetwork();

        SignedTransaction signedTx = future.get();
        signedTx.verifySignaturesExcept(bookYourStay.getInfo().getLegalIdentities().get(0).getOwningKey());
    }
    @Test
    public void flowRecordsATransactionInBothPartiesTransactionStorages() throws Exception {
        String checkIn="4/7/2020";
        String checkOut = "4/9/2020";
        String creditCardExp = "4/4/2022";
        Date checkInDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkIn);
        Date checkOutDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkOut);
        Date creditCardExpDate=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);

        BookingInitiatorFlow flow = new BookingInitiatorFlow("Sonal", 27, checkInDate, checkOutDate, "NK", 100,
                "12345678901234", creditCardExpDate , 85, HotelHeaven);
        CordaFuture<SignedTransaction> future = bookYourStay.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTx = future.get();

        // We check the recorded transaction in both vaults.
        for (StartedMockNode node : ImmutableList.of(bookYourStay, hotelHeaven)) {
            assertEquals(signedTx, node.getServices().getValidatedTransactions().getTransaction(signedTx.getId()));
        }
    }
    @Test
    public void recordedTransactionHasNoInputsAndASingleOutputTheInputIOU() throws Exception {
        String checkIn="4/7/2020";
        String checkOut = "4/9/2020";
        String creditCardExp = "4/4/2022";
        Date checkInDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkIn);
        Date checkOutDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkOut);
        Date creditCardExpDate=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);
        int  custAge = 27;

        BookingInitiatorFlow flow = new BookingInitiatorFlow("Sonal", 27, checkInDate, checkOutDate, "NK", 100,
                "12345678901234", creditCardExpDate , 85, HotelHeaven);
        CordaFuture<SignedTransaction> future = bookYourStay.startFlow(flow);
        network.runNetwork();
        SignedTransaction signedTx = future.get();

        // We check the recorded transaction in both vaults.
        for (StartedMockNode node : ImmutableList.of(bookYourStay, hotelHeaven)) {
            SignedTransaction recordedTx = node.getServices().getValidatedTransactions().getTransaction(signedTx.getId());
            List<TransactionState<ContractState>> txOutputs = recordedTx.getTx().getOutputs();
            assert (txOutputs.size() == 1);

            BookingState recordedState = (BookingState) txOutputs.get(0).getData();
            assertEquals(recordedState.getCustAge(), custAge);
            assertEquals(recordedState.getBookYourStay(), bookYourStay.getInfo().getLegalIdentities().get(0));
            assertEquals(recordedState.getHotelHeaven(), hotelHeaven.getInfo().getLegalIdentities().get(0));
        }
    }

    @Test
    public void flowRecordsTheCorrectIOUInBothPartiesVaults() throws Exception {
        String checkIn="4/7/2020";
        String checkOut = "4/9/2020";
        String creditCardExp = "4/4/2022";
        Date checkInDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkIn);
        Date checkOutDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkOut);
        Date creditCardExpDate=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);

        int custAge = 27;
        BookingInitiatorFlow flow = new BookingInitiatorFlow("Sonal", 27, checkInDate, checkOutDate, "NK", 100,
                "12345678901234", creditCardExpDate , 85, HotelHeaven);
        CordaFuture<SignedTransaction> future = bookYourStay.startFlow(flow);
        network.runNetwork();
        future.get();

        // We check the recorded IOU in both vaults.
        for (StartedMockNode node : ImmutableList.of(bookYourStay, hotelHeaven)) {
            node.transaction(() -> {
                List<StateAndRef<BookingState>> reqOutput = node.getServices().getVaultService().queryBy(BookingState.class).getStates();
                assertEquals(1,reqOutput.size());
                BookingState recordedState = reqOutput.get(0).getState().getData();
                assertEquals(recordedState.getCustAge(), custAge);
                assertEquals(recordedState.getBookYourStay(), bookYourStay.getInfo().getLegalIdentities().get(0));
                assertEquals(recordedState.getHotelHeaven(), hotelHeaven.getInfo().getLegalIdentities().get(0));
                return null;
            });
        }
    }
}








