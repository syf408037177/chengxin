$(function () {
    /*采购入库菜单点击*/
    $("#purchaseInQuery").click(function () {
        if($("#mainTab").tabs('exists', '采购入库')) {
            $("#mainTab").tabs('select', '采购入库');
        } else {
            $("#mainTab").tabs("add", {
                title : '采购入库',
                closable : true,
                tools:[{
                    iconCls:'icon-reload',
                    handler:function(){
                        $("#mainTab").tabs('getTab','采购入库').panel('refresh');
                    }
                }],
                href : rootPath + '/purchaseIn/init'
            })
        }
    });
})