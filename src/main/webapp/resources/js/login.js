function setFailureMessage(message) {
    document
        .getElementById("fail-container")
        .removeAttribute("hidden");
    document
        .getElementById("fail-message")
        .innerText = message;
}

document.getElementById("input-creds").addEventListener("click", async function () {
    // extract input from user
    let uname = document.getElementById("input-username");
    let pwd = document.getElementById("input-password");
    
    // abort if either input is empty
    if (!regexTestMultiple(/^\s*[0-9a-zA-Z][0-9a-zA-Z '-]*$/, uname.value, pwd.value)) {
        setFailureMessage("Enter your username and password.");
        return;
    }

    // try to login
    let result = await (async function () {
        const respPayload =
            await fetch("http://localhost:9047/ExpenseReimbursementSystem/authenticate", {
                "method": "post",
                "headers": { "Content-type": "application/x-www-form-urlencoded" },
                "body": `${uname.name}=${uname.value}&${pwd.name}=${pwd.value}`
            });

        // get json from response
        return await respPayload.json().then(response => { return response; });
    })();

    // redirect to home if logged in, show failure message if not
    if (result.success) {
        sessionStorage.clear(); // also clear the current username and reimbursements in case they're stored from an old user
        window.location.assign("http://localhost:9047/ExpenseReimbursementSystem/account/home");
    } else {
        setFailureMessage(result.what);
    }
});
