package com.xema.midas.network;

import com.xema.midas.network.service.AccountService;
import com.xema.midas.network.service.PostService;

public class ApiUtil {
    public static AccountService getAccountService() {
        return RetrofitClient.getClient().create(AccountService.class);
    }
    public static PostService getPostService() {
        return RetrofitClient.getClient().create(PostService.class);
    }
}
