package com.techindustan.office.ui.message;

import com.techindustan.office.ui.base.BasePresenterInterface;

/**
 * Created by binod on 4/4/18.
 */

public interface MesssagePresenterInterface extends BasePresenterInterface {
    void sendReply(String userId, String message, String accessToken);
}
