var app = new Vue({
  el: '#app',
  data: {
    business_period: '按年',
    business_year: '2018',
    business_season: '春',
    business_type: '储蓄',
    business_month: '1',
    years: [],
    rows: []
  },
  mounted: function() {
    for (var i = 1970; i < 2019; i++) {
      this.years.push(i.toString());
    }
  },
  methods: {
    do_stat: function() {
      var _self = this;
      var req = {
        is_saving: this.business_type == '储蓄',
        period: this.business_period,
        year: this.business_year,
        season: this.business_season,
        month: this.business_month
      };
      $.ajax({
        'url': 'api/stat',
        'type': 'get',
        'contentType': 'application/json',
        'data': req,
        'error': function(xhr) {
          alert('Error');
        },
        'success': function(response) {
          _self.rows = response.rows;
        }
      });
    }
  }
});