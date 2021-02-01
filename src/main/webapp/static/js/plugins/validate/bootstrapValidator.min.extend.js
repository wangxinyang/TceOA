(function($) {
    //这里的notSameAndContinuity是验证的名字
    //default是默认信息
    $.fn.bootstrapValidator.i18n.cash = $.extend($.fn.bootstrapValidator.i18n.cash || {}, {
        'default': 'Please enter a valid cash number'
    });
    //validate是验证的方法
    $.fn.bootstrapValidator.validators.cash = {
        validate: function(validator, $field, options) {
            var value = $field.val();
            var ArrMen= value.split(".");
            if(ArrMen.length == 2) {
                if(ArrMen[1].length > 2) {    //判断小数点后面的字符串长度
                    return false;
                }
            }
            return true;
        }
    };
}(window.jQuery));