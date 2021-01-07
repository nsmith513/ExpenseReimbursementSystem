// execute function once window is loaded
addWindowLoadFunc(async function () {
    // do DOM manipulation if the user is stored
    if (sessionStorage.getItem("user-stored")) {
        // add all reimbursement tickets to the table
        reimbTable = document.getElementById("reimb-table-body");
        reimbTable.innerHTML = "";
        for (let reimbObj of JSON.parse(sessionStorage.getItem("reimbursements"))) {
            reimbTable.insertAdjacentHTML("beforeEnd",
                "<tr>\n" + (reimbObj.desc === null ? 
                `    <td colspan="2">${reimbObj.type}</td>\n` :
                `    <td>${reimbObj.type}</td>\n` + 
                `    <td><div class="overflow-hover">${reimbObj.desc}<div></td>\n`) +
                `    <td>${reimbObj.status}</td>\n` + 
                `    <td>$${reimbObj.amount}</td>\n` + (reimbObj.resolved === null ? 
                `    <td colspan="3">${reimbObj.submitted}</td>\n` :
                `    <td>${reimbObj.submitted}</td>\n` +
                `    <td>${reimbObj.resolved}</td>\n` + 
                `    <td>${reimbObj.resolver_first_name} ${reimbObj.resolver_last_name}</td>\n`) +
                "</tr>"
            );
        }
    } else {
        alert("ERROR: USER INFO WAS NOT STORED");
    }
});
