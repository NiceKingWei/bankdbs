var app = new Vue({
  el: "#app",
  data: {
    page_cur: 0,
    page_count: 1,
    page_items: 10,
    offs: [-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5],
    search_bank_name: "",
    search_contact_name: "",
    search_id: "",
    search_customer_id: "",
    search_date_from: "",
    search_date_to: "",
    search_staff_id: "",
    search_min: 0,
    search_max: 1e20,
    search_account_type_saving: true,
    search_account_type_checking: true,
    rows: []
      /*[{
              is_saving: true,
              accountId: "123",
              bankName: "bank",
              money: 123,
              date: "2018-01-01",
              rate_creditLine: 2342,
              currency: "type"
            }]*/
      ,
    row_over: -1,
    row_choose: -1,
    label1: "利率",
    label2: "货币类型",
    hide: false,
    edit_selected: "储蓄账户",
    choosed_item: {},
    debug: ""
  },
  mounted: function () {
    this.change_page(0);
    if (this.rows.length != 0) this.on_choose(0);
  },
  computed: {
    search_account_type: function () {
      if (this.search_account_type_saving) {
        if (this.search_account_type_checking) {
          return 2;
        } else {
          return 0;
        }
      } else {
        if (this.search_account_type_checking) {
          return 1;
        } else {
          return -1;
        }
      }
    }
  },
  watch: {
    edit_selected: function (newItem, oldItem) {
      if (newItem == "储蓄账户") this.choosed_item.is_saving = true;
      else this.choosed_item.is_saving = false;
      this.label1 = this.choosed_item.is_saving ? "利率" : "透支额";
      this.label2 = this.choosed_item.is_saving ? "货币类型" : "无效";
      this.hide = this.choosed_item.is_saving ? "visible" : "hidden";
    }
  },
  methods: {
    change_page: function (delta) {
      var _self = this;
      var new_cur = this.page_cur + delta;
      if (new_cur < 0 || new_cur > this.page_count || this.search_account_type < 0) return;
      this.page_cur = new_cur;
      var req = {
        'pg': new_cur,
        'account_id': this.search_id,
        'customer_id': this.search_customer_id,
        'account_type': this.search_account_type,
        'bank_name': this.search_bank_name,
        'min_money': parseFloat(this.search_min),
        'max_money': parseFloat(this.search_max),
        'date_from': this.search_date_from,
        'date_to': this.search_date_to
      };
      $.ajax({
        'url': "api/account",
        'type': 'get',
        'contentType': 'application/json',
        'data': req,
        'error': function (xhr) {
          alert("Error");
        },
        'success': function (response) {
          _self.page_count = response.count;
          _self.rows = response.rows;
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
      this.choosed_item = $.extend(true, {}, this.rows[index]);
      if(this.choosed_item.isSaving){
        this.edit_selected = "储蓄账户";
      }else{
        this.edit_selected = "支票账户";
      }
    },
    refresh: function () {
      this.search_bank_name = "";
      this.search_contact_name = "";
      this.search_id = "";
      this.search_customer_id = "",
      this.search_date_from = "";
      this.search_date_to = "";
      this.search_staff_id = "";
      this.search_min = 0;
      this.search_max = 1e20;
      this.change_page(0);
    },
    do_insert: function () {
      var _self = this;
      this.choosed_item.money = parseFloat(this.choosed_item.money);
      this.choosed_item.isSaving = this.edit_selected == "储蓄账户";
      this.choosed_item.accountId = "";
      if (!this.choosed_item.currency) this.choosed_item.currency = "";
      this.choosed_item.rate_creditLine = parseFloat(this.choosed_item.rate_creditLine);
      $.ajax({
        'url': "api/account",
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
        'url': "api/account",
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
    },
    do_update: function () {
      var _self = this;
      this.choosed_item.money = parseFloat(this.choosed_item.money);
      this.choosed_item.accountId = this.rows[this.row_choose].accountId;
      if (!this.choosed_item.currency) this.choosed_item.currency = "";
      this.choosed_item.rate_creditLine = parseFloat(this.choosed_item.rate_creditLine);
      $.ajax({
        'url': "api/account",
        'type': 'put',
        'contentType': 'application/json',
        'data': JSON.stringify({
          'old_row': this.rows[this.row_choose],
          'new_row': this.choosed_item
        }),
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