/*该方法使日期列的显示符合阅读习惯*/
//datagrid中用法：{ field:'StartDate',title:'开始日期', formatter: formatDatebox, width:80}
function formatDatebox(value) {
	if (value == null || value == '') {
		return '';
	}
	var dt = parseToDate(value);
	return dt.format("yyyy-MM-dd");
}

/* 转换日期字符串为带时间的格式 */
function formatDateBoxFull(value) {
	if (value == null || value == '') {
		return '';
	}
	var dt = parseToDate(value);
	return dt.format("yyyy-MM-dd hh:mm:ss");
}

// 辅助方法(转换日期)
function parseToDate(value) {
	if (value == null || value == '') {
		return undefined;
	}

	var dt;
	if (value instanceof Date) {
		dt = value;
	} else {
		if (!isNaN(value)) {
			dt = new Date(value);
		} else if (value.indexOf('/Date') > -1) {
			value = value.replace(/\/Date\((-?\d+)\)\//, '$1');
			dt = new Date();
			dt.setTime(value);
		} else if (value.indexOf('/') > -1) {
			dt = new Date(Date.parse(value.replace(/-/g, '/')));
		} else {
			dt = new Date(value);
		}
	}
	return dt;
}

// 为Date类型拓展一个format方法，用于格式化日期
Date.prototype.format = function(format) // author: meizz
{
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
};

// 以下拓展是为了datagrid的日期列在编辑状态下显示正确日期
$.extend($.fn.datagrid.defaults.editors, {
	datebox : {
		init : function(container, options) {
			var input = $('<input type="text">').appendTo(container);
			input.datebox(options);
			return input;
		},
		destroy : function(target) {
			$(target).datebox('destroy');
		},
		getValue : function(target) {
			return $(target).datebox('getValue');
		},
		setValue : function(target, value) {
			$(target).datebox('setValue', formatDatebox(value));
		},
		resize : function(target, width) {
			$(target).datebox('resize', width);
		}
	},
	datetimebox : {
		init : function(container, options) {
			var input = $('<input type="text">').appendTo(container);
			input.datetimebox(options);
			return input;
		},
		destroy : function(target) {
			$(target).datetimebox('destroy');
		},
		getValue : function(target) {
			return $(target).datetimebox('getValue');
		},
		setValue : function(target, value) {
			$(target).datetimebox('setValue', formatDateBoxFull(value));
		},
		resize : function(target, width) {
			$(target).datetimebox('resize', width);
		}
	}
});

/*
 * jsp页面中 调用测试 <script> var date = new Date(); alert(date);
 * alert(formatDatebox(1450247972000)); alert(date.format("yyyy-MM-dd
 * hh:mm:ss")); </script>
 */
//以上方法可以解析Long类型时间格式到datagrid,并修改d:/文档/utils/Dateboxform.zip>中原文件使用说明 
/********************华丽分割线*************************/
//datagrid Timestramp时间格式化 只能解析显示
function formattime(val) {
	var year = parseInt(val.year) + 1900;
	var month = (parseInt(val.month) + 1);
	month = month > 9 ? month : ('0' + month);
	var date = parseInt(val.date);
	date = date > 9 ? date : ('0' + date);
	var hours = parseInt(val.hours);
	hours = hours > 9 ? hours : ('0' + hours);
	var minutes = parseInt(val.minutes);
	minutes = minutes > 9 ? minutes : ('0' + minutes);
	var seconds = parseInt(val.seconds);
	seconds = seconds > 9 ? seconds : ('0' + seconds);
	var time = year + '-' + month + '-' + date + ' ' + hours + ':' + minutes
			+ ':' + seconds;
	return time;
}