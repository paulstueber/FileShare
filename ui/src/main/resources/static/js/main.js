/**
 * NOTIFICATIONS, powered by 'Bootstrap Notify', http://bootstrap-notify.remabledesigns.com/
 *
 * @param message
 * @param type
 * @param timeout
 */
$.notifyDefaults({
    type: 'success',
    allow_dismiss: true,
    showProgressbar: false,
    delay: 3000
});
function showNotification(message, type, timeout) {
    $.notify({
        // options
        message: message
    },{
        // settings
        type: type
    });
}

/**
 * multi select functionality for tables with class 'multiselect':
 * class="[...] multiselect [...]"
 */
$(document).on('click', function (e) {
    if (!e.ctrlKey) {
        $('table.multiselect tr.selected').toggleClass('selected')
    }
});

$(document).on('click', 'table.multiselect tr', function (e) {
    if (e.ctrlKey) {
        $(this).toggleClass('selected');
    }
});