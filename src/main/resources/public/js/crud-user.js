'use strict';
$(function () {
    var host = location.host;
    var protocol = location.protocol + '//';
    var serverUrl = protocol + host;

    new Vue({
        el: '#crud-user',
        data: function () {
            return {
                users: null,
                actionsLocked: false,
                userToEditOrAdd: null,
                searchKeyword: null
            }
        },
        mounted: function () {
            console.log('mounted');
            this.reloadTableUsers();
        },
        methods: {
            reloadTableUsers: function () {
                var url = serverUrl + '/user/all';
                var that = this;
                $.get(url, function (data) {
                    that.users = data.content; // todo add pagination/sort support
                    that.userToEditOrAdd = null;
                    that.actionsLocked = false;
                });
            },
            search: function () {
                var url = serverUrl + '/user/find-by-username-containing';
                var that = this;
                $.get(url, {key: this.searchKeyword}, function (data) {
                    that.users = data;
                });
            },
            deleteById: function (id) {
                var url = serverUrl + '/user/delete' + '?id=' + id;
                var that = this;
                $.post(url, function (data) {
                    alert('User deleted');
                    that.reloadTableUsers();
                });
            },
            showTableRowAddOrEdit: function (user) {
                this.actionsLocked = true;
                this.userToEditOrAdd = user;
            },
            addOrUpdate: function () {
                var url = serverUrl + '/user/create-or-update';
                var that = this;
                $.ajax({
                    url: url,
                    type: "POST",
                    dataType: "json",
                    contentType: "application/json", // send as JSON
                    data: JSON.stringify(this.userToEditOrAdd),
                    success: function (data) {
                        alert('User added or updated');
                        that.reloadTableUsers();
                    }
                });
            }
        }
    });
});
