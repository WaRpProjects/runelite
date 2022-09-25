package net.runelite.api.queries;

import java.util.Collection;
import java.util.stream.Collectors;
import net.runelite.api.Client;
import net.runelite.api.LocatableQueryResults;
import net.runelite.api.NPC;

public class NPCQuery extends ActorQuery<NPC, NPCQuery>
{
    @Override
    public LocatableQueryResults<NPC> result(Client client)
    {
        return new LocatableQueryResults<>(client.getNpcs().stream()
                .filter(predicate)
                .collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    public NPCQuery idEquals(int... ids)
    {
        predicate = and(object ->
        {
            for (int id : ids)
            {
                if (object.getId() == id)
                {
                    return true;
                }
            }
            return false;
        });
        return this;
    }

    @SuppressWarnings("unchecked")
    public NPCQuery idEquals(Collection<Integer> ids)
    {
        predicate = and((object) -> ids.contains(object.getId()));
        return this;
    }
}
