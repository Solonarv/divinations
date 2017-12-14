package solonarv.mods.thegreatweb.common.leyweb;

public class LeyLine {

    private int flowAmount;
    private int source, target;

    public LeyLine(int source, int target, int flowAmount) {
        this.source = source;
        this.target = target;
        this.flowAmount = flowAmount;
    }

    public int getFlowAmount() {
        return flowAmount;
    }

    public void setFlowAmount(int flowAmount) {
        this.flowAmount = flowAmount;
    }

    public int getSource() {
        return source;
    }

    public int getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return String.format("LeyLine[source=%d, target%d, flowAmount=%d]", source, target, flowAmount);
    }
}
