package com.techindustan.office.ui.base;

/**
 * Created by binod on 8/1/18.
 */

public interface BasePresenterInterface<V extends BaseView> {

    void attachView(V mvpView);

    void detachView();
}
