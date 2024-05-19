//Quitar los botones de borrar, necesarios para editar

let botonesBorrar = document.getElementsByClassName("boton-borrar");
for(let i = 0; i < 3; i++){
    botonesBorrar[i].style.display = 'none';
}

function borrar(id) {
    let elementoABorrar = document.getElementById(id);
    
    if (elementoABorrar) {
      elementoABorrar.remove();
      console.log('Elemento borrado correctamente.');
    } else {
      console.log('Elemento no encontrado.');
    }
  }

function crear_row_participantes(){
    let id = Math.random();
    // Create the elements
    const rowDiv = document.createElement('div');
    rowDiv.className = 'row participante';
    rowDiv.id = id;

    const col1Div = document.createElement('div');
    col1Div.className = 'col-lg-2';

    const form1Div = document.createElement('div');
    form1Div.className = 'form-floating mb-3';

    let input1 = document.createElement('input');
    input1.className = 'form-control';
    input1.id = 'usuario'+id;
    input1.type = 'text';
    input1.placeholder = 'Username';
    input1.name = 'usuario'+id;

    const label1 = document.createElement('label');
    label1.htmlFor = 'usuario'+id;
    label1.textContent = 'Username';

    form1Div.appendChild(input1);
    form1Div.appendChild(label1);
    col1Div.appendChild(form1Div);

    //Email
    const col2Div = document.createElement('div');
    col2Div.className = 'col-lg-3';

    const form2Div = document.createElement('div');
    form2Div.className = 'form-floating mb-3';

    let input2 = document.createElement('input');
    input2.className = 'form-control';
    input2.id = 'email'+id;
    input2.type = 'text';
    input2.placeholder = 'Email';
    input2.name = 'email'+id;

    const label2 = document.createElement('label');
    label2.htmlFor = 'email'+id;
    label2.textContent = 'Email';

    form2Div.appendChild(input2);
    form2Div.appendChild(label2);
    col2Div.appendChild(form2Div);

    //Contraseña
    const col3Div = document.createElement('div');
    col3Div.className = 'col-lg-2';

    const form3Div = document.createElement('div');
    form3Div.className = 'form-floating mb-3';

    let input3 = document.createElement('input');
    input3.className = 'form-control';
    input3.id = 'pass'+id;
    input3.type = 'password';
    input3.placeholder = 'Password';
    input3.name = 'pass'+id;

    const label3 = document.createElement('label');
    label3.htmlFor = 'pass'+id;
    label3.textContent = 'Password';

    form3Div.appendChild(input3);
    form3Div.appendChild(label3);
    col3Div.appendChild(form3Div);

    //SubTeam
    const col4Div = document.createElement('div');
    col4Div.className = 'col-lg-2';

    const form4Div = document.createElement('div');
    form4Div.className = 'form-floating mb-3';

    let input4 = document.createElement('input');
    input4.className = 'form-control subTeam';
    input4.id = 'subTeam'+id;
    input4.type = 'number';
    input4.min = '0';
    input4.max = '10';
    input4.placeholder = 'SubTeam';
    input4.name = 'subtTeam'+id;

    const label4 = document.createElement('label');
    label4.htmlFor = 'subtTeam'+id;
    label4.textContent = 'subTeam';

    form4Div.appendChild(input4);
    form4Div.appendChild(label4);
    col4Div.appendChild(form4Div);

    //Rol
    const col5Div = document.createElement('div');
    col5Div.className = 'col-lg-2';

    const form5Div = document.createElement('div');
    form5Div.className = 'form-floating mb-3';

    const colDiv = document.createElement('div');
    colDiv.className = 'col-lg-6';

    const formDiv = document.createElement('div');
    formDiv.className = 'form-floating mb-3';

    const selectElement = document.createElement('select');
    selectElement.className = 'form-control rol';
    selectElement.id = 'rol'+id;
    selectElement.name = 'tipo';
    selectElement.required = true;

    const option1 = document.createElement('option');
    option1.value = 'Developer';
    option1.textContent = 'Developer';
    selectElement.appendChild(option1);

    const option2 = document.createElement('option');
    option2.value = 'Stakeholder';
    option2.textContent = 'Stakeholder';
    selectElement.appendChild(option2);

    const option3 = document.createElement('option');
    option3.value = 'Product Owner';
    option3.textContent = 'Product Owner';
    selectElement.appendChild(option3);

    const label = document.createElement('label');
    label.htmlFor = 'rol'+id;
    label.textContent = 'Select role';

    form5Div.appendChild(selectElement);
    form5Div.appendChild(label);
    col5Div.appendChild(form5Div);

    //Boton Borrar
    // Crear el elemento div con las clases necesarias
    const col6Div = document.createElement("div");
    col6Div.classList.add("col-lg-1");

    // Crear el elemento div con la clase "form-floating mb-3"
    const form6Div = document.createElement("div");
    form6Div.classList.add("form-floating", "mb-3");

    // Crear el botón con la clase "btn btn-red m-2" y la función onclick
    const boton = document.createElement("button");
    boton.classList.add("btn", "btn-red", "m-2");
    boton.textContent = "Delete";
    boton.setAttribute("onclick", "borrar("+id+")");

    form6Div.appendChild(boton);
    col6Div.appendChild(form6Div);
    
    rowDiv.appendChild(col1Div);
    rowDiv.appendChild(col2Div);
    rowDiv.appendChild(col3Div);
    rowDiv.appendChild(col4Div);
    rowDiv.appendChild(col5Div);
    rowDiv.appendChild(col6Div);

    // Append the row to the div with id "participantes"
    const participantesDiv = document.getElementById('participantes');
    participantesDiv.appendChild(rowDiv);
}
function obtenerIdsDeRows() {
    let divContenedor = document.getElementById('participantes');
    let elementosRow = divContenedor.getElementsByClassName('row');
    let myArray = [];

    // Obtener y mostrar los IDs de los elementos
    for (const element of elementosRow) {
        let idElemento = element.id;
        myArray.push(idElemento);
    }
    return myArray;
}
function checkInputs(usuario, email, pass, contador, crear){
    if(((usuario.value.trim() == "")||(email.value.trim() == "")) || (crear && (pass.value.trim() == ""))){
        alert("The user " + contador + " has fields in blank.");
        return false;            
    }
    return true;
}
function checkEmail(inputValues, email){
    if (inputValues.has(email.value)) {
        alert('The email ' + email.value +' is duplicated.');
        return false;
    }
    inputValues.add(email.value);
    return true;
}
function contarParticipantesEquipos() {
    let array = [];
    let subEquipos = document.getElementsByClassName("subTeam");
    console.log(subEquipos);
    // Iterate through each element in subEquipos
    for (const element of subEquipos) {
        if(element.value == ""){
            alert("Assign a team to everyone.");
            return false;
        }
        if(array[element.value] == null)
            array[element.value] = 0;
        array[element.value] = array[element.value]+1;
        console.log(array[element.value]);
        if (array[element.value] >= 10) {
            alert("There should not be more than 10 people on the same team."+
            " Check team " + element.value);
            return false;
        }
    }
    return true;
}
function sprintCorrecto() {
    const oneDay = 24 * 60 * 60 * 1000;
    
    let inicioSprint = new Date(document.getElementById("inicioSprint").value);
    let finSprint = new Date(document.getElementById("finSprint").value);
    
    let differenceDays = Math.round((finSprint - inicioSprint) / oneDay);
    
    // Check if the difference in days is greater than 30
    if (differenceDays > 31) {
        alert("The sprint cannot last more than one month.");
        return false;
    }
    
    return true;
}


