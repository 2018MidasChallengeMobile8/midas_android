package com.xema.midas.network;

import com.xema.midas.network.service.AccountService;

public class ApiUtil {
    public static AccountService getAccountService() {
        return RetrofitClient.getClient().create(AccountService.class);
    }
}
