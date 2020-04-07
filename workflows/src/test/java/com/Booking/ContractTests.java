package com.Booking;

import com.Booking.contracts.BookingContract;
import com.Booking.states.BookingState;
import com.google.common.collect.ImmutableList;
import net.corda.core.identity.CordaX500Name;
import net.corda.testing.core.TestIdentity;
import net.corda.testing.node.MockServices;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.util.Arrays.asList;
import static net.corda.testing.node.NodeTestUtils.ledger;

public class ContractTests  {
    //private final MockServices ledgerServices = new MockServices();
    static private final MockServices ledgerServices = new MockServices(asList("com.Booking.contracts", "com.Booking.flows"));
    static private final TestIdentity BookMyStay = new TestIdentity(new CordaX500Name("MegaCorp", "London", "GB"));
    static private final TestIdentity HotelHeaven = new TestIdentity(new CordaX500Name("MiniCorp", "London", "GB"));
   // static private final int iouValue = 1;

    //public ContractTests() throws ParseException {
    //}
    @Rule
    public final ExpectedException exception = ExpectedException.none();


    @Test
    public void transactionMustIncludeBookingCommand() throws ParseException {
        String checkIn="4/7/2020";
        String checkOut = "4/9/2020";
        String creditCardExp = "4/4/2022";
        Date checkInDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkIn);
        Date checkOutDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkOut);
        Date creditCardExpDate=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);

        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(BookingContract.BOOKING_CONTRACT_ID, new BookingState(id, "Sonal", 27, checkInDate,
                        checkOutDate, "NK", 100, "12345678901234",
                        creditCardExpDate , 85, BookMyStay.getParty(), HotelHeaven.getParty()));
                tx.fails();
               // exception.expectCause(instanceOf(TransactionVerificationException.class));

                tx.command(ImmutableList.of(BookMyStay.getPublicKey(), HotelHeaven.getPublicKey()), new BookingContract.Commands.Booking());
                tx.verifies();
                return null;
            });
            return null;
        }));
    }

    @Test
    public void transactionMustHaveNoInputs() throws ParseException {
        String checkIn="4/7/2020";
        String checkOut = "4/9/2020";
        String creditCardExp = "4/4/2022";
        Date checkInDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkIn);
        Date checkOutDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkOut);
        Date creditCardExpDate=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);

        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.input(BookingContract.BOOKING_CONTRACT_ID, new BookingState(id, "Sonal", 27, checkInDate,
                        checkOutDate, "NK", 100, "12345678901234",
                        creditCardExpDate , 85, BookMyStay.getParty(), HotelHeaven.getParty()));
                tx.output(BookingContract.BOOKING_CONTRACT_ID, new BookingState(id, "Sonal", 27, checkInDate,
                        checkOutDate, "NK", 100, "12345678901234",
                        creditCardExpDate , 85, BookMyStay.getParty(), HotelHeaven.getParty()));
                tx.command(ImmutableList.of(BookMyStay.getPublicKey(), HotelHeaven.getPublicKey()), new BookingContract.Commands.Booking());
                tx.failsWith("No inputs should be consumed when requesting a booking.");
                return null;
            });
            return null;
        }));
    }

    @Test
    public void transactionMustHaveOneOutput() throws ParseException {
        String checkIn="4/7/2020";
        String checkOut = "4/9/2020";
        String creditCardExp = "4/4/2022";
        Date checkInDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkIn);
        Date checkOutDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkOut);
        Date creditCardExpDate=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);

        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(BookingContract.BOOKING_CONTRACT_ID, new BookingState(id, "Sonal", 27, checkInDate,
                        checkOutDate, "NK", 100, "12345678901234",
                        creditCardExpDate , 85, BookMyStay.getParty(), HotelHeaven.getParty()));
                tx.output(BookingContract.BOOKING_CONTRACT_ID, new BookingState(id, "Sonal", 27, checkInDate,
                        checkOutDate, "NK", 100, "12345678901234",
                        creditCardExpDate , 85, BookMyStay.getParty(), HotelHeaven.getParty()));
                tx.command(ImmutableList.of(BookMyStay.getPublicKey(), HotelHeaven.getPublicKey()), new BookingContract.Commands.Booking());
                tx.failsWith("Only one output state should be created.");
                return null;
            });
            return null;
        }));
    }
    @Test
    public void BookMyStayMustSignTransaction() throws ParseException {
        String checkIn="4/7/2020";
        String checkOut = "4/9/2020";
        String creditCardExp = "4/4/2022";
        Date checkInDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkIn);
        Date checkOutDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkOut);
        Date creditCardExpDate=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);

        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(BookingContract.BOOKING_CONTRACT_ID, new BookingState(id, "Sonal", 27, checkInDate,
                        checkOutDate, "NK", 100, "12345678901234",
                        creditCardExpDate , 85, BookMyStay.getParty(), HotelHeaven.getParty()));
                tx.command(BookMyStay.getPublicKey(), new BookingContract.Commands.Booking());
                tx.failsWith("BookMyStay must be signers.");
                return null;
            });
            return null;
        }));
    }
    @Test
    public void HotelHevenMustSignTransaction() throws ParseException {
        String checkIn="4/7/2020";
        String checkOut = "4/9/2020";
        String creditCardExp = "4/4/2022";
        Date checkInDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkIn);
        Date checkOutDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkOut);
        Date creditCardExpDate=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);

        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(BookingContract.BOOKING_CONTRACT_ID, new BookingState(id, "Sonal", 27, checkInDate,
                        checkOutDate, "NK", 100, "12345678901234",
                        creditCardExpDate , 85, BookMyStay.getParty(), HotelHeaven.getParty()));
                tx.command(HotelHeaven.getPublicKey(), new BookingContract.Commands.Booking());
                tx.failsWith("HotelHeaven must be signers.");
                return null;
            });
            return null;
        }));
    }

    @Test
    public void SenderIsNotReceiver() throws ParseException {
        String checkIn="4/7/2020";
        String checkOut = "4/9/2020";
        String creditCardExp = "4/4/2022";
        Date checkInDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkIn);
        Date checkOutDate=new SimpleDateFormat("dd/MM/yyyy").parse(checkOut);
        Date creditCardExpDate=new SimpleDateFormat("dd/MM/yyyy").parse(creditCardExp);

        ledger(ledgerServices, (ledger -> {
            ledger.transaction(tx -> {
                tx.output(BookingContract.BOOKING_CONTRACT_ID, new BookingState(id, "Sonal", 27, checkInDate,
                        checkOutDate, "NK", 100, "12345678901234",
                        creditCardExpDate , 85, BookMyStay.getParty(), BookMyStay.getParty()));
                tx.command(ImmutableList.of(BookMyStay.getPublicKey(), HotelHeaven.getPublicKey()), new BookingContract.Commands.Booking());
                tx.failsWith("booking(sender) and hotel(receiver) cannot be the same entity.");
                return null;
            });
            return null;
        }));
    }





    @Test
    public void dummyTest() {

    }
}