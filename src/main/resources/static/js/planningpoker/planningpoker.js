let tarea1;
let tarea2;
let arrayDeVotos = [];
let arrayParaEliminar = [];
let idActual;

function changeClass(id) {
    console.log(id);
    let element = document.getElementById(id);
    if (element.classList.contains('bi-x-circle')) {
        element.classList.remove('bi-x-circle');
        element.classList.add('bi-check2');
    }
}

function revelarVotos(){ //Revela la votación de los participantes
    let div = document.getElementById('messages');
    //No se encuentran X, todos han votado
    let elements = div.querySelectorAll('*');
    elements.forEach(function(element) {
    if (element.style.visibility === "hidden") {
        element.style.visibility = "visible";
    }
    });
    votosIdenticosYCalificar()
}
function votosIdenticosYCalificar(){
    console.log(arrayDeVotos);
    let firstElement = arrayDeVotos[0];

    for (let i = 1; i < arrayDeVotos.length; i++) {
        if (arrayDeVotos[i] !== firstElement) 
            return false;
    }

    //Todos han votado lo mismo
    document.getElementById("tarea-votado-"+idActual).style.display = "block";
    document.getElementById("dificultad-"+idActual).textContent = arrayDeVotos[0];
    document.getElementById("tarea-votando-"+idActual).style.display="none";
    activarBotones();
}
function checkTodosVotados(clase) {
    let elementos = document.getElementsByClassName(clase);
    for (const element of elementos) {
        let elemento = element;
        if (elemento.querySelector('.bi-x-circle')) {
            return false;
        }
    }
    return true;
}

function resetearTabla(){
    var div = document.getElementById('messages');
    var elements = div.querySelectorAll('*');
    elements.forEach(function(element) {
        // Modify the class names here
        if (element.classList.contains('bi-check2')){
            element.classList.remove('bi-check2');
            element.classList.add('bi-x-circle');
        }
    });
}
function borrarVotacion(){
    resetearTabla()
    console.log("Borrando votos");
    var div = document.getElementById('messages');
    if (!div.querySelector('.bi-check2')) {
        let elements = div.querySelectorAll('*');
        elements.forEach(function(element) {
        if (element.style.visibility === "visible") {
            element.style.visibility = "hidden";
        }
    });
    }
    arrayDeVotos = [];
}

function votar(id, voto){
    document.getElementById("valor-"+id).textContent = voto;
    arrayDeVotos.push(voto);
}
function mensajeDividirTarea(message){
    let p = document.getElementById("texto-dividir-tarea");
    let boton = document.getElementById("boton-dividir");
    boton.removeAttribute("data-bs-toggle");
    boton.removeAttribute("data-bs-target");

    tarea1 = message.s1;
    tarea2 = message.s2;
console.log("T1" + tarea1);
    p.textContent = "The user " + message.usuario + " wants to split the item in " + tarea1 + " and " + tarea2;

}

function fraccionarTarea(id1, id2){
    let lista = document.getElementById("paravotar");
    let liElements = lista.querySelectorAll('li');
    let elementoAFraccionar;
    console.log(idActual);

    arrayParaEliminar.push(idActual);


    //Encontramos el que se está votando actualmente
    let orden = document.getElementById("orden-"+idActual);
    for (const element of liElements) {
        if (getComputedStyle(element).display === 'block') {
            elementoAFraccionar = element;
            break;
        }
    }
    elementoAFraccionar.parentNode.removeChild(elementoAFraccionar);
    
    insertar(tarea2, id2, orden.textContent);
    insertar(tarea1, id1, orden.textContent);
    tarea1 = null;
    tarea2 = null;
    let p = document.getElementById("texto-dividir-tarea");
    p.textContent = "";
    //Devolver modal
    let boton = document.getElementById("boton-dividir");
    boton.setAttribute("data-bs-toggle", "modal");
    boton.setAttribute("data-bs-target", "#exampleModal");

    activarBotones();
    resetearTabla()
}

function moverTarea(id){
    idActual = id;
    console.log(id);
    document.getElementById("tarea-todo-"+id).style.display = "none";
    document.getElementById("tarea-votando-"+id).style.display = "block";
    //Boton dividir tarea
    let boton = document.getElementById("boton-dividir");
    boton.disabled = false;
}
function devolverTarea(id){
    idActual = null;
    console.log(id);
    document.getElementById("tarea-todo-"+id).style.display = "block";
    document.getElementById("tarea-votando-"+id).style.display = "none";
    document.getElementById("tarea-votado-"+id).style.display = "none";
    //Boton dividir tarea
    let boton = document.getElementById("boton-dividir");
    boton.disabled = true;
}

