package com.xema.midas.network;

import com.xema.midas.network.service.AccountService;

/**
 * Created by diygame5 on 2017-08-18.
 * Project : Buyble
 */

public class ApiUtil {
    public static AccountService getAccountService() {
        return RetrofitClient.getClient().create(AccountService.class);
    }
}
