package com.techindustan.office.ui.login;

import com.techindustan.model.login.Response;
import com.techindustan.office.ui.base.BaseView;

/**
 * Created by binod on 4/4/18.
 */

public interface LoginView extends BaseView {
    void onLoginSuccessfull(Response response);
}
