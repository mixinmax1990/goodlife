package com.news.goodlife.Data.Remote.Klarna.Interfaces.Callbacks;

import com.news.goodlife.Data.Remote.Klarna.Models.SessionModels.GETSessionData;

public interface KlarnaGetSessionResponseCallback {

    void success(GETSessionData sessionData);
    void error();
}
