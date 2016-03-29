import com.ocean.core.rule.TableRule;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class Main {

    public static void main(String[] args) throws Exception{
        DataSource dataSource = getShardDataSource();
        select(dataSource);
        System.out.println("-----------------------------");
        groupBy(dataSource);
    }

    private static void select(final DataSource dataSource){

        String sql = "SELECT b.* FROM t_order a JOIN t_order_item b ON a.order_id=b.order_id WHERE a.user_id=? AND a.order_id=?";
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 10);
            pstmt.setInt(2, 1001);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt(1));
                System.out.println(rs.getInt(2));
                System.out.println(rs.getInt(3));
            }
        }catch(Exception e)
        {
            e.printStackTrace(System.out);
        }

    }

    private static void groupBy(final DataSource dataSource){

        String sql = "SELECT a.user_id, COUNT(*) FROM t_order a JOIN t_order_item b ON a.order_id=b.order_id GROUP BY a.user_id";
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("user_id: " + rs.getInt(1) + ", count: " + rs.getInt(2));
            }
        }catch(Exception e)
        {
            e.printStackTrace(System.out);
        }
    }

    private static ShardDataSource getShardDataSource() throws {

        Map<String, DataSource> dataSourceMap = createDataSourceMap();

        TableRule orderTableRule = new TableRule("t_order", Arrays.asList("t_order_0", "t_order_1"));
        TableRule orderItemTableRule = new TableRule("t_order_item", Arrays.asList("t_order_item_0", "t_order_item_1"));

        ShardRule shardRule = new ShardingRule(dataSourceRule, Arrays.asList(orderTableRule, orderItemTableRule),
                Arrays.asList(new BindingTableRule(Arrays.asList(orderTableRule, orderItemTableRule))),
                new DatabaseShardingStrategy("user_id", new ModuloDatabaseShardingAlgorithm()),
                new TableShardingStrategy("order_id", new ModuloTableShardingAlgorithm()));
        return new ShardingDataSource(shardRule);
    }

    private static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<String, DataSource>(2);
        result.put("ds_0", createDataSource("ds_0"));
        result.put("ds_1", createDataSource("ds_1"));
        return result;
    }

    private static DataSource createDataSource(String dataSourceName) {
        BasicDataSource result = new BasicDataSource();
        result.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
        result.setUrl(String.format("jdbc:mysql://localhost:3306/%s", dataSourceName));
        result.setUsername("root");
        result.setPassword("root");
        return result;
    }
}
