(function($){
    $(function () {
        var formAuth = {
            form: null,
            token: null,
            btnLogout: null,
            actions: null,

            auth: function () {
                var obj = {
                    login: this.form.find('.js-field-login').val(),
                    pass: this.form.find('.js-field-pass').val()
                };
                var self = this;
                $.post(this.form.attr('action'), obj, function (data) {
                    if (data.status == 'ok') {
                        self.token = data.token;
                        self.btnLogout.removeClass('d-none');
                        self.actions.removeClass('d-none');
                        self.form.addClass('d-none');
                    } else {

                    }
                    console.log(data);
                });
            },

            init: function () {
                this.form = $('.js-form-auth');
                if (!this.form.length) return;
                var self = this;
                this.form.on('submit', function (event) {
                    event.preventDefault();
                    self.auth.call(self);
                });
                this.btnLogout = $('.js-btn-logout');
                this.actions = $('.js-tracker-actions');
            }
        };
        formAuth.init();
    });
})(jQuery);