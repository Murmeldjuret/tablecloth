$(document).ready(function () {
    $('.select2-country').select2();
    // delegate a click event on the input box
    $('.select2-input').on('click',function()
    {
        // remove select2-disabled class from all li under the dropdown
        $('.select2-drop .select2-results li').removeClass('select2-disabled');
        // add select2-result-selectable class to all li which are missing the respective class
        $('.select2-drop .select2-results li').each(function()
        {
            if(!$(this).hasClass('select2-result-selectable'))
                $(this).addClass('select2-result-selectable');
        });
    });

    // had to include the following code as a hack since the click event required double click on 'select2-input' to invoke the event
    $('.select2-container-multi').on('mouseover',function()
    {
        $('.select2-input').click();
    });
});