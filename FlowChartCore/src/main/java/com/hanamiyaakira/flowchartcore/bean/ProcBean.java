package com.hanamiyaakira.flowchartcore.bean;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author 彬ちゃん
 * @Date 2019/3/12
 */
public class ProcBean {

    /**
     * uuid : 0ef4fb445844463b96de83b3c271e9f4
     * processKey : test_process
     * processGroup : null
     * remark : null
     * name : 测试流程
     * description : null
     * flowLines : [{"id":34,"lineKey":null,"fromNode":"test_eg","toNode":"test_end","condition":"${@approveResult@ == \"invalid\"}","description":null,"name":"无效","defaultFlow":false,"fromAnchor":3,"toAnchor":3},{"id":33,"lineKey":null,"fromNode":"test_eg","toNode":"test_end","condition":"${@approveResult@ == \"true\"}","description":null,"name":"通过","defaultFlow":false,"fromAnchor":2,"toAnchor":0},{"id":32,"lineKey":null,"fromNode":"test_eg","toNode":"test_kfzy","condition":"${@approveResult@ == \"false\"}","description":null,"name":"打回修改","defaultFlow":false,"fromAnchor":1,"toAnchor":1},{"id":31,"lineKey":null,"fromNode":"test_dzzy","toNode":"test_eg","condition":null,"description":null,"name":null,"defaultFlow":false,"fromAnchor":2,"toAnchor":0},{"id":30,"lineKey":null,"fromNode":"test_kfzy","toNode":"test_dzzy","condition":null,"description":null,"name":null,"defaultFlow":false,"fromAnchor":2,"toAnchor":0},{"id":29,"lineKey":null,"fromNode":"test_start","toNode":"test_kfzy","condition":null,"description":null,"name":null,"defaultFlow":false,"fromAnchor":2,"toAnchor":0}]
     * nodePositions : [{"nodeKey":"test_end","x":335,"y":638},{"nodeKey":"test_eg","x":335,"y":497},{"nodeKey":"test_dzzy","x":335,"y":351},{"nodeKey":"test_kfzy","x":335,"y":225},{"nodeKey":"test_start","x":335,"y":97}]
     * nodes : [{"uuid":"e792e3c4b5e14be3b302f65cb5191b05","name":"结束节点","nodeKey":"test_end","remark":null,"description":"结束节点","type":"E","indexCheckExp":null,"roles":null,"formProperties":null,"persistence":false,"needAssignHandler":false},{"uuid":"ed34b2f4228640b698ade64d4629ccf0","name":"审核结果","nodeKey":"test_eg","remark":null,"description":"审核结果","type":"EG","indexCheckExp":null,"roles":null,"formProperties":null,"persistence":false,"needAssignHandler":false},{"uuid":"a2062329bb3048f8a18f3d9495a0d940","name":"单证专员审核","nodeKey":"test_dzzy","remark":null,"description":"单证专员审核","type":"UT","indexCheckExp":null,"roles":null,"formProperties":null,"persistence":false,"needAssignHandler":false},{"uuid":"0129686b876b4e49aff6cf89656bc16e","name":"客服专员提交申请","nodeKey":"test_kfzy","remark":null,"description":"客服专员提交申请","type":"UT","indexCheckExp":null,"roles":null,"formProperties":null,"persistence":false,"needAssignHandler":false},{"uuid":"5530348db8f54376a7d1e968be57f87e","name":"开始节点","nodeKey":"test_start","remark":null,"description":"开始节点","type":"S","indexCheckExp":null,"roles":null,"formProperties":null,"persistence":false,"needAssignHandler":false}]
     * version : 0
     * newVersion : false
     */

    private String uuid;
    private String processKey;
    private Object processGroup;
    private Object remark;
    private String name;
    private Object description;
    private int version;
    private boolean newVersion;
    private String createdAt;
    private String updatedAt;
    private String approveType;
    private boolean defaultProcess;
    private List<FlowLinesBean> flowLines;
    private LinkedList<NodePositionsBean> nodePositions;
    private List<NodesBean> nodes;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public Object getProcessGroup() {
        return processGroup;
    }

