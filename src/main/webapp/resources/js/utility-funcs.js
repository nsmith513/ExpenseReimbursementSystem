/**
 * Returns true if all given strings match the given regex pattern.
 * @param {RegExp} regex Pattern to match with strings.
 * @param  {...string} toTest Strings to match.
 */
function regexTestMultiple(regex, ...toTest) {
    for (s of toTest)
        if (!regex.test(s))
            return false;
    return true;
}

let toRunOnWindowLoad = [];
if (window) window.onload = async function ()
    { for (let func of toRunOnWindowLoad) await func(); }
 // using await to ensure the function is run entirely before the next is called

/**
 * Add a function to be run once the window is loaded. Functions
 * are run in the order they were added.
 * 
 * @param {Function} func 
 */
function addWindowLoadFunc(func) {
    toRunOnWindowLoad.push(func);
}
