let ArraydeIds = [];
const ulElement = document.getElementById("lista-backlog");
// Obtener todos los elementos li dentro del ul
const liElements = ulElement.querySelectorAll('li');

// Iterar sobre los elementos li y obtener sus IDs
liElements.forEach(li => {
    ArraydeIds.push(li.id);
});


function anadirItem(){
    var ul = document.getElementById('lista-backlog');
    var li = document.createElement('li');
    let id = Math.random();
    li.setAttribute('id', id);

    var icono = document.createElement('i');
    icono.classList.add('bi', 'bi-trash');
    icono.setAttribute('onclick', 'borrar(' + id + ')');

    var input = document.createElement('input');
    input.classList.add('item-backlog-crear');
    input.required = "true";
    li.appendChild(icono);
    li.appendChild(input);
    ul.appendChild(li);
}
function borrar(id) {
    let elemento = document.getElementById(id);
    elemento.parentNode.removeChild(elemento);
}
function guardarProductGoal(id){
    let data = {
        "productGoal" : document.getElementById('textarea').value,
        "definitionOfDone" : document.getElementById('textareadone').value,
    }
    enviar('/api/proyecto/'+ id +'/atributos', JSON.stringify(data), "PUT");
}
function guardar(sprintId, proyectoId) {
    guardarProductGoal(proyectoId);
    let lista = document.getElementById('lista-backlog');
    let elementos = lista.getElementsByTagName('li');
    let data = [];

    for (let i = 0; i < elementos.length; i++) {
        let inputElement = elementos[i].querySelector('input');
        let inputValue = inputElement.value.trim();
        let idItem = elementos[i].id;

        if (idItem.includes("0."))
            idItem = 0;
        
        if (inputValue !== "") {
            let item = {
                "id": idItem,
                "orden": i,
                "item": inputValue,
                "sprint": sprintId,
                "estado": "TODO"
            };
            data.push(item);
        }
    }
    if (data.length > 0) {

        for (let element of data) {
            if (element.id == 0) {
                console.log("Creando elemento: " + JSON.stringify(element));
                enviar('/api/backlog/', JSON.stringify(element), "POST");
            } else {
                console.log("Modificando elemento: " + JSON.stringify(element));
                enviar('/api/backlog/'+ element.id, JSON.stringify(element), "PUT");
                let index = ArraydeIds.indexOf(element.id);
                if (index != -1) {
                    ArraydeIds.splice(index, 1);
                }
            }
        }
        
        for(let element of ArraydeIds)
            enviarBorrar('/api/backlog/'+ element);
        enviar('/api/sprint/'+ sprintId +'/sprintplanningdone', null, "PUT");
        //sleep(500).then(() => { window.location.reload(); });
    } else {
        alert("El Backlog no puede ser vac\u00EDo.");
    }
}         