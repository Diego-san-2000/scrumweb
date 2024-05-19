function connect() {
    var socket = new SockJS('http://localhost:8080/planningpoker/websocket');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/receiveMessage', function (message) {
            showMessage(JSON.parse(message.body));
        });
    });
}
function sendWrapped(op, datos){
    let data = {
        "op": op,
        ...datos
    }
    stompClient.send("/app/sendMessage", {}, JSON.stringify(data));
}

function sendClasificar(id){
    sendWrapped(6, {
        "id": id
    });
}
function sendVotacion(idUsuario, voto){
    sendWrapped(1,{
        "id": idUsuario,
        "voto": voto
    })
}
function sendAsignar(id){
    sendWrapped(2, {
        "id": id,
        "idUsuario": document.getElementById("email").textContent,
    });
}
function sendDividirTarea(id){
    let p = document.getElementById("texto-dividir-tarea");
    if(!p.textContent == "")
{
    sendWrapped(4, {
        "id": id,
        "random1": Math.random(),
        "random2":Math.random()
    });
}
}
function enviarFraccionarTarea(username, idUsuario) {
    sendWrapped(3, {
        "s1": document.getElementById("subTarea1").value,
        "s2": document.getElementById("subTarea2").value,
        "random1": Math.random(),
        "random2":Math.random(),
        "usuario": username,
        "id": idUsuario
    });
}
function sendDevolverTarea(idTarea){
    sendWrapped(7, {
        "idTarea": idTarea
    });
}

function sendGuardar(idUsuario, idSprint) {
    if(isCheckboxChecked("cbox1") && isCheckboxChecked("cbox2") && isCheckboxChecked("cbox3")){
        changeClass("votacion-guardar-"+ idUsuario); //Realizamos en local para  poder guardar sólo una vez
        sendWrapped(8, {
            "idUsuario": idUsuario,
        })
        if(checkTodosVotados("votacion-guardar"))
            guardarPlanningPoker(idSprint);
    }
}
function sendAvisarSM(idUsuario){
    sendWrapped(5, {
        "idUsuario": idUsuario,
    });
}
function sendBorrar(){
    sendWrapped(0, {});
}
function showMessage(message) {
    switch(message.op){
        //Código de operación de repetir votación
        case 0:
            borrarVotacion();
            break;
        //Código de votación
        case 1:
            votar(message.id, message.voto);
            changeClass("votacion-"+message.id);
            if(checkTodosVotados("votacion-votos"))
                revelarVotos();

            break;
        //Código de asignación
        case 2:
            asignarTareaAUsuario(message.id, message.idUsuario);
            borrarVotacion();
            break;
        //Código de iniciar fraccionar tarea
        case 3:
            mensajeDividirTarea(message);
        //Código de apoyar fraccionar tarea
        case 4:
            changeClass("votacion-dividir-"+message.id);
            if(checkTodosVotados("dividir-sm"))
                fraccionarTarea(message.random1, message.random2);
            break;
        //Código de llamar a SM
        case 5:
            changeClass("votacion-sm-"+message.idUsuario);
            if(checkTodosVotados("votacion-sm"))
                alert("El Scrum Master puede entrar");
            break;

        //Clasificar una tarea
        case 6:
            moverTarea(message.id);
            desactivarBotones();
            borrarVotacion();
            break;
        //Devolver una tarea votando/Sprint Backlog -> Product Backlog
        case 7:
            devolverTarea(message.idTarea);
            activarBotones();
            borrarVotacion();
            break;
        //Guardar
        case 8:
            changeClass("votacion-guardar-"+message.idUsuario); //Enviamos el change class al planning poker
            break;
    }
}