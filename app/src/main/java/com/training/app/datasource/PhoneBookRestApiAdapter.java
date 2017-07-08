package com.training.app.datasource;

import com.training.app.util.VariableUtil;
import com.training.app.util.restapi.BaseRestApiAdapter;

/**
 * Created by Dell on 7/8/2017.
 */

public class PhoneBookRestApiAdapter extends BaseRestApiAdapter {

    public static PhoneBookRestApi getPhoneBookRestApi() {
        restAdapter = getRestAdapter(VariableUtil.url);
        return restAdapter.create(PhoneBookRestApi.class);
    }
}
