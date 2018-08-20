'use strict';

function createVueJSApp() {
    return new Vue({
        el: '#app',
        data: function () {
            return {
                quote: null,
                websocket: null,
                started: false
            }
        },
        methods: {
            connect: function () {
                var host = location.host;
                var protocol = location.protocol === 'https:' ? 'wss://' : 'ws://';
                this.websocket = new WebSocket(protocol + host + '/quote-ws');
                var that = this;

                this.websocket.onerror = function (error) {
                    console.log("errors!\n", error);
                };

                this.websocket.onclose = function (evt) {
                    console.log("connection closed");
                };

                this.websocket.onmessage = function (message) {
                    try {
                        var quote = JSON.parse(message.data);
                        that.quote = quote;
                    } catch (e) {
                        console.log('An error occured on message:', message.data);
                    }
                    return false;
                };

            },
            disconnect: function () {
                if (this.websocket) {
                    this.websocket.close();
                    this.websocket = null;
                }
            },
            start: function (e) {
                e.preventDefault();
                try {
                    if (this.websocket === null || this.websocket.readyState === WebSocket.CLOSED) {
                        this.connect();
                    }
                    this.started = true;
                } catch (e) {
                    console.log(e);
                }

            },
            stop: function (e) {
                e.preventDefault();
                if (this.websocket !== null && this.websocket.readyState !== WebSocket.CLOSED) {
                    this.disconnect();
                    this.quote = null;
                    this.started = false;
                }
            }
        }
    });
}