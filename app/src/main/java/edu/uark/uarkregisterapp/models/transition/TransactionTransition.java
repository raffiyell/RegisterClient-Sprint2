package edu.uark.uarkregisterapp.models.transition;

import android.os.Parcel;
import android.os.Parcelable;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.UUID;

import edu.uark.uarkregisterapp.commands.converters.ByteToUUIDConverterCommand;
import edu.uark.uarkregisterapp.commands.converters.UUIDToByteConverterCommand;
import edu.uark.uarkregisterapp.models.api.Transaction;

public class TransactionTransition implements Parcelable {

    private UUID recordId;
    public UUID getRecordId() {
        return recordId;
    }
    public TransactionTransition setRecordId(UUID recordId) {
        this.recordId = recordId;
        return this;
    }

    private String cashierId;
    public String getCashierId() {
        return cashierId;
    }
    public TransactionTransition setCashierId(String cashierId) {
        this.cashierId = cashierId;
        return this;
    }

    private double totalAmount;
    public double getTotalAmount() {
        return totalAmount;
    }
    public TransactionTransition setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    private String transactionType;
    public String getTransactionType() {
        return transactionType;
    }
    public TransactionTransition setTransactionType(String transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    private UUID referenceId;
    public UUID getReferenceId() {
        return referenceId;
    }
    public TransactionTransition setReferenceType(UUID referenceType) {
        this.referenceId = referenceType;
        return this;
    }

    private Date createdOn;
    public Date getCreatedOn() {
        return createdOn;
    }
    public TransactionTransition setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    private int totalItemSold;// todo remove whenever
    public int getTotalItemSold() {
        return totalItemSold;
    }
    public TransactionTransition setTotalItemSold(int totalItemSold) {
        this.totalItemSold = totalItemSold;
        return this;
    }

    


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.recordId).execute());
        dest.writeString(cashierId);
        dest.writeDouble(totalAmount);
        dest.writeString(transactionType);
        dest.writeByteArray((new UUIDToByteConverterCommand()).setValueToConvert(this.referenceId).execute());
        dest.writeInt(totalItemSold);
        dest.writeLong(this.createdOn.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TransactionTransition> CREATOR = new Creator<TransactionTransition>() {
        @Override
        public TransactionTransition createFromParcel(Parcel in) {
            return new TransactionTransition(in);
        }

        @Override
        public TransactionTransition[] newArray(int size) {
            return new TransactionTransition[size];
        }
    };


    public TransactionTransition() {
        this.recordId = new UUID(0, 0);
        this.cashierId = StringUtils.EMPTY;
        this.totalAmount = 0.0;
        this.transactionType = StringUtils.EMPTY;
        this.referenceId = new UUID(0,0);
        this.createdOn = new Date();
        this.totalItemSold = 0;
    }

    public TransactionTransition(Transaction transaction) {
        this.recordId = transaction.getId();
        this.cashierId = transaction.getCashierId();
        this.totalAmount = transaction.getTotalAmount();
        this.transactionType = transaction.getTransactionType();
        this.referenceId = transaction.getReferenceId();
        this.createdOn = transaction.getCreatedOn();
        this.totalItemSold = transaction.getTotalItemSold();
    }


    public TransactionTransition (Parcel transactionTransitionParcel) {
        this.recordId =  (new ByteToUUIDConverterCommand()).setValueToConvert(transactionTransitionParcel.createByteArray()).execute();
        this.cashierId = transactionTransitionParcel.readString();
        this.totalAmount = transactionTransitionParcel.readDouble();
        this.transactionType = transactionTransitionParcel.readString();
        this.referenceId = (new ByteToUUIDConverterCommand()).setValueToConvert(transactionTransitionParcel.createByteArray()).execute();
        this.totalItemSold =  transactionTransitionParcel.readInt();

        this.createdOn = new Date();
        this.createdOn.setTime(transactionTransitionParcel.readLong());

    }
}
