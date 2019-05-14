<%--
  Created by IntelliJ IDEA.
  User: SYF
  Date: 2019/5/14
  Time: 15:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="common/common.jsp"%>
<html>
<head>
    <title>采购入库</title>
</head>
<body>
    <div id="purchaseInTbl">
        <div class="easyui-accordion">
            <h3>查询条件</h3>
        </div>
        <form action="#" id="formFindPurchaseIn">
            <div id="aa" class="easyui-accordion">
                <div title="" data-options="selected:true">
                    <ul class="search-form">
                        <li><input id="sourceOrgCode" class="easyui-textbox" data-options=" label:'始发节点',prompt:'编号/名称模糊匹配'" style="width: 250px"></li>
                        <li><input id="destOrgCode" class="easyui-textbox" data-options=" label:'目的节点',prompt:'编号/名称模糊匹配'" style="width: 250px"></li>
                        <li><input id="sourceProvCode" class="easyui-combobox" label="始发省" labelPosition="left" data-options="prompt:'全网'"></li>
                        <li><input id="destProvCode" class="easyui-combobox" label="目的省" labelPosition="left" data-options="prompt:'全网'"></li>
                        <li><input id="sourceMasterNodeCode" class="easyui-textbox" data-options="label:'始发所属主节点',prompt:'编号/名称模糊匹配'" style="width: 250px"></li>
                        <li><input id="sourceFirstNodeCode" class="easyui-textbox" data-options="label:'始发归属一级节点',prompt:'编号/名称模糊匹配'" style="width: 250px"></li>
                        <li><input id="sourceNodeLevel" class="easyui-combobox" label="始发节点层级" labelPosition="left" data-options="prompt:'全部'"></li>
                        <li><input id="sourceNodeAttri" class="easyui-combobox" label='始发节点属性' labelPosition="left" data-options="prompt:'全部'"></li>
                        <li><input id="destMasterNodeCode" class="easyui-textbox" data-options="label:'目的所属主节点',prompt:'编号/名称模糊匹配'" style="width: 250px"></li>
                        <li><input id="destFirstNodeCode" class="easyui-textbox" data-options="label:'目的归属一级节点',prompt:'编号/名称模糊匹配'" style="width: 250px"></li>
                        <li><input id="destNodeLevel" class="easyui-combobox" label="目的节点层级" labelPosition="left" data-options="prompt:'全部'"></li>
                        <li><input id="destNodeAttri" class="easyui-combobox" label='目的节点属性' labelPosition="left" data-options="prompt:'全部'"></li>
                        <li><input id="effective" class="easyui-combobox" label='时效标识' labelPosition="left" data-options="prompt:'全部'" style="width: 250px"></li>
                        <li><input id="status" class="easyui-combobox" label="规则状态" labelPosition="left" data-options="prompt:'全部'"></li>
                    </ul>
                    <div style="float: right;" class="toolbar-margin">
                        <a href="#" class="easyui-linkbutton search" onclick="findRouteClassesData()">查询</a>
                        <a href="#" class="easyui-linkbutton export" onclick="exportData()">导出Excel</a>
                        <a href="#" class="easyui-linkbutton reset" onclick="resetQuery()">重置</a>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <div class="easyui-accordion" style="border-bottom: 1px solid #ccc">
        <h3>采购入库数据列表</h3>
    </div>
    <table id="dgRouteClasses" class="easyui-datagrid"></table>
</body>
</html>
