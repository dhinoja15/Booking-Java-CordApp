Definition:

There is two party in BookingCordApp.

    BookYourStay
    HotelHeaven
BookingCordApp is for BookYourStay.

Note: BookYourStay is assuming HotelHeaven has availability.

BookYourStay will send with following detail to HotelHeaven to book stay for his customer

    Customer name
    Customer Age
    Check In Date
    Check out Date
    Room Type
    Original Room Rate
    Merchant Credit Card Number
    Credit Card exp date.
    Credit Card Amount (After 15% commission deduction on room rate)

Following Validations should be performed before sending booking detail to HotelHeaven:

    Customer Age should be greater than 18.
    Check in and Check Out date Should be Future Date.
    Check Out date should be greater than Check in date.
    Room Type format is from this only:  K, NK, DD, NDD
    After commission price should 85% of Original room price.
    Credit Card number length should be 16.
    Credit Card Exp date should not be in past.

Run at terminal:

flow start BookingInitiatorFlow custName: Sonal,custAge: 27,checkInDate: "2020-10-23T10:12:35Z",checkOutDate: "2020-10-24T10:12:35Z",roomType: NK,roomRate: 100,creditCardNumber: 1234567890123456,creditCardExpDate: "2020-10-23T10:12:35Z",creditCardAmount: 85,hotelHeaven: "O=HotelHeaven,L=New York,C=US"
