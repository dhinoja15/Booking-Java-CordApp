package com.Booking.schemas;

import com.google.common.collect.ImmutableList;
import net.corda.core.schemas.MappedSchema;

/**
 * MappedSchema subclass representing the custom schema for the Booking QueryableState.
 */
public class BookingSchemaV1 extends MappedSchema {

    /**Sending it to flow hospital to be triged.
         * kotlin.collections.CollectionsKt__MutableCollectionsKt.addAll
     * The constructor of the MappedSchema requires the schemafamily, verison, and a list of all JPA entity classes for
     * the Schema.
     */
    public BookingSchemaV1() {
        super(BookingSchemaFamily.class, 1, ImmutableList.of(PersistentBookingClass.class));
    }
}