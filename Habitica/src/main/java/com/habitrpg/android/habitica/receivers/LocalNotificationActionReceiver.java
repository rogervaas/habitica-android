package com.habitrpg.android.habitica.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;

import com.habitrpg.android.habitica.APIHelper;
import com.habitrpg.android.habitica.HabiticaApplication;
import com.habitrpg.android.habitica.R;
import com.habitrpg.android.habitica.callbacks.HabitRPGUserCallback;
import com.magicmicky.habitrpgwrapper.lib.models.HabitRPGUser;

import javax.inject.Inject;

/**
 * Created by keithholliday on 6/30/16.
 */
public class LocalNotificationActionReceiver extends BroadcastReceiver implements HabitRPGUserCallback.OnUserReceived {
    @Inject
    public APIHelper apiHelper;

    private HabitRPGUser user;
    private String action;
    private Resources resources;

    @Override
    public void onReceive(Context context, Intent arg1) {
        HabiticaApplication.getInstance(context).getComponent().inject(this);
        this.resources = context.getResources();

        this.action = arg1.getAction();

        this.apiHelper.apiService.getUser()
                .compose(this.apiHelper.configureApiCallObserver())
                .subscribe(new HabitRPGUserCallback(this), throwable -> {});
    }

    @Override
    public void onUserReceived(HabitRPGUser user) {
        this.user = user;
        this.handleLocalNotificationAction(action);
    }

    private void handleLocalNotificationAction(String action) {
        //@TODO: This is a good place for a factory and event emitter pattern
        Log.v("test", action);
        if (action.equals(this.resources.getString(R.string.accept_party_invite))) {
            if (this.user.getInvitations().getParty() == null) return;
            String partyId = this.user.getInvitations().getParty().getId();
            apiHelper.apiService.joinGroup(partyId)
                    .compose(apiHelper.configureApiCallObserver())
                    .subscribe(aVoid -> {}, throwable -> {});
        } else if (action.equals(this.resources.getString(R.string.reject_party_invite))) {
            if (this.user.getInvitations().getParty() == null) return;
            String partyId = this.user.getInvitations().getParty().getId();
            apiHelper.apiService.rejectGroupInvite(partyId)
                    .compose(apiHelper.configureApiCallObserver())
                    .subscribe(aVoid -> {}, throwable -> {});
        } else if (action.equals(this.resources.getString(R.string.accept_quest_invite))) {
            if (this.user.getParty() == null) return;
            String partyId = this.user.getParty().getId();
            apiHelper.apiService.acceptQuest(partyId)
                    .compose(apiHelper.configureApiCallObserver())
                    .subscribe(aVoid -> {}, throwable -> {});
        } else if (action.equals(this.resources.getString(R.string.reject_quest_invite))) {
            if (this.user.getParty() == null) return;
            String partyId = this.user.getParty().getId();
            apiHelper.apiService.rejectQuest(partyId)
                    .compose(apiHelper.configureApiCallObserver())
                    .subscribe(aVoid -> {}, throwable -> {});
        }
    }
}
