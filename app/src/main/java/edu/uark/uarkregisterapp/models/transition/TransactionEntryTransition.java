package edu.uark.uarkregisterapp.models.transition;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.UUID;

import edu.uark.uarkregisterapp.commands.converters.ByteToUUIDConverterCommand;
import edu.uark.uarkregisterapp.commands.converters.UUIDToByteConverterCommand;
import edu.uark.uarkregisterapp.models.api.TransactionEntry;

public class TransactionEntryTransition implements Parcelable {



    private UUID recordId;
    public UUID getRecordId() {
        return recordId;
    }
    public TransactionEntryTransition setRecordId(UUID recordId) {
        this.recordId = recordId;
        return this;
    }

    private String productLookupCode;
    public String getLookupCode() {
        return productLookupCode;
    }
    public TransactionEntryTransition setProductLookupCode(String productLookupCode) {
        this.productLookupCode = productLookupCode;
        return this;
    }

    private int quantity;
    public int getQuantity() {
        return quantity;
    }
    public TransactionEntryTransition setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    private double price;
    public double getPrice() {
        return price;
    }
    public TransactionEntryTransition setPrice(double price) {
        this.price = price;
        return this;
    }

    private Date createdOn;
    public Date getCreatedOn() {
        return createdOn;
    }
    public TransactionEntryTransition setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    //todo add all getters setters. CHECK!!

    private UUID transactionReferenceId;
    public UUID getTransactionReferenceId() {
        return transactionReferenceId;
    }
    public TransactionEntryTransition setTransactionReferenceId(UUID transactionReferenceId) {
        this.transactionReferenceId = transactionReferenceId;
        return this;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.recordId).execute());
        dest.writeString(productLookupCode);
        dest.writeInt(quantity);
        dest.writeDouble(price);
        dest.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.transactionReferenceId).execute());
        dest.writeLong(this.createdOn.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Parcelable.Creator<TransactionEntryTransition> CREATOR = new Parcelable.Creator<TransactionEntryTransition>() {
        @Override
        public TransactionEntryTransition createFromParcel(Parcel in) {
            return new TransactionEntryTransition(in);
        }

        @Override
        public TransactionEntryTransition[] newArray(int size) {
            return new TransactionEntryTransition[size];
        }
    };

    public TransactionEntryTransition() {
        this.recordId = new UUID(0, 0);
        this.productLookupCode = StringUtils.EMPTY;
        this.quantity = 0;
        this.price = 0.0;
        this.transactionReferenceId = new UUID(0, 0);
        this.createdOn = this.createdOn = new Date();
    }

    public TransactionEntryTransition(TransactionEntry transactionEntry) {
        this.recordId = transactionEntry.getRecordId();
        this.productLookupCode = transactionEntry.getLookupCode();
        this.quantity = transactionEntry.getQuantity();
        this.price = transactionEntry.getPrice();
        this.createdOn = transactionEntry.getCreatedOn();
        this.transactionReferenceId = transactionEntry.getTransactionReferenceId();
    }

    public TransactionEntryTransition(UUID transactionReferenceId, String productLookupCode, double price){
        this.recordId = new UUID(0, 0);
        this.transactionReferenceId = transactionReferenceId;
        this.productLookupCode = productLookupCode;
        this.quantity = 0;
        this.price = price;
        this.createdOn = new Date();
    }


    public TransactionEntryTransition(Parcel in) {
        this.recordId = (new ByteToUUIDConverterCommand()).setValueToConvert(in.createByteArray()).execute();
        this.productLookupCode = in.readString();
        this.quantity = in.readInt();
        this.price = in.readDouble();
        this.transactionReferenceId = (new ByteToUUIDConverterCommand()).setValueToConvert(in.createByteArray()).execute();
        this.createdOn = new Date();
        this.createdOn.setTime(in.readLong());
    }



}
