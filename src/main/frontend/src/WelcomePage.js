export default function WelcomePage() {


    return(<button onClick = {LogIN}> Log In </button>)

}
function LogIN(){
    localStorage.setItem("user", "honza")
    console.log(localStorage.getItem("user"))
    window.location.reload(false)
}
