package org.smigo.species;

public class RuleBean {

    private final int id;
    private final int host;
    private final int type;
    private final int param;

    public RuleBean(int id, int host, int type, int param) {
        this.id = id;
        this.host = host;
        this.type = type;
        this.param = param;
    }

    public int getId() {
        return id;
    }

    public int getHost() {
        return host;
    }

    public int getType() {
        return type;
    }

    public int getParam() {
        return param;
    }

    public static RuleBean create(int id, int host, int type, int causerSpecies, int causerFamily, int gap) {
        int param = 0;
        if (type == 0 || type == 1 || type == 2 || type == 3 || type == 4) {
            param = causerSpecies;
        } else if (type == 5 || type == 6) {
            param = causerFamily;
        } else if (type == 7) {
            param = gap;
        } else {
            throw new IllegalArgumentException("No rule with type" + type);
        }
        return new RuleBean(id, host, type, param);
    }
}
