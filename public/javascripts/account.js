var app = new Vue({
    el: "#app",
    data: {
        page_cur: 0,
        page_count: 1,
        page_items: 10,
        offs: [-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5],
        search_id: "",
        search_name: "",
        search_date_from: "",
        search_date_to: "",
        search_bankname: "",
        rows: [],
        row_over: -1,
        row_choose: -1,
        choosed_item: {},
        debug: ""
    },
    mounted: function () {
        this.change_page(0);
        if (this.rows.length != 0) this.on_choose(0);
    },
    methods: {
        change_page: function (delta) {
            var _self = this;
            var new_cur = this.page_cur + delta;
            if (new_cur < 0 || new_cur > this.page_count) return;
            this.page_cur = new_cur;
            var req = {
                'pg': new_cur,
                'id': this.search_id,
                'name': this.search_name,
                'bankName': this.search_bankname,
                'dateFrom': this.search_date_from,
                'dateTo': this.search_date_to
            };
            $.ajax({
                'url': "api/staff",
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
            this.row_choose = index;
            this.choosed_item = $.extend(true, {}, this.rows[index]);
        },
        refresh: function () {
            this.search_bankname = "";
            this.search_name = "";
            this.search_date_from = "";
            this.search_date_to = "";
            this.search_id = "";
            this.change_page(0);
        },
        do_insert: function () {
            var _self = this;
            $.ajax({
                'url': "api/staff",
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
                'url': "api/staff",
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
            $.ajax({
                'url': "api/staff",
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