function insertar(tarea, id, orden, emailUsuario){
    let lista = document.getElementById("tareas");
    let lista2 = document.getElementById("clasificados");
    let paraVotar = document.getElementById("paravotar");
    console.log(lista);
    let newLi = document.createElement("li");
    newLi.id = "tarea-todo-"+id;
    newLi.className = "item-backlog mt-4";
    newLi.style.display = "block";

    // Crear el elemento de texto para la orden
    let spanElement = document.createElement("span");
    spanElement.id = "tarea-"+id;
    spanElement.textContent = tarea; // Texto dentro del span

    // Crear el elemento contenedor div
    let containerDiv = document.createElement("div");

    // Crear un nodo de texto con el texto "Orden: "
    let ordenText = document.createTextNode("Item: ");
    // Adjuntar el nodo de texto y el span al contenedor div
    containerDiv.appendChild(ordenText);
    containerDiv.appendChild(spanElement);
    newLi.appendChild(containerDiv);



    // Crear el elemento de texto para la orden
    spanElement = document.createElement("span");
    spanElement.id = "orden-"+id;
    spanElement.textContent = orden; // Texto dentro del span

    // Crear el elemento contenedor div
    containerDiv = document.createElement("div");

    // Crear un nodo de texto con el texto "Orden: "
    ordenText = document.createTextNode("Order: ");
    // Adjuntar el nodo de texto y el span al contenedor div
    containerDiv.appendChild(ordenText);
    containerDiv.appendChild(spanElement);

    newLi.appendChild(containerDiv);

    let buttonElement = document.createElement("button");
    buttonElement.className = "btn btn-red btn-pb";
    buttonElement.textContent = "Rate";
    buttonElement.onclick = function() {
        sendClasificar(id);
    };

    
    let newLi2 = newLi.cloneNode(true);
    let newLi3 = newLi.cloneNode(true);

    //Para el votando
    newLi.appendChild(buttonElement);
    
    newLi2.id = "tarea-votando-"+id;
    let buttonElement2 = document.createElement("button");
    buttonElement2.className = "btn btn-red";
    buttonElement2.textContent = "Return";
    buttonElement2.onclick = function() {
        sendDevolverTarea(id);
    };
    newLi2.appendChild(buttonElement2);
    newLi2.style.display = "none";

    //Para el sprint backlog
    newLi3.id="tarea-votado-"+id;
    newLi3.style.display ="none";

    // Crear el elemento de texto para la orden
    let spanDificultad = document.createElement("span");
    spanDificultad.id = "dificultad-"+id;

    // Crear el elemento contenedor div
    let contenedorDificultad = document.createElement("div");

    // Crear un nodo de texto con el texto "Orden: "
    let dificultadText = document.createTextNode("Estimation: ");
    // Adjuntar el nodo de texto y el span al contenedor div
    contenedorDificultad.appendChild(dificultadText);
    contenedorDificultad.appendChild(spanDificultad);

    // Crear el elemento de texto para la orden
    // Crear el elemento de texto para la orden
    let spanAsignado = document.createElement("span");
    spanAsignado.id = "asignado-"+id;

    // Crear el elemento contenedor div
    let contenedorAsignado = document.createElement("div");

    // Crear un nodo de texto con el texto "Orden: "
    let asignadoText = document.createTextNode("In charge: ");
    // Adjuntar el nodo de texto y el span al contenedor div
    contenedorAsignado.appendChild(asignadoText);
    contenedorAsignado.appendChild(spanAsignado);

    newLi3.appendChild(contenedorDificultad);
    newLi3.appendChild(contenedorAsignado);

    let buttonElement3 = document.createElement("button");
    buttonElement3.className = "btn btn-red";
    buttonElement3.textContent = "Return";
    buttonElement3.onclick = function() {
        sendDevolverTarea(id);
    };

    let buttonElement4 = document.createElement("button");
    buttonElement4.id ="asignar-"+id;
    buttonElement4.className = "btn btn-red";
    buttonElement4.textContent = "Assign";
    buttonElement4.onclick = function() {
        sendAsignar(id);
    };
    console.log(emailUsuario);
    newLi3.appendChild(buttonElement3);
    newLi3.appendChild(buttonElement4);

    console.log("Insertando: " + newLi.textContent +" delante de: " + lista.firstChild);
    lista.insertBefore(newLi, lista.firstChild);
    lista2.append(newLi3);
    paraVotar.append(newLi2);
}


