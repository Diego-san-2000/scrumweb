let mensaje = document.getElementById("mensaje_contrasena");
let registrarse = document.getElementById("registrarse");

function comprobarPass() {
    let input1 = document.getElementById("contrasena").value;
    let input2 = document.getElementById("contrasena2").value;

    if (input1 !== input2) {
        mensaje.textContent = "Las contrase\u00F1as no coinciden";
        mensaje.style.color = "red";
        registrarse.disabled = true;
    }
    else if ((input1.trim() === "") || (input2.trim() === "")){
        mensaje.textContent = "Inserte contrase\u00F1a";
        registrarse.disabled = true;
    }else {
        mensaje.textContent = "Las contrase\u00F1as coinciden";
        mensaje.style.color = "green";
        registrarse.disabled = false;
    }
}