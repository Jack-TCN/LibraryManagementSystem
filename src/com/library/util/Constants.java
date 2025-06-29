package com.library.util;

public class Constants {

    // 系统常量
    public static final String SYSTEM_TITLE = "学校图书销售管理系统";
    public static final String VERSION = "1.0";

    // 默认值
    public static final String DEFAULT_PASSWORD = "123456";
    public static final int DEFAULT_BORROW_DAYS = 30;
    public static final double FINE_PER_DAY = 0.5;

    // 状态常量
    public static final String STATUS_NORMAL = "正常";
    public static final String STATUS_LOST = "挂失";
    public static final String STATUS_CANCELLED = "注销";

    public static final String BORROW_STATUS_BORROWING = "借阅中";
    public static final String BORROW_STATUS_RETURNED = "已归还";
    public static final String BORROW_STATUS_OVERDUE = "已逾期";

    // 性别常量
    public static final String GENDER_MALE = "男";
    public static final String GENDER_FEMALE = "女";

    // 表格列名
    public static final String[] BOOK_COLUMN_NAMES = {
            "图书ID", "ISBN", "书名", "作者", "出版社", "类型", "价格", "库存", "总库存"
    };

    public static final String[] READER_COLUMN_NAMES = {
            "读者ID", "姓名", "性别", "读者类型", "电话", "邮箱", "注册日期", "状态"
    };

    public static final String[] BORROW_COLUMN_NAMES = {
            "记录ID", "读者姓名", "图书名称", "借阅日期", "应还日期", "归还日期", "状态", "逾期天数", "罚款"
    };

    public static final String[] SALE_COLUMN_NAMES = {
            "编号", "客户姓名", "图书名称", "数量", "销售日期", "总价"
    };

    // 消息常量
    public static final String MSG_LOGIN_SUCCESS = "登录成功！";
    public static final String MSG_LOGIN_FAILED = "用户名或密码错误！";
    public static final String MSG_SAVE_SUCCESS = "保存成功！";
    public static final String MSG_DELETE_SUCCESS = "删除成功！";
    public static final String MSG_BORROW_SUCCESS = "借书成功！";
    public static final String MSG_RETURN_SUCCESS = "还书成功！";

    // 错误消息
    public static final String ERROR_EMPTY_FIELD = "请填写所有必填字段！";
    public static final String ERROR_INVALID_FORMAT = "输入格式不正确！";
    public static final String ERROR_DATABASE = "数据库操作失败！";
}
