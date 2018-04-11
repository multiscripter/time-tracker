(function($){
    $(function () {
        var app = {
            form: null,
            token: null,
            btnLogout: null,
            wdays: null,
            actions: null,
            worktime: null,
            schedule: null,

            addRow: function (mark) {
                var tr = this.schedule.find('tbody tr:first-child').clone();
                tr.children('td:nth-child(1)').text(mark.wday);
                tr.children('td:nth-child(2)').text(mark.mark);
                tr.children('td:nth-child(3)').attr('data-state',
                    mark.state).text(mark.state ? 'Ќа работе' : 'ќтсутствует');
                this.schedule.children('tbody').append(tr);
                if (this.schedule.find('tbody tr:not(:first)').length > 1)
                    this.actions.find('a[data-action=done]').removeClass('d-none');
            },

            fillSchedule: function (marks) {
                for (var a = 0; a < marks.length; a++)
                    this.addRow(marks[a]);
                this.actions.find('a[data-action='
                    + (marks[marks.length - 1].state ? 'resume' : 'wait')
                    + ']').addClass('d-none');
            },

            prepare: function (data) {
                this.actions.removeClass('d-none');
                this.worktime.removeClass('d-none');
                this.schedule.removeClass('d-none');
                if ('marks' in data && data.marks.length)
                    this.fillSchedule(data.marks);
                else
                    this.actions.find('a[data-action=wait]').addClass('d-none');
            },

            login: function (event) {
                event.preventDefault();
                var obj = {
                    login: this.form.find('.js-field-login').val(),
                    pass: this.form.find('.js-field-pass').val()
                };
                var self = this;
                $.post(this.form.attr('action'), obj, function (data) {
                    if (data.status == 'ok') {
                        self.token = data.token;
                        self.form.addClass('d-none');
                        self.btnLogout.removeClass('d-none');
                        if ('validwday' in data && !data.validwday)
                            self.wdays.removeClass('d-none');
                        else
                            self.prepare(data);
                    } else {
                        //TODO: обработка ошибок.
                    }
                    console.log(data);
                });
            },

            chooseWday: function (event) {
                var tar = $(event.target);
                if (!tar.hasClass('js-wday')) return;
                event.preventDefault();
                var obj = {
                    token: this.token,
                    action: tar.attr('data-action')
                };
                var self = this;
                $.post(window.location.href, obj, function (data) {
                    if (data.status == 'ok') {
                        self.wdays.addClass('d-none');
                        self.prepare(data);
                    } else {
                        //TODO: обработка ошибок.
                    }
                    console.log(data);
                });
            },

            action: function (event) {
                var tar = $(event.target);
                if (!tar.hasClass('js-tracker-action')) return;
                event.preventDefault();
                var obj = {
                    token: this.token,
                    action: tar.attr('data-action')
                };
                var self = this;
                $.post(window.location.href, obj, function (data) {
                    if (data.status == 'ok') {
                        if (tar.attr('data-action') != 'done')
                            self.actions.find('.js-tracker-action')
                                .filter(':not(a[data-action=done])').toggleClass('d-none');
                        var action = tar.attr('data-action');
                        var lastRow = self.schedule.find('tbody tr:last-child');
                        var state = lastRow.children('td:nth-child(3)').attr('data-state');
                        if ((action == 'resume' && state != 'true') 
                            || (action == 'wait' && state == 'true')) {
                            self.addRow(data.mark);
                        } else if (action == 'done') {
                            var time = '';
                            for (var a = 0; a < data.worktime.length; a++) {
                                if (data.worktime[a] < 10)
                                    time += '0';
                                time += data.worktime[a] + ':';
                            }
                            self.worktime.children('span').text(time.substr(0, time.length - 1));
                        }
                    } else {
                        //TODO: обработка ошибок.
                    }
                    console.log(data);
                });
            },

            init: function () {
                this.form = $('.js-form-auth');
                if (!this.form.length) return;
                var self = this;
                this.form.on('submit', function (event) {
                    self.login.call(self, event);
                });
                this.btnLogout = $('.js-btn-logout');
                this.wdays = $('.js-wdays');
                this.wdays.on('click', function (event) {
                    self.chooseWday.call(self, event);
                });
                this.actions = $('.js-tracker-actions');
                this.actions.on('click', function (event) {
                    self.action.call(self, event);
                });
                this.worktime = $('.js-worktime');
                this.schedule = $('.js-schedule');
            }
        };
        app.init();
    });
})(jQuery);