function generarJsonProyecto(URL, crear, metodo) { 
    let arrayDeIds = obtenerIdsDeRows();
    let arrayParticipantes = [];
    let arraySprint = [];
    let inputValues = new Set();

    let contador = 0;
    let siEnviar = true;
    if(contarParticipantesEquipos() && sprintCorrecto()){
        arrayDeIds.forEach(function(i) {
            contador++;
            let usuario = document.getElementById(`usuario${i}`);
            let email = document.getElementById(`email${i}`);
            let pass = document.getElementById(`pass${i}`);
            let subTeam = document.getElementById(`subTeam${i}`);
            if(subTeam == null)
                subTeam = null;
            else
                subTeam = subTeam.value;
            let rol = document.getElementById(`rol${i}`);
            if(checkInputs(usuario, email, pass, contador, crear) && checkEmail(inputValues, email)){
                let rolAux;
                switch(rol.value) {
                    case "Product Owner":
                        rolAux = "PRODUCT_OWNER";
                        break;
                    case "Scrum Master":
                        rolAux = "SCRUM_MASTER";
                        break;
                    case "Stakeholder":
                        rolAux = "STAKEHOLDER";
                        break;
                    default:
                        rolAux = "DEVELOPER";
                }
                let participanteInfo = {
                    username: usuario.value,
                    email: email.value,
                    scrumSubTeam: subTeam,
                    rol: rolAux
                };

                if(!crear){
                    if (i.includes("0."))
                        i = 0;
                    
                    participanteInfo.id = i;
                }
                if(pass.value != "")
                    participanteInfo.pass=pass.value;
                arrayParticipantes.push(participanteInfo);
            }
            else
                siEnviar = false;
        });
        if (siEnviar){
            let sprint = {
                inicioSprint: document.getElementById('inicioSprint').value,
                finSprint: document.getElementById('finSprint').value,
            }
            arraySprint.push(sprint);
            let proyecto = {
                nombre: document.getElementById('nombre').value,
                personas: arrayParticipantes,
                sprint: arraySprint,
            };
        
            let jsonProyecto = JSON.stringify(proyecto);
            console.log(jsonProyecto);
            enviar(URL, jsonProyecto, metodo);
            //window.location.href = "/rol";
        }
    }
    
}