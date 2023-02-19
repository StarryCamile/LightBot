package qwq.wumie.module.impl.games.impl;

import qwq.wumie.Launch;
import qwq.wumie.events.EventHandler;
import qwq.wumie.events.message.MessageEvent;
import qwq.wumie.module.Manager;
import qwq.wumie.module.impl.Game;
import qwq.wumie.module.impl.games.GameMode;
import qwq.wumie.module.impl.register.Register;
import qwq.wumie.module.impl.register.User;
import qwq.wumie.module.results.AtResult;
import qwq.wumie.utils.I18nUtils;
import qwq.wumie.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class TicTacToe extends GameMode {
    private int counts = 5;
    private boolean can = false;
    private boolean first = false;
    private boolean idk = true;
    private double money = 20;
    private List<Integer> boomPosition = new ArrayList<>();

    public TicTacToe() {
        super("井字棋", 1);
        boomPosition.add(RandomUtils.nextInt(1,counts));
    }

    @Override
    public void onJoin(String qq) {
        AtResult result = new AtResult(qq);
        if (started) {
            info(result.toJSON() + I18nUtils.get("game-pyramid-started-info"));
            return;
        }
        if (players.size() < maxPlayer) {
            if (getUser(qq) == null) {
                players.add(Register.get(qq));
            } else {
                info(result.toJSON() + I18nUtils.get("game-pyramid-in-game-info"));
            }
            super.onJoin(qq);
        } else {
            info(result.toJSON() + I18nUtils.get("game-pyramid-full-info"));
        }
    }

    @EventHandler
    private void onMessage(MessageEvent e) {
        String message = e.message.getRaw_message();
        String qq = e.message.getUser_id();
        loop(qq,message,e.message.getGroup_id());
    }

    public void loop(String qq,String message,String group) {
        AtResult result = new AtResult(qq);
        if (qq.equalsIgnoreCase(players.get(0).getUser_id())) {
            if (counts >= 5+6) {
                info(group,result.toJSON()+ I18nUtils.getConversion("game-pyramid-game-end","money",money+""));
                User user = Register.get(qq,false);
                user.setBal((int) (user.getBal()+this.money));
                Register.save();
                end();
                return;
            }

            if (first) {
                if (message.startsWith("投入")) {
                    int money = Integer.parseInt(message.substring("投入".length()));
                    User user = Register.get(qq);
                    if (user.getBal() >= 20 && user.getBal() < money) {
                        info(group,I18nUtils.getConversion("game-pyramid-money-not-enough","money",money+""));
                        if (!(money < 20)) {
                            this.money = money;
                        }
                        info(group,result.toJSON()+ I18nUtils.getConversion("game-pyramid-money-get","money",this.money+""));
                        first = false;
                        loop(qq,"",group);
                        return;
                    }
                    if (user.getBal() <= 0) {
                        info(group,I18nUtils.get("game-pyramid-money-equals-0"));
                        end();
                        return;
                    }
                    info(group,result.toJSON()+ I18nUtils.getConversion("game-pyramid-money-get","money",money+""));
                }
                first = false;
                loop(qq,"",group);
                return;
            }
            if (can) {
                if (message.startsWith("进入")) {
                    int booms = RandomUtils.nextInt(1,3);
                    switch (booms) {
                        case 1 -> {
                        }
                        case 2 -> {
                            boomPosition.add(RandomUtils.nextInt(1,counts));
                        }
                        case 3 -> {
                            for (int i = 0;i<2;i++) {
                                boomPosition.add(RandomUtils.nextInt(1,counts));
                            }
                        }
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int b : boomPosition) {
                        sb.append(b).append(" ");
                    }
                    sb.setLength(sb.length()-1);
                    Launch.info("炸弹"+ sb);

                    int num = Integer.parseInt(message.substring("进入".length()));
                    if (num < 1 || num > counts) {
                        info(Integer.parseInt(group),result.toJSON()+ I18nUtils.getConversion("game-pyramid-num-oversize-info","num",counts+""), I18nUtils.get("game-pyramid-num-oversize-info-1"));
                        return;
                    }
                    if (boom(num)) {
                        double money = this.money*(RandomUtils.nextDouble(counts-counts*10,counts) * (0.1*(RandomUtils.nextInt(counts-counts/2,counts) * 0.1)));
                        double offset = (int) (money - this.money);
                        String tip = (offset < 0) ? I18nUtils.get("game-pyramid-money-sub") : I18nUtils.get("game-pyramid-money-add");
                        if (idk) {
                            info(Integer.parseInt(group),result.toJSON()+I18nUtils.get("game-pyramid-skin-info")+tip,I18nUtils.getConversion("game-pyramid-counts-info","count",counts+""));
                            idk = false;
                            counts++;
                            can = false;
                            loop(qq, "",group);
                            return;
                        }
                        this.money = money;
                        info(Integer.parseInt(group),result.toJSON()+I18nUtils.get("game-pyramid-boom-info"), I18nUtils.get("game-pyramid-money-changed-info")+tip+offset,I18nUtils.getConversion("game-pyramid-money-info","money",this.money+""),I18nUtils.getConversion("game-pyramid-counts-info","count",this.counts+""));
                    } else {
                        info(Integer.parseInt(group),result.toJSON()+I18nUtils.get("game-pyramid-next-info"),I18nUtils.getConversion("game-pyramid-counts-info","count",this.counts+""));
                    }
                    counts++;
                    can = false;
                    loop(qq,"",group);
                }
            } else {
                info(I18nUtils.getConversion("game-pyramid-counts-info","count",this.counts+""), result.toJSON()+I18nUtils.getConversion("game-pyramid-next-command","count",this.counts+""));
                can = true;
            }
        }
    }

    @Override
    public void start() {
        AtResult result = new AtResult(players.get(0).getUser_id());
        started = true;
        info(result.toJSON()+I18nUtils.get("game-pyramid-money-command"));
        first = true;
        super.start();
    }

    private boolean boom(int pos) {
        for (int i : boomPosition) {
            if (i==pos) return true;
        }
        return false;
    }

    @Override
    public void end() {
        Launch.EVENT_BUS.unsubscribe(this);
        ((Game) Manager.get(Game.class)).removeGame(gameId);
        super.end();
    }
}
