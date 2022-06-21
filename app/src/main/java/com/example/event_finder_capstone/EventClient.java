package com.example.event_finder_capstone;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;

public class EventClient extends OAuthBaseClient {
    //public static final BaseApi REST_API_INSTANCE = EventBriteApi.instance;
    public static final String REST_URL = "https://www.eventbriteapi.com/v3"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = BuildConfig.CONSUMER_KEY;
    public static final String REST_CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET;

    public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

    public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

    public EventClient(Context context) {
        super(context, REST_API_INSTANCE,
                REST_URL,
                REST_CONSUMER_KEY,
                REST_CONSUMER_SECRET,
                null,
                String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
                        context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
    }
}
