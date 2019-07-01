Vue.use(VeeValidate);
new Vue({
    el: '#app',
    data: {
        streamers: [],
        streamer: "",
        tabs: "streams",
        headers: [
            {text: "id", align: "left", value: "_id"},
            {text: "Title", value: "title"},
            {text: "Date", value: "date"},
            {text: "Genre", value: "game"},
            {text: "Actions", value: "name", sortable: false}
        ],
        streams: [],
        pagination: {
            rowsPerPage: 25,
            descending: true,
            sortBy: "date"
        },
        pages: 0,
        search: ""
    },
    filters: {
        date(value) {
            if (!value) return "";
            let [date, time] = value.split(/[T.]/);
            date = date.split('-').reverse().join('.');
            return date + " " + time;
        }
    },
    mounted() {
        // this.$http.get('/api/v1/server/storage').then(response => (console.log('storage', response.data)));
        // this.$http.get('/api/v1/status_list').then(response => (console.log('statuses', response.data)));
        this.$http.get('/api/v1/subscriptions').then(response => {
            this.streamers = response.data;
            this.streamer = this.streamers[0];
        });
    },
    watch: {
        streamer: function (newStreamer) {
            this.$http.get('/api/v1/database/streams/' + newStreamer).then(response => {
                this.streams = response.data;
                this.pagination.totalItems = response.data.length;
                if (this.pagination.rowsPerPage == null || this.pagination.totalItems == null) this.pages = 0;
                else this.pages = Math.ceil(this.pagination.totalItems / this.pagination.rowsPerPage);
            });
        }
    }
});