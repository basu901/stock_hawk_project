package com.sam_chordas.android.stockhawk.service;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by cse on 19-05-2016.
 */
public class StockAuthenticator extends AbstractAccountAuthenticator {
    public StockAuthenticator(Context context){
        super(context);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response,String s){
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response,
                             String s,
                             String s2,
                             String[] strings,
                             Bundle bundle)throws NetworkErrorException{
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response,
                                     Account account,
                                     Bundle bundle)throws NetworkErrorException{
        return null;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response,
                               Account account,
                               String s,
                               Bundle bundle) throws NetworkErrorException{
        throw new UnsupportedOperationException();
    }

    @Override
    public String getAuthTokenLabel(String s){
        throw new UnsupportedOperationException();
    }

    public Bundle updateCredentials(AccountAuthenticatorResponse response,
                                    Account account,
                                    String s,
                                    Bundle bundle)throws NetworkErrorException{
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response,
                              Account account,
                              String[] strings) throws NetworkErrorException{
        throw new UnsupportedOperationException();
    }
}
