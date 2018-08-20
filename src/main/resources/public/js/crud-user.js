'use strict';
$(function () {
    var host = location.host;
    var protocol = location.protocol + '//';
    var serverUrl = protocol + host;

    new Vue({
        el: '#crud-user',
        data: function () {
            return {
                users: null
            }
        },
        mounted: function () {
            console.log('mounted');
            this.loadUsers();
        },
        methods: {
            loadUsers: function () {
                var url = serverUrl + '/user/all';
                var that = this;
                $.get(url, function (data) {
                    that.users = data.content; // todo add pagination/sort support
                });
            },
            deleteById: function (id) {
                var url = serverUrl + '/user/delete' + '?id=' + id;
                var that = this;
                $.post(url, function (data) {
                    alert('User deleted');
                    that.loadUsers();
                })
            },
            addOrupdate: function (user) {
                console.log('update ', user); // todo
            }
        }
    });
});
