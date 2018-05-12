var page_size = 10
var current_page = 0
var total_size = 1
var choose_line = 0

function choose_item() {
    return page_size * current_page + choose_line;
}

function over_row() {
    if(parseInt($(this).attr("line"))!=choose_line)
        $(this).attr("class", "success");
}

function out_row() {
    if(parseInt($(this).attr("line"))!=choose_line)
        $(this).attr("class", "");
}

function click_row() {
    $("tbody tr[line="+choose_line+"]").attr("class","");
    choose_line = parseInt($(this).attr("line"));
    $("tbody tr[line="+choose_line+"]").attr("class","danger");
}

$("tbody tr").mouseout(out_row).mouseover(over_row).click(click_row);
$("tbody tr[line="+choose_line+"]").attr("class","danger");