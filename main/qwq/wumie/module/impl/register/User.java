package qwq.wumie.module.impl.register;

public class User {
    String user_id;
    String perm;
    double bal;

    public User(String user_id, Prem perm, double bal) {
        this.user_id = user_id;
        this.perm = perm.name();
        this.bal = bal;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
        Register.save();
    }

    public Prem getPerm() {
        return Prem.get(perm);
    }

    public void setPerm(Prem perm) {
        this.perm = perm.name();
        Register.save();
    }

    public double getBal() {
        return bal;
    }

    public void setBal(double bal) {
        this.bal = bal;
        Register.save();
    }

    public enum Prem {
        Member(0),
        Admin(1),
        Owner(2);


        public final int level;
        Prem(int level) {
            this.level = level;
        }

        public static Prem get(String name) {
            for (Prem p : values()) {
                if (p.name().equalsIgnoreCase(name)) {
                    return p;
                }
            }
            return null;
        }
    }
}
