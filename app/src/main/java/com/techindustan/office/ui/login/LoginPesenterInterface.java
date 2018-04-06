package com.techindustan.office.ui.login;

import com.techindustan.office.ui.base.BasePresenterInterface;

/**
 * Created by binod on 4/4/18.
 */

public interface LoginPesenterInterface extends BasePresenterInterface {
    void login(String email, String password, String devicetoken);
}
