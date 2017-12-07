package solonarv.mods.thegreatweb.common.leyweb;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;

public interface ILeyWeb {
    Collection<LeyNode> nodes();

    Collection<LeyLine> allLeylines();

    Collection<LeyLine> leyLinesAround(LeyNode node);

    Collection<LeyLine> leyLinesFrom(LeyNode node);

    Collection<LeyLine> leyLinesTo(LeyNode node);

    Pair leylineEndpoints(LeyLine leyLine);

    boolean areNodesConnected(LeyNode source, LeyNode target);
}
