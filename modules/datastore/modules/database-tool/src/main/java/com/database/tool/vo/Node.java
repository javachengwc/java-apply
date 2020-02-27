package com.database.tool.vo;

import java.util.List;

public class Node {

    private Integer id;

    private String text;

    private List<Node> children;

    private NodeType type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", children=" + children +
                ", type=" + type +
                '}';
    }

    public static class NodeBuilder {
        private Integer id;
        private String text;
        private List<Node> children;
        private NodeType type;

        private NodeBuilder() {
        }

        public static NodeBuilder newInstance() {
            return new NodeBuilder();
        }

        public NodeBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public NodeBuilder text(String text) {
            this.text = text;
            return this;
        }

        public NodeBuilder children(List<Node> children) {
            this.children = children;
            return this;
        }

        public NodeBuilder type(NodeType type) {
            this.type = type;
            return this;
        }

        public Node build() {
            Node node = new Node();
            node.setId(id);
            node.setText(text);
            node.setChildren(children);
            node.setType(type);
            return node;
        }
    }
}
