# 概述

## 系统目标

该系统的名称为银行业务管理系统，主要目的是为银行内部提供一个管理支行、员工、客户、贷款等业务的数据库应用程序，并要求在为管理人员提供便利的同时保证数据的一致性、完整性和访问安全性。

## 需求说明

本系统的主要功能如下：

* 支行管理：提供支行所有信息的增、删、改、查功能；如果支行存在着关联信息，如员工、账户等，则不允许删除；

* 员工管理：提供支行员工所有信息的增、删、改、查功能；如果员工存在着关联数据，则不允许删除；

* 客户管理：提供客户所有信息的增、删、改、查功能；如果客户存在着关联账户或者贷款记录，则不允许删除；

* 账户管理：提供账户开户、销户、修改、查询功能，包括储蓄账户和支票账户；账户号不允许修改；

* 贷款管理：提供贷款信息的增、删、查功能，提供贷款发放功能；贷款信息一旦添加成功后不允许修改；要求能查询每笔贷款的当前状态（未开始发放、发放中、已全部发放）；处于发放中状态的贷款记录不允许删除；

* 业务统计：按业务分类（储蓄、贷款）和时间（月、季、年）统计各个支行的业务总金额和用户数（选做：对统计结果以饼图或曲线图显示）。

## 本报告的主要贡献

本报告将主要对该数据库应用的设计和具体实现进行描述，与此同时，还会提供一组测试数据及其结果。

# 总体设计

framework.dot

# 详细设计

# 前端 frontend 模块

输入: 用户输入

输出: 有查询数据的 HTML 页面

# 后端 controller 模块

输入：前端请求 (request)，例如:

```js
post:/api/subbbranch
data:{
    name:"bank1",
    money:10000,
    city:"city1"
}
```

输出：查询结果（若成功，则为序列化为 Json 的查询数据，若失败，则为错误原因）。一个成功的例子如下:

```js
{
    count:1,
    rows:[
        {name:"bank1",money:10000,city:"city1"},
        {name:"bank2",money:20000,city:"city2"}
    ]
}
```

# 后端 model 模块

输入：结构化的请求数据，例如 `case class UpdateReq(old_row:SubbranchRow,new_row:SUbbranchRow)`
 
输出：结构化的查询结果，例如 `Seq[SubbrachRow]` (支行记录的集合)

# 后端 view 模块

输入：HTML 模板和待填入模板的数据

输出：完成数据渲染的 HTML 页面

# 后端 myutils 模块

无状态的纯函数模块，为后端数据处理提供工具函数

# 实现与测试

## 实现结果

snapshop

## 测试结果

附件一的演示视频中详细演示了一组覆盖了大部分操作的测试样例。具体操作见演示视频，等价的 sql 语句如下：

