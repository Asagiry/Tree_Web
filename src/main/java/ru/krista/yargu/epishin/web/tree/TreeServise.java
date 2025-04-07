package ru.krista.yargu.epishin.web.tree;

import ru.krista.yargu.epishin.exceptions.tree.ClearDBTreeException;
import ru.krista.yargu.epishin.exceptions.tree.EmptyDBTreeException;
import ru.krista.yargu.epishin.exceptions.tree.InitDBTreeException;
import ru.krista.yargu.epishin.exceptions.tree.LoadDBTreeException;
import ru.krista.yargu.epishin.exceptions.tree.SaveDBTreeException;
import ru.krista.yargu.epishin.tree.Node;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TreeServise {
    private static final String JDBC_URL = "jdbc:h2:file:./tree_database;AUTO_SERVER=TRUE";
    private Node tree = null;
    private final Map<UUID,Node> nodes = new HashMap<>();
    private final Map<UUID,UUID> parentRelations = new HashMap<>();

    public TreeServise() throws EmptyDBTreeException, InitDBTreeException, LoadDBTreeException {
        createTable();
        loadTree();
    }
    public void createTable() throws InitDBTreeException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL);
             Statement statement = connection.createStatement()) {

            String sql = "CREATE TABLE IF NOT EXISTS TREE_TABLE (" +
                    "id VARCHAR(36) PRIMARY KEY," +
                    "parent_id VARCHAR(36)," +
                    "name VARCHAR(255)," +
                    "FOREIGN KEY (parent_id) REFERENCES TREE_TABLE (id))";

            statement.execute(sql);

        } catch (SQLException e) {
            throw new InitDBTreeException("Failed to initialize database", e);
        }
    }
    private void loadTree() throws EmptyDBTreeException, LoadDBTreeException {

        loadNodes();

        createTreeFromNodes();

    }
    private void loadNodes() throws LoadDBTreeException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            String query = "SELECT id, parent_id, name FROM TREE_TABLE";
            try (Statement statement = connection.createStatement();
                 ResultSet rs = statement.executeQuery(query)) {

                while (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    String parentIdStr = rs.getString("parent_id");
                    UUID parentId = parentIdStr != null ? UUID.fromString(parentIdStr) : null;
                    String name = rs.getString("name");

                    Node node = new Node(name);
                    node.setId(id);
                    nodes.put(id, node);
                    parentRelations.put(id, parentId);
                }
            }
        } catch (SQLException e) {
            throw new LoadDBTreeException("Не удалось загрузить вершины дерева");
        }
    }
    private void createTreeFromNodes() throws EmptyDBTreeException {
        for (Map.Entry<UUID, Node> entry : nodes.entrySet()) {
            UUID nodeId = entry.getKey();
            UUID parentId = parentRelations.get(nodeId);

            if (parentId == null) {
                tree = entry.getValue();
            } else {
                Node parent = nodes.get(parentId);
                if (parent != null) {
                    parent.addChild(entry.getValue());
                }
            }
        }
        if (tree == null)
            throw new EmptyDBTreeException("База данных пуста");
    }

    private void clearDatabase() throws ClearDBTreeException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL);
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM TREE_TABLE");
        } catch (SQLException e) {
            throw new ClearDBTreeException("Failed to clear tree data", e);
        }
    }

    public void save() throws SaveDBTreeException {

        try {
            clearDatabase();
        } catch (ClearDBTreeException e) {
            throw new SaveDBTreeException("Не удалось очистить базу перед сохранением");
        }

        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            String insertSQL = "INSERT INTO TREE_TABLE (id, parent_id, name) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                pstmt.setString(1, tree.getId().toString());
                pstmt.setNull(2, Types.VARCHAR);
                pstmt.setString(3, tree.getName());
                pstmt.executeUpdate();
                saveChildrenRecursive(connection, tree, pstmt);
            }
        } catch (SQLException e) {
            throw new SaveDBTreeException("Не удалось сохранить базу данных");
        }
    }

    private static void saveChildrenRecursive(Connection connection, Node parent, PreparedStatement pstmt)
            throws SQLException {
        for (Node child : parent.getChildren()) {
            pstmt.setString(1, child.getId().toString());
            pstmt.setString(2, parent.getId().toString());
            pstmt.setString(3, child.getName());
            pstmt.executeUpdate();

            saveChildrenRecursive(connection, child, pstmt);
        }
    }

    public Node getTree(){
        return tree;
    }


}
