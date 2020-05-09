package com.qtimes.pavilion.delegate;

import com.qtimes.pavilion.app.AppConstant;
import com.qtimes.pavilion.audio.Player;
import com.qtimes.domain.event.PlayerEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

/**
 * Author: JackHou
 * Date: 1/4/2018.
 */

public class PlayerDelegate implements BaseDelegate {

    private Player player;

    @Inject
    public PlayerDelegate(Player player) {
        this.player = player;
    }

    @Override
    public void register() {
        if (EventBus.getDefault().isRegistered(this)) {
            return;
        }
        EventBus.getDefault().register(this);
    }

    @Override
    public void unRegister() {
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onPlayerEvent(PlayerEvent event) {
        switch (event.getAction()) {
            case AppConstant.PlayerAction.RESTART:
                player.restart();
                break;
            case AppConstant.PlayerAction.START:
                player.play();
                break;
            case AppConstant.PlayerAction.LOOP_START:
                player.play(event.getSource());
                break;
            case AppConstant.PlayerAction.STOP:
                player.stop();
                break;
            case AppConstant.PlayerAction.PAUSE:
                player.pause();
                break;
            default:
                break;
        }
    }
}
