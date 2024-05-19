let arrayDeCategorias = []
const elementsWithClassCategoria = document.querySelectorAll('.categoria');
elementsWithClassCategoria.forEach(element => {
    arrayDeCategorias.push(element.value);
});

const selects = document.querySelectorAll('.select');
let i = 0;
selects.forEach(select => {
    let optionToSelect;
    switch(arrayDeCategorias[i]){
        case "Social":
            optionToSelect = select.querySelector('option[value="SOCIAL"]');
            break;
        case "Tecnolog\u00EDa":
                optionToSelect = select.querySelector('option[value="TECNOLOGIA"]');
                break;
        case "Negocio":
            optionToSelect = select.querySelector('option[value="NEGOCIO"]');
            break;
        case "Creativa":
            optionToSelect = select.querySelector('option[value="CREATIVA"]');
            break;
        case "Educativa":
            optionToSelect = select.querySelector('option[value="EDUCATIVA"]');
            break;
    }
    i++;
    optionToSelect.selected = true;

});


// Función para generar el HTML del select con las opciones
function generarSelect(id) {
    let select = document.createElement('select');
    select.id = 'categoria-'+id;
    select.className = 'form-control';

    for(let i = 0; i< 5; i++){
        let option = document.createElement('option');
        switch(i){
            case 0:
                option.value = "SOCIAL";
                option.text = "Social";
                break;
            case 1:
                option.value = "TECNOLOGIA";
                option.text = "Tecnology";
                break;
            case 2:
                option.value = "NEGOCIO";
                option.text = "Business";
                break;
            case 3:
                option.value = "CREATIVA";
                option.text = "Creative";
                break;
            case 4:
                option.value = "EDUCATIVA";
                option.text = "Educative";
                break
        }
        select.appendChild(option);
    }
    
    return select;
}

// Función para generar el HTML del input
function generarInput(id) {
    let input = document.createElement('input');
    input.id = 'item-'+id;
    input.type = 'text';
    input.className = 'form-control';
    return input;
}
function generarBorrar(id){
    let divCol = document.createElement('div');
    divCol.className = 'col-lg-1';

    let iconoTrash = document.createElement('i');
    iconoTrash.className = 'bi bi-trash';
    iconoTrash.onclick = function() {
        borrar("fila-"+id);
    };
    
    divCol.appendChild(iconoTrash);
    return divCol;
}
// Función principal para generar los elementos y agregarlos al DOM
function anadirTecnologia() {
    let id = Math.random();
    let divRow = document.createElement('li');
    divRow.id ="fila-"+id;
    divRow.className='row mt-4';
    let divCol1 = generarBorrar(id);

    let divCol2 = document.createElement('div');
    divCol2.className = 'col-lg-4';
    divCol2.appendChild(generarSelect(id));

    let divCol3 = document.createElement('div');
    divCol3.className = 'col-lg-7';
    divCol3.appendChild(generarInput(id));

    let contenedor = document.getElementById('contenedorTecnologias');
    divRow.appendChild(divCol1);
    divRow.appendChild(divCol2);
    divRow.appendChild(divCol3);
    contenedor.append(divRow);
}

function borrar(id) {
    let elemento = document.getElementById(id);
    elemento.parentNode.removeChild(elemento);
}

function generarJsonPersona(id){
    let username = document.getElementById("username").value;
    let email = document.getElementById("email").value;
    let pass = document.getElementById("pass").value;
    let scrumTeam = document.getElementById("ScrumTeam");
    if(scrumTeam != null)
        scrumTeam = scrumTeam.value;
    else
        scrumTeam = null;
    let rol = document.getElementById("rol").value;
    let tecnologias = [];

    let items = document.getElementById('contenedorTecnologias').querySelectorAll('li');

    items.forEach(function(item) {
        let select = item.querySelector('select');
        let input = item.querySelector('input');
        let id = select.id.split("-")[1];
        if(id.includes("0."))
            id=0;
        let objeto = {
            "id": id,
            categoria: select.value,
            item: input.value
        };
        if(input.value != "")
            tecnologias.push(objeto);
    });
    let data = {
        "username": username,
        "email": email,
        "scrumSubTeam": scrumTeam,
        "rol": rol,
        "tecnologias": tecnologias,
    };
    
    if (pass !== "") 
        data.pass = pass;
    
    console.log(data);
    enviar("/api/persona/"+id, JSON.stringify(data), "PUT");
}

function generarJsonEmpresa(id){
    let username = document.getElementById("username").value;
    let email = document.getElementById("email").value;
    let pass = document.getElementById("pass").value;

    let data ={
        "username": username,
        "email": email,
        "pass": pass,
    }
    console.log(data);
    enviar("/api/empresa/"+id, JSON.stringify(data), "PUT");
}
function enviarBorrarEmpresa(url){
    enviarBorrar('/api/empresa/'+url);
    window.location.href = "/login";
}
