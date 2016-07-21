package com.ocean.router.mixed;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ocean.router.single.SingleRoutingResult;
import com.ocean.router.single.SingleRoutingTableFactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 笛卡尔积的库表路由
 */
public class CartesianTablesRouter {

    private static Logger logger = LoggerFactory.getLogger(CartesianTablesRouter.class);

    private Collection<SingleRoutingResult> routingResults;

    public CartesianTablesRouter()
    {

    }

    public CartesianTablesRouter(Collection<SingleRoutingResult> routingResults)
    {
        this.routingResults = routingResults;
    }

    public Collection<SingleRoutingResult> getRoutingResults() {
        return routingResults;
    }

    public void setRoutingResults(Collection<SingleRoutingResult> routingResults) {
        this.routingResults = routingResults;
    }

    public CartesianResult route() {
        CartesianResult result = new CartesianResult();
        for (Map.Entry<String, Set<String>> entry : getDataSourceLogicTablesMap().entrySet()) {
            List<Set<String>> actualTableGroups = getActualTableGroups(entry.getKey(), entry.getValue());
            List<Set<SingleRoutingTableFactor>> routingTableFactorGroups = toRoutingTableFactorGroups(entry.getKey(), actualTableGroups);
            result.merge(entry.getKey(), getCartesianTableReferences(Sets.cartesianProduct(routingTableFactorGroups)));
        }
        logger.info("CartesianTablesRouter cartesian tables sharding result: {}", result);
        return result;
    }

    private Map<String, Set<String>> getDataSourceLogicTablesMap() {
        Collection<String> intersectionDataSources = getIntersectionDataSources();
        Map<String, Set<String>> result = new HashMap<String, Set<String>>(routingResults.size());
        for (SingleRoutingResult each : routingResults) {
            for (Map.Entry<String, Set<String>> entry : each.getDataSourceLogicTablesMap(intersectionDataSources).entrySet()) {
                if (result.containsKey(entry.getKey())) {
                    result.get(entry.getKey()).addAll(entry.getValue());
                } else {
                    result.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return result;
    }

    private Collection<String> getIntersectionDataSources() {
        Collection<String> result = new HashSet<String>();
        for (SingleRoutingResult each : routingResults) {
            if (result.isEmpty()) {
                result.addAll(each.getDataSources());
            }
            result.retainAll(each.getDataSources());
        }
        return result;
    }

    private List<Set<String>> getActualTableGroups(final String dataSource, final Set<String> logicTables) {
        List<Set<String>> result = new ArrayList<Set<String>>(logicTables.size());
        for (SingleRoutingResult each : routingResults) {
            result.addAll(each.getActualTableGroups(dataSource, logicTables));
        }
        return result;
    }

    private List<Set<SingleRoutingTableFactor>> toRoutingTableFactorGroups(final String dataSource, final List<Set<String>> actualTableGroups) {
        List<Set<SingleRoutingTableFactor>> result = new ArrayList<Set<SingleRoutingTableFactor>>(actualTableGroups.size());
        for (Set<String> each : actualTableGroups) {
            result.add(new HashSet<SingleRoutingTableFactor>(Lists.transform(new ArrayList<String>(each), new Function<String, SingleRoutingTableFactor>() {

                @Override
                public SingleRoutingTableFactor apply(final String input) {
                    return findRoutingTableFactor(dataSource, input);
                }
            })));
        }
        return result;
    }

    private SingleRoutingTableFactor findRoutingTableFactor(final String dataSource, final String actualTable) {
        for (SingleRoutingResult each : routingResults) {
            Optional<SingleRoutingTableFactor> result = each.findRoutingTableFactor(dataSource, actualTable);
            if (result.isPresent()) {
                return result.get();
            }
        }
        throw new IllegalStateException(String.format("Cannot found routing table factor, data source: %s, actual table: %s", dataSource, actualTable));
    }

    private List<CartesianTableReference> getCartesianTableReferences(final Set<List<SingleRoutingTableFactor>> cartesianRoutingTableFactorGroups) {
        List<CartesianTableReference> result = new ArrayList<CartesianTableReference>(cartesianRoutingTableFactorGroups.size());
        for (List<SingleRoutingTableFactor> each : cartesianRoutingTableFactorGroups) {
            result.add(new CartesianTableReference(each));
        }
        return result;
    }
}
