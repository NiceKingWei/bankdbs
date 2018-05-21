var app = new Vue({
  el: "#app",
  data: {
    page_cur: 0,
    page_count: 1,
    page_items: 10,
    offs: [-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5],
    search_bank_name: "",
    search_loan_number: "",
    search_customers: "",
    search_min: 0,
    search_max: 1e20,
    pay_date: "",
    pay_money: 0,
    rows: [],
    row_over: -1,
    row_choose: -1,
    choosed_pay: [],
    choosed_item: {
      loan: {}
    },
    debug: ""
  },
  mounted: function () {
    this.change_page(0);
    if (this.rows.length != 0) this.on_choose(0);
  },
  methods: {
    isString: function (str) {
      return (typeof str == 'string') && str.constructor == String;
    },
    IsNum: function (s) {
      if (s != null) {
        return !isNaN(s);
      }
      return false;
    },
    parseSet: function (set) {
      if (this.isString(set))
        return set.split(',');
      else
        return set;
    },
    do_pay: function () {
      var _self = this;
      var data = {
        payId: "",
        loanNumber: this.choosed_item.loan.loanNumber,
        date: this.pay_date,
        money: parseFloat(this.pay_money)
      };
      $.ajax({
        'url': "api/pay",
        'type': 'post',
        'contentType': 'application/json',
        'data': JSON.stringify(data),
        'success': function (response) {
          _self.on_choose(_self.row_choose);
        }
      });
    },
    change_page: function (delta) {
      var _self = this;
      var new_cur = this.page_cur + delta;
      if (new_cur < 0 || new_cur > this.page_count) return;
      this.page_cur = new_cur;
      var req = {
        'pg': new_cur,
        'loan_number': this.search_loan_number,
        'customer_id': this.search_customers,
        'bank_name': this.search_bank_name,
        'min_money': parseFloat(this.search_min),
        'max_money': parseFloat(this.search_max)
      };
      $.ajax({
        'url': "api/loan",
        'type': 'get',
        'contentType': 'application/json',
        'data': req,
        'error': function (xhr) {
          alert("Error");
        },
        'success': function (response) {
          _self.page_count = response.count;
          _self.rows = response.rows;
          for (var i in _self.rows) {
            if (_self.rows[i].states == 0) {
              _self.rows[i].states_show = "未开始发放";
            } else if (_self.rows[i].states == 1) {
              _self.rows[i].states_show = "发放中";
            } else {
              _self.rows[i].states_show = "发放结束";
            }
          }
          _self.on_choose(0);
        }
      });
    },
    in_range: function (n) {
      return n >= 0 && n < this.page_count;
    },
    do_search: function () {
      this.page_cur = 0;
      this.change_page(0);
    },
    on_choose: function (index) {
      var _self = this;
      this.row_choose = index;
      if (this.rows[index]) {
        this.choosed_item = $.extend(true, {}, this.rows[index]);
        this.choosed_item.pay = [];
        var req = {
          loan_number: this.choosed_item.loan.loanNumber
        };
        $.ajax({
          'url': "api/pay",
          'type': 'get',
          'data': req,
          'contentType': 'application/json',
          'success': function (response) {
            _self.choosed_pay = response.rows;
          }
        });
      } else {
        this.choosed_item.pay = [];
      }
    },
    refresh: function () {
      this.search_bank_name = "";
      this.search_loan_number = "";
      this.search_customers = "";
      this.search_min = 0;
      this.search_max = 1e20;
      this.change_page(0);
    },
    do_insert: function () {
      var _self = this;
      this.choosed_item.loan.amount = parseFloat(this.choosed_item.loan.amount);
      this.choosed_item.loan.loanNumber = "";
      this.choosed_item.customers = this.parseSet(this.choosed_item.customers);
      $.ajax({
        'url': "api/loan",
        'type': 'post',
        'contentType': 'application/json',
        'data': JSON.stringify(this.choosed_item),
        'error': function (xhr) {
          alert(xhr.responseText);
        },
        'success': function (response) {
          _self.page_cur = 0;
          _self.refresh();
        }
      });
    },
    do_remove: function () {
      var _self = this;
      $.ajax({
        'url': "api/loan",
        'type': 'delete',
        'contentType': 'application/json',
        'data': JSON.stringify(this.rows[this.row_choose]),
        'error': function (xhr) {
          alert(xhr.responseText);
        },
        'success': function (response) {
          _self.refresh();
        }
      });
    }
  }
});