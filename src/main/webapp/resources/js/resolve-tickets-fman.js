// change ticket filter on window load
addWindowLoadFunc(async function () {
    // do DOM manipulation if the user is stored
    if (sessionStorage.getItem("user-stored")) {
        // add reimbursement tickets to the table
        reimbTable = document.getElementById("reimb-table-body");
        reimbTable.innerHTML = "";
        for (let reimbObj of JSON.parse(sessionStorage.getItem("reimbursements"))) {
            if (reimbObj.status.toLowerCase() == "pending") {
                // define ids for elements
                let bttnApproveId = `bttn-approve-${reimbObj.id}`;
                let bttnDenyId = `bttn-deny-${reimbObj.id}`;
                
                // define HTML for approve/deny buttons
                let buttons =
                    '<div class="btn-group" role="group">\n' +
                    `    <button id=${bttnApproveId} type="button" class="btn btn-success" value="${reimbObj.id}">Approve</button>\n` +
                    `    <button id=${bttnDenyId} type="button" class="btn btn-danger" value="${reimbObj.id}">Deny</button>\n` +
                    "</div>";

                // add row to table
                reimbTable.insertAdjacentHTML("beforeEnd",
                    "<tr>\n" + 
                    `    <td>${reimbObj.author_first_name} ${reimbObj.author_last_name}</td>\n` + (reimbObj.desc === null ? 
                    `    <td colspan="2">${reimbObj.type}</td>\n` :
                    `    <td>${reimbObj.type}</td>\n` + 
                    `    <td><div class="overflow-hover">${reimbObj.desc}<div></td>\n`) +
                    `    <td>$${reimbObj.amount}</td>\n` +
                    `    <td>${reimbObj.submitted}</td>\n` +
                    `    <td>${buttons}</td>\n` +
                    "</tr>"
                );

                // add listeners to buttons
                document.getElementById(bttnApproveId).addEventListener("click", approveReimb);
                document.getElementById(bttnDenyId).addEventListener("click", denyReimb);
            }
        }
    } else {
        alert("ERROR: USER INFO WAS NOT STORED");
    }
});

async function approveReimb(event) {
    await updateReimbStatus(event.currentTarget, "Approved");
}

async function denyReimb(event) {
    await updateReimbStatus(event.currentTarget, "Denied");
}

async function updateReimbStatus(bttnElem, status) {
    let targetReimbId = bttnElem.value;
    
    let result = await (async function () {
        const respPayload =
            await fetch("http://localhost:9047/ExpenseReimbursementSystem/submit/update-ticket-status", {
                "method": "post",
                "headers": { "Content-type": "application/x-www-form-urlencoded" },
                "body": `reimb=${targetReimbId}&status=${status}`
            });

        // get json from response
        return await respPayload.json().then(response => { return response; });
    })();
    
    console.log(result);
    if (result.success) {
        // update status in client cache
        reimbList = JSON.parse(sessionStorage.getItem("reimbursements"));
        reimbList.find(reimb => reimb.id == targetReimbId).status = status;
        sessionStorage.setItem("reimbursements", JSON.stringify(reimbList));

        // remove the row containing this button
        bttnElem.closest("tr").remove();
    }
}