function desactivarBotones(){
    let botones = document.querySelectorAll('.btn-pb');

    // Iterar sobre los botones y desactivarlos
    botones.forEach(function(boton) {
        boton.disabled = true;
    });
    botones = document.querySelectorAll('.voto');

    // Iterar sobre los botones y desactivarlos
    botones.forEach(function(boton) {
        boton.disabled = false;
    });
    let guardar = document.getElementById("guardar");
    guardar.disabled = true;
}
function activarBotones(){
    let botones = document.querySelectorAll('.btn-pb');

    // Iterar sobre los botones y desactivarlos
    botones.forEach(function(boton) {
        boton.disabled = false;
    });
    botones = document.querySelectorAll('.voto');

    // Iterar sobre los botones y desactivarlos
    botones.forEach(function(boton) {
        boton.disabled = true;
    });

    let guardar = document.getElementById("guardar");
    guardar.disabled = false;
}
function asignarTareaAUsuario(id, emailUsuario){
    console.log(id);
    let texto = document.getElementById("asignado-"+id);
    console.log(texto); //this is null
    console.log(emailUsuario);
    texto.textContent = emailUsuario;
}
function todosEncargado(){
    let ulElement = document.getElementById('clasificados');

    let liElements = ulElement.querySelectorAll('li[style*="display: block"]');

    //Elementos del sprint backlog
    for (const element of liElements) {
        console.log("En el for con: " + element);
        let spanElemets = element.getElementsByTagName('span');
        console.log(spanElemets[3].textContent);
        if(spanElemets[3].textContent === ""){
            alert("There must be someone in charge of every item in the Sprint Backlog");
            resetearTabla();
            return false;
        }
    }
    return true;
}

function isCheckboxChecked(checkboxId) {
    let checkbox = document.getElementById(checkboxId);
    if(!checkbox.checked){
        alert("All Sprint points must be discussed");
        return false;
    }
    return true;    
}
function guardarGoal(id){
    let data = {
        sprintGoal: document.getElementById("textarea").value,
    }
    enviar('/api/sprint/'+ id+'/sprintgoal', JSON.stringify(data), "PUT");
}
function guardarPlanningPoker(sprintId){
    if(!todosEncargado())
        return;
    let ulElement = document.getElementById('tareas');

    let liElements = ulElement.querySelectorAll('li[style*="display: block"]');
    let resultArray = [];
    //Elementos del product backlog
    for (const element of liElements) {
        let spanElemets = element.getElementsByTagName('span');
        console.log(spanElemets);
        let id = element.id.split("-")[2];
        if(id.includes("0."))
            id = null;
        resultArray.push({
            "id": id,
            "item": spanElemets[0].textContent,
            "orden": parseInt(spanElemets[1].textContent),
            "dificultad": null,
            "encargado": null,
            "estado": "TODO",
            "sprint": sprintId,
        });
        
    }
    ulElement = document.getElementById('clasificados');

    liElements = ulElement.querySelectorAll('li[style*="display: block"]');

    //Elementos del sprint backlog
    for (const element of liElements) {
        let spanElemets = element.getElementsByTagName('span');
        console.log(spanElemets);
        let id = element.id.split("-")[2];
        if(id.includes("0."))
            id = null;
        resultArray.push({
            "id": id,
            "item": spanElemets[0].textContent,
            "orden": parseInt(spanElemets[1].textContent),
            "dificultad": parseInt(spanElemets[2].textContent),
            "encargado": spanElemets[3].textContent,
            "estado": "DOING",
            "sprint": sprintId,
        });
        
    }

    for (let element of resultArray) {
        if (element.id == null) {
            console.log("Creando elemento: " + JSON.stringify(element));
            enviar('/api/backlog/', JSON.stringify(element), "POST");
        } else {
            console.log("Modificando elemento: " + JSON.stringify(element));
            enviar('/api/backlog/'+ element.id, JSON.stringify(element), "PUT");
            let index = resultArray.indexOf(element.id);
            if (index != -1) {
                resultArray.splice(index, 1);
            }
        }
    }
    for (let element of arrayParaEliminar){
        console.log("Borrando: " + element);
        //if (typeof element === 'string' && !element.includes("0."))
            enviarBorrar("/api/backlog/"+ element);
    }
    //window.location.replace("/rol");
}
