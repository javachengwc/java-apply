import com.ocean.jdbc.ShardDataSource;
import com.ocean.shard.model.Table;
import com.ocean.shard.rule.DataSourceRule;
import com.ocean.shard.rule.ShardRule;
import com.ocean.shard.rule.TableRule;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/**
 *
 */
public class Main {

    public static void main(String[] args) throws Exception{
        ShardDataSource dataSource = getShardDataSource();
        Map<String, DatabaseMetaData> metaDataMap =dataSource.getDatabaseMetaDataMap();
        System.out.println("----------------metaData-------------");
        for(String key:metaDataMap.keySet())
        {
            System.out.println(key+"-->"+metaDataMap.get(key));
        }
        //select(dataSource);
        System.out.println("-----------------------------");
        groupBy(dataSource);
    }

    private static void select(final DataSource dataSource){

        String sql = "SELECT b.product_id,b.per_price,b.count FROM t_order a JOIN t_order_item b ON a.order_id=b.order_id WHERE a.user_id=? order by b.count desc limit 2,2";
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, 1);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getInt(1)+" "+rs.getInt(2)+" "+rs.getInt(3));
            }
        }catch(Exception e)
        {
            e.printStackTrace(System.out);
        }

    }

    private static void groupBy(final DataSource dataSource){

        String sql = "SELECT a.user_id, sum(a.amount) as amount  FROM t_order a JOIN t_order_item b ON a.order_id=b.order_id GROUP BY a.user_id";
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            System.out.println("---------groupBy get resultset");
            while (rs.next()) {
                System.out.println("--------------user_id: " + rs.getInt(1) +",amount:"+ rs.getInt(2));
            }
        }catch(Exception e)
        {
            e.printStackTrace(System.out);
        }
    }

    private static ShardDataSource getShardDataSource() throws Exception{

        Map<String, DataSource> dataSourceMap = createDataSourceMap();

        Table order0 = new Table("ds_0","t_order_0","t_order");
        Table order1 = new Table("ds_1","t_order_1","t_order");
        List<Table> orderTableList= new ArrayList<Table>();
        orderTableList.add(order0);
        orderTableList.add(order1);

        Table orderItem0 = new Table("ds_0","t_order_item_0","t_order_item");
        Table orderItem1 = new Table("ds_1","t_order_item_1","t_order_item");
        List<Table> orderItemList= new ArrayList<Table>();
        orderItemList.add(orderItem0);
        orderItemList.add(orderItem1);

        TableRule orderTableRule = new TableRule("t_order",orderTableList);
        TableRule orderItemTableRule = new TableRule("t_order_item", orderItemList);

        DataSourceRule dataSourceRule= new DataSourceRule(dataSourceMap);

        ShardRule shardRule = new ShardRule(dataSourceRule, Arrays.asList(orderTableRule, orderItemTableRule));
        return new ShardDataSource(shardRule);
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
