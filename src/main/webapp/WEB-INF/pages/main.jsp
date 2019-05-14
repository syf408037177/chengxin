<%--
  Created by IntelliJ IDEA.
  User: SYF
  Date: 2019/5/10
  Time: 17:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="common/common.jsp"%>
<html>
<head>
    <title>主页面</title>
</head>
<body class="easyui-layout">
    <div data-options="region:'north',split:true" style="height:100px;background-color: greenyellow;
            display: -moz-box;/*兼容Firefox*/
            display: -webkit-box;/*兼容FSafari、Chrome*/
            -moz-box-align: center;/*兼容Firefox*/
            -webkit-box-align: center;/*兼容FSafari、Chrome */
            -moz-box-pack: center;/*兼容Firefox*/
            -webkit-box-pack: center;/*兼容FSafari、Chrome */">
        <span style="font-size: 50px;font-family: 楷体;color: #0000FF" >查询界面</span>
    </div>
    <!-- 左侧菜单 -->
    <div data-id="leftmenu" style="width:150px;overflow:auto;" data-options="region:'west',split:false,collapsible:false,style:{textAlign:'left'},title:'菜单'">
        <ul id="menutree">
            <li id="purchaseInQuery">采购入库查询</li>
            <li>采购入库查询</li>
            <li>采购入库查询</li>
            <li>采购入库查询</li>
            <li>采购入库查询</li>
            <li>采购入库查询</li>
        </ul>
    </div>
    <!--主体内容-->
    <div data-options="region:'center',border:false" style="width:100%!important;">
        <div id="mainTab" class="easyui-tabs" fit="true"></div>
    </div>
</body>
</html>
