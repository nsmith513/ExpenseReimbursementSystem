// code to execute when logout button is pressed
document.getElementById("logout").addEventListener("click", () => {
    let xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = (() => {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            sessionStorage.clear();
            window.location.assign("http://localhost:9047/ExpenseReimbursementSystem/login");
        }
    });
    xhttp.open("POST", "http://localhost:9047/ExpenseReimbursementSystem/logout");
    xhttp.send();
});

// execute THIS function once window is loaded
addWindowLoadFunc(async function () {
    // try to fetch user info if it's not already stored
    if (!sessionStorage.getItem("user-stored")) {
        let userInfo = await (async function () {
            const respPayload =
                await fetch("http://localhost:9047/ExpenseReimbursementSystem/retrieve/user-info", {"method": "post"});

            // get json from response
            return await respPayload.json().then(response => { return response; });
        })();

        if (userInfo.success) {
            let uname = userInfo.data.shift() // username is the first element of the data array
            document.getElementById("user-dropdown").innerText = uname.toUpperCase(); // put username on page
            
            sessionStorage.setItem("user-stored", true);
            sessionStorage.setItem("username", uname);
            sessionStorage.setItem("reimbursements", JSON.stringify(userInfo.data));
        } else {
            sessionStorage.setItem("user-stored", false);
        }
    } else {
        // put username on page
        document.getElementById("user-dropdown").innerText =
        	sessionStorage.getItem("username").toUpperCase();
    }
});