    public void setProcessGroup(Object processGroup) {
        this.processGroup = processGroup;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isNewVersion() {
        return newVersion;
    }

    public void setNewVersion(boolean newVersion) {
        this.newVersion = newVersion;
    }

    public List<FlowLinesBean> getFlowLines() {
        return flowLines;
    }

    public void setFlowLines(List<FlowLinesBean> flowLines) {
        this.flowLines = flowLines;
    }

    public LinkedList<NodePositionsBean> getNodePositions() {
        return nodePositions;
    }

    public void setNodePositions(LinkedList<NodePositionsBean> nodePositions) {
        this.nodePositions = nodePositions;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getApproveType() {
        return approveType;
    }

    public void setApproveType(String approveType) {
        this.approveType = approveType;
    }

    public boolean isDefaultProcess() {
        return defaultProcess;
    }

    public void setDefaultProcess(boolean defaultProcess) {
        this.defaultProcess = defaultProcess;
    }

    public List<NodesBean> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodesBean> nodes) {
        this.nodes = nodes;
    }

    public static class FlowLinesBean {
        /**
         * id : 34
         * lineKey : null
         * fromNode : test_eg
         * toNode : test_end
         * condition : ${@approveResult@ == "invalid"}
         * description : null
         * name : 无效
         * defaultFlow : false
         * fromAnchor : 3
         * toAnchor : 3
         */

        private int id;
        private Object lineKey;
        private String fromNode;
        private String toNode;
        private String condition;
        private Object description;
        private String name;
        private boolean defaultFlow;
        private int fromAnchor;
        private int toAnchor;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Object getLineKey() {
            return lineKey;
        }

        public void setLineKey(Object lineKey) {
            this.lineKey = lineKey;
        }

        public String getFromNode() {
            return fromNode;
        }

        public void setFromNode(String fromNode) {
            this.fromNode = fromNode;
        }

        public String getToNode() {
            return toNode;
        }

        public void setToNode(String toNode) {
            this.toNode = toNode;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public Object getDescription() {
            return description;
        }

        public void setDescription(Object description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isDefaultFlow() {
            return defaultFlow;
        }

        public void setDefaultFlow(boolean defaultFlow) {
            this.defaultFlow = defaultFlow;
        }

        public int getFromAnchor() {
            return fromAnchor;
        }

        public void setFromAnchor(int fromAnchor) {
            this.fromAnchor = fromAnchor;
        }

        public int getToAnchor() {
            return toAnchor;
        }

        public void setToAnchor(int toAnchor) {
            this.toAnchor = toAnchor;
        }

        @Override
        public String toString() {
            return "FlowLinesBean{" +
                    "id=" + id +
                    ", lineKey=" + lineKey +
                    ", fromNode='" + fromNode + '\'' +
                    ", toNode='" + toNode + '\'' +
                    ", condition='" + condition + '\'' +
                    ", description=" + description +
                    ", name='" + name + '\'' +
                    ", defaultFlow=" + defaultFlow +
                    ", fromAnchor=" + fromAnchor +
                    ", toAnchor=" + toAnchor +
                    '}';
        }
    }

    public static class NodePositionsBean {
        /**
         * nodeKey : test_end
         * x : 335
         * y : 638
         */

        private String nodeKey;
        private String name;
        private int x;
        private int y;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getNodeKey() {
            return nodeKey;
        }

        public void setNodeKey(String nodeKey) {
            this.nodeKey = nodeKey;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public String toString() {
            return "NodePositionsBean{" +
                    "nodeKey='" + nodeKey + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

    public static class NodesBean {
        /**
         * uuid : e792e3c4b5e14be3b302f65cb5191b05
         * name : 结束节点
         * nodeKey : test_end
         * remark : null
         * description : 结束节点
         * type : E
         * indexCheckExp : null
         * roles : null
         * formProperties : null
         * persistence : false
         * needAssignHandler : false
         */

        private String uuid;
        private String name;
        private String nodeKey;
        private Object remark;
        private String description;
        private String type;
        private Object indexCheckExp;
        private Object roles;
        //这个值好像是不用用到的
        private Object formProperties;
        private boolean persistence;
        private boolean needAssignHandler;
        private int series;
        private int lineState;
        //线上面的文字描述
        private String leftLineAppendix;
        private String rightLineAppendix;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNodeKey() {
            return nodeKey;
        }

        public void setNodeKey(String nodeKey) {
            this.nodeKey = nodeKey;
        }

        public Object getRemark() {
            return remark;
        }

        public void setRemark(Object remark) {
            this.remark = remark;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getIndexCheckExp() {
            return indexCheckExp;
        }

        public void setIndexCheckExp(Object indexCheckExp) {
            this.indexCheckExp = indexCheckExp;
        }

        public Object getRoles() {
            return roles;
        }

        public void setRoles(Object roles) {
            this.roles = roles;
        }

        public Object getFormProperties() {
            return formProperties;
        }

        public void setFormProperties(Object formProperties) {
            this.formProperties = formProperties;
        }

        public boolean isPersistence() {
            return persistence;
        }

        public void setPersistence(boolean persistence) {
            this.persistence = persistence;
        }

        public boolean isNeedAssignHandler() {
            return needAssignHandler;
        }

        public void setNeedAssignHandler(boolean needAssignHandler) {
            this.needAssignHandler = needAssignHandler;
        }

        public int getSeries() {
            return series;
        }

        public void setSeries(int series) {
            this.series = series;
        }

        public int getLineState() {
            return lineState;
        }

        public void setLineState(int lineState) {
            this.lineState = lineState;
        }

        public String getLeftLineAppendix() {
            return leftLineAppendix;
        }

        public void setLeftLineAppendix(String leftLineAppendix) {
            this.leftLineAppendix = leftLineAppendix;
        }

        public String getRightLineAppendix() {
            return rightLineAppendix;
        }

        public void setRightLineAppendix(String rightLineAppendix) {
            this.rightLineAppendix = rightLineAppendix;
        }

        @Override
        public String toString() {
            return "NodesBean{" +
                    "uuid='" + uuid + '\'' +
                    ", name='" + name + '\'' +
                    ", nodeKey='" + nodeKey + '\'' +
                    ", remark=" + remark +
                    ", description='" + description + '\'' +
                    ", type='" + type + '\'' +
                    ", indexCheckExp=" + indexCheckExp +
                    ", roles=" + roles +
                    ", formProperties=" + formProperties +
                    ", persistence=" + persistence +
                    ", needAssignHandler=" + needAssignHandler +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ProcBean{" +
                "uuid='" + uuid + '\'' +
                ", processKey='" + processKey + '\'' +
                ", processGroup=" + processGroup +
                ", remark=" + remark +
                ", name='" + name + '\'' +
                ", description=" + description +
                ", version=" + version +
                ", newVersion=" + newVersion +
                ", flowLines=" + flowLines +
                ", nodePositions=" + nodePositions +
                ", nodes=" + nodes +
                '}';
    }
}
