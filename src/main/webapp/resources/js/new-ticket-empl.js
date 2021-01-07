document.getElementById("input-ticket").addEventListener("click", async function () {
    // extract input from user
    let type = document.getElementById("select-type");
    let amount = document.getElementById("input-amount");
    let desc = document.getElementById("textarea-desc");
    
    // clear invalidity
    if (type.classList.contains("is-invalid"))
        type.classList.remove("is-invalid");
    if (amount.classList.contains("is-invalid"))
        amount.classList.remove("is-invalid");

    let badInput = false;

    // check type input
    if (type.value === "") {
        badInput = true;
        type.classList.add("is-invalid");
    }

    // check amount input
    if (!/^([1-9]\d*(\.\d\d)?|0?\.\d\d)$/.test(amount.value)) {
        badInput = true;
        amount.classList.add("is-invalid");
    }

    if (!badInput) {
        // add reimbursement to DB
        let reimb = await (async function () {
            const respPayload =
                await fetch("http://localhost:9047/ExpenseReimbursementSystem/submit/new-ticket", {
                    "method": "post",
                    "headers": { "Content-type": "application/x-www-form-urlencoded" },
                    "body":
                        `${type.name}=${type.value}&` +
                        `${amount.name}=${amount.value}&` +
                        `${desc.name}=${desc.value}`
                });

            // get json from response
            return await respPayload.json().then(response => { return response; });
        })();
        
        if (reimb.success) {
            // add reimbursement to (client's) session data
            reimbList = JSON.parse(sessionStorage.getItem("reimbursements"));
            reimbList.unshift(reimb.data[0]);
            sessionStorage.setItem("reimbursements", JSON.stringify(reimbList));
            
            // play "accepted" animation
            let acc = document.getElementById("accepted-text");
            acc.style.animation = "none";
            acc.offsetHeight; /* trigger reflow */
            acc.style.animation = "fade-out 3s linear 1";
        }
    }
    
});