```sql
INSERT INTO bankdb.account (account_id, bank_name, money, date) VALUES ('1526973164194', 'bank1', 1000, '2018-05-20');
INSERT INTO bankdb.account (account_id, bank_name, money, date) VALUES ('1526973174923', 'bank1', 1000, '2018-05-20');INSERT INTO bankdb.checking_account (account_id, bank_name, money, date, credit_line) VALUES ('1526973164194', 'bank1', 1000, '2018-05-20', 1000);INSERT INTO bankdb.customer_loan (id_card, loan_number) VALUES ('03', '1526973201470');
INSERT INTO bankdb.customer_loan (id_card, loan_number) VALUES ('04', '1526973201470');INSERT INTO bankdb.customer (id_card, staff_id_card, name, phone, address, contact_name, contact_phone, contact_email, contact_role, role) VALUES ('03', null, 'c1', '55', 'r', 'cc1', '133', 'xx@yy.com', 'f', null);
INSERT INTO bankdb.customer (id_card, staff_id_card, name, phone, address, contact_name, contact_phone, contact_email, contact_role, role) VALUES ('04', '01', 'c2', '55', 'r', 'cc1', '133', 'xx@yy.com', 'f', '贷款负责人');INSERT INTO bankdb.has_account (account_id, id_card, latest_date, is_saving) VALUES ('1526973164194', '03', '2018-05-22', 0);
INSERT INTO bankdb.has_account (account_id, id_card, latest_date, is_saving) VALUES ('1526973174923', '03', '2018-05-22', 1);INSERT INTO bankdb.loan_pay (pay_id, loan_number, date, money) VALUES ('1526973213057', '1526973201470', '2018-05-21', 150);
INSERT INTO bankdb.loan_pay (pay_id, loan_number, date, money) VALUES ('1526973218708', '1526973201470', '2018-05-22', 150);INSERT INTO bankdb.loan (loan_number, bank_name, amount) VALUES ('1526973201470', 'bank2', 300);INSERT INTO bankdb.saving_account (account_id, bank_name, money, date, rate, currency) VALUES ('1526973174923', 'bank1', 1000, '2018-05-20', 0.01, 'RMB');INSERT INTO bankdb.staff (staff_id_card, bank_name, name, phone, address, start_date) VALUES ('01', 'bank1', 'n1', '133555', 'x', '2018-01-01');
INSERT INTO bankdb.staff (staff_id_card, bank_name, name, phone, address, start_date) VALUES ('02', 'bank1', 'n2', '155', 'x', '2018-01-01');INSERT INTO bankdb.subbranch (bank_name, city, money) VALUES ('bank1', 'c1', 30000);
INSERT INTO bankdb.subbranch (bank_name, city, money) VALUES ('bank2', 'c2', 20000);
```

操作后数据库的数据为
<html>

