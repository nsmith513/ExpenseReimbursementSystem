// change ticket filter on window load
addWindowLoadFunc(async function () {
    // add ALL reimbursement tickets to the table
    fillTicketTable("any");
});

// change ticket filter on option select
document.getElementById("select-status").addEventListener("change", event => {
    fillTicketTable(event.currentTarget.value);
}); 

function fillTicketTable(status) {
    // do DOM manipulation if the user is stored
    if (sessionStorage.getItem("user-stored")) {
        // add reimbursement tickets to the table
        reimbTable = document.getElementById("reimb-table-body");
        reimbTable.innerHTML = "";
        for (let reimbObj of JSON.parse(sessionStorage.getItem("reimbursements"))) {
            let reimbStatus = reimbObj.status.toLowerCase();
            if (reimbStatus != "pending" && (status == "any" || reimbStatus == status)) {
                reimbTable.insertAdjacentHTML("beforeEnd",
                    "<tr>\n" + 
                    `    <td>${reimbObj.author_first_name} ${reimbObj.author_last_name}</td>\n` + (reimbObj.desc === null ? 
                    `    <td colspan="2">${reimbObj.type}</td>\n` :
                    `    <td>${reimbObj.type}</td>\n` + 
                    `    <td><div class="overflow-hover">${reimbObj.desc}<div></td>\n`) +
                    `    <td>${reimbObj.status}</td>\n` + 
                    `    <td>$${reimbObj.amount}</td>\n` + (reimbObj.resolved === null ? 
                    `    <td colspan="2">${reimbObj.submitted}</td>\n` :
                    `    <td>${reimbObj.submitted}</td>\n` +
                    `    <td>${reimbObj.resolved}</td>\n`) + 
                    "</tr>"
                );
            }
        }
    } else {
        alert("ERROR: USER INFO WAS NOT STORED");
    }
}
