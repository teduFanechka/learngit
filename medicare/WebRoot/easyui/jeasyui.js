var jeasyui = {
    Tabs: {},                                           //选项卡
    Messager: {},                                     //消息框
    Redirect: {},                                         //重定向
    Window: {}                                          //窗口
};
//////////////参考文档.......////////////////////////////////////////////////////////////////////
/*
选项卡
id                          easyui标签的ID
maxlength             设置选项卡最大个数
*/
jeasyui.Tabs = function (id, maxlength) {
    this.id = id;
    this.maxlength = maxlength;
    this.currTabCount = 1;
    //自动关闭选项卡函数
    this.autoCloseTab = function () {
        $('#' + this.id).tabs('close', 1);
    };
    //关闭指定选项卡函数
    this.CloseTab = function (title) {
        $('#' + this.id).tabs('close', title);
    };
    //关闭除选中外所有选项卡函数
    this.CloseAllTabExceptThis = function (title) {
        var alltabs = $('#' + this.id).tabs('tabs');
        var currtab = $('#' + this.id).tabs("getTab", title);
        var titlelist = new Array();
        var listcount = 0;
        for (var i = 0; i < alltabs.length; i++) {
            if (alltabs[i] != currtab && alltabs[i].panel('options').title != "首页") {
                titlelist[listcount] = alltabs[i].panel('options').title;
                listcount++;
            }
        }
        for (var j = 0; j < listcount; j++) {
            $('#' + this.id).tabs('close', titlelist[j]);
        }
    };
};
//添加一个选项卡
jeasyui.Tabs.prototype.addTab = function (titleName, url) {
    if (!this.exists(titleName)) {
        var iframe = $('<iframe style="width:100%;height:100%;border:0" />');
        $('#' + this.id).tabs('add', {
            title: titleName,
            content: iframe,
            //href:url,
            closable: true,
            cache: false,
            fit: true
        });
        iframe.attr('src', url);
    } else {
        this.selectTab(titleName);
    }
};
//选中指定选项卡（参数titleName:选项卡标题名）
jeasyui.Tabs.prototype.selectTab = function (titleName) {
    $('#'+this.id).tabs('select', titleName);
};
//获取当前选项卡
jeasyui.Tabs.prototype.getSelected = function () {
    return $('#' + this.id).tabs('getSelected');
};
//刷新选项卡（参数tab:选项卡）
jeasyui.Tabs.prototype.refresh = function (currTab) {
    var content=currTab.panel('options').content;
    if (content==null) return;
    var url = content.attr('src');
    content.attr('src', url);
};
//验证选项卡是否存在（参数titleName:选项卡标题名）
jeasyui.Tabs.prototype.exists = function (titleName) {
    var tab = $('#' + this.id).tabs('exists', titleName);
    return tab;
};


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
jeasyui.Messager = {};

//提示框

jeasyui.Messager.Show = function (message, titleName, time, type) {
    titleName = titleName || '提示';
    type = type || 'slide';
    time = time || 5000;
    $.messager.show({
        title: titleName,
        msg: message,
        timeout: time,
        showType: type
    });
};

//弹出提示信息
jeasyui.Messager.Alert = function (msg, title, type) {
    title = title || '提示';
    type = type || 'info';
    $.messager.alert(title, msg, type);
};

//弹出提示确认后重定向
jeasyui.Messager.ConfirmAndRedirect = function (msg, title, url) {
    title = title || '提示';
    $.messager.confirm(title, msg, function (r) {
        if (r) {
            location.href = url;
        }
    });
};

//弹出提示确认后关闭窗口
jeasyui.Messager.ConfirmAndClose = function (msg, title) {
    title = title || '提示';
    $.messager.confirm(title, msg, function (r) {
        if (r) {
            window.close();
            parent.location.href = parent.location.href;
        }
    });
};

//弹出提示信息后父窗体重定向
jeasyui.Messager.MRedirect = function (msg, url, title ) {
    title = title || '提示';
    $.messager.alert(title, msg, 'info', function () {
            location.href = url;
    });
};

//弹出提示信息后重定向
jeasyui.Messager.Redirect = function (msg, url, title) {
    title = title || '提示';
    $.messager.alert(title, msg, 'info', function () {
        var selectedTab = $('#tabs').tabs('getSelected');
        selectedTab.panel('refresh', url);
    });
};

//进度条开启
jeasyui.Messager.ProgressOpen=function () {
    $.messager.progress({
        msg: '正在处理，请稍后...',
        interval: 100
    });
}

//进度条关闭
jeasyui.Messager.ProgressClose=function () {
    $.messager.progress('close');
}



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//重定向
jeasyui.Redirect = {};

jeasyui.Redirect.TabRedirect = function (url) {
    var selectedTab = $('#tabs').tabs('getSelected');
    selectedTab.panel('refresh', url);
};


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//显示窗口，自动居中
jeasyui.Window.ShowModal = function (title, windowurl, width, height) {
    window.parent.ShowModal(title, windowurl, width, height);
}

//关闭窗口
jeasyui.Window.CloseModal = function () {
	window.parent.CloseModal();
}