<body>
    账户表 Account
    <table border="1" style="border-collapse:collapse">
        <tr>
            <th>account_id</th>
            <th>bank_name</th>
            <th>money</th>
            <th>date</th>
        </tr>
        <tr>
            <td>1526973164194</td>
            <td>bank1</td>
            <td>1000</td>
            <td>2018-05-20</td>
        </tr>
        <tr>
            <td>1526973174923</td>
            <td>bank1</td>
            <td>1000</td>
            <td>2018-05-20</td>
        </tr>
    </table>
    支票账户表 CheckingAccount
    <table border="1" style="border-collapse:collapse">
        <tr>
            <th>account_id</th>
            <th>bank_name</th>
            <th>money</th>
            <th>date</th>
            <th>credit_line</th>
        </tr>
        <tr>
            <td>1526973164194</td>
            <td>bank1</td>
            <td>1000</td>
            <td>2018-05-20</td>
            <td>1000</td>
        </tr>
    </table>
    客户表 Customer
    <table border="1" style="border-collapse:collapse">
        <tr>
            <th>id_card</th>
            <th>staff_id_card</th>
            <th>name</th>
            <th>phone</th>
            <th>address</th>
            <th>contact_name</th>
            <th>contact_phone</th>
            <th>contact_email</th>
            <th>contact_role</th>
            <th>role</th>
        </tr>
        <tr>
            <td>03</td>
            <td>NULL</td>
            <td>c1</td>
            <td>55</td>
            <td>r</td>
            <td>cc1</td>
            <td>133</td>
            <td>xx@yy.com</td>
            <td>f</td>
            <td>NULL</td>
        </tr>
        <tr>
            <td>04</td>
            <td>01</td>
            <td>c2</td>
            <td>55</td>
            <td>r</td>
            <td>cc1</td>
            <td>133</td>
            <td>xx@yy.com</td>
            <td>f</td>
            <td>贷款负责人</td>
        </tr>
    </table>
    用户贷款表 CustomerLoan
    <table border="1" style="border-collapse:collapse">
        <tr>
            <th>id_card</th>
            <th>loan_number</th>
        </tr>
        <tr>
            <td>03</td>
            <td>1526973201470</td>
        </tr>
        <tr>
            <td>04</td>
            <td>1526973201470</td>
        </tr>
    </table>
    用户拥有账户表 HasAccount
    <table border="1" style="border-collapse:collapse">
        <tr>
            <th>account_id</th>
            <th>id_card</th>
            <th>latest_date</th>
            <th>is_saving</th>
        </tr>
        <tr>
            <td>1526973164194</td>
            <td>03</td>
            <td>2018-05-22</td>
            <td>0</td>
        </tr>
        <tr>
            <td>1526973174923</td>
            <td>03</td>
            <td>2018-05-22</td>
            <td>1</td>
        </tr>
    </table>
    贷款表 Loan
    <table border="1" style="border-collapse:collapse">
        <tr>
            <th>loan_number</th>
            <th>bank_name</th>
            <th>amount</th>
        </tr>
        <tr>
            <td>1526973201470</td>
            <td>bank2</td>
            <td>300</td>
        </tr>
    </table>
    贷款发放表 LoanPay
    <table border="1" style="border-collapse:collapse">
        <tr>
            <th>pay_id</th>
            <th>loan_number</th>
            <th>date</th>
            <th>money</th>
        </tr>
        <tr>
            <td>1526973213057</td>
            <td>1526973201470</td>
            <td>2018-05-21</td>
            <td>150</td>
        </tr>
        <tr>
            <td>1526973218708</td>
            <td>1526973201470</td>
            <td>2018-05-22</td>
            <td>150</td>
        </tr>
    </table>
    储蓄账户表 SavingAccount
    <table border="1" style="border-collapse:collapse">
        <tr>
            <th>account_id</th>
            <th>bank_name</th>
            <th>money</th>
            <th>date</th>
            <th>rate</th>
            <th>currency</th>
        </tr>
        <tr>
            <td>1526973174923</td>
            <td>bank1</td>
            <td>1000</td>
            <td>2018-05-20</td>
            <td>0.01</td>
            <td>RMB</td>
        </tr>
    </table>
    员工表 Staff
    <table border="1" style="border-collapse:collapse">
        <tr>
            <th>staff_id_card</th>
            <th>bank_name</th>
            <th>name</th>
            <th>phone</th>
            <th>address</th>
            <th>start_date</th>
        </tr>
        <tr>
            <td>01</td>
            <td>bank1</td>
            <td>n1</td>
            <td>133555</td>
            <td>x</td>
            <td>2018-01-01</td>
        </tr>
        <tr>
            <td>02</td>
            <td>bank1</td>
            <td>n2</td>
            <td>155</td>
            <td>x</td>
            <td>2018-01-01</td>
        </tr>
    </table>
    支行表 Subbranch
    <table border="1" style="border-collapse:collapse">
        <tr>
            <th>bank_name</th>
            <th>city</th>
            <th>money</th>
        </tr>
        <tr>
            <td>bank1</td>
            <td>c1</td>
            <td>30000</td>
        </tr>
        <tr>
            <td>bank2</td>
            <td>c2</td>
            <td>20000</td>
        </tr>
    </table>
</body>

</html>

# 总结与讨论

这次银行管理系统的开发耗时相当长，前期花了大量时间用于学习 play framework, slick, vue.js 等框架，自己实现了一个响应式表格组件，虽然有一些收获，但大部分时间都浪费在 debug 上了。数据库模式的设计在 lab2 中已经做完，这次只需做一些微小的调整。有些调整是针对 mysql 数据库专门做的，例如增加 default charset=utf8，把 CHAR(256) 改为 CHAR(255) 等。slick 提供了一个 codegen 插件，可以从数据库模式直接导出，产生 scala 代码，这个非常有用，减少了许多重复的工作。这次实验也加深了我对设计模式的理解，这次使用了一个典型的 MVC 框架，但是却没有使用完整的 MVC，而是把后端写成了一个 Restful API 的架构，View 模块几乎不做任何事情，数据的动态渲染靠的是前端的异步请求。这种骑墙的做法其实并不太好，用 MVC 架构来做前后端分离的事情显得太过笨重，关注的焦点也很难集中，这是值得吸取教训的地方。