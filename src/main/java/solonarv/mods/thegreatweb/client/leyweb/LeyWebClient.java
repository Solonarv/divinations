package solonarv.mods.thegreatweb.client.leyweb;

import org.apache.commons.lang3.tuple.Pair;
import solonarv.mods.thegreatweb.common.leyweb.ILeyWeb;
import solonarv.mods.thegreatweb.common.leyweb.LeyLine;
import solonarv.mods.thegreatweb.common.leyweb.LeyNode;

import java.util.Collection;

public class LeyWebClient implements ILeyWeb {
    @Override
    public Collection<LeyNode> nodes() {
        return null;
    }

    @Override
    public Collection<LeyLine> allLeylines() {
        return null;
    }

    @Override
    public Collection<LeyLine> leyLinesAround(LeyNode node) {
        return null;
    }

    @Override
    public Collection<LeyLine> leyLinesFrom(LeyNode node) {
        return null;
    }

    @Override
    public Collection<LeyLine> leyLinesTo(LeyNode node) {
        return null;
    }

    @Override
    public Pair<LeyNode, LeyNode> leylineEndpoints(LeyLine leyLine) {
        return null;
    }

    @Override
    public boolean areNodesConnected(LeyNode source, LeyNode target) {
        return false;
    }
}
