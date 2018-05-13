var app = new Vue({
  el: "#app",
  data: {
    page_cur: 0,
    page_count: 2,
    page_items: 10,
    offs: [-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5],
    search_name: "",
    search_city: "",
    search_min: 0,
    search_max: 1e12,
    rows: [{bankName:"bank",city:"city",money:1},{bankName:"bank",city:"city",money:2}],
    row_over:-1,
    row_choose:-1,
    choosed_item:{},
    debug: ""
  },
  methods: {
    change_page: function (delta) {
      var new_cur = this.page_cur + delta;
      if (new_cur < 0 || new_cur >= this.page_count) return;
      this.page_cur = new_cur;
      var req = {
        'name': this.search_name,
        'city': this.search_city,
        'min': this.search_min,
        'max': this.search_max
      };
      $.ajax({
        'url': "api/subbranch",
        'type': 'get',
        'contentType': 'application/json',
        'data': req,
        'error': function (xhr) {
          alert("Error");
        },
        'success': function (response) {
          this.page_count = response.count;
          this.rows = response.rows;
        }
      });
    },
    in_range: function (n) {
      return n >= 0 && n < this.page_count;
    },
    do_serach: function () {
      this.page_cur = 0;
      change_page(0);
    },
    on_choose: function (index) {
      this.row_choose = index;
      this.choosed_item = this.rows[index];
    },
    do_insert:function(){
      $.ajax({
        'url': "api/subbranch",
        'type': 'post',
        'contentType': 'application/json',
        'data': JSON.stringify(this.choosed_item),
        'error': function (xhr) {
          alert(xhr);
        },
        'success': function (response) {
          this.page_count = response.count;
          this.rows = response.rows;
        }
      });
    },
    do_remove:function(){
      $.ajax({
        'url': "api/subbranch",
        'type': 'delete',
        'contentType': 'application/json',
        'data': JSON.stringify(this.rows[this.row_choose]),
        'error': function (xhr) {
          alert(xhr);
        },
        'success': function (response) {
          this.page_count = response.count;
          this.rows = response.rows;
        }
      });
    },
    do_update:function(){
      $.ajax({
        'url': "api/subbranch",
        'type': 'put',
        'contentType': 'application/json',
        'data': JSON.stringify({
          'old':this.rows[this.row_choose],
          'new':this.choosed_item
        }),
        'error': function (xhr) {
          alert(xhr);
        },
        'success': function (response) {
          this.page_cur = 0;
          this.page_count = response.count;
          this.rows = response.rows;
        }
      });
    }
  }
});