package com.database.tool.service.impl;

import com.database.tool.service.DbService;
import com.database.tool.vo.Field;
import com.database.tool.vo.Node;
import com.database.tool.vo.NodeType;
import com.database.tool.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Service
public class DbServiceImpl implements DbService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbServiceImpl.class);

    public static final String DB_NAME = "mysql";
    public static final String TABLE_CAT = "TABLE_CAT";
    public static final String TABLE_NAME = "TABLE_NAME";
    public static final String COLUMN_NAME = "COLUMN_NAME";
    public static final String TABLE_COLUMNS = "table:columns:";
    public static final String DB_TABLE_NODES = "db:table:nodes";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<?> findAllNodes() {
        List<?> list = (List<?>) redisTemplate.opsForValue().get(DB_TABLE_NODES);
        if (isNotEmpty(list)) {
            return list;
        }
        List<Node> result = new ArrayList<>();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
//            List<String> catalogs = getCatalogs(connection);
            List<String> catalogs = Arrays.asList(DB_NAME);
            for (String catalog : catalogs) {
                Node.NodeBuilder builder = Node.NodeBuilder.newInstance().text(catalog).type(NodeType.DB);
                ResultSet rs = connection.getMetaData().getTables(catalog, null, "%", null);
                List<Node> children = new ArrayList<>();
                while (rs.next()) {
                    String tableName = rs.getString(TABLE_NAME);
                    children.add(Node.NodeBuilder.newInstance().text(tableName).type(NodeType.TABLE).build());
                }
                Node node = builder.children(children).build();
                result.add(node);
            }
        } catch (Exception e) {
            LOGGER.error("error:", e);
            throw new RuntimeException(e);
        }
        redisTemplate.opsForValue().set(DB_TABLE_NODES, result);
        return result;
    }

    @Override
    public Result query(String sql) {
        String originalSql = sql;
        final Result result = new Result();
        final List<Field> fields = new ArrayList<>();
        if (!sql.toLowerCase().contains("limit")) {
            sql += " limit 0,10000";
        }
        List<Map<String, Object>> rows = jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> row = new HashMap<>();
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = metaData.getColumnName(i);
                    if (rowNum == 0) {
                        fields.add(new Field(columnName));
                    }
                    Object obj = rs.getObject(columnName);
                    row.put(columnName, obj == null ? null : obj.toString());
                }
                return row;
            }
        });
        if (rows.size() < 10000) {
            result.setCount(rows.size());
        } else {
            String countSql = String.format("select count(1) from (%s) tmp_table", originalSql);
            result.setCount(jdbcTemplate.queryForObject(countSql,Long.class));
        }
        result.setFields(fields);
        result.setRows(rows);
        return result;
    }

    @Override
    public List<?> getColumns(String tableName) {
        List<?> list = (List<?>) redisTemplate.opsForHash().get(TABLE_COLUMNS, tableName);
        if (isNotEmpty(list)) {
            return list;
        }
        List<Field> result = new ArrayList<>();
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            ResultSet rs = connection.getMetaData().getColumns(DB_NAME, null, tableName, null);
            while (rs.next()) {
                String columnName = rs.getString(COLUMN_NAME);
                result.add(new Field(columnName));
            }
        } catch (Exception e) {
            LOGGER.error("error:", e);
            throw new RuntimeException(e);
        }
        redisTemplate.opsForHash().put(TABLE_COLUMNS, tableName, result);
        return result;
    }

    @Override
    public List<?> refresh(Node node) {
        if (node.getType() == NodeType.DB) {
            redisTemplate.opsForValue().set(DB_TABLE_NODES, null);
            return findAllNodes();
        } else if (node.getType() == NodeType.TABLE) {
            redisTemplate.opsForHash().delete(TABLE_COLUMNS, node.getText());
            return getColumns(node.getText());
        }
        return Collections.emptyList();
    }

    private List<String> getCatalogs(Connection connection) throws SQLException {
        List<String> result = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getCatalogs();
        while (rs.next()) {
            result.add(rs.getString(TABLE_CAT));
        }
        return result;
    }
}
