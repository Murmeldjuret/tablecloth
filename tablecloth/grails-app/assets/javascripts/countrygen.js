$(document).ready(function () {
    $('.select2-country').select2();
    $('.select2-input').on('click',function()
    {
        $('.select2-drop .select2-results li').removeClass('select2-disabled');
        $('.select2-drop .select2-results li').each(function()
        {
            if(!$(this).hasClass('select2-result-selectable'))
                $(this).addClass('select2-result-selectable');
        });
    });

    $('.select2-container-multi').on('mouseover',function()
    {
        $('.select2-input').click();
    });
});