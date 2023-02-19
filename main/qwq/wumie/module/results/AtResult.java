package qwq.wumie.module.results;

import qwq.wumie.module.Result;
import qwq.wumie.utils.GsonUtils;

public class AtResult extends Result {
    String qq;

    public AtResult(String qq) {
        this.qq = qq;
    }

    @Override
    public String toJSON() {
        return "[CQ:at,qq="+qq+"]";
    }

    @Override
    public Result get() {
        return this;
    }
}
