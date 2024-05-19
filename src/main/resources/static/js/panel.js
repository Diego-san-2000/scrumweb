function iniciarCronometro(tiempoMinutos, id) {
    let tiempoSegundos = tiempoMinutos * 60;
    let cronometroDisplay = document.getElementById(id);

    // Actualizar el cronómetro cada segundo
    let intervalo = setInterval(function() {
        let horas = Math.floor(tiempoSegundos / 3600);
        let minutos = Math.floor((tiempoSegundos % 3600) / 60);
        let segundos = tiempoSegundos % 60;

        // Formatear los valores
        horas = horas < 10 ? "0" + horas : horas;
        minutos = minutos < 10 ? "0" + minutos : minutos;
        segundos = segundos < 10 ? "0" + segundos : segundos;

        // Mostrar el tiempo
        cronometroDisplay.innerHTML = horas + ":" + minutos + ":" + segundos;

        if (tiempoSegundos <= 0) {
            clearInterval(intervalo);
            cronometroDisplay.innerHTML = "00:00:00";
            alert("¡Tiempo terminado!");
        }

        tiempoSegundos--;
    }, 1000);
}


function ocultar(id){
    document.getElementById("fila-"+id).style.display = "none";
}
function modificar(id, accion){
    ocultar(id);
    enviar('/api/backlog/'+id +'/'+accion, null, 'PUT');
}