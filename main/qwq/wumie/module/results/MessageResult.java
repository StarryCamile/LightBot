package qwq.wumie.module.results;

import qwq.wumie.module.Result;
import qwq.wumie.utils.GsonUtils;

public class MessageResult extends Result {
    String action = "send_group_msg";
    Params params;

    public MessageResult(Params params) {
        this.params = params;
    }

    @Override
    public String toJSON() {
        return GsonUtils.beanToJson(get());
    }

    public MessageResult get() {
        return this;
    }

    public static class Params {
        String group_id;
        String message;

        public Params(String group_id,String message) {
            this.group_id = group_id;
            this.message = message;
        }
    }